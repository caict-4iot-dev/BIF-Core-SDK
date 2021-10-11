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
import cn.bif.utils.http.HttpUtils;
import cn.bif.model.request.BIFPrivateTransactionReceiveRawRequest;
import cn.bif.model.request.BIFPrivateTransactionReceiveRequest;
import cn.bif.model.request.BIFPrivateTransactionSendRequest;
import cn.bif.model.request.BIFPrivateTransactionStoreRawRequest;
import cn.bif.model.response.BIFPrivateTransactionReceiveRawResponse;
import cn.bif.model.response.BIFPrivateTransactionReceiveResponse;
import cn.bif.model.response.BIFPrivateTransactionSendResponse;
import cn.bif.model.response.BIFPrivateTransactionStoreRawResponse;
import cn.bif.model.response.result.BIFPrivateTransactionReceiveRawResult;
import cn.bif.model.response.result.BIFPrivateTransactionReceiveResult;
import cn.bif.model.response.result.BIFPrivateTransactionSendResult;
import cn.bif.model.response.result.BIFPrivateTransactionStoreRawResult;
import cn.bif.module.blockchain.BIFPrivateTransactionService;

import java.util.HashMap;
import java.util.Map;


public class BIFPrivateTransactionServiceImpl implements BIFPrivateTransactionService {

    @Override
    public BIFPrivateTransactionStoreRawResponse storeRaw(BIFPrivateTransactionStoreRawRequest privateTransactionStoreRawRequest) {
        BIFPrivateTransactionStoreRawResponse privateTransactionStoreRawResponse = new BIFPrivateTransactionStoreRawResponse();
        BIFPrivateTransactionStoreRawResult privateTransactionStoreRawResult = new BIFPrivateTransactionStoreRawResult();
        try {
            if (Tools.isEmpty(privateTransactionStoreRawRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String payload = privateTransactionStoreRawRequest.getPayload();
            String from = privateTransactionStoreRawRequest.getFrom();
            if (Tools.isEmpty(payload)) {
                throw new SDKException(SdkError.INVALID_PRITX_PAYLAOD_ERROR);
            }
            if (Tools.isEmpty(from)) {
                throw new SDKException(SdkError.INVALID_PRITX_FROM_ERROR);
            }
            Map<String, Object> params = new HashMap<>();
            params.put("payload", payload);
            params.put("from", from);
            String priTxStoreRawUrl = General.getInstance().priTxStoreRaw();
            String result = HttpUtils.httpPost(priTxStoreRawUrl, JsonUtils.toJSONString(params));

            privateTransactionStoreRawResult = JsonUtils.toJavaObject(result, BIFPrivateTransactionStoreRawResult.class);
            privateTransactionStoreRawResponse.buildResponse(SdkError.SUCCESS, privateTransactionStoreRawResult);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            privateTransactionStoreRawResponse.buildResponse(errorCode, errorDesc, privateTransactionStoreRawResult);
        } catch (Exception e) {
            privateTransactionStoreRawResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), privateTransactionStoreRawResult);
        }

        return privateTransactionStoreRawResponse;
    }

    @Override
    public BIFPrivateTransactionReceiveRawResponse receiveRaw(BIFPrivateTransactionReceiveRawRequest privateTransactionReceiveRawRequest) {
        BIFPrivateTransactionReceiveRawResponse privateTransactionReceiveRawResponse = new BIFPrivateTransactionReceiveRawResponse();
        BIFPrivateTransactionReceiveRawResult privateTransactionReceiveRawResult = new BIFPrivateTransactionReceiveRawResult();
        try {
            if (Tools.isEmpty(privateTransactionReceiveRawRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String priTxHash = privateTransactionReceiveRawRequest.getPriTxHash();
            if (Tools.isEmpty(priTxHash)) {
                throw new SDKException(SdkError.INVALID_PRITX_HASH_ERROR);
            }

            String priTxReceiveRawUrl = General.getInstance().priTxReceiveRaw(priTxHash);
            String result = HttpUtils.httpGet(priTxReceiveRawUrl);

            privateTransactionReceiveRawResult = JsonUtils.toJavaObject(result, BIFPrivateTransactionReceiveRawResult.class);
            privateTransactionReceiveRawResponse.buildResponse(SdkError.SUCCESS, privateTransactionReceiveRawResult);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            privateTransactionReceiveRawResponse.buildResponse(errorCode, errorDesc, privateTransactionReceiveRawResult);
        } catch (Exception e) {
            privateTransactionReceiveRawResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), privateTransactionReceiveRawResult);
        }

        return privateTransactionReceiveRawResponse;
    }

    @Override
    public BIFPrivateTransactionSendResponse send(BIFPrivateTransactionSendRequest privateTransactionSendRequest) {
        BIFPrivateTransactionSendResponse privateTransactionSendResponse = new BIFPrivateTransactionSendResponse();
        BIFPrivateTransactionSendResult privateTransactionSendResult = new BIFPrivateTransactionSendResult();
        try {
            if (Tools.isEmpty(privateTransactionSendRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String payload = privateTransactionSendRequest.getPayload();
            String from = privateTransactionSendRequest.getFrom();
            String[] to = privateTransactionSendRequest.getTo();
            if (Tools.isEmpty(payload)) {
                throw new SDKException(SdkError.INVALID_PRITX_PAYLAOD_ERROR);
            }
            if (Tools.isEmpty(from)) {
                throw new SDKException(SdkError.INVALID_PRITX_FROM_ERROR);
            }
            if (Tools.isEmpty(to)) {
                throw new SDKException(SdkError.INVALID_PRITX_TO_ERROR);
            }
            Map<String, Object> params = new HashMap<>();
            params.put("payload", payload);
            params.put("from", from);
            params.put("to", to);
            String priTxSendUrl = General.getInstance().priTxSend();
            String result = HttpUtils.httpPost(priTxSendUrl, JsonUtils.toJSONString(params));
            if (!Tools.isEmpty(result)) {
                Integer errorCode = JsonUtils.getInt(JsonUtils.toMap(result),"error_code");
                if (!Tools.isEmpty(errorCode) && !errorCode.equals(Constant.INIT_ZERO)) {
                    throw new SDKException(errorCode, result);
                }
            }
            privateTransactionSendResult = JsonUtils.toJavaObject(result, BIFPrivateTransactionSendResult.class);
            privateTransactionSendResponse.buildResponse(SdkError.SUCCESS, privateTransactionSendResult);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            privateTransactionSendResponse.buildResponse(errorCode, errorDesc, privateTransactionSendResult);
        } catch (Exception e) {
            privateTransactionSendResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), privateTransactionSendResult);
        }

        return privateTransactionSendResponse;
    }

    @Override
    public BIFPrivateTransactionReceiveResponse receive(BIFPrivateTransactionReceiveRequest privateTransactionReceiveRequest) {
        BIFPrivateTransactionReceiveResponse privateTransactionReceiveResponse = new BIFPrivateTransactionReceiveResponse();
        BIFPrivateTransactionReceiveResult privateTransactionReceiveResult = new BIFPrivateTransactionReceiveResult();
        try {
            if (Tools.isEmpty(privateTransactionReceiveRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String priTxHash = privateTransactionReceiveRequest.getPriTxHash();
            if (Tools.isEmpty(priTxHash)) {
                throw new SDKException(SdkError.INVALID_PRITX_HASH_ERROR);
            }

            String priTxReceiveUrl = General.getInstance().priTxReceive(priTxHash);
            String result = HttpUtils.httpGet(priTxReceiveUrl);

            privateTransactionReceiveResult = JsonUtils.toJavaObject(result, BIFPrivateTransactionReceiveResult.class);
            privateTransactionReceiveResponse.buildResponse(SdkError.SUCCESS, privateTransactionReceiveResult);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            privateTransactionReceiveResponse.buildResponse(errorCode, errorDesc, privateTransactionReceiveResult);
        } catch (Exception e) {
            privateTransactionReceiveResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), privateTransactionReceiveResult);
        }

        return privateTransactionReceiveResponse;
    }
}

