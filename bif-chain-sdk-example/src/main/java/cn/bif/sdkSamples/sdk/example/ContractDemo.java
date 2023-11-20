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

import org.junit.jupiter.api.Test;

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
        request.setDomainId(20);

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
        request.setContractAddress("did:bid:efMzSfrumHe3iBd27ck984hZsnVU3YP1");
        request.setDomainId(20);

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
        String hash = "85b84c516c1a3083cbd3b354abf05d48b834a2580e9a50a387c2f053f8a09fb3";
        BIFContractGetAddressRequest request = new BIFContractGetAddressRequest();
        request.setHash(hash);
        request.setDomainId(20);

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
        String contractAddress = "did:bid:efz8GLoWABsdYFNuofcmK3eyCXRTiotv";
        String input  = "{\"method\": \"getServiceNodeApply\",\"params\":{\"domainId\":20, \"nodeAddr\":\"did:bid:ef2CuJPNBW7HQhTTiiT3wPYvFbSbthtzC\"}}";
        // Init request
        BIFContractCallRequest request = new BIFContractCallRequest();
        request.setContractAddress(contractAddress);
        request.setDomainId(20);
        request.setInput(input);

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
        String senderAddress = "did:bid:efaJmqSioZ59czxR6LjBUQjt6n1iph12";
        String senderPrivateKey = "priSPKmopQGLoE7ZBT6urhS9rayboAE5ER3v4ajWPMCuze4SC8";
//        String senderAddress =  "did:bid:efqhQu9YWEWpUKQYkAyGevPGtAdD1N6p";
//        String senderPrivateKey = "priSPKqru2zMzeb14XWxPNM1sassFeqyyUZotCAYcvCjhNof7t";
        String payload = "'use strict';\n\nfunction init(){return;}\n\nfunction checkDocument(params){\n    Utils.assert(params.id!== undefined, '10700,The id is not existed');\n    Utils.assert(params['@context']!== undefined, '10700,The context is not existed');\n}\n\nfunction creation(params){\n   let input = params;\n   checkDocument(input.document);\n   let id = 'bid_document_'+input.document.id;\n   Utils.assert(Utils.addressCheck(input.document.id)!==false,'10702,The bid address format is incorrect');\n   let data  = JSON.parse(Chain.load(id));\n   Utils.assert(data === false, '10703,The bid document is already existed');\n   let document = {};\n   document = input.document;\n   Chain.store(id,JSON.stringify(document));\n}\n\n\nfunction isAuth(authList) {\n\tUtils.assert(authList!== undefined, '10704,auth or recovery param is null');\n    let i = 0;\n    for(i=0;i < authList.length;i+=1){\n        let authId = authList[i];\n        let len = authId.indexOf('#');\n        if(len === -1){\n            len = authId.length;\n        }\n        if(authId.substr(0,len) === Chain.msg.sender){\n           return true;\n        }\n    }\n    return false;\n\n}\n\nfunction update(params){\n    let input = params; \n    checkDocument(input.document);\n    let id = 'bid_document_'+input.document.id;\n    let data  = JSON.parse(Chain.load(id));\n    Utils.assert(data !== false, '10706,The bid document is not existed');\n    Utils.assert(isAuth(data.authentication) !== false,'10707,sender had no right');\n    Utils.assert(Utils.addressCheck(input.document.id)!==false,'10708,id is invalid');\n    let document = {};\n    document = input.document;\n    Chain.store(id,JSON.stringify(document));\n}\n\nfunction reAuth(params){\n    let input = params; \n    Utils.assert(Utils.addressCheck(input.id)!==false,'10708,id is invalid');\n    let id = 'bid_document_'+input.id;\n    let data  = JSON.parse(Chain.load(id));\n    Utils.assert(data !== false, '10706,The bid document is not existed');\n    Utils.assert(data.extension.recovery !== false, '10709,The bid document had not recovery');\n    Utils.assert(isAuth(data.extension.recovery) !== false,'10710,sender had no right recovery');\n    data.authentication = input.authentication;\n    Chain.store(id,JSON.stringify(data));\n}\n\n\nfunction revoke(params){\n    let input = params; \n    let id = 'bid_document_'+input.document.id;\n    let data  = JSON.parse(Chain.load(id));\n    Utils.assert(data !== false, '10706,The bid document is not existed');\n    Utils.assert(Chain.ddressCheck(input.document.id)===false);\n} \n\nfunction queryBid(params) {\n   let input = params;\n   let id = 'bid_document_'+input.id;\n   let data  = JSON.parse(Chain.load(id));\n   Utils.assert(data !== false, '10706,The bid document is not existed');\n   return data;\n}\n\nfunction main(input_str){\n    let input = JSON.parse(input_str);\n\n    if(input.method === 'creation'){\n        creation(input.params);\n    }\n    else if(input.method === 'update'){\n        update(input.params);\n    }\n    else if(input.method === 'reAuth') {\n        reAuth(input.params);\n    }\n    else if(input.method === 'revoke') {\n        revoke(input.params);\n    }\n    else{\n        throw '<Main interface passes an invalid operation type>';\n    }\n}\n\nfunction query(input_str){\n    let input  = JSON.parse(input_str);\n    let object ={};\n    if(input.method === 'queryBid'){\n        object = queryBid(input.params);\n    }\n    else{\n       \tthrow '<unidentified operation type>';\n    }\n    return JSON.stringify(object);\n}";
        //String payload = "\"use strict\";function init(bar){/*init whatever you want*/return;}function main(input){let para = JSON.parse(input);if (para.do_foo)\n            {\n              let x = {\n                \'hello\' : \'world\'\n              };\n            }\n          }\n          \n          function query(input)\n          { \n            return input;\n          }\n        ";
        Long initBalance = ToBaseUnit.ToUGas("0.01");

        BIFContractCreateRequest request = new BIFContractCreateRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setInitBalance(initBalance);
        request.setPayload(payload);
        request.setRemarks("create contract");
        request.setType(0);
        request.setFeeLimit(100369100L);
        request.setDomainId(21);

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
//        String senderAddress = "did:bid:ef2CuJPNBW7HQhTTiiT3wPYvFbSbthtzC";
//        String contractAddress = "did:bid:efz8GLoWABsdYFNuofcmK3eyCXRTiotv";
//        String senderPrivateKey = "priSPKoiBtG9qHz5R29KC2rDJ6hfx1uJgvj1w3TtmKjF7ekpHg";
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String contractAddress = "did:bid:eftzENB3YsWymQnvsLyF4T2ENzjgEg41";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";
        Long amount = 0L;
        // String  input = "{\"method\": \"creation\",\"params\": {\"document\": {\"@context\": [\"https://w3.org/ns/did/v1\"],\"context\": \"https://w3id.org/did/v1\",\"id\": \"did:bid:zfBkyZwxE3JxAuzvABzoAKG2T9sd5u1U\",\"version\": \"1\"}}}";
        String  input = "{\"method\":\"applyServiceNode\",\"params\":{\"domainId\":20,\"nodeAddr\":\"did:bid:ef2CuJPNBW7HQhTTiiT3wPYvFbSbthtzC\"}}";

        BIFContractInvokeRequest request = new BIFContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setContractAddress(contractAddress);
        request.setBIFAmount(amount);
        request.setRemarks("contract invoke");
        request.setDomainId(0);
        request.setInput(input);

        // 调用 bifContractInvoke 接口
        BIFContractInvokeResponse response = sdk.getBIFContractService().contractInvoke(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * batchContractInvoke
     */
    @Test
    public void batchContractInvoke() {
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

        BIFBatchContractInvokeRequest request = new BIFBatchContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setOperations(operations);
        request.setRemarks("contract invoke");
        request.setDomainId(0);

        // 调用 bifContractInvoke 接口
        BIFContractInvokeResponse response = sdk.getBIFContractService().batchContractInvoke(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * EVM contractInvoke
     */
    @Test
    public void evmContractCreate() {
        // 初始化参数
        String senderAddress = "did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
        String payload="6080604052600060025534801561001557600080fd5b50610c90806100256000396000f3006080604052600436106100c5576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306bfe913146100ca57806329081b6f146100e15780635a111457146101545780636db269211461018e57806379df423114610234578063814d8033146102cb578063853255cc146102e25780638f004bc21461030d578063a3c1eccc14610324578063ace2332a14610368578063bb5f614e1461037f578063c08ea6b9146103da578063de06b5661461041b575b600080fd5b3480156100d657600080fd5b506100df610484565b005b3480156100ed57600080fd5b50610152600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610556565b005b61018c600480360381019080803577ffffffffffffffffffffffffffffffffffffffffffffffff16906020019092919050505061061c565b005b34801561019a57600080fd5b506101b96004803603810190808035906020019092919050505061066a565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101f95780820151818401526020810190506101de565b50505050905090810190601f1680156102265780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561024057600080fd5b5061024961071f565b6040518080602001838152602001828103825284818151815260200191508051906020019080838360005b8381101561028f578082015181840152602081019050610274565b50505050905090810190601f1680156102bc5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b3480156102d757600080fd5b506102e06107cc565b005b3480156102ee57600080fd5b506102f76107f9565b6040518082815260200191505060405180910390f35b34801561031957600080fd5b506103226107ff565b005b610366600480360381019080803577ffffffffffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610841565b005b34801561037457600080fd5b5061037d610890565b005b34801561038b57600080fd5b506103c4600480360381019080803577ffffffffffffffffffffffffffffffffffffffffffffffff169060200190929190505050610aca565b6040518082815260200191505060405180910390f35b3480156103e657600080fd5b5061040560048036038101908080359060200190929190505050610b1a565b6040518082815260200191505060405180910390f35b34801561042757600080fd5b50610482600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610b2d565b005b3077ffffffffffffffffffffffffffffffffffffffffffffffff16316008819055504560098190555043600a8190555042600b8190555033600c60006101000a81548177ffffffffffffffffffffffffffffffffffffffffffffffff021916908377ffffffffffffffffffffffffffffffffffffffffffffffff1602179055503a600d8190555032600e60006101000a81548177ffffffffffffffffffffffffffffffffffffffffffffffff021916908377ffffffffffffffffffffffffffffffffffffffffffffffff160217905550565b816006908051906020019061056c929190610bbf565b50806007819055507fd33ca00949dcc9f70406559812beac75d6af7916be10a21a435e7ccf8ad967be82826040518080602001838152602001828103825284818151815260200191508051906020019080838360005b838110156105dd5780820151818401526020810190506105c2565b50505050905090810190601f16801561060a5780820380516001836020036101000a031916815260200191505b50935050505060405180910390a15050565b8077ffffffffffffffffffffffffffffffffffffffffffffffff166108fc349081150290604051600060405180830381858888f19350505050158015610666573d6000803e3d6000fd5b5050565b6060600160008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107135780601f106106e857610100808354040283529160200191610713565b820191906000526020600020905b8154815290600101906020018083116106f657829003601f168201915b50505050509050919050565b606060006006600754818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107bd5780601f10610792576101008083540402835291602001916107bd565b820191906000526020600020905b8154815290600101906020018083116107a057829003601f168201915b50505050509150915091509091565b5b68056bc75e2d6310000060035410156107f7576003600081548092919060010191905055506107cd565b565b60025481565b60405180807f68656c6c6f0000000000000000000000000000000000000000000000000000008152506005019050604051809103902060058160001916905550565b8177ffffffffffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f1935050505015801561088b573d6000803e3d6000fd5b505050565b60405180807f68656c6c6f000000000000000000000000000000000000000000000000000000815250600501905060405180910390a07f746c6f673100000000000000000000000000000000000000000000000000000060405180807f68656c6c6f310000000000000000000000000000000000000000000000000000815250600601905060405180910390a17f746c6f67320000000000000000000000000000000000000000000000000000007f746c6f673100000000000000000000000000000000000000000000000000000060405180807f68656c6c6f000000000000000000000000000000000000000000000000000000815250600501905060405180910390a27f746c6f67330000000000000000000000000000000000000000000000000000007f746c6f67320000000000000000000000000000000000000000000000000000007f746c6f673100000000000000000000000000000000000000000000000000000060405180807f68656c6c6f000000000000000000000000000000000000000000000000000000815250600501905060405180910390a37f746c6f6733000000000000000000000000000000000000000000000000000000807f746c6f67320000000000000000000000000000000000000000000000000000007f746c6f673100000000000000000000000000000000000000000000000000000060405180807f68656c6c6f000000000000000000000000000000000000000000000000000000815250600501905060405180910390a4565b60008060008377ffffffffffffffffffffffffffffffffffffffffffffffff1677ffffffffffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000816004819055506004549050919050565b60003390506002600081548092919060010191905055506002546000808377ffffffffffffffffffffffffffffffffffffffffffffffff1677ffffffffffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550816001600060025481526020019081526020016000209080519060200190610bba929190610bbf565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c0057805160ff1916838001178555610c2e565b82800160010185558215610c2e579182015b82811115610c2d578251825591602001919060010190610c12565b5b509050610c3b9190610c3f565b5090565b610c6191905b80821115610c5d576000816000905550600101610c45565b5090565b905600a165627a7a723058203319e1d32df7ad876c7aa1fbfcf829662f965a056e523e4bb9d45166bfd05e610029";
        Long initBalance = ToBaseUnit.ToUGas("0.01");

        BIFContractCreateRequest request = new BIFContractCreateRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setInitBalance(initBalance);
        request.setPayload(payload);
        request.setRemarks("create contract");
        request.setType(1);
        request.setFeeLimit(2000000000L);
        //request.setDomainId(21);

        // 调用bifContractCreate接口
        BIFContractCreateResponse response = sdk.getBIFContractService().contractCreate(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * EVM contractInvoke
     */
    @Test
    public void evmContractInvoke() {
        // 初始化参数
        String senderAddress = "did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
        String contractAddress = "did:bid:efXkBsC2nQN6PJLjT9nv3Ah7S3zJt2WW";
        Long amount = 0L;
        String input="{\"function\":\"register6122a(string)\",\"args\":\"'hello world'\"}";

        BIFContractInvokeRequest request = new BIFContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setContractAddress(contractAddress);
        request.setBIFAmount(amount);
        request.setRemarks("contract invoke");
        //request.setDomainId(20);
        request.setInput(input);

        // 调用 bifContractInvoke 接口
        BIFContractInvokeResponse response = sdk.getBIFContractService().contractInvoke(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * EVM getContractAddress
     */
    @Test
    public void getEvmContractAddress() {
        // Init request
        String hash = "85b84c516c1a3083cbd3b354abf05d48b834a2580e9a50a387c2f053f8a09fb3";
        BIFContractGetAddressRequest request = new BIFContractGetAddressRequest();
        request.setHash(hash);
        //request.setDomainId(20);

        // Call getAddress
        BIFContractGetAddressResponse response = sdk.getBIFContractService().getContractAddress(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * EVM getContractInfo
     */
    @Test
    public void getEvmContractInfo() {
        // Init request
        BIFContractGetInfoRequest request = new BIFContractGetInfoRequest();
        request.setContractAddress("did:bid:efXkBsC2nQN6PJLjT9nv3Ah7S3zJt2WW");
        //request.setDomainId(20);

        // Call getContractInfo
        BIFContractGetInfoResponse response = sdk.getBIFContractService().getContractInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

}
