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
import cn.bif.model.response.*;
import cn.bif.model.response.result.BIFContractCallResult;
import cn.bif.model.response.result.BIFContractCheckValidResult;
import org.junit.Test;

/**
 *
 */
public class ContractDemo {
    BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);

    /**
     * checkContractAddress
     */
    @Test
    public void checkContractAddress() {
        // Init request
        BIFContractCheckValidRequest request = new BIFContractCheckValidRequest();
        request.setContractAddress("did:bid:efzE8AcDgWUeNbgujA5hK3oUeuG9k19b");

        // Call checkContractAddress
        BIFContractCheckValidResponse response = sdk.getBIFContractService().checkContractAddress(request);
        String s=JsonUtils.toJSONString(response);
        System.out.println(s);
        System.out.println(JsonUtils.toJSONString(response,null,true));
        if (response.getErrorCode() == 0) {
            BIFContractCheckValidResult result = response.getResult();
            System.out.println(result.getValid());
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * getContractInfo
     */
    @Test
    public void getContractInfo() {
        // Init request
        BIFContractGetInfoRequest request = new BIFContractGetInfoRequest();
        request.setContractAddress("did:bid:efiBacNvVSnr5QxgB282XGWkg4RXLLxL");

        // Call getContractInfo
        BIFContractGetInfoResponse response = sdk.getBIFContractService().getContractInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * getContractAddress
     */
    @Test
    public void getContractAddress() {
        // Init request
        String hash = "ff6a9d1a0c0011fbb9f51cfb99e4cd5e7c31380046fda3fd6e0daae44d1d4648";
        BIFContractGetAddressRequest request = new BIFContractGetAddressRequest();
        request.setHash(hash);

        // Call getAddress
        BIFContractGetAddressResponse response = sdk.getBIFContractService().getContractAddress(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * contractQuery
     */
    @Test
    public void contractQuery() {
        // Init variable
        // Contract address
        String contractAddress = "did:bid:ef2gAT82SGdnhj87wQWb9suPKLbnk9NP";

        // Init request
        BIFContractCallRequest request = new BIFContractCallRequest();
        request.setContractAddress(contractAddress);

        // Call contractQuery
        BIFContractCallResponse response = sdk.getBIFContractService().contractQuery(request);
        if (response.getErrorCode() == 0) {
            BIFContractCallResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * contractCreate
     */
    @Test
    public void contractCreate() {
        // 初始化参数
        String senderAddress = "did:bid:ef21AHDJWnFfYQ3Qs3kMxo64jD2KATwBz";
        String senderPrivateKey = "priSPKkL8XpxHiRLuNoxph2ThSbexeRUGEETprvuVHkxy2yBDp";
        String payload = "\"use strict\";function init(bar){/*init whatever you want*/return;}function main(input){let para = JSON.parse(input);if (para.do_foo)\n            {\n              let x = {\n                \'hello\' : \'world\'\n              };\n            }\n          }\n          \n          function query(input)\n          { \n            return input;\n          }\n        ";
        Long initBalance = ToBaseUnit.ToUGas("1");

        BIFContractCreateRequest request = new BIFContractCreateRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setInitBalance(initBalance);
        request.setPayload(payload);
        request.setRemarks("create contract");
        request.setType(1);
        request.setFeeLimit(10000000000L);

        // 调用bifContractCreate接口
        BIFContractCreateResponse response = sdk.getBIFContractService().contractCreate(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * contractInvoke
     */
    @Test
    public void contractInvoke() {
        // 初始化参数
        String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
        String contractAddress = "did:bid:ef2gAT82SGdnhj87wQWb9suPKLbnk9NP";
        String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
        Long amount = 0L;

        BIFContractInvokeRequest request = new BIFContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setContractAddress(contractAddress);
        request.setBIFAmount(amount);
        request.setRemarks("contract invoke");

        // 调用 bifContractInvoke 接口
        BIFContractInvokeResponse response = sdk.getBIFContractService().contractInvoke(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }
}
