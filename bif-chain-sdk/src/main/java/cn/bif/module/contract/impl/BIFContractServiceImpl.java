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
package cn.bif.module.contract.impl;

import cn.bif.common.Constant;
import cn.bif.common.General;
import cn.bif.common.JsonUtils;
import cn.bif.common.Tools;
import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;
import cn.bif.utils.http.HttpUtils;
import cn.bif.model.request.*;
import cn.bif.model.request.operation.BIFContractCreateOperation;
import cn.bif.model.request.operation.BIFContractInvokeOperation;
import cn.bif.model.response.*;
import cn.bif.model.response.result.*;
import cn.bif.model.response.result.data.BIFContractAddressInfo;
import cn.bif.model.response.result.data.BIFContractInfo;
import cn.bif.model.response.result.data.BIFTransactionHistory;
import cn.bif.module.blockchain.BIFTransactionService;
import cn.bif.module.blockchain.impl.BIFTransactionServiceImpl;
import cn.bif.module.contract.BIFContractService;
import cn.bif.module.encryption.key.PublicKeyManager;
import cn.bif.protobuf.Chain;

import com.google.protobuf.ByteString;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.stream.Collectors;


public class BIFContractServiceImpl implements BIFContractService {
    /**
     * @Method create
     * @Params [contractCreateOperation]
     * @Return cn.bif.model.response.ContractCreateResponse
     */
    public static Chain.Operation create(BIFContractCreateOperation contractCreateOperation) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = contractCreateOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            Long initBalance = contractCreateOperation.getInitBalance();
            if (!Tools.isEmpty(initBalance) && initBalance <= Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_INITBALANCE_ERROR);
            }
            Integer type = contractCreateOperation.getType();
            if (!Tools.isEmpty(type) && type < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_CONTRACT_TYPE_ERROR);
            }
            String payload = contractCreateOperation.getPayload();
            if (Tools.isEmpty(payload)) {
                throw new SDKException(SdkError.PAYLOAD_EMPTY_ERROR);
            }
            String metadata = contractCreateOperation.getMetadata();
            String initInput = contractCreateOperation.getInitInput();
            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.CREATE_ACCOUNT);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }

            Chain.OperationCreateAccount.Builder operationCreateContract = operation.getCreateAccountBuilder();
            if (!Tools.isEmpty(initBalance)) {
                operationCreateContract.setInitBalance(initBalance);
            }

            if (!Tools.isEmpty(initInput)) {
                operationCreateContract.setInitInput(initInput);
            }
            Chain.Contract.Builder contract = operationCreateContract.getContractBuilder();
            if (!Tools.isEmpty(type)) {
                Chain.Contract.ContractType contractType = Chain.Contract.ContractType.forNumber(type);
                if (Tools.isEmpty(contractType)) {
                    throw new SDKException(SdkError.INVALID_CONTRACT_TYPE_ERROR);
                }
                contract.setType(contractType);
            }
            contract.setPayload(payload);
            Chain.AccountPrivilege.Builder accountPrivilege = operationCreateContract.getPrivBuilder();
            accountPrivilege.setMasterWeight(Constant.INIT_ZERO);
            Chain.AccountThreshold.Builder accountThreshold = accountPrivilege.getThresholdsBuilder();
            accountThreshold.setTxThreshold(Constant.INIT_ONE);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }


    /**
     * @Method invokeByGas
     * @Params [contractInvokeByGasOperation]
     * @Return cn.bif.model.response.ContractInvokeByGasResponse
     */
    public static Chain.Operation invokeByGas(BIFContractInvokeOperation contractInvokeByGasOperation, String transSourceAddress) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = contractInvokeByGasOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String contractAddress = contractInvokeByGasOperation.getContractAddress();
            if (!PublicKeyManager.isAddressValid(contractAddress)) {
                throw new SDKException(SdkError.INVALID_CONTRACTADDRESS_ERROR);
            }
            if ((!Tools.isEmpty(sourceAddress) && sourceAddress.equals(contractAddress)) || transSourceAddress.equals(contractAddress)) {
                throw new SDKException(SdkError.SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR);
            }
            Long bifAmount = contractInvokeByGasOperation.getBIFAmount();
            if (Tools.isEmpty(bifAmount) || bifAmount < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_AMOUNT_ERROR);
            }
            String metadata = contractInvokeByGasOperation.getMetadata();
            Integer domainId=contractInvokeByGasOperation.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
//            if (!checkContractValid(contractAddress,domainId)) {
//                throw new SDKException(SdkError.CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR);
//            }
            String input = contractInvokeByGasOperation.getInput();

            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.PAY_COIN);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            Chain.OperationPayCoin.Builder operationPayCoin = operation.getPayCoinBuilder();
            operationPayCoin.setDestAddress(contractAddress);
            operationPayCoin.setAmount(bifAmount);
            if (!Tools.isEmpty(input)) {
                operationPayCoin.setInput(input);
            }
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }

    public static BIFContractCallResponse callContract(String sourceAddress, String contractAddress, Integer optType,
                                                       String input, Long gasPrice, Long feeLimit,Integer domainId)
            throws Exception {
        if (Tools.isEmpty(General.getInstance().getUrl())) {
            throw new SDKException(SdkError.URL_EMPTY_ERROR);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("opt_type", optType);
        params.put("fee_limit", feeLimit);
        if (!Tools.isEmpty(sourceAddress)) {
            params.put("source_address", sourceAddress);
        }
        if (!Tools.isEmpty(contractAddress)) {
            params.put("contract_address", contractAddress);
        }
        if (!Tools.isEmpty(input)) {
            params.put("input", input);
        }
        if (!Tools.isEmpty(gasPrice)) {
            params.put("gas_price", gasPrice);
        }
        params.put("domain_id",domainId);
        // call contract
        String contractCallUrl = General.getInstance().contractCallUrl();
        String result = HttpUtils.httpPost(contractCallUrl, JsonUtils.toJSONString(params));
        return JsonUtils.toJavaObject(result, BIFContractCallResponse.class);
    }


    private static BIFContractGetInfoResponse getContractInfo(String contractAddress,Integer domainId) throws Exception {
        if (Tools.isEmpty(General.getInstance().getUrl())) {
            throw new SDKException(SdkError.URL_EMPTY_ERROR);
        }
        BIFContractGetInfoResponse contractGetInfoResponse;
        String contractGetInfoUrl = General.getInstance().accountGetInfoUrl(contractAddress,domainId);
        String result = HttpUtils.httpGet(contractGetInfoUrl);
        contractGetInfoResponse = JsonUtils.toJavaObject(result, BIFContractGetInfoResponse.class);
        Integer errorCode = contractGetInfoResponse.getErrorCode();
        String errorDesc = contractGetInfoResponse.getErrorDesc();
        if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
            throw new SDKException(errorCode, (null == errorDesc ? "contract account (" + contractAddress + ") doest not exist" : errorDesc));
        }else if(!Tools.isEmpty(errorCode) && errorCode.equals(Constant.DOMAINID_ERRORCODE)){
            throw new SDKException(errorCode, (null == errorDesc ? "Domainid(" + domainId + ") (" + contractAddress + ") doest not exist" : errorDesc));
        }
        SdkError.checkErrorCode(contractGetInfoResponse);
        BIFContractInfo contractInfo = contractGetInfoResponse.getResult().getContract();
        if (Tools.isEmpty(contractInfo)) {
            throw new SDKException(SdkError.CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR);
        }
        String payLoad = contractInfo.getPayload();
        if (Tools.isEmpty(payLoad)) {
            throw new SDKException(SdkError.CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR);
        }
        return contractGetInfoResponse;
    }

    private static boolean checkContractValid(String contractAddress,Integer domainId) throws Exception {
        boolean isValid = false;
        try {
            getContractInfo(contractAddress,domainId);
            isValid = true;
        } catch (SDKException sdkException) {
        }
        return isValid;
    }

    /**
     * @Method getInfo
     * @Params [contractGetInfoRequest]
     * @Return ContractGetInfoResponse
     */
    @Override
    public BIFContractGetInfoResponse getContractInfo(BIFContractGetInfoRequest contractGetInfoRequest) {
        BIFContractGetInfoResponse contractGetInfoResponse = new BIFContractGetInfoResponse();
        BIFContractGetInfoResult contractGetInfoResult = new BIFContractGetInfoResult();
        try {
            if (Tools.isEmpty(contractGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String contractAddress = contractGetInfoRequest.getContractAddress();
            if (!PublicKeyManager.isAddressValid(contractAddress)) {
                throw new SDKException(SdkError.INVALID_CONTRACTADDRESS_ERROR);
            }
            Integer domainId=contractGetInfoRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId)  && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            contractGetInfoResponse = getContractInfo(contractAddress,domainId);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            contractGetInfoResponse.buildResponse(errorCode, errorDesc, contractGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            contractGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, contractGetInfoResult);
        } catch (Exception e) {
            contractGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), contractGetInfoResult);
        }

        return contractGetInfoResponse;
    }

    /**
     * @Method checkValid
     * @Params [contractCheckValidRequest]
     * @Return ContractCheckValidResponse
     */
    @Override
    public BIFContractCheckValidResponse checkContractAddress(BIFContractCheckValidRequest contractCheckValidRequest) {
        BIFContractCheckValidResponse contractCheckValidResponse = new BIFContractCheckValidResponse();
        BIFContractCheckValidResult contractCheckValidResult = new BIFContractCheckValidResult();
        try {
            if (Tools.isEmpty(contractCheckValidRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String contractAddress = contractCheckValidRequest.getContractAddress();
            if (!PublicKeyManager.isAddressValid(contractAddress)) {
                throw new SDKException(SdkError.INVALID_CONTRACTADDRESS_ERROR);
            }
            Integer domainId=contractCheckValidRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            boolean isValid = checkContractValid(contractAddress,domainId);
            contractCheckValidResult.setValid(isValid);
            contractCheckValidResponse.buildResponse(SdkError.SUCCESS, contractCheckValidResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            contractCheckValidResponse.buildResponse(errorCode, errorDesc, contractCheckValidResult);
        } catch (Exception e) {
            contractCheckValidResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), contractCheckValidResult);
        }
        return contractCheckValidResponse;
    }

    /**
     * @Method call
     * @Params [contractCallRequest]
     * @Return ContractCallResponse
     */
    @Override
    public BIFContractCallResponse contractQuery(BIFContractCallRequest contractCallRequest) {
        BIFContractCallResponse contractCallResponse = new BIFContractCallResponse();
        BIFContractCallResult contractCallResult = new BIFContractCallResult();
        try {
            if (Tools.isEmpty(contractCallRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String sourceAddress = contractCallRequest.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !sourceAddress.isEmpty() && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String contractAddress = contractCallRequest.getContractAddress();
            if (!Tools.isNULL(contractAddress) && !contractAddress.isEmpty() && !PublicKeyManager.isAddressValid(contractAddress)) {
                throw new SDKException(SdkError.INVALID_CONTRACTADDRESS_ERROR);
            }
            if (!Tools.isEmpty(sourceAddress) && !Tools.isNULL(contractAddress) && sourceAddress.equals(contractAddress)) {
                throw new SDKException(SdkError.SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR);
            }
            String input = contractCallRequest.getInput();
            Long feeLimit = contractCallRequest.getFeeLimit();
            if (Tools.isEmpty(feeLimit)) {
                feeLimit = Constant.FEE_LIMIT;
            }
            if (Tools.isEmpty(feeLimit) || feeLimit < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }

            Long gasPrice = contractCallRequest.getGasPrice();
            if (Tools.isEmpty(gasPrice)) {
                gasPrice = Constant.GAS_PRICE;
            }
            if (Tools.isEmpty(gasPrice) || gasPrice < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }
            Integer domainId=contractCallRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            contractCallResponse = callContract(sourceAddress, contractAddress, Constant.CONTRACT_QUERY_OPT_TYPE, input,gasPrice,
                    feeLimit,domainId);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            contractCallResponse.buildResponse(errorCode, errorDesc, contractCallResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            contractCallResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, contractCallResult);
        } catch (Exception e) {
            contractCallResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), contractCallResult);
        }
        return contractCallResponse;
    }

    @Override
    public BIFContractGetAddressResponse getContractAddress(BIFContractGetAddressRequest contractGetAddressRequest) {
        BIFContractGetAddressResponse contractGetAddressResponse = new BIFContractGetAddressResponse();
        BIFContractGetAddressResult contractGetAddressResult = new BIFContractGetAddressResult();
        try {
            if (Tools.isEmpty(contractGetAddressRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String hash = contractGetAddressRequest.getHash();
            if (Tools.isEmpty(hash) || hash.length() != Constant.HASH_HEX_LENGTH) {
                throw new SDKException(SdkError.INVALID_HASH_ERROR);
            }
            Integer domainId=contractGetAddressRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            BIFTransactionGetInfoResponse transactionGetInfoResponse = BIFTransactionServiceImpl.getTransactionInfo(hash,domainId);
            SdkError.checkErrorCode(transactionGetInfoResponse);
            BIFTransactionHistory transactionHistory = transactionGetInfoResponse.getResult().getTransactions()[0];
            if (Tools.isEmpty(transactionHistory)) {
                throw new SDKException(SdkError.INVALID_CONTRACT_HASH_ERROR);
            }
            SdkError.checkErrorCode(transactionHistory.getErrorCode(), transactionHistory.getErrorDesc());
            String contractAddress = transactionHistory.getErrorDesc();
            if (Tools.isEmpty(contractAddress)) {
                throw new SDKException(SdkError.INVALID_CONTRACT_HASH_ERROR);
            }
            List<BIFContractAddressInfo> contractAddressInfos = JsonUtils.toJavaObjectList(contractAddress, BIFContractAddressInfo.class);
            if (Tools.isEmpty(contractAddressInfos)) {
                throw new SDKException(SdkError.INVALID_CONTRACT_HASH_ERROR);
            }
            contractGetAddressResult.setContractAddressInfos(contractAddressInfos);
            contractGetAddressResponse.buildResponse(SdkError.SUCCESS, contractGetAddressResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            contractGetAddressResponse.buildResponse(errorCode, errorDesc, contractGetAddressResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            contractGetAddressResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, contractGetAddressResult);
        } catch (Exception e) {
            contractGetAddressResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), contractGetAddressResult);
        }
        return contractGetAddressResponse;
    }


    @Override
    public BIFContractCreateResponse contractCreate(BIFContractCreateRequest request) {
        BIFContractCreateResponse response = new BIFContractCreateResponse();
        BIFContractCreateResult result = new BIFContractCreateResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String senderAddress = request.getSenderAddress();
            if (!PublicKeyManager.isAddressValid(senderAddress)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            BIFContractCreateOperation operation = new BIFContractCreateOperation();
            Long initBalance = request.getInitBalance();
            if (!Tools.isEmpty(initBalance) && initBalance <= Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_INITBALANCE_ERROR);
            }
            String payload = request.getPayload();
            if (Tools.isEmpty(payload)) {
                throw new SDKException(SdkError.PAYLOAD_EMPTY_ERROR);
            }
            String initInput = request.getInitInput();
            Integer type = request.getType();
            operation.setInitBalance(initBalance);
            operation.setPayload(payload);
            operation.setInitInput(initInput);
            operation.setType(type);

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
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            // 广播交易
            BIFTransactionService transactionService = new BIFTransactionServiceImpl();
            String hash = transactionService.radioTransaction(senderAddress, feeLimit, gasPrice, operation,
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

    @Override
    public BIFContractInvokeResponse batchContractInvoke(BIFBatchContractInvokeRequest request) {
        BIFContractInvokeResponse response = new BIFContractInvokeResponse();
        BIFContractInvokeResult result = new BIFContractInvokeResult();
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
            List<BIFContractInvokeOperation> operations = request.getOperations();
            if(Tools.isEmpty(operations)){
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }
            Map<String, List<BIFContractInvokeOperation>> map = new HashMap<>();
            for (BIFContractInvokeOperation opt: operations) {
                if(map.containsKey(opt.getContractAddress())){//map中存在此id，将数据存放当前key的map中
                    map.get(opt.getContractAddress()).add(opt);
                }else{//map中不存在，新建key，用来存放数据
                    List<BIFContractInvokeOperation> tmpList = new ArrayList<>();
                    tmpList.add(opt);
                    if (!PublicKeyManager.isAddressValid(opt.getContractAddress())) {
                        throw new SDKException(SdkError.INVALID_CONTRACTADDRESS_ERROR);
                    }
                    if (!checkContractValid(opt.getContractAddress(),domainId)) {
                        throw new SDKException(SdkError.CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR);
                    }
                    map.put(opt.getContractAddress(), tmpList);
                }
                Long bifAmount = opt.getBIFAmount();
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
            String hash = transactionService.radioTransaction(senderAddress, feeLimit, gasPrice, operations,
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

    @Override
    public BIFContractInvokeResponse contractInvoke(BIFContractInvokeRequest request) {
        BIFContractInvokeResponse response = new BIFContractInvokeResponse();
        BIFContractInvokeResult result = new BIFContractInvokeResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String senderAddress = request.getSenderAddress();
            if (!PublicKeyManager.isAddressValid(senderAddress)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            BIFContractInvokeOperation operation = new BIFContractInvokeOperation();
            String contractAddress = request.getContractAddress();
            if (!PublicKeyManager.isAddressValid(contractAddress)) {
                throw new SDKException(SdkError.INVALID_CONTRACTADDRESS_ERROR);
            }
            Long bifAmount = request.getBIFAmount();
            if (Tools.isEmpty(bifAmount) || bifAmount < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_AMOUNT_ERROR);
            }
            String input = request.getInput();
            operation.setContractAddress(contractAddress);
            operation.setBIFAmount(bifAmount);
            operation.setInput(input);

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
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            if (!checkContractValid(contractAddress,domainId)) {
                throw new SDKException(SdkError.CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR);
            }
            // 广播交易
            BIFTransactionService transactionService = new BIFTransactionServiceImpl();
            String hash = transactionService.radioTransaction(senderAddress, feeLimit, gasPrice, operation,
                    ceilLedgerSeq, remarks, privateKey,domainId);
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
