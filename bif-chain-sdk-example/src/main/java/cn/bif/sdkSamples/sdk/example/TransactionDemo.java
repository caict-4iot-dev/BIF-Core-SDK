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
import cn.bif.model.request.*;
import cn.bif.model.request.operation.BIFGasSendOperation;
import cn.bif.model.response.*;
import cn.bif.model.response.result.BIFTransactionEvaluateFeeResult;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.utils.hex.HexFormat;
import org.junit.Test;

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
     * privateContractCall
     */
    @Test
    public void privateContractCall() throws InterruptedException {
        // 初始化参数
        String senderAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
        String senderPrivateKey = "priSPKqvAwSG3cp63GAuWfXASGXUSokYeA5nNkuWxKeBF54yEC";
        String input = "{\"method\":\"queryBanance\",\"params\":{\"address\":\"567890哈哈=======\"}}";
        String from = "bDRE8iIfGdwDeQOcJqZabZQH5Nd6cfTOMOorudtgXjQ=";
        String[] to = {"0VEtPRytTaDEf0g62KyAVeEHnwfd6ZM59/YQXfngaRs="};
        //设置调用的私有合约地址
        String destAddress = "did:bid:efTuswkPE1HP9Uc7vpNbRVokuQqhxaCE";

        BIFTransactionPrivateContractCallRequest request = new BIFTransactionPrivateContractCallRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setInput(input);
        request.setFrom(from);
        request.setTo(to);
        request.setDestAddress(destAddress);
        request.setRemarks("private Contract Call");

        // 调用 privateContractCall 接口
        BIFTransactionPrivateContractCallResponse response = sdk.getBIFTransactionService().privateContractCall(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println("error:      " + JsonUtils.toJSONString(response));
            return;
        }

        Thread.sleep(5000);

        BIFTransactionGetInfoRequest transactionRequest = new BIFTransactionGetInfoRequest();
        transactionRequest.setHash(response.getResult().getHash());
        // 调用getTransactionInfo接口
        BIFTransactionGetInfoResponse transactionResponse = sdk.getBIFTransactionService().getTransactionInfo(transactionRequest);
        if (transactionResponse.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(transactionResponse.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(transactionResponse));
        }
    }

    /**
     * privateContractCreate
     */
    @Test
    public void privateContractCreate() throws InterruptedException {
        // 初始化参数
        String senderAddress = "did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
        String payload = "\"use strict\";function queryBanance(address)\r\n{return \" test query private contract sdk_3\";}\r\nfunction sendTx(to,amount)\r\n{return Chain.payCoin(to,amount);}\r\nfunction init(input)\r\n{return;}\r\nfunction main(input)\r\n{let args=JSON.parse(input);if(args.method===\"sendTx\"){return sendTx(args.params.address,args.params.amount);}}\r\nfunction query(input)\r\n{let args=JSON.parse(input);if(args.method===\"queryBanance\"){return queryBanance(args.params.address);}}";
        String from = "bDRE8iIfGdwDeQOcJqZabZQH5Nd6cfTOMOorudtgXjQ=";
        String[] to = {"bwPdcwfUEtSZnaDmi2Nvj9HTwOcRvCRDh0cRdvX9BFw="};

        BIFTransactionPrivateContractCreateRequest request = new BIFTransactionPrivateContractCreateRequest();
        request.setType(1);
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setPayload(payload);
        request.setFrom(from);
        request.setTo(to);
        request.setRemarks("init account");

        // 调用 privateContractCreate 接口
        BIFTransactionPrivateContractCreateResponse response = sdk.getBIFTransactionService().privateContractCreate(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println("error:      " + JsonUtils.toJSONString(response));
            return;
        }
        Thread.sleep(10000);

        BIFContractGetAddressRequest addressRequest = new BIFContractGetAddressRequest();
        addressRequest.setHash(response.getResult().getHash());

        // Call getAddress
        BIFContractGetAddressResponse addressResponse = sdk.getBIFContractService().getContractAddress(addressRequest);
        if (addressResponse.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(addressResponse.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(addressResponse));
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

}

