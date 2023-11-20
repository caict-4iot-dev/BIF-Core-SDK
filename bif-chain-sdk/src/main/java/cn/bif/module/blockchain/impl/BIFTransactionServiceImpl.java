/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * © COPYRIGHT 2021 Corporation CAICT All rights reserved.
 * http://www.caict.ac.cn
 */
package cn.bif.module.blockchain.impl;


import cn.bif.api.BIFSDK;
import cn.bif.common.*;
import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;
import cn.bif.model.response.bid.BID;
import cn.bif.model.response.result.data.*;
import cn.bif.utils.http.HttpUtils;
import cn.bif.model.request.*;
import cn.bif.model.request.operation.*;
import cn.bif.model.response.*;
import cn.bif.model.response.result.*;
import cn.bif.module.account.impl.BIFAccountServiceImpl;
import cn.bif.module.blockchain.BIFBlockService;
import cn.bif.module.blockchain.BIFTransactionService;
import cn.bif.module.contract.impl.BIFContractServiceImpl;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.module.encryption.key.PublicKeyManager;
import cn.bif.utils.hash.HashUtil;
import cn.bif.utils.hash.model.HashType;
import cn.bif.utils.hex.HexFormat;

import cn.bif.protobuf.Chain;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BIFTransactionServiceImpl implements BIFTransactionService {

    /**
     * @Method BIFSerializable
     * @Params [bifTransactionSerializeRequest]
     * @Return BIFTransactionSerializeResponse
     */
    @Override
    public BIFTransactionSerializeResponse BIFSerializable(BIFTransactionSerializeRequest bifTransactionSerializeRequest) {
        BIFTransactionSerializeResponse bifTransactionSerializeResponse = new BIFTransactionSerializeResponse();
        BIFTransactionSerializeResult transactionSerializeResult = new BIFTransactionSerializeResult();
        try {
            if (Tools.isEmpty(bifTransactionSerializeRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            // check sourceAddress
            String sourceAddress = bifTransactionSerializeRequest.getSourceAddress();
            if (!PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            // check nonce
            Long nonce = bifTransactionSerializeRequest.getNonce();
            if (Tools.isEmpty(nonce) || nonce < Constant.INIT_ONE) {
                throw new SDKException(SdkError.INVALID_NONCE_ERROR);
            }
            // check gasPrice
            Long gasPrice = bifTransactionSerializeRequest.getGasPrice();
            if (Tools.isEmpty(gasPrice) || gasPrice < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }

            // check feeLimit
            Long feeLimit = bifTransactionSerializeRequest.getFeeLimit();
            if (Tools.isEmpty(feeLimit) || feeLimit < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }

            Long ceilLedgerSeq = bifTransactionSerializeRequest.getCeilLedgerSeq();
            if (!Tools.isEmpty(ceilLedgerSeq) && ceilLedgerSeq < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_CEILLEDGERSEQ_ERROR);
            }
            // check metadata
            String metadata = bifTransactionSerializeRequest.getMetadata();
            // build transaction
            Chain.Transaction.Builder transaction = Chain.Transaction.newBuilder();
            // add note
            if (!Tools.isEmpty(metadata)) {
                transaction.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            // check operation
            BIFBaseOperation[] operations = bifTransactionSerializeRequest.getOperations();
            //domainId
            Integer domainId = bifTransactionSerializeRequest.getDomainId();
            operations[0].setDomainId(domainId);
            if (Tools.isEmpty(operations)) {
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }
            buildOperations(operations, sourceAddress, transaction);
            // add other information
            transaction.setSourceAddress(sourceAddress);
            transaction.setNonce(nonce);
            transaction.setFeeLimit(feeLimit);
            transaction.setGasPrice(gasPrice);
            transaction.setDomainId(domainId);

            if (!Tools.isEmpty(BIFSDK.getSdk().getChainId())) {
                transaction.setChainId(BIFSDK.getSdk().getChainId());
            }
            if (!Tools.isEmpty(ceilLedgerSeq)) {
                // get blockNumber
                BIFBlockGetNumberInfoRequest request =new BIFBlockGetNumberInfoRequest();
                request.setDomainId(domainId);
                BIFBlockService blockService = new BIFBlockServiceImpl();
                BIFBlockGetNumberResponse blockGetNumberResponse = blockService.getBlockNumber(request);
                Integer errorCode = blockGetNumberResponse.getErrorCode();
                if (!Tools.isEmpty(errorCode) && !errorCode.equals(Constant.SUCCESS)) {
                    String errorDesc = blockGetNumberResponse.getErrorDesc();
                    throw new SDKException(errorCode, errorDesc);
                } else if (Tools.isEmpty(errorCode)) {
                    throw new SDKException(SdkError.CONNECTNETWORK_ERROR);
                }
                // check ceilLedgerSeq
                Long blockNumber = blockGetNumberResponse.getResult().getHeader().getBlockNumber();
                transaction.setCeilLedgerSeq(ceilLedgerSeq + blockNumber);
            }
            byte[] transactionBlobBytes = transaction.build().toByteArray();
            String transactionBlob = HexFormat.byteToHex(transactionBlobBytes);
            transactionSerializeResult.setTransactionBlob(transactionBlob);
            transactionSerializeResult.setHash(HashUtil.GenerateHashHex(transactionBlobBytes, HashType.SHA256));
            bifTransactionSerializeResponse.buildResponse(SdkError.SUCCESS, transactionSerializeResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            bifTransactionSerializeResponse.buildResponse(errorCode, errorDesc, transactionSerializeResult);
        } catch (Exception e) {
            bifTransactionSerializeResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionSerializeResult);
        }

        return bifTransactionSerializeResponse;
    }



    /**
     * @Method submit
     * @Params [transactionSubmitRequest]
     * @Return TransactionSubmitResponse
     */
    @Override
    public BIFTransactionSubmitResponse BIFSubmit(BIFTransactionSubmitRequest bifTransactionSubmitRequest) {
        BIFTransactionSubmitResponse bifTransactionSubmitResponse = new BIFTransactionSubmitResponse();
        BIFTransactionSubmitResult transactionSubmitResult = new BIFTransactionSubmitResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(bifTransactionSubmitRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String blob = bifTransactionSubmitRequest.getSerialization();
            if (Tools.isEmpty(blob)) {
                throw new SDKException(SdkError.INVALID_SERIALIZATION_ERROR);
            }
            Chain.Transaction.parseFrom(HexFormat.hexToByte(blob));
            // serializable transaction request
            Map<String, Object> transactionItemsRequest = new HashMap<>();
            List<Object> transactionItems = new ArrayList<>();
            Map<String, Object> transactionItem = new HashMap<>();
            transactionItem.put("transaction_blob", blob);
            // build sign
            List<Object> signatureItems = new ArrayList<>();
            if(bifTransactionSubmitRequest.getSignatures() != null && bifTransactionSubmitRequest.getSignatures().length > 0){
                for (BIFSignature signature:bifTransactionSubmitRequest.getSignatures()) {
                    Map<String, Object> signatureItem = new HashMap<>();
                    String signData = signature.getSignData();
                    if (Tools.isEmpty(signData)) {
                        throw new SDKException(SdkError.SIGNDATA_NULL_ERROR);
                    }
                    String publicKey = signature.getPublicKey();
                    if (Tools.isEmpty(publicKey)) {
                        throw new SDKException(SdkError.PUBLICKEY_NULL_ERROR);
                    }
                    signatureItem.put("sign_data", signData);
                    signatureItem.put("public_key", publicKey);
                    signatureItems.add(signatureItem);
                }
            }else {
                String signData = bifTransactionSubmitRequest.getSignData();
                if (Tools.isEmpty(signData)) {
                    throw new SDKException(SdkError.SIGNDATA_NULL_ERROR);
                }
                String publicKey = bifTransactionSubmitRequest.getPublicKey();
                if (Tools.isEmpty(publicKey)) {
                    throw new SDKException(SdkError.PUBLICKEY_NULL_ERROR);
                }
                Map<String, Object> signatureItem = new HashMap<>();
                signatureItem.put("sign_data", signData);
                signatureItem.put("public_key", publicKey);
                signatureItems.add(signatureItem);
            }
            transactionItem.put("signatures", signatureItems);
            transactionItems.add(transactionItem);
            transactionItemsRequest.put("items", transactionItems);
            // submit
            String submitUrl = General.getInstance().transactionSubmitUrl();
            String transactionRequest = JsonUtils.toJSONString(transactionItemsRequest);
            String result = HttpUtils.httpPost(submitUrl, transactionRequest);
            BIFTransactionSubmitHttpResponse transactionSubmitHttpResponse = JsonUtils.toJavaObject(result, BIFTransactionSubmitHttpResponse.class);
            Integer successCount = transactionSubmitHttpResponse.getSuccessCount();
            BIFTransactionSubmitHttpResult[] httpResults = transactionSubmitHttpResponse.getResults();
            if (!Tools.isEmpty(httpResults)) {
                transactionSubmitResult.setHash(httpResults[0].getHash());
                if (!Tools.isEmpty(successCount) && 0 == successCount) {
                    Integer errorCode = httpResults[0].getErrorCode();
                    String errorDesc = httpResults[0].getErrorDesc();
                    throw new SDKException(errorCode, errorDesc);
                }
            } else {
                throw new SDKException(SdkError.INVALID_SERIALIZATION_ERROR);
            }
            bifTransactionSubmitResponse.buildResponse(SdkError.SUCCESS, transactionSubmitResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            bifTransactionSubmitResponse.buildResponse(errorCode, errorDesc, transactionSubmitResult);
        } catch (InvalidProtocolBufferException | IllegalArgumentException e) {
            bifTransactionSubmitResponse.buildResponse(SdkError.INVALID_SERIALIZATION_ERROR, transactionSubmitResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            bifTransactionSubmitResponse.buildResponse(SdkError.CONNECTN_BLOCKCHAIN_ERROR, transactionSubmitResult);
        } catch (Exception e) {
            bifTransactionSubmitResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionSubmitResult);
        }
        return bifTransactionSubmitResponse;
    }

    /**
     * 广播交易
     *
     * @return
     */
    @Override
    public String radioTransaction(String senderAddress, Long feeLimit, Long gasPrice, BIFBaseOperation operation,
                                   Long ceilLedgerSeq, String remarks, String senderPrivateKey,Integer domainId) {
        BIFAccountServiceImpl accountService = new BIFAccountServiceImpl();
        // 一、获取交易发起的账号nonce值
        BIFAccountGetNonceRequest getNonceRequest = new BIFAccountGetNonceRequest();
        getNonceRequest.setAddress(senderAddress);
        getNonceRequest.setDomainId(domainId);
        // 调用getBIFNonce接口
        BIFAccountGetNonceResponse nonceResponse = accountService.getNonce(getNonceRequest);
        if (!nonceResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(nonceResponse.getErrorCode(), nonceResponse.getErrorDesc());
        }
        Long nonce = nonceResponse.getResult().getNonce();
        if (Tools.isEmpty(nonce) || nonce < 1) {
            nonce=0L;
        }
        // 二、构建操作、序列化交易
        // 初始化请求参数
        BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest();
        serializeRequest.setSourceAddress(senderAddress);
        serializeRequest.setNonce(nonce + 1);
        serializeRequest.setFeeLimit(feeLimit);
        serializeRequest.setGasPrice(gasPrice);
        serializeRequest.setOperation(operation);
        serializeRequest.setCeilLedgerSeq(ceilLedgerSeq);
        serializeRequest.setDomainId(domainId);

        // 调用BIFSerializable接口
        serializeRequest.setMetadata(remarks);
        BIFTransactionSerializeResponse serializeResponse = BIFSerializable(serializeRequest);
        if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(serializeResponse.getErrorCode(), serializeResponse.getErrorDesc());
        }
        String transactionBlob = serializeResponse.getResult().getTransactionBlob();

        // 三、签名
        byte[] signBytes = PrivateKeyManager.sign(HexFormat.hexToByte(transactionBlob), senderPrivateKey);
        String publicKey = PrivateKeyManager.getEncPublicKey(senderPrivateKey);

        //四、提交交易
        BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
        submitRequest.setSerialization(transactionBlob);
        submitRequest.addSignature(publicKey,HexFormat.byteToHex(signBytes));
        //submitRequest.setPublicKey(publicKey);
        //submitRequest.setSignData(HexFormat.byteToHex(signBytes));
        // 调用bifSubmit接口
        BIFTransactionSubmitResponse transactionSubmitResponse = BIFSubmit(submitRequest);
        if (!transactionSubmitResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(transactionSubmitResponse.getErrorCode(), transactionSubmitResponse.getErrorDesc());
        }
        return transactionSubmitResponse.getResult().getHash();
    }

    /**
     * 广播交易
     *
     * @return
     */
    @Override
    public String radioTransaction(String senderAddress, Long feeLimit, Long gasPrice, List<BIFContractInvokeOperation> operations,
                                   Long ceilLedgerSeq, String remarks, String senderPrivateKey, Integer domainId) {
        BIFAccountServiceImpl accountService = new BIFAccountServiceImpl();
        // 一、获取交易发起的账号nonce值
        BIFAccountGetNonceRequest getNonceRequest = new BIFAccountGetNonceRequest();
        getNonceRequest.setAddress(senderAddress);
        getNonceRequest.setDomainId(domainId);
        // 调用getBIFNonce接口
        BIFAccountGetNonceResponse nonceResponse = accountService.getNonce(getNonceRequest);
        if (!nonceResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(nonceResponse.getErrorCode(), nonceResponse.getErrorDesc());
        }
        Long nonce = nonceResponse.getResult().getNonce();
        if (Tools.isEmpty(nonce) || nonce < 1) {
            nonce=0L;
        }
        // 二、构建操作、序列化交易
        // 初始化请求参数
        BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest();
        serializeRequest.setSourceAddress(senderAddress);
        serializeRequest.setNonce(nonce + 1);
        serializeRequest.setFeeLimit(feeLimit);
        serializeRequest.setGasPrice(gasPrice);
        serializeRequest.setDomainId(domainId);

        for (BIFContractInvokeOperation opt: operations
        ) {
            serializeRequest.addOperation(opt);
        }
        serializeRequest.setCeilLedgerSeq(ceilLedgerSeq);

        // 调用BIFSerializable接口
        serializeRequest.setMetadata(remarks);
        BIFTransactionSerializeResponse serializeResponse = BIFSerializable(serializeRequest);
        if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(serializeResponse.getErrorCode(), serializeResponse.getErrorDesc());
        }
        String transactionBlob = serializeResponse.getResult().getTransactionBlob();

        // 三、签名
        byte[] signBytes = PrivateKeyManager.sign(HexFormat.hexToByte(transactionBlob), senderPrivateKey);
        String publicKey = PrivateKeyManager.getEncPublicKey(senderPrivateKey);

        //四、提交交易
        BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
        submitRequest.setSerialization(transactionBlob);
        submitRequest.addSignature(publicKey,HexFormat.byteToHex(signBytes));
        //submitRequest.setPublicKey(publicKey);
        //submitRequest.setSignData(HexFormat.byteToHex(signBytes));
        // 调用bifSubmit接口
        BIFTransactionSubmitResponse transactionSubmitResponse = BIFSubmit(submitRequest);
        if (!transactionSubmitResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(transactionSubmitResponse.getErrorCode(), transactionSubmitResponse.getErrorDesc());
        }
        return transactionSubmitResponse.getResult().getHash();
    }

    /**
     * 广播交易--批量gas
     *
     * @return
     */
    @Override
    public String radioGasTransaction(String senderAddress, Long feeLimit, Long gasPrice, List<BIFGasSendOperation> operations,
                                      Long ceilLedgerSeq, String remarks, String senderPrivateKey, Integer domainId) {
        BIFAccountServiceImpl accountService = new BIFAccountServiceImpl();
        // 一、获取交易发起的账号nonce值
        BIFAccountGetNonceRequest getNonceRequest = new BIFAccountGetNonceRequest();
        getNonceRequest.setAddress(senderAddress);
        getNonceRequest.setDomainId(domainId);
        // 调用getBIFNonce接口
        BIFAccountGetNonceResponse nonceResponse = accountService.getNonce(getNonceRequest);
        if (!nonceResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(nonceResponse.getErrorCode(), nonceResponse.getErrorDesc());
        }
        Long nonce = nonceResponse.getResult().getNonce();
        if (Tools.isEmpty(nonce) || nonce < 1) {
            nonce=0L;
        }
        // 二、构建操作、序列化交易
        // 初始化请求参数
        BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest();
        serializeRequest.setSourceAddress(senderAddress);
        serializeRequest.setNonce(nonce + 1);
        serializeRequest.setFeeLimit(feeLimit);
        serializeRequest.setGasPrice(gasPrice);
        serializeRequest.setDomainId(domainId);

        for (BIFGasSendOperation opt: operations
        ) {
            serializeRequest.addOperation(opt);
        }
        serializeRequest.setCeilLedgerSeq(ceilLedgerSeq);

        // 调用BIFSerializable接口
        serializeRequest.setMetadata(remarks);
        BIFTransactionSerializeResponse serializeResponse = BIFSerializable(serializeRequest);
        if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(serializeResponse.getErrorCode(), serializeResponse.getErrorDesc());
        }
        String transactionBlob = serializeResponse.getResult().getTransactionBlob();

        // 三、签名
        byte[] signBytes = PrivateKeyManager.sign(HexFormat.hexToByte(transactionBlob), senderPrivateKey);
        String publicKey = PrivateKeyManager.getEncPublicKey(senderPrivateKey);

        //四、提交交易
        BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
        submitRequest.setSerialization(transactionBlob);
        submitRequest.addSignature(publicKey,HexFormat.byteToHex(signBytes));
        //submitRequest.setPublicKey(publicKey);
        //submitRequest.setSignData(HexFormat.byteToHex(signBytes));
        // 调用bifSubmit接口
        BIFTransactionSubmitResponse transactionSubmitResponse = BIFSubmit(submitRequest);
        if (!transactionSubmitResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(transactionSubmitResponse.getErrorCode(), transactionSubmitResponse.getErrorDesc());
        }
        return transactionSubmitResponse.getResult().getHash();
    }
    /**
     * @Method getInfo
     * @Params [transactionGetInfoRequest]
     * @Return TransactionGetInfoResponse
     */
    @Override
    public BIFTransactionGetInfoResponse getTransactionInfo(BIFTransactionGetInfoRequest transactionGetInfoRequest) {
        BIFTransactionGetInfoResponse transactionGetInfoResponse = new BIFTransactionGetInfoResponse();
        BIFTransactionGetInfoResult transactionGetInfoResult = new BIFTransactionGetInfoResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(transactionGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String hash = transactionGetInfoRequest.getHash();
            if (Tools.isEmpty(hash) || hash.length() != Constant.HASH_HEX_LENGTH) {
                throw new SDKException(SdkError.INVALID_HASH_ERROR);
            }
            Integer domainId=transactionGetInfoRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            transactionGetInfoResponse = getTransactionInfo(hash,domainId);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionGetInfoResponse.buildResponse(errorCode, errorDesc, transactionGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            transactionGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, transactionGetInfoResult);
        } catch (Exception e) {
            transactionGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionGetInfoResult);
        }
        return transactionGetInfoResponse;
    }

    @Override
    public BIFTransactionGasSendResponse gasSend(BIFTransactionGasSendRequest request) {
        BIFTransactionGasSendResponse response = new BIFTransactionGasSendResponse();
        BIFTransactionGasSendResult result = new BIFTransactionGasSendResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String senderAddress = request.getSenderAddress();
            if (!PublicKeyManager.isAddressValid(senderAddress)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            String privateKey = request.getPrivateKey();
            if (Tools.isEmpty(privateKey)) {
                throw new SDKException(SdkError.PRIVATEKEY_NULL_ERROR);
            }
            Long ceilLedgerSeq = request.getCeilLedgerSeq();
            String remarks = request.getRemarks();

            BIFGasSendOperation operation = new BIFGasSendOperation();
            String destAddress = request.getDestAddress();
            if (!PublicKeyManager.isAddressValid(destAddress)) {
                throw new SDKException(SdkError.INVALID_DESTADDRESS_ERROR);
            }
            Long amount = request.getAmount();
            if (Tools.isEmpty(amount) || amount < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GAS_AMOUNT_ERROR);
            }
            operation.setDestAddress(destAddress);
            operation.setAmount(amount);

            Long feeLimit = request.getFeeLimit();
            if (Tools.isEmpty(feeLimit)) {
                feeLimit = Constant.FEE_LIMIT;
            }
            if (Tools.isEmpty(feeLimit) || feeLimit < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }

            Long gasPrice = request.getGasPrice();
            if (Tools.isEmpty(gasPrice)) {
                gasPrice = Constant.GAS_PRICE;
            }
            if (Tools.isEmpty(gasPrice) || gasPrice < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            // 广播交易
            String hash = radioTransaction(senderAddress, feeLimit, gasPrice, operation, ceilLedgerSeq,
                    remarks, privateKey,domainId);
            result.setHash(hash);
            response.buildResponse(SdkError.SUCCESS, result);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            response.buildResponse(errorCode, errorDesc, result);
        } catch (Exception e) {
            response.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), result);
        }
        return response;
    }


    /**
     * @Method evaluateFee
     * @Params [TransactionEvaluateFeeRequest]
     * @Return TransactionEvaluateFeeResponse
     */
    @Override
    public BIFTransactionEvaluateFeeResponse evaluateFee(BIFTransactionEvaluateFeeRequest BIFTransactionEvaluateFeeRequest) {
        BIFTransactionEvaluateFeeResponse BIFTransactionEvaluateFeeResponse = new BIFTransactionEvaluateFeeResponse();
        BIFTransactionEvaluateFeeResult BIFTransactionEvaluateFeeResult = new BIFTransactionEvaluateFeeResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(BIFTransactionEvaluateFeeRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            // check sourceAddress
            String sourceAddress = BIFTransactionEvaluateFeeRequest.getSourceAddress();

            if (!PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            // check nonce
            BIFAccountServiceImpl accountService = new BIFAccountServiceImpl();
            // 一、获取交易发起的账号nonce值
            BIFAccountGetNonceRequest getNonceRequest = new BIFAccountGetNonceRequest();
            getNonceRequest.setAddress(sourceAddress);
            getNonceRequest.setDomainId(BIFTransactionEvaluateFeeRequest.getDomainId());
            // 调用getBIFNonce接口
            BIFAccountGetNonceResponse nonceResponse = accountService.getNonce(getNonceRequest);
            if (!nonceResponse.getErrorCode().equals(Constant.SUCCESS)) {
                throw new SDKException(nonceResponse.getErrorCode(), nonceResponse.getErrorDesc());
            }
            Long nonce = nonceResponse.getResult().getNonce();
            if (Tools.isEmpty(nonce) || nonce < 1) {
                nonce=0L;
            }
            // check signatureNum
            Integer signatureNum = BIFTransactionEvaluateFeeRequest.getSignatureNumber();
            if (Tools.isEmpty(signatureNum) || signatureNum < 1) {
                throw new SDKException(SdkError.INVALID_SIGNATURENUMBER_ERROR);
            }

            // check metadata
            String metadata = BIFTransactionEvaluateFeeRequest.getRemarks();
            // build transaction
            Chain.Transaction.Builder transaction = Chain.Transaction.newBuilder();

            BIFBaseOperation baseOperations = BIFTransactionEvaluateFeeRequest.getOperation();
            if(Tools.isEmpty(baseOperations)){
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }

            Long feeLimit = BIFTransactionEvaluateFeeRequest.getFeeLimit();
            if (Tools.isEmpty(feeLimit)) {
                feeLimit = Constant.FEE_LIMIT;
            }
            if (Tools.isEmpty(feeLimit) || feeLimit < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }
            Long gasPrice = BIFTransactionEvaluateFeeRequest.getGasPrice();
            if (Tools.isEmpty(gasPrice) || gasPrice < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }
            Integer domainId = BIFTransactionEvaluateFeeRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }

            BIFBaseOperation[] operations = new BIFBaseOperation[1];
            operations[0] = baseOperations;
            buildOperations(operations, sourceAddress, transaction);

            transaction.setSourceAddress(sourceAddress);
            transaction.setNonce(nonce+1);
            transaction.setDomainId(domainId);
            if (!Tools.isEmpty(feeLimit)) {
                transaction.setFeeLimit(feeLimit);
            }
            if(!Tools.isEmpty(gasPrice)){
                transaction.setGasPrice(gasPrice);
            }
            if (!Tools.isEmpty(metadata)) {
                transaction.setMetadata(ByteString.copyFromUtf8(metadata));
            }

            // protocol buffer to json
            JsonFormat jsonFormat = new JsonFormat();
            String transactionStr = jsonFormat.printToString(transaction.build());

            Map<String, Object> transactionJson = JsonUtils.toMap(transactionStr);

            // build testTransaction request
            Map<String, Object> testTransactionRequest = new HashMap<>();;
            List<Object> transactionItems = new ArrayList<>();
            Map<String, Object> transactionItem =  new HashMap<>();
            transactionItem.put("transaction_json", transactionJson);
            transactionItems.add(transactionItem);
            testTransactionRequest.put("items", transactionItems);

            String evaluationFeeUrl = General.getInstance().transactionEvaluationFee();
            String result = HttpUtils.httpPost(evaluationFeeUrl, JsonUtils.toJSONString(testTransactionRequest));
            BIFTransactionEvaluateFeeResponse = JsonUtils.toJavaObject(result, BIFTransactionEvaluateFeeResponse.class);

        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            BIFTransactionEvaluateFeeResponse.buildResponse(errorCode, errorDesc, BIFTransactionEvaluateFeeResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            BIFTransactionEvaluateFeeResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, BIFTransactionEvaluateFeeResult);
        } catch (Exception e) {
            BIFTransactionEvaluateFeeResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), BIFTransactionEvaluateFeeResult);
        }
        return  BIFTransactionEvaluateFeeResponse;
    }

    @Override
    public BIFTransactionEvaluateFeeResponse batchEvaluateFee(BIFTransactionEvaluateFeeRequest BIFTransactionEvaluateFeeRequest) {
        BIFTransactionEvaluateFeeResponse BIFTransactionEvaluateFeeResponse = new BIFTransactionEvaluateFeeResponse();
        BIFTransactionEvaluateFeeResult BIFTransactionEvaluateFeeResult = new BIFTransactionEvaluateFeeResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(BIFTransactionEvaluateFeeRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            // check sourceAddress
            String sourceAddress = BIFTransactionEvaluateFeeRequest.getSourceAddress();

            if (!PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            // check nonce
            BIFAccountServiceImpl accountService = new BIFAccountServiceImpl();
            // 一、获取交易发起的账号nonce值
            BIFAccountGetNonceRequest getNonceRequest = new BIFAccountGetNonceRequest();
            getNonceRequest.setAddress(sourceAddress);
            getNonceRequest.setDomainId(BIFTransactionEvaluateFeeRequest.getDomainId());
            // 调用getBIFNonce接口
            BIFAccountGetNonceResponse nonceResponse = accountService.getNonce(getNonceRequest);
            if (!nonceResponse.getErrorCode().equals(Constant.SUCCESS)) {
                throw new SDKException(nonceResponse.getErrorCode(), nonceResponse.getErrorDesc());
            }
            Long nonce = nonceResponse.getResult().getNonce();
            if (Tools.isEmpty(nonce) || nonce < 1) {
                nonce=0L;
            }
            // check signatureNum
            Integer signatureNum = BIFTransactionEvaluateFeeRequest.getSignatureNumber();
            if (Tools.isEmpty(signatureNum) || signatureNum < 1) {
                throw new SDKException(SdkError.INVALID_SIGNATURENUMBER_ERROR);
            }
            // check metadata
            String metadata = BIFTransactionEvaluateFeeRequest.getRemarks();
            // build transaction
            Chain.Transaction.Builder transaction = Chain.Transaction.newBuilder();


            List<BIFBaseOperation> baseOperations = BIFTransactionEvaluateFeeRequest.getOperations();
            if(Tools.isEmpty(baseOperations)){
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }

            Long feeLimit = BIFTransactionEvaluateFeeRequest.getFeeLimit();
            if (Tools.isEmpty(feeLimit)) {
                feeLimit = Constant.FEE_LIMIT;
            }
            if (Tools.isEmpty(feeLimit) || feeLimit < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }
            Long gasPrice = BIFTransactionEvaluateFeeRequest.getGasPrice();
            if (Tools.isEmpty(gasPrice) || gasPrice < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }
            Integer domainId = BIFTransactionEvaluateFeeRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }

            //批量处理
            BIFBaseOperation[] operations = new BIFBaseOperation[baseOperations.size()];
            for (int i = 0; i < baseOperations.size(); i++) {
                operations[i]=baseOperations.get(i);
            }
            buildOperations(operations, sourceAddress, transaction);

            transaction.setSourceAddress(sourceAddress);
            transaction.setNonce(nonce+1);
            transaction.setDomainId(domainId);
            if (!Tools.isEmpty(feeLimit)) {
                transaction.setFeeLimit(feeLimit);
            }
            if(!Tools.isEmpty(gasPrice)){
                transaction.setGasPrice(gasPrice);
            }
            if (!Tools.isEmpty(metadata)) {
                transaction.setMetadata(ByteString.copyFromUtf8(metadata));
            }

            // protocol buffer to json
            JsonFormat jsonFormat = new JsonFormat();
            String transactionStr = jsonFormat.printToString(transaction.build());
            Map<String, Object> transactionJson = JsonUtils.toMap(transactionStr);

            // build testTransaction request
            Map<String, Object> testTransactionRequest = new HashMap<>();;
            List<Object> transactionItems = new ArrayList<>();
            Map<String, Object> transactionItem =  new HashMap<>();
            transactionItem.put("transaction_json", transactionJson);
            transactionItems.add(transactionItem);
            testTransactionRequest.put("items", transactionItems);

            String evaluationFeeUrl = General.getInstance().transactionEvaluationFee();
            String result = HttpUtils.httpPost(evaluationFeeUrl, JsonUtils.toJSONString(testTransactionRequest));
            BIFTransactionEvaluateFeeResponse = JsonUtils.toJavaObject(result, BIFTransactionEvaluateFeeResponse.class);

        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            BIFTransactionEvaluateFeeResponse.buildResponse(errorCode, errorDesc, BIFTransactionEvaluateFeeResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            BIFTransactionEvaluateFeeResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, BIFTransactionEvaluateFeeResult);
        } catch (Exception e) {
            BIFTransactionEvaluateFeeResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), BIFTransactionEvaluateFeeResult);
        }
        return  BIFTransactionEvaluateFeeResponse;
    }

    /**
     * @Method buildOperations
     * @Params [operationBase, transaction]
     * @Return void
     */
    private void buildOperations(BIFBaseOperation[] operationBase, String transSourceAddress, Chain.Transaction.Builder transaction) throws SDKException {
        for (int i = 0; i < operationBase.length; i++) {
            Chain.Operation operation;
            OperationType operationType = operationBase[i].getOperationType();
            switch (operationType) {
                case ACCOUNT_ACTIVATE:
                    operation = BIFAccountServiceImpl.activate((BIFAccountActivateOperation) operationBase[i], transSourceAddress);
                    break;
                case ACCOUNT_SET_METADATA:
                    operation = BIFAccountServiceImpl.accountSetMetadata((BIFAccountSetMetadataOperation) operationBase[i]);
                    break;
                case ACCOUNT_SET_PRIVILEGE:
                    operation = BIFAccountServiceImpl.accountSetPrivilege((BIFAccountSetPrivilegeOperation) operationBase[i]);
                    break;
                case GAS_SEND:
                    operation = BIFGasServiceImpl.send((BIFGasSendOperation) operationBase[i], transSourceAddress);
                    break;
                case CONTRACT_CREATE:
                    operation = BIFContractServiceImpl.create((BIFContractCreateOperation) operationBase[i]);
                    break;
                case CONTRACT_INVOKE:
                    operation = BIFContractServiceImpl.invokeByGas((BIFContractInvokeOperation) operationBase[i], transSourceAddress);
                    break;
                default:
                    throw new SDKException(SdkError.OPERATIONS_ONE_ERROR);
            }
            if (Tools.isEmpty(operation)) {
                throw new SDKException(SdkError.OPERATIONS_ONE_ERROR);
            }
            transaction.addOperations(operation);
        }
    }
    /**
     * @Method getTxCacheSize
     * @Return BIFTransactionGetTxCacheSizeResponse
     */
    @Override
    public BIFTransactionGetTxCacheSizeResponse getTxCacheSize() {
        BIFTransactionGetTxCacheSizeResponse response = new BIFTransactionGetTxCacheSizeResponse();
        Long queueSize=0L;
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String getInfoUrl = General.getInstance().getTxCacheSize(null);
            String result = HttpUtils.httpGet(getInfoUrl);
            response = JsonUtils.toJavaObject(result, BIFTransactionGetTxCacheSizeResponse.class);
            response.buildResponse(SdkError.SUCCESS,response.getQueueSize());
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            response.buildResponse(errorCode, errorDesc, queueSize);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            response.buildResponse(SdkError.CONNECTNETWORK_ERROR, queueSize);
        } catch (Exception e) {
            response.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), queueSize);
        }
        return response;
    }
    /**
     * @Method getTxCacheSize
     * @Return BIFTransactionGetTxCacheSizeResponse
     */
    @Override
    public BIFTransactionGetTxCacheSizeResponse getTxCacheSize(Integer domainId) {
        BIFTransactionGetTxCacheSizeResponse response = new BIFTransactionGetTxCacheSizeResponse();
        Long queueSize=0L;
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String getInfoUrl = General.getInstance().getTxCacheSize(domainId);
            String result = HttpUtils.httpGet(getInfoUrl);
            response = JsonUtils.toJavaObject(result, BIFTransactionGetTxCacheSizeResponse.class);
            response.buildResponse(SdkError.SUCCESS,response.getQueueSize());
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            response.buildResponse(errorCode, errorDesc, queueSize);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            response.buildResponse(SdkError.CONNECTNETWORK_ERROR, queueSize);
        } catch (Exception e) {
            response.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), queueSize);
        }
        return response;
    }
    public static BIFTransactionGetInfoResponse getTransactionInfo(String hash,Integer domainId) throws Exception {
        if (Tools.isEmpty(General.getInstance().getUrl())) {
            throw new SDKException(SdkError.URL_EMPTY_ERROR);
        }
        String getInfoUrl = General.getInstance().transactionGetInfoUrl(hash,domainId);
        String result = HttpUtils.httpGet(getInfoUrl);
        return JsonUtils.toJavaObject(result, BIFTransactionGetInfoResponse.class);
    }
    /**
     * @Method getTxCacheData
     * @Return BIFTransactionCacheRequest
     */
    @Override
    public BIFTransactionCacheResponse getTxCacheData(BIFTransactionCacheRequest request) {
        BIFTransactionCacheResponse response = new BIFTransactionCacheResponse();
        BIFTransactionCacheResult bifTransactionCacheResult = new BIFTransactionCacheResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            Integer domainId = Constant.INIT_ZERO;
            String hash = null;
            if (!Tools.isEmpty(request)) {
                 hash = request.getHash();
                if (!Tools.isEmpty(hash) && hash.length() != Constant.HASH_HEX_LENGTH) {
                    throw new SDKException(SdkError.INVALID_HASH_ERROR);
                }
                domainId=request.getDomainId();
            }

            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String getInfoUrl = General.getInstance().getTxCacheData(domainId,hash);
            String result = HttpUtils.httpGet(getInfoUrl);
            response = JsonUtils.toJavaObject(result, BIFTransactionCacheResponse.class);
            response.buildResponse(SdkError.SUCCESS, response.getResult());
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            response.buildResponse(errorCode, errorDesc, bifTransactionCacheResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            response.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifTransactionCacheResult);
        } catch (Exception e) {
            response.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifTransactionCacheResult);
        }
        return response;
    }

    @Override
    public BIFTransactionParseBlobResponse parseBlob(String blob) {
        BIFTransactionParseBlobResponse transactionParseBlobResponse = new BIFTransactionParseBlobResponse();
        BIFTransactionParseBlobResult transactionParseBlobResult = new BIFTransactionParseBlobResult();
        try {
            if (Tools.isEmpty(blob)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Chain.Transaction transaction = Chain.Transaction.parseFrom(HexFormat.hexToByte(blob));
            JsonFormat jsonFormat = new JsonFormat();
            ByteString meta=transaction.getMetadata();
            String remarks =meta.toStringUtf8();
            String transactionJson = jsonFormat.printToString(transaction);
            transactionParseBlobResult = JsonUtils.toJavaObject(transactionJson, BIFTransactionParseBlobResult.class);
            if(!Tools.isEmpty(remarks)){
                transactionParseBlobResult.setRemarks(remarks);
            }
            transactionParseBlobResponse.buildResponse(SdkError.SUCCESS, transactionParseBlobResult);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            transactionParseBlobResponse.buildResponse(errorCode, errorDesc, transactionParseBlobResult);
        } catch (InvalidProtocolBufferException | IllegalArgumentException e) {
            transactionParseBlobResponse.buildResponse(SdkError.INVALID_SERIALIZATION_ERROR, transactionParseBlobResult);
        } catch (Exception e) {
            transactionParseBlobResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionParseBlobResult);
        }
        return transactionParseBlobResponse;
    }

    /**
     * @Method getBidByHash
     * @Return BIFTransactionGetBidResponse
     */
    @Override
    public BIFTransactionGetBidResponse getBidByHash(BIFTransactionGetInfoRequest request) {
        BIFTransactionGetInfoResponse transactionGetInfoResponse = new BIFTransactionGetInfoResponse();
        BIFTransactionGetInfoResult transactionGetInfoResult = new BIFTransactionGetInfoResult();
        List<String> result=new ArrayList<>();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(request.getHash()) || request.getHash().length() != Constant.HASH_HEX_LENGTH) {
                throw new SDKException(SdkError.INVALID_HASH_ERROR);
            }
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            transactionGetInfoResponse = getTransactionInfo(request.getHash(),domainId);
        }catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionGetInfoResponse.buildResponse(errorCode, errorDesc, transactionGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            transactionGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, transactionGetInfoResult);
        } catch (Exception e) {
            transactionGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionGetInfoResult);
        }
        BIFTransactionGetBidResponse response=new BIFTransactionGetBidResponse();
        if (transactionGetInfoResponse.getErrorCode() == 0) {
            if(transactionGetInfoResponse.getResult().getTotalCount().intValue() > 0){
                BIFTransactionHistory transactionHistory[]=transactionGetInfoResponse.getResult().getTransactions();
                for (BIFTransactionHistory item:transactionHistory) {
                    BIFTransactionInfo transactionInfo=item.getTransaction();
                    if(transactionInfo.getOperations().length > 0){
                        BIFOperation operation[]=transactionInfo.getOperations();
                        for (BIFOperation oper:operation) {
                            BIFGasSendInfo sendInfo= oper.getSendGas();
                            String destAddress= sendInfo.getDestAddress();
                            if(destAddress.equals(Constant.DDO_CONTRACT)){
                                String input=sendInfo.getInput();
                                Map<String, Object> inputMap=JsonUtils.toMap(input);
                                String params=JsonUtils.toJSONString(inputMap.get("params"));
                                BID bid=JsonUtils.toJavaObject(params,BID.class);
                                result.add(bid.getDocument().getId());
                            }
                        }

                    }
                }
            }
            if(result.size() > 0){
                response.buildResponse(SdkError.SUCCESS,result);
            }else{
                response.buildResponse(SdkError.INVALID_HASH_ERROR, result);
            }
        }else{
            response.buildResponse(SdkError.INVALID_HASH_ERROR, result);
        }
        return response;
    }
    @Override
    public BIFTransactionGasSendResponse batchGasSend(BIFBatchGasSendRequest request) {
        BIFTransactionGasSendResponse response = new BIFTransactionGasSendResponse();
        BIFTransactionGasSendResult result = new BIFTransactionGasSendResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String senderAddress = request.getSenderAddress();
            if (!PublicKeyManager.isAddressValid(senderAddress)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            List<BIFGasSendOperation> operations = request.getOperations();
            if(Tools.isEmpty(operations)){
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }
            if(operations.size()>100 || operations.size()==0){
                throw new SDKException(SdkError.OPERATIONS_INVALID_ERROR);
            }
            Map<String, Integer> map = new HashMap<>();
            for (BIFGasSendOperation opt: operations) {
                if(!map.containsKey(opt.getDestAddress())){//map中存在此id，将数据存放当前key的map中
                    if (!PublicKeyManager.isAddressValid(opt.getDestAddress())) {
                        throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
                    }
                    map.put(opt.getDestAddress(),1);
                }
                Long bifAmount = opt.getAmount();
                if (Tools.isEmpty(bifAmount) || bifAmount < Constant.INIT_ZERO) {
                    throw new SDKException(SdkError.INVALID_AMOUNT_ERROR);
                }
            }
            String privateKey = request.getPrivateKey();
            if (Tools.isEmpty(privateKey)) {
                throw new SDKException(SdkError.PRIVATEKEY_NULL_ERROR);
            }
            Long feeLimit = request.getFeeLimit();
            if (Tools.isEmpty(feeLimit)) {
                feeLimit = Constant.FEE_LIMIT;
            }
            if (Tools.isEmpty(feeLimit) || feeLimit < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }
            Long gasPrice = request.getGasPrice();
            if (Tools.isEmpty(gasPrice)) {
                gasPrice = Constant.GAS_PRICE;
            }
            if (Tools.isEmpty(gasPrice) || gasPrice < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }
            Long ceilLedgerSeq = request.getCeilLedgerSeq();
            String remarks = request.getRemarks();

            // 广播交易
            BIFTransactionService transactionService = new BIFTransactionServiceImpl();
            String hash = transactionService.radioGasTransaction(senderAddress, feeLimit, gasPrice, operations,
                    ceilLedgerSeq, remarks, privateKey, domainId);
            result.setHash(hash);
            response.buildResponse(SdkError.SUCCESS, result);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            response.buildResponse(errorCode, errorDesc, result);
        } catch (Exception e) {
            response.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), result);
        }
        return response;
    }
}

