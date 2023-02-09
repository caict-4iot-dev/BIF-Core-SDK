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
package cn.bif.sdkSamples.sdk.example;

import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.common.SampleConstant;
import cn.bif.common.ToBaseUnit;
import cn.bif.model.crypto.KeyPairEntity;
import cn.bif.model.request.*;
import cn.bif.model.request.operation.BIFGasSendOperation;
import cn.bif.model.response.*;
import cn.bif.model.response.result.BIFTransactionEvaluateFeeResult;
import cn.bif.model.response.result.BIFTransactionGasSendResult;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.utils.hex.HexFormat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TransactionDemo {
    BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);

    /**
     * getTransactionInfo
     */
    @Test
    public void getTransactionInfo() {
        BIFTransactionGetInfoRequest request = new BIFTransactionGetInfoRequest();
        request.setHash("fa0eaf3ebaa42a10f1e1db472db22ab03efb7b74a0cce489e17347978fbec799");
        BIFTransactionGetInfoResponse response = sdk.getBIFTransactionService().getTransactionInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }


    /**
     * gasSend
     */
    @Test
    public void gasSend() {
        // 初始化参数
        String senderAddress = "did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
        String destAddress = "did:bid:efDS6MSSLHn41LdgmfxAweE8ruedD8hG";
        Long amount = ToBaseUnit.ToUGas("1");

        BIFTransactionGasSendRequest request = new BIFTransactionGasSendRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setDestAddress(destAddress);
        request.setAmount(amount);
        request.setRemarks("gas send");

        // 调用 gasSend 接口
        BIFTransactionGasSendResponse response = sdk.getBIFTransactionService().gasSend(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }



    /**
     * evaluateFee
     */
    @Test
    public void evaluateFee() {
        // 初始化变量
        String senderAddresss = "did:bid:efAsXt5zM2Hsq6wCYRMZBS5Q9HvG2EmK";
        String destAddress = "did:bid:ef14uPsX7XYLzsU4t2rnRrsK2zfUbW3r";
        Long bifAmount = ToBaseUnit.ToUGas("10.9");

        // 构建sendGas操作
        BIFGasSendOperation gasSendOperation = new BIFGasSendOperation();
        gasSendOperation.setSourceAddress(senderAddresss);
        gasSendOperation.setDestAddress(destAddress);
        gasSendOperation.setAmount(bifAmount);

        // 初始化评估交易请求参数
        BIFTransactionEvaluateFeeRequest request = new BIFTransactionEvaluateFeeRequest();
        request.setOperation(gasSendOperation);
        request.setSourceAddress(senderAddresss);
        request.setSignatureNumber(1);
        request.setRemarks(HexFormat.byteToHex("evaluate fees".getBytes()));

       // 调用evaluateFee接口
        BIFTransactionEvaluateFeeResponse response = sdk.getBIFTransactionService().evaluateFee(request);
        if (response.getErrorCode() == 0) {
            BIFTransactionEvaluateFeeResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }
    /**
     * bifSubmit
     */
    @Test
    public void bifSubmit() {
        // 初始化参数
        String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
        //序列化交易
        String serialization ="";
        //签名
        byte[] signBytes = PrivateKeyManager.sign(HexFormat.hexToByte(serialization), senderPrivateKey);
        String publicKey = PrivateKeyManager.getEncPublicKey(senderPrivateKey);
        //提交交易
        BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
        submitRequest.setSerialization(serialization);
        submitRequest.setPublicKey(publicKey);
        submitRequest.setSignData(HexFormat.byteToHex(signBytes));
        // 调用bifSubmit接口
        BIFTransactionSubmitResponse response = sdk.getBIFTransactionService().BIFSubmit(submitRequest);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * getTxCacheSize
     */
    @Test
    public void getTxCacheSize() {

        BIFTransactionGetTxCacheSizeResponse response = sdk.getBIFTransactionService().getTxCacheSize();
        if (response.getErrorCode() == 0) {
            System.out.println("txCacheSize: "+JsonUtils.toJSONString(response.getQueueSize()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }
    @Test
    public void getTxCacheData() {
        BIFTransactionCacheRequest cacheRequest=new BIFTransactionCacheRequest();
        cacheRequest.setHash("");

        BIFTransactionCacheResponse response = sdk.getBIFTransactionService().getTxCacheData(cacheRequest);
        if (response.getErrorCode() == 0) {
            System.out.println("txCacheData: "+JsonUtils.toJSONString(response.getResult().getTransactions()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }
    @Test
    public void Offline_ParseBlob() {
        String transactionBlobResult = "0a286469643a6269643a65666e5655677151466659657539374142663673476d335746745658485a4232100d2244080962400a0132122c0a286469643a6269643a656641735874357a4d3248737136774359524d5a425335513948764732456d4b10021a01322204080110012204080710022a0ce8aebee7bdaee69d83e9999030c0843d38016014";
        // Parsing the transaction Blob
        BIFTransactionParseBlobResponse transaction = sdk.getBIFTransactionService().parseBlob(transactionBlobResult);
        if(transaction.getErrorCode()==0){
            System.out.println("parseBlob: " + JsonUtils.toJSONString(transaction));
        }else {
            System.out.println(JsonUtils.toJSONString(transaction));
        }
    }
    /**
     * 根据hash获取bid标识
     */
    @Test
    public void getBidByHash(){
        String hash="9950eb981a9683698a0cdcc88d285d52b1452a12ebfcbb6ff407c4d5f618172b";
        BIFTransactionGetBidResponse result = sdk.getBIFTransactionService().getBidByHash(hash);
        if (result.getErrorCode() == 0) {
            System.out.println("bids: "+JsonUtils.toJSONString(result.getBids()));
        } else {
            System.out.println(JsonUtils.toJSONString(result));
        }
    }

    @Test
    public void batchGasSend() {
        // 初始化参数
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";

        String destAddress1 = KeyPairEntity.getBidAndKeyPairBySM2().getEncAddress();
        String destAddress2 = KeyPairEntity.getBidAndKeyPairBySM2().getEncAddress();
        Long bifAmount1 = ToBaseUnit.ToUGas("1");
        Long bifAmount2 = ToBaseUnit.ToUGas("1");

        List<BIFGasSendOperation> operations = new ArrayList<BIFGasSendOperation>();
        BIFGasSendOperation operation1 = new BIFGasSendOperation();
        operation1.setDestAddress(destAddress1);
        operation1.setAmount(bifAmount1);

        BIFGasSendOperation operation2 = new BIFGasSendOperation();
        operation2.setDestAddress(destAddress2);
        operation2.setAmount(bifAmount2);

        operations.add(operation1);
        operations.add(operation2);
        // 初始化请求参数
        BIFBatchGasSendRequest request = new BIFBatchGasSendRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setOperations(operations);
        request.setRemarks(HexFormat.byteToHex("batch gas send".getBytes()));
        request.setGasPrice(10L);
        //request.setFeeLimit(500L);
        // 调用batchGasSend接口
        BIFTransactionGasSendResponse response = sdk.getBIFTransactionService().batchGasSend(request);
        if (response.getErrorCode() == 0) {
            BIFTransactionGasSendResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

}

