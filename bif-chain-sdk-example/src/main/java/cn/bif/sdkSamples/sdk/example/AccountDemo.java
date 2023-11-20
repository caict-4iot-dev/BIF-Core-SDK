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
import cn.bif.model.response.result.BIFAccountGetMetadatasResult;
import cn.bif.model.response.result.BIFAccountPrivResult;
import cn.bif.model.response.result.data.BIFSigner;
import cn.bif.model.response.result.data.BIFTypeThreshold;
import org.junit.jupiter.api.Test;

public class AccountDemo {
    BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);

    /**
     * getAccount
     */
    @Test
    public void getAccount() {
        // 初始化请求参数
        String accountAddress = "did:bid:efaJmqSioZ59czxR6LjBUQjt6n1iph12";
        BIFAccountGetInfoRequest request = new BIFAccountGetInfoRequest();
        request.setAddress(accountAddress);
        request.setDomainId(20);
        // 调用getAccount接口
        BIFAccountGetInfoResponse response = sdk.getBIFAccountService().getAccount(request);

        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * getNonce
     */
    @Test
    public void getNonce() {
        String accountAddress = "did:bid:efqhQu9YWEWpUKQYkAyGevPGtAdD1N6p";
        BIFAccountGetNonceRequest request = new BIFAccountGetNonceRequest();
        request.setAddress(accountAddress);
        request.setDomainId(20);
        BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(request);
        if (0 == response.getErrorCode()) {
            System.out.println("Account nonce:" + response.getResult().getNonce());
        }else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * getAccountBalance
     */
    @Test
    public void getAccountBalance() {
        String accountAddress = "did:bid:efaJmqSioZ59czxR6LjBUQjt6n1iph12";
        BIFAccountGetBalanceRequest request = new BIFAccountGetBalanceRequest();
        request.setAddress(accountAddress);
        request.setDomainId(20);

        BIFAccountGetBalanceResponse response = sdk.getBIFAccountService().getAccountBalance(request);
        if (0 == response.getErrorCode()) {
            System.out.println("Gas balance：" + ToBaseUnit.ToGas(response.getResult().getBalance().toString()) + "Gas");
        }else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * getAccountMetadata
     */
    @Test
    public void getAccountMetadatas() {
        // 初始化请求参数
        String accountAddress = "did:bid:eft6d191modv1cxBC43wjKHk85VVhQDc";
        BIFAccountGetMetadatasRequest request = new BIFAccountGetMetadatasRequest();
        request.setAddress(accountAddress);
        request.setDomainId(20);
        //request.setKey("20210902-01");

        // 调用getBIFMetadatas接口
        BIFAccountGetMetadatasResponse response =
                sdk.getBIFAccountService().getAccountMetadatas(request);
        if (response.getErrorCode() == 0) {
            BIFAccountGetMetadatasResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * createAccount
     */
    @Test
    public void createAccount() {
        // 初始化参数
//        String senderAddress = "did:bid:efaJmqSioZ59czxR6LjBUQjt6n1iph12";
//        String senderPrivateKey = "priSPKmopQGLoE7ZBT6urhS9rayboAE5ER3v4ajWPMCuze4SC8";
        String senderAddress = "did:bid:efqhQu9YWEWpUKQYkAyGevPGtAdD1N6p";
        String senderPrivateKey = "priSPKqru2zMzeb14XWxPNM1sassFeqyyUZotCAYcvCjhNof7t";
         String destAddress = "did:bid:efzE8AcDgWUeNbgujA5hK3oUeuG9k19b";
       //  String destAddress = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        System.out.println(destAddress);
        Long initBalance = ToBaseUnit.ToUGas("0.01");

        BIFCreateAccountRequest request = new BIFCreateAccountRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setDestAddress(destAddress);
        request.setInitBalance(initBalance);
        request.setDomainId(21);

        // 调用 createAccount 接口
        BIFCreateAccountResponse response = sdk.getBIFAccountService().createAccount(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * setMetadata
     */
    @Test
    public void setMetadatas() {
        // 初始化参数
        String senderAddress = "did:bid:eft6d191modv1cxBC43wjKHk85VVhQDc";
        String senderPrivateKey = "priSPKff1hvKVFYYFKSgfMb17wJ4dYZAHhLREarvh4Cy6fgn5b";
        String key = "20211029-01";
        String value = "metadata-20211029-01";

        BIFAccountSetMetadatasRequest request = new BIFAccountSetMetadatasRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setKey(key);
        request.setValue(value);
        request.setRemarks("set remarks");
        request.setDomainId(20);

        // 调用 setMetadata 接口
        BIFAccountSetMetadatasResponse response = sdk.getBIFAccountService().setMetadatas(request);

        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * getAccountPriv
     */
    @Test
    public void getAccountPriv() {
        // 初始化请求参数
        String accountAddress = "did:bid:efaJmqSioZ59czxR6LjBUQjt6n1iph12";
        BIFAccountPrivRequest request = new BIFAccountPrivRequest();
        request.setAddress(accountAddress);
        request.setDomainId(20);

        // 调用getAccountPriv接口
        BIFAccountPrivResponse response = sdk.getBIFAccountService().getAccountPriv(request);

        if (response.getErrorCode() == 0) {
            BIFAccountPrivResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * setPrivilege
     */
    @Test
    public void setPrivilege() {
        // 初始化参数
        String senderAddress = "did:bid:zf2bbxDwdzm4g4fJNTH2ah6gbHu6PdAX2";
        String senderPrivateKey = "priSrrfxxMAvnify3iRtTEW2zy87qo4N4B2gwhXSN4WFbXFJUs";
        String masterWeight = "";
        BIFSigner[] signers = new BIFSigner[1];
        BIFSigner s=new BIFSigner();
        s.setAddress("did:bid:efAsXt5zM2Hsq6wCYRMZBS5Q9HvG2EmK");
        s.setWeight(8L);
        signers[0]=s;

        String txThreshold = "2";
        BIFTypeThreshold[] typeThresholds = new BIFTypeThreshold[1];
        BIFTypeThreshold d=new BIFTypeThreshold();
        d.setThreshold(8L);
        d.setType(1);
        typeThresholds[0]=d;

        BIFAccountSetPrivilegeRequest request = new BIFAccountSetPrivilegeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setSigners(signers);
        request.setTxThreshold(txThreshold);
        request.setMasterWeight(masterWeight);
        request.setTypeThresholds(typeThresholds);
        request.setRemarks("set privilege");
        request.setDomainId(20);

        // 调用 setPrivilege 接口
        BIFAccountSetPrivilegeResponse response = sdk.getBIFAccountService().setPrivilege(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }
}

