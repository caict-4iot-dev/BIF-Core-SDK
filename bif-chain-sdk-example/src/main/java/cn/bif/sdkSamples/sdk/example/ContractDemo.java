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
import cn.bif.model.request.operation.BIFContractInvokeOperation;
import cn.bif.model.response.*;
import cn.bif.model.response.result.BIFContractCallResult;
import cn.bif.model.response.result.BIFContractCheckValidResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        String hash = "66e04b71d4f18fcb1f35fc5d70c54e3ea80810db4b74e024b0bd1d12c008c5fb";
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
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";
        String payload = "'use strict';\n\nfunction init(){return;}\n\nfunction checkDocument(params){\n    Utils.assert(params.id!== undefined, '10700,The id is not existed');\n    Utils.assert(params['@context']!== undefined, '10700,The context is not existed');\n}\n\nfunction creation(params){\n   let input = params;\n   checkDocument(input.document);\n   let id = 'bid_document_'+input.document.id;\n   Utils.assert(Utils.addressCheck(input.document.id)!==false,'10702,The bid address format is incorrect');\n   let data  = JSON.parse(Chain.load(id));\n   Utils.assert(data === false, '10703,The bid document is already existed');\n   let document = {};\n   document = input.document;\n   Chain.store(id,JSON.stringify(document));\n}\n\n\nfunction isAuth(authList) {\n\tUtils.assert(authList!== undefined, '10704,auth or recovery param is null');\n    let i = 0;\n    for(i=0;i < authList.length;i+=1){\n        let authId = authList[i];\n        let len = authId.indexOf('#');\n        if(len === -1){\n            len = authId.length;\n        }\n        if(authId.substr(0,len) === Chain.msg.sender){\n           return true;\n        }\n    }\n    return false;\n\n}\n\nfunction update(params){\n    let input = params; \n    checkDocument(input.document);\n    let id = 'bid_document_'+input.document.id;\n    let data  = JSON.parse(Chain.load(id));\n    Utils.assert(data !== false, '10706,The bid document is not existed');\n    Utils.assert(isAuth(data.authentication) !== false,'10707,sender had no right');\n    Utils.assert(Utils.addressCheck(input.document.id)!==false,'10708,id is invalid');\n    let document = {};\n    document = input.document;\n    Chain.store(id,JSON.stringify(document));\n}\n\nfunction reAuth(params){\n    let input = params; \n    Utils.assert(Utils.addressCheck(input.id)!==false,'10708,id is invalid');\n    let id = 'bid_document_'+input.id;\n    let data  = JSON.parse(Chain.load(id));\n    Utils.assert(data !== false, '10706,The bid document is not existed');\n    Utils.assert(data.extension.recovery !== false, '10709,The bid document had not recovery');\n    Utils.assert(isAuth(data.extension.recovery) !== false,'10710,sender had no right recovery');\n    data.authentication = input.authentication;\n    Chain.store(id,JSON.stringify(data));\n}\n\n\nfunction revoke(params){\n    let input = params; \n    let id = 'bid_document_'+input.document.id;\n    let data  = JSON.parse(Chain.load(id));\n    Utils.assert(data !== false, '10706,The bid document is not existed');\n    Utils.assert(Chain.ddressCheck(input.document.id)===false);\n} \n\nfunction queryBid(params) {\n   let input = params;\n   let id = 'bid_document_'+input.id;\n   let data  = JSON.parse(Chain.load(id));\n   Utils.assert(data !== false, '10706,The bid document is not existed');\n   return data;\n}\n\nfunction main(input_str){\n    let input = JSON.parse(input_str);\n\n    if(input.method === 'creation'){\n        creation(input.params);\n    }\n    else if(input.method === 'update'){\n        update(input.params);\n    }\n    else if(input.method === 'reAuth') {\n        reAuth(input.params);\n    }\n    else if(input.method === 'revoke') {\n        revoke(input.params);\n    }\n    else{\n        throw '<Main interface passes an invalid operation type>';\n    }\n}\n\nfunction query(input_str){\n    let input  = JSON.parse(input_str);\n    let object ={};\n    if(input.method === 'queryBid'){\n        object = queryBid(input.params);\n    }\n    else{\n       \tthrow '<unidentified operation type>';\n    }\n    return JSON.stringify(object);\n}";
        Long initBalance = ToBaseUnit.ToUGas("1");

        BIFContractCreateRequest request = new BIFContractCreateRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setInitBalance(initBalance);
        request.setPayload(payload);
        request.setRemarks("create contract");
        request.setType(0);
        request.setFeeLimit(101382900L);

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
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String contractAddress = "did:bid:eftzENB3YsWymQnvsLyF4T2ENzjgEg41";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";
        Long amount = 0L;
        String destAddress1 = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        String destAddress2 = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        String input1 = "{\"method\":\"creation\",\"params\":{\"document\":{\"@context\": [\"https://w3.org/ns/did/v1\"],\"context\": \"https://w3id.org/did/v1\"," +
                "\"id\": \""+destAddress1+"\", \"version\": \"1\"}}}";
        String input2 = "{\"method\":\"creation\",\"params\":{\"document\":{\"@context\": [\"https://w3.org/ns/did/v1\"],\"context\": \"https://w3id.org/did/v1\"," +
                "\"id\": \""+destAddress2+"\", \"version\": \"1\"}}}";

        List<BIFContractInvokeOperation> operations = new ArrayList<BIFContractInvokeOperation>();
        //操作对象1
        BIFContractInvokeOperation operation1=new BIFContractInvokeOperation();
        operation1.setContractAddress(contractAddress);
        operation1.setBIFAmount(amount);
        operation1.setInput(input1);
        //操作对象2
        BIFContractInvokeOperation operation2=new BIFContractInvokeOperation();
        operation2.setContractAddress(contractAddress);
        operation2.setBIFAmount(amount);
        operation2.setInput(input2);

        operations.add(operation1);
        operations.add(operation2);

        BIFContractInvokeRequest request = new BIFContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setOperations(operations);
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
