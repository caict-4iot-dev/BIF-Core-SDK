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

import cn.bif.common.Constant;
import cn.bif.common.General;
import cn.bif.common.JsonUtils;
import cn.bif.common.Tools;
import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;
import cn.bif.model.request.*;
import cn.bif.utils.http.HttpUtils;
import cn.bif.model.response.*;
import cn.bif.model.response.result.*;
import cn.bif.module.blockchain.BIFBlockService;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class BIFBlockServiceImpl implements BIFBlockService {
    /**
     * @Method getNumber
     * @Params []
     * @Return BlockGetNumberResponse
     */
    @Override
    public BIFBlockGetNumberResponse getBlockNumber(BIFBlockGetNumberInfoRequest request) {
        BIFBlockGetNumberResponse blockGetNumberResponse = new BIFBlockGetNumberResponse();
        BIFBlockGetNumberResult bifBlockGetNumberResult = new BIFBlockGetNumberResult();
        try {
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
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
            String getNumberUrl = General.getInstance().blockGetNumberUrl(domainId);
            String result = HttpUtils.httpGet(getNumberUrl);
            blockGetNumberResponse = JsonUtils.toJavaObject(result, BIFBlockGetNumberResponse.class);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetNumberResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifBlockGetNumberResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetNumberResponse.buildResponse(errorCode, errorDesc, bifBlockGetNumberResult);
        } catch (Exception e) {
            blockGetNumberResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifBlockGetNumberResult);
        }
        return blockGetNumberResponse;
    }

    /**
     * @Method getTransactions
     * @Params [blockGetTransactionsRequest]
     * @Return BlockGetTransactionsResponse
     */
    @Override
    public BIFBlockGetTransactionsResponse getTransactions(BIFBlockGetTransactionsRequest blockGetTransactionsRequest) {
        BIFBlockGetTransactionsResponse blockGetTransactions = new BIFBlockGetTransactionsResponse();
        BIFBlockGetTransactionsResult transactionGetInfoResult = new BIFBlockGetTransactionsResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetTransactionsRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetTransactionsRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < Constant.INIT_ONE_L) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            Integer domainId=blockGetTransactionsRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String getTransactionsUrl = General.getInstance().blockGetTransactionsUrl(blockNumber,domainId);
            String result = HttpUtils.httpGet(getTransactionsUrl);
            blockGetTransactions = JsonUtils.toJavaObject(result, BIFBlockGetTransactionsResponse.class);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetTransactions.buildResponse(errorCode, errorDesc, transactionGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetTransactions.buildResponse(SdkError.CONNECTNETWORK_ERROR, transactionGetInfoResult);
        } catch (Exception e) {
            blockGetTransactions.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionGetInfoResult);
        }
        return blockGetTransactions;
    }

    /**
     * @Method getInfo
     * @Params [blockGetInfoRequest]
     * @Return BlockGetInfoResponse
     */
    @Override
    public BIFBlockGetInfoResponse getBlockInfo(BIFBlockGetInfoRequest blockGetInfoRequest) {
        BIFBlockGetInfoResponse blockGetInfoResponse = new BIFBlockGetInfoResponse();
        BIFBlockGetInfoResult bifBlockGetInfoResult = new BIFBlockGetInfoResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetInfoRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < Constant.INIT_ONE_L) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            Integer domainId=blockGetInfoRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            Boolean withLeader = false;
            if (!Tools.isEmpty(blockGetInfoRequest.getWithLeader())) {
                withLeader = blockGetInfoRequest.getWithLeader();
            }
            String getInfoUrl = General.getInstance().blockGetInfoUrl(blockNumber,domainId,withLeader);
            String result = HttpUtils.httpGet(getInfoUrl);
            blockGetInfoResponse = JsonUtils.toJavaObject(result, BIFBlockGetInfoResponse.class);
            Integer errorCode = blockGetInfoResponse.getErrorCode();
            String errorDesc = blockGetInfoResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(4, (Tools.isEmpty(errorDesc) ? "Block (" + blockNumber + ") does not exist" : errorDesc));
            }
            SdkError.checkErrorCode(blockGetInfoResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetInfoResponse.buildResponse(errorCode, errorDesc, bifBlockGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifBlockGetInfoResult);
        } catch (Exception e) {
            blockGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifBlockGetInfoResult);
        }

        return blockGetInfoResponse;
    }

    /**
     * @Method getLatestInfo
     * @Params []
     * @Return BlockGetLatestInfoResponse
     */
    @Override
    public BIFBlockGetLatestInfoResponse getBlockLatestInfo(BIFBlockGetLatestInfoRequest request) {
        BIFBlockGetLatestInfoResponse blockGetLatestInfoResponse = new BIFBlockGetLatestInfoResponse();
        BIFBlockGetLatestInfoResult bifBlockGetLatestInfoResult = new BIFBlockGetLatestInfoResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetLatestInfoUrl(domainId);
            String result = HttpUtils.httpGet(getInfoUrl);
            blockGetLatestInfoResponse = JsonUtils.toJavaObject(result, BIFBlockGetLatestInfoResponse.class);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetLatestInfoResponse.buildResponse(errorCode, errorDesc, bifBlockGetLatestInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetLatestInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifBlockGetLatestInfoResult);
        } catch (Exception e) {
            blockGetLatestInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifBlockGetLatestInfoResult);
        }
        return blockGetLatestInfoResponse;
    }

    /**
     * @Method getValidators
     * @Params [blockGetValidatorsRequest]
     * @Return BlockGetValidatorsResponse
     */
    @Override
    public BIFBlockGetValidatorsResponse getValidators(BIFBlockGetValidatorsRequest blockGetValidatorsRequest) {
        BIFBlockGetValidatorsResponse blockGetValidatorsResponse = new BIFBlockGetValidatorsResponse();
        BIFBlockGetValidatorsResult blockGetValidatorsResult = new BIFBlockGetValidatorsResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetValidatorsRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetValidatorsRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < 1) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            Integer domainId=blockGetValidatorsRequest.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetValidatorsUrl(blockNumber,domainId);
            String result = HttpUtils.httpGet(getInfoUrl);
            blockGetValidatorsResponse = JsonUtils.toJavaObject(result, BIFBlockGetValidatorsResponse.class);
            Integer errorCode = blockGetValidatorsResponse.getErrorCode();
            String errorDesc = blockGetValidatorsResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode.equals(Constant.ERRORCODE)) {
                throw new SDKException(Constant.ERRORCODE, (Tools.isEmpty(errorDesc) ? "Block (" + blockNumber + ") does not exist" : errorDesc));
            }
            SdkError.checkErrorCode(blockGetValidatorsResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetValidatorsResponse.buildResponse(errorCode, errorDesc, blockGetValidatorsResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetValidatorsResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetValidatorsResult);
        } catch (Exception e) {
            blockGetValidatorsResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetValidatorsResult);
        }

        return blockGetValidatorsResponse;
    }

    /**
     * @Method getLatestValidators
     * @Params []
     * @Return BlockGetLatestValidatorsResponse
     */
    @Override
    public BIFBlockGetLatestValidatorsResponse getLatestValidators(BIFBlockGetLatestValidatorsRequest request) {
        BIFBlockGetLatestValidatorsResponse blockGetLatestValidatorsResponse = new BIFBlockGetLatestValidatorsResponse();
        BIFBlockGetLatestValidatorsResult bifBlockGetLatestValidatorsResult = new BIFBlockGetLatestValidatorsResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(request)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Integer domainId=request.getDomainId();
            if(Tools.isNULL(domainId)){
                domainId=Constant.INIT_ZERO;
            }
            if(!Tools.isNULL(domainId) && domainId < Constant.INIT_ZERO){
                throw new SDKException(SdkError.INVALID_DOMAINID_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetLatestValidatorsUrl(domainId);
            String result = HttpUtils.httpGet(getInfoUrl);
            blockGetLatestValidatorsResponse = JsonUtils.toJavaObject(result, BIFBlockGetLatestValidatorsResponse.class);
            SdkError.checkErrorCode(blockGetLatestValidatorsResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetLatestValidatorsResponse.buildResponse(errorCode, errorDesc, bifBlockGetLatestValidatorsResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetLatestValidatorsResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, bifBlockGetLatestValidatorsResult);
        } catch (Exception e) {
            blockGetLatestValidatorsResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), bifBlockGetLatestValidatorsResult);
        }

        return blockGetLatestValidatorsResponse;
    }
}
