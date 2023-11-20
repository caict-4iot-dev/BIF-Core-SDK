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
package cn.bif.module.account.impl;

import cn.bif.common.Constant;
import cn.bif.common.General;
import cn.bif.common.JsonUtils;
import cn.bif.common.Tools;
import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;
import cn.bif.utils.http.HttpUtils;
import cn.bif.model.request.*;
import cn.bif.model.request.operation.BIFAccountActivateOperation;
import cn.bif.model.request.operation.BIFAccountSetMetadataOperation;
import cn.bif.model.request.operation.BIFAccountSetPrivilegeOperation;
import cn.bif.model.response.*;
import cn.bif.model.response.result.*;
import cn.bif.model.response.result.data.BIFMetadataInfo;
import cn.bif.model.response.result.data.BIFSigner;
import cn.bif.model.response.result.data.BIFTypeThreshold;
import cn.bif.module.account.BIFAccountService;
import cn.bif.module.blockchain.BIFTransactionService;
import cn.bif.module.blockchain.impl.BIFTransactionServiceImpl;
import cn.bif.module.encryption.key.PublicKeyManager;
import cn.bif.protobuf.Chain;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public class BIFAccountServiceImpl implements BIFAccountService {

    /**
     * @Method activate
     * @Params [accountActivateRequest]
     * @Return cn.bif.model.response.AccountActivateResponse
     */
    public static Chain.Operation activate(BIFAccountActivateOperation accountActivateOperation, String transSourceAddress) throws SDKException {
        Chain.Operation operation;
        try {
            if (Tools.isEmpty(accountActivateOperation)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String sourceAddress = accountActivateOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String destAddress = accountActivateOperation.getDestAddress();
            if (!PublicKeyManager.isAddressValid(destAddress)) {
                throw new SDKException(SdkError.INVALID_DESTADDRESS_ERROR);
            }
            boolean isNotValid = (!Tools.isEmpty(sourceAddress) && sourceAddress.equals(destAddress)) || transSourceAddress.equals(destAddress);
            if (isNotValid) {
                throw new SDKException(SdkError.SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR);
            }
            Long initBalance = accountActivateOperation.getInitBalance();
            if (Tools.isEmpty(initBalance) || initBalance <= Constant.INIT_ZERO_L) {
                throw new SDKException(SdkError.INVALID_INITBALANCE_ERROR);
            }
            String metadata = accountActivateOperation.getMetadata();
            // build operation
            operation = buildActivateOperation(sourceAddress, destAddress, initBalance, metadata);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation;
    }

    /**
     * @Method setMetadata
     * @Params [accountSetMetadataRequest]
     * @Return cn.bif.model.response.AccountSetMetadataResponse
     */
    public static Chain.Operation accountSetMetadata(BIFAccountSetMetadataOperation accountSetMetadataOperation) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = accountSetMetadataOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String key = accountSetMetadataOperation.getKey();
            if (Tools.isEmpty(key) || key.length() > Constant.METADATA_KEY_MAX) {
                throw new SDKException(SdkError.INVALID_DATAKEY_ERROR);
            }
            String value = accountSetMetadataOperation.getValue();
            if (Tools.isEmpty(value) || value.length() > Constant.METADATA_VALUE_MAX) {
                throw new SDKException(SdkError.INVALID_DATAVALUE_ERROR);
            }
            Long version = accountSetMetadataOperation.getVersion();
            if (!Tools.isNULL(version) && version < Constant.VERSION) {
                throw new SDKException(SdkError.INVALID_DATAVERSION_ERROR);
            }
            Boolean deleteFlag = accountSetMetadataOperation.getDeleteFlag();
            String metadata = accountSetMetadataOperation.getMetadata();
            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.SET_METADATA);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            Chain.OperationSetMetadata.Builder operationSetMetadata = operation.getSetMetadataBuilder();
            operationSetMetadata.setKey(key);
            operationSetMetadata.setValue(value);
            if (!Tools.isEmpty(version) && version > Constant.VERSION) {
                operationSetMetadata.setVersion(version);
            }
            if (!Tools.isEmpty(deleteFlag) && deleteFlag != false) {
                operationSetMetadata.setDeleteFlag(deleteFlag);
            }
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }

    /**
     * @Method setPrivilege
     * @Params [accountSetPrivilegeRequest]
     * @Return cn.bif.model.response.AccountSetPrivilegeResponse
     */
    public static Chain.Operation accountSetPrivilege(BIFAccountSetPrivilegeOperation accountSetPrivilegeOperation) throws SDKException {
        Chain.Operation operation;
        try {
            String sourceAddress = accountSetPrivilegeOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String masterWeight = accountSetPrivilegeOperation.getMasterWeight();
            if (!Tools.isEmpty(masterWeight)) {
                Pattern pattern = compile("^[-\\+]?[\\d]*$");
                boolean isNumber = pattern.matcher(masterWeight).matches();
                if (!isNumber || Long.valueOf(masterWeight) < Constant.INIT_ZERO_L || Long.valueOf(masterWeight) > Constant.UINT_MAX) {
                    throw new SDKException(SdkError.INVALID_MASTERWEIGHT_ERROR);
                }
            }
            String txThreshold = accountSetPrivilegeOperation.getTxThreshold();
            if (!Tools.isEmpty(txThreshold)) {
                Pattern pattern = compile("^[-\\+]?[\\d]*$");
                boolean isNumber = pattern.matcher(txThreshold).matches();
                if (!isNumber || Long.valueOf(txThreshold) < Constant.INIT_ZERO_L) {
                    throw new SDKException(SdkError.INVALID_TX_THRESHOLD_ERROR);
                }
            }
            String metadata = accountSetPrivilegeOperation.getMetadata();
            // build operation
            BIFSigner[] signers = accountSetPrivilegeOperation.getSigners();
            BIFTypeThreshold[] typeThresholds = accountSetPrivilegeOperation.getTypeThresholds();
            operation = buildSetPrivilegeOperation(sourceAddress, masterWeight, txThreshold, signers, typeThresholds, metadata);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (NumberFormatException exception) {
            throw new SDKException(SdkError.INVALID_TX_THRESHOLD_ERROR);
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        return operation;
    }


    /**
     * @Method getInfo
     * @Params [accountGetInfoRequest]
     * @Return AccountGetInfoResponse
     */
    @Override
    public BIFAccountGetInfoResponse getAccount(BIFAccountGetInfoRequest bifAccountGetInfoRequest) {
        BIFAccountGetInfoResponse bifAccountGetInfoResponse = new BIFAccountGetInfoResponse();

        BIFAccountGetInfoResult bifAccountGetInfoResult = new BIFAccountGetInfoResult();
        try {
            if (Tools.isEmpty(bifAccountGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = bifAccountGetInfoRequest.getAddress();
            if (!PublicKeyManager.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            Integer domainId=bifAccountGetInfoRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            bifAccountGetInfoResponse = getInfo(address,domainId);
            Integer errorCode = bifAccountGetInfoResponse.getErrorCode();
            String errorDesc = bifAccountGetInfoResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }else if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.DOMAINID_ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "DomainId (" + domainId + ") (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(bifAccountGetInfoResponse);
            if (bifAccountGetInfoResponse.getResult().getNonce() == null) {
                bifAccountGetInfoResponse.getResult().setNonce(Constant.INIT_NONCE);
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            bifAccountGetInfoResponse.buildResponse(errorCode, errorDesc, bifAccountGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            bifAccountGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifAccountGetInfoResult);
        } catch (Exception e) {
            bifAccountGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifAccountGetInfoResult);
        }

        return bifAccountGetInfoResponse;
    }

    /**
     * @Method getNonce
     * @Params [accountGetNonceRequest]
     * @Return AccountGetNonceResponse
     */
    @Override
    public BIFAccountGetNonceResponse getNonce(BIFAccountGetNonceRequest bifAccountGetNonceRequest) {
        BIFAccountGetNonceResponse bifAccountGetNonceResponse = new BIFAccountGetNonceResponse();

        BIFAccountGetNonceResult bifAccountGetNonceResult = new BIFAccountGetNonceResult();
        try {
            if (Tools.isEmpty(bifAccountGetNonceRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = bifAccountGetNonceRequest.getAddress();
            if (!PublicKeyManager.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            Integer domainId=bifAccountGetNonceRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetInfoUrl(address,domainId);
            String result = HttpUtils.httpGet(accountGetInfoUrl);
            bifAccountGetNonceResponse = JsonUtils.toJavaObject(result, BIFAccountGetNonceResponse.class);
            Integer errorCode = bifAccountGetNonceResponse.getErrorCode();
            String errorDesc = bifAccountGetNonceResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }else if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.DOMAINID_ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "DomainId (" + domainId + ") (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(bifAccountGetNonceResponse);
            if (bifAccountGetNonceResponse.getResult().getNonce() == null) {
                bifAccountGetNonceResponse.getResult().setNonce(Constant.INIT_NONCE);
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            bifAccountGetNonceResponse.buildResponse(errorCode, errorDesc, bifAccountGetNonceResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            bifAccountGetNonceResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifAccountGetNonceResult);
        } catch (Exception e) {
            bifAccountGetNonceResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifAccountGetNonceResult);
        }

        return bifAccountGetNonceResponse;
    }

    /**
     * @Method getBalance
     * @Params [accountGetBalanceRequest]
     * @Return AccountGetBalanceResponse
     */
    @Override
    public BIFAccountGetBalanceResponse getAccountBalance(BIFAccountGetBalanceRequest bifAccountGetBalanceRequest) {
        BIFAccountGetBalanceResponse bifAccountGetBalanceResponse = new BIFAccountGetBalanceResponse();
        BIFAccountGetBalanceResult bifAccountGetBalanceResult = new BIFAccountGetBalanceResult();
        try {
            if (Tools.isEmpty(bifAccountGetBalanceRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = bifAccountGetBalanceRequest.getAddress();
            if (!PublicKeyManager.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            Integer domainId=bifAccountGetBalanceRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetInfoUrl(address,domainId);
            String result = HttpUtils.httpGet(accountGetInfoUrl);
            bifAccountGetBalanceResponse = JsonUtils.toJavaObject(result, BIFAccountGetBalanceResponse.class);
            Integer errorCode = bifAccountGetBalanceResponse.getErrorCode();
            String errorDesc = bifAccountGetBalanceResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }else if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.DOMAINID_ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "DomainId (" + domainId + ") (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(bifAccountGetBalanceResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            bifAccountGetBalanceResponse.buildResponse(errorCode, errorDesc, bifAccountGetBalanceResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            bifAccountGetBalanceResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifAccountGetBalanceResult);
        } catch (Exception e) {
            bifAccountGetBalanceResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifAccountGetBalanceResult);
        }

        return bifAccountGetBalanceResponse;
    }

    /**
     * @Method getMetadatas
     * @Params [accountGetMetadataRequest]
     * @Return AccountGetMetadataResponse
     */
    @Override
    public BIFAccountGetMetadatasResponse getAccountMetadatas(BIFAccountGetMetadatasRequest bifAccountGetMetadatasRequest) {
        BIFAccountGetMetadatasResponse bifAccountGetMetadatasResponse = new BIFAccountGetMetadatasResponse();
        BIFAccountGetMetadatasResult bifAccountGetMetadatasResult = new BIFAccountGetMetadatasResult();
        try {
            if (Tools.isEmpty(bifAccountGetMetadatasRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = bifAccountGetMetadatasRequest.getAddress();
            if (!PublicKeyManager.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            String key = bifAccountGetMetadatasRequest.getKey();
            if (!Tools.isNULL(key) && (key.length() > Constant.METADATA_KEY_MAX || key.length() < 1)) {
                throw new SDKException(SdkError.INVALID_DATAKEY_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            Integer domainId=bifAccountGetMetadatasRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetMetadataUrl(address, key, domainId);
            String result = HttpUtils.httpGet(accountGetInfoUrl);
            bifAccountGetMetadatasResponse = JsonUtils.toJavaObject(result, BIFAccountGetMetadatasResponse.class);
            Integer errorCode = bifAccountGetMetadatasResponse.getErrorCode();
            String errorDesc = bifAccountGetMetadatasResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }else if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.DOMAINID_ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "DomainId (" + domainId + ") (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(bifAccountGetMetadatasResponse);
            BIFMetadataInfo[] metadataInfos = bifAccountGetMetadatasResponse.getResult().getMetadatas();
            if (Tools.isEmpty(metadataInfos)) {
                throw new SDKException(SdkError.NO_METADATAS_ERROR);
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            bifAccountGetMetadatasResponse.buildResponse(errorCode, errorDesc, bifAccountGetMetadatasResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            bifAccountGetMetadatasResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifAccountGetMetadatasResult);
        } catch (Exception e) {
            bifAccountGetMetadatasResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifAccountGetMetadatasResult);
        }

        return bifAccountGetMetadatasResponse;
    }

    @Override
    public BIFCreateAccountResponse createAccount(BIFCreateAccountRequest request) {
        BIFCreateAccountResponse response = new BIFCreateAccountResponse();
        BIFAccountCreateAccountResult result = new BIFAccountCreateAccountResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String senderAddress = request.getSenderAddress();
            if (!PublicKeyManager.isAddressValid(senderAddress)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            BIFAccountActivateOperation operation = new BIFAccountActivateOperation();
            String destAddress = request.getDestAddress();
            if (!PublicKeyManager.isAddressValid(destAddress)) {
                throw new SDKException(SdkError.INVALID_DESTADDRESS_ERROR);
            }
            Long initBalance = request.getInitBalance();
            if (Tools.isEmpty(initBalance) || initBalance <= Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_INITBALANCE_ERROR);
            }
            operation.setDestAddress(destAddress);
            operation.setInitBalance(initBalance);

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
            Integer domainId= request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
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

    @Override
    public BIFAccountSetMetadatasResponse setMetadatas(BIFAccountSetMetadatasRequest request) {
        BIFAccountSetMetadatasResponse response = new BIFAccountSetMetadatasResponse();
        BIFAccountSetMetadataResult result = new BIFAccountSetMetadataResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String senderAddress = request.getSenderAddress();
            if (!PublicKeyManager.isAddressValid(senderAddress)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            BIFAccountSetMetadataOperation operation = new BIFAccountSetMetadataOperation();
            String key = request.getKey();
            if (Tools.isEmpty(key) || key.length() > Constant.METADATA_KEY_MAX) {
                throw new SDKException(SdkError.INVALID_DATAKEY_ERROR);
            }
            String value = request.getValue();
            if (Tools.isEmpty(value) || value.length() > Constant.METADATA_VALUE_MAX) {
                throw new SDKException(SdkError.INVALID_DATAVALUE_ERROR);
            }
            Long version = request.getVersion();
            Boolean deleteFlag = request.getDeleteFlag();
            operation.setKey(key);
            operation.setValue(value);
            operation.setVersion(version);
            operation.setDeleteFlag(deleteFlag);

            String privateKey = request.getPrivateKey();
            if (Tools.isEmpty(privateKey)) {
                throw new SDKException(SdkError.PRIVATEKEY_NULL_ERROR);
            }

            Long ceilLedgerSeq = request.getCeilLedgerSeq();
            String remarks = request.getRemarks();
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
            // 交易
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

    @Override
    public BIFAccountPrivResponse getAccountPriv(BIFAccountPrivRequest request) {
        BIFAccountPrivResponse response = new BIFAccountPrivResponse();
        BIFAccountPrivResult result = new BIFAccountPrivResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = request.getAddress();
            if (!PublicKeyManager.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            // get info
            String url = General.getInstance().accountGetInfoUrl(address,domainId);
            String resultInfo = HttpUtils.httpGet(url);
            response = JsonUtils.toJavaObject(resultInfo, BIFAccountPrivResponse.class);

            Integer errorCode = response.getErrorCode();
            String errorDesc = response.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }else if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.DOMAINID_ERRORCODE)) {
                throw new SDKException(errorCode, (null == errorDesc ? "DomainId (" + domainId + ") (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(response);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            response.buildResponse(errorCode, errorDesc, result);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            response.buildResponse(SdkError.CONNECTNETWORK_ERROR, result);
        } catch (Exception e) {
            response.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), result);
        }

        return response;
    }

    @Override
    public BIFAccountSetPrivilegeResponse setPrivilege(BIFAccountSetPrivilegeRequest request) {
        BIFAccountSetPrivilegeResponse response = new BIFAccountSetPrivilegeResponse();
        BIFAccountSetPrivilegeResult result = new BIFAccountSetPrivilegeResult();
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

            BIFAccountSetPrivilegeOperation operation = new BIFAccountSetPrivilegeOperation();
            BIFSigner[] signers = request.getSigners();
            String txThreshold = request.getTxThreshold();
            BIFTypeThreshold[] typeThresholds = request.getTypeThresholds();
            String masterWeight = request.getMasterWeight();
            operation.setSigners(signers);
            operation.setTxThreshold(txThreshold);
            operation.setMasterWeight(masterWeight);
            operation.setTypeThresholds(typeThresholds);

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
            BIFTransactionService transactionService = new BIFTransactionServiceImpl();
            String hash = transactionService.radioTransaction(senderAddress, feeLimit, gasPrice, operation, ceilLedgerSeq, remarks, privateKey, domainId);
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

    public static Chain.Operation buildActivateOperation(String sourceAddress,
                                                         String destAddress, Long initBalance, String metadata) {
        Chain.Operation.Builder operation = Chain.Operation.newBuilder();
        operation.setType(Chain.Operation.Type.CREATE_ACCOUNT);
        if (!Tools.isEmpty(sourceAddress)) {
            operation.setSourceAddress(sourceAddress);
        }
        if (!Tools.isEmpty(metadata)) {
            operation.setMetadata(ByteString.copyFromUtf8(metadata));
        }
        Chain.OperationCreateAccount.Builder operationCreateAccount = operation.getCreateAccountBuilder();
        operationCreateAccount.setDestAddress(destAddress);
        operationCreateAccount.setInitBalance(initBalance);
        Chain.AccountPrivilege.Builder accountPrivilege = operationCreateAccount.getPrivBuilder();
        accountPrivilege.setMasterWeight(Constant.INIT_ONE);
        Chain.AccountThreshold.Builder accountThreshold = accountPrivilege.getThresholdsBuilder();
        accountThreshold.setTxThreshold(Constant.INIT_ONE);
        return operation.build();
    }

    public static Chain.Operation buildSetPrivilegeOperation(String sourceAddress, String masterWeight,
                                                             String txThreshold, BIFSigner[] signers, BIFTypeThreshold[] typeThresholds, String metadata) {
        Chain.Operation.Builder operation = Chain.Operation.newBuilder();
        operation.setType(Chain.Operation.Type.SET_PRIVILEGE);
        if (!Tools.isEmpty(sourceAddress)) {
            operation.setSourceAddress(sourceAddress);
        }
        if (!Tools.isEmpty(metadata)) {
            operation.setMetadata(ByteString.copyFromUtf8(metadata));
        }
        Chain.OperationSetPrivilege.Builder operationSetPrivilege = operation.getSetPrivilegeBuilder();
        if (!Tools.isEmpty(masterWeight)) {
            operationSetPrivilege.setMasterWeight(masterWeight);
        }
        if (!Tools.isEmpty(txThreshold)) {
            operationSetPrivilege.setTxThreshold(txThreshold);
        }

        // add signers
        if (!Tools.isEmpty(signers)) {
            int i = 0;
            int signersLength = signers.length;
            for (; i < signersLength; i++) {
                BIFSigner signer = signers[i];
                String signerAddress = signer.getAddress();
                if (!PublicKeyManager.isAddressValid(signerAddress)) {
                    throw new SDKException(SdkError.INVALID_SIGNER_ADDRESS_ERROR);
                }
                Long signerWeight = signer.getWeight();
                if (Tools.isEmpty(signerWeight) || signerWeight < Constant.INIT_ZERO || signerWeight > Constant.UINT_MAX) {
                    throw new SDKException(SdkError.INVALID_SIGNER_WEIGHT_ERROR);
                }
                Chain.Signer.Builder signerBuilder = operationSetPrivilege.addSignersBuilder();
                signerBuilder.setAddress(signerAddress);
                signerBuilder.setWeight(signerWeight);
            }
        }

        // add type_thresholds
        if (!Tools.isEmpty(typeThresholds)) {
            int i = 0;
            int typeThresholdsLength = typeThresholds.length;
            for (; i < typeThresholdsLength; i++) {
                BIFTypeThreshold typeThreshold = typeThresholds[i];
                Integer type = typeThreshold.getType();
                if (Tools.isEmpty(type) || type < Constant.INIT_ONE) {
                    throw new SDKException(SdkError.INVALID_TYPETHRESHOLD_TYPE_ERROR);
                }
                Long threshold = typeThreshold.getThreshold();
                if (Tools.isEmpty(threshold) || threshold < Constant.INIT_ZERO) {
                    throw new SDKException(SdkError.INVALID_TYPE_THRESHOLD_ERROR);
                }
                Chain.OperationTypeThreshold.Builder typeThresholdBuilder = operationSetPrivilege.addTypeThresholdsBuilder();
                Chain.Operation.Type operationType = Chain.Operation.Type.forNumber(type);
                if (Tools.isEmpty(operationType)) {
                    throw new SDKException(SdkError.INVALID_TYPETHRESHOLD_TYPE_ERROR);
                }
                typeThresholdBuilder.setType(operationType);
                typeThresholdBuilder.setThreshold(threshold);
            }
        }
        return operation.build();
    }


    private static BIFAccountGetInfoResponse getInfo(String address,Integer domainId) throws Exception {
        String accountGetInfoUrl = General.getInstance().accountGetInfoUrl(address,domainId);
        String result = HttpUtils.httpGet(accountGetInfoUrl);
        return JsonUtils.toJavaObject(result, BIFAccountGetInfoResponse.class);
    }
}
