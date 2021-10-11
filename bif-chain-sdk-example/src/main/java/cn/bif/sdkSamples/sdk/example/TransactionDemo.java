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
import cn.bif.common.SampleConstant;
import cn.bif.common.ToBaseUnit;
import cn.bif.model.request.BIFTransactionGasSendRequest;
import cn.bif.model.request.BIFTransactionGetInfoRequest;
import cn.bif.model.request.BIFTransactionPrivateContractCallRequest;
import cn.bif.model.request.BIFTransactionPrivateContractCreateRequest;
import cn.bif.model.response.BIFTransactionGasSendResponse;
import cn.bif.model.response.BIFTransactionGetInfoResponse;
import cn.bif.model.response.BIFTransactionPrivateContractCallResponse;
import cn.bif.model.response.BIFTransactionPrivateContractCreateResponse;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class TransactionDemo {
    BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);

    /**
     * getTransactionInfo
     */
    @Test
    public void getTransactionInfo() {
        BIFTransactionGetInfoRequest request = new BIFTransactionGetInfoRequest();
        request.setHash("80483f9f1bba9a37f7de2e0aa9c8796be6b1b6d5853fa56e24e6219d79e84e22");
        BIFTransactionGetInfoResponse response = sdk.getBIFTransactionService().getTransactionInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
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
        request.setMetadata("gas send");

        // 调用 gasSend 接口
        BIFTransactionGasSendResponse response = sdk.getBIFTransactionService().gasSend(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error:      " + response.getErrorDesc());
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
        request.setMetadata("private Contract Call");

        // 调用 privateContractCall 接口
        BIFTransactionPrivateContractCallResponse response = sdk.getBIFTransactionService().privateContractCall(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error:      " + response.getErrorDesc());
            return;
        }

        Thread.sleep(5000);

        BIFTransactionGetInfoRequest transactionRequest = new BIFTransactionGetInfoRequest();
        transactionRequest.setHash(response.getResult().getHash());
        // 调用getTransactionInfo接口
        BIFTransactionGetInfoResponse transactionResponse = sdk.getBIFTransactionService().getTransactionInfo(transactionRequest);
        if (transactionResponse.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(transactionResponse.getResult(), true));
        } else {
            System.out.println("error: " + transactionResponse.getErrorDesc());
        }
    }

    /**
     * privateContractCreate
     */
    @Test
    public void privateContractCreate() throws InterruptedException {
        // 初始化参数
        String senderAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
        String senderPrivateKey = "priSPKqvAwSG3cp63GAuWfXASGXUSokYeA5nNkuWxKeBF54yEC";
        String payload = "\"use strict\";function queryBanance(address)\r\n{return \" test query private contract sdk_3\";}\r\nfunction sendTx(to,amount)\r\n{return Chain.payCoin(to,amount);}\r\nfunction init(input)\r\n{return;}\r\nfunction main(input)\r\n{let args=JSON.parse(input);if(args.method===\"sendTx\"){return sendTx(args.params.address,args.params.amount);}}\r\nfunction query(input)\r\n{let args=JSON.parse(input);if(args.method===\"queryBanance\"){return queryBanance(args.params.address);}}";
        String from = "bDRE8iIfGdwDeQOcJqZabZQH5Nd6cfTOMOorudtgXjQ=";
        String[] to = {"0VEtPRytTaDEf0g62KyAVeEHnwfd6ZM59/YQXfngaRs="};

        BIFTransactionPrivateContractCreateRequest request = new BIFTransactionPrivateContractCreateRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setPayload(payload);
        request.setFrom(from);
        request.setTo(to);
        request.setMetadata("init account");

        // 调用 privateContractCreate 接口
        BIFTransactionPrivateContractCreateResponse response = sdk.getBIFTransactionService().privateContractCreate(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error:      " + JSON.toJSONString(response));
            return;
        }
        Thread.sleep(5000);

        BIFTransactionGetInfoRequest transactionRequest = new BIFTransactionGetInfoRequest();
        transactionRequest.setHash(response.getResult().getHash());
        // 调用getTransactionInfo接口
        BIFTransactionGetInfoResponse transactionResponse = sdk.getBIFTransactionService().getTransactionInfo(transactionRequest);
        if (transactionResponse.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(transactionResponse.getResult(), true));
        } else {
            System.out.println("error: " + transactionResponse.getErrorDesc());
        }
    }
}

