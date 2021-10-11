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
import cn.bif.model.crypto.KeyPairEntity;
import cn.bif.model.request.*;
import cn.bif.model.response.*;
import cn.bif.model.response.result.BIFAccountGetMetadataResult;
import cn.bif.model.response.result.BIFAccountPrivResult;
import cn.bif.model.response.result.data.BIFSigner;
import cn.bif.model.response.result.data.BIFTypeThreshold;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class AccountDemo {
    BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);

    /**
     * getAccount
     */
    @Test
    public void getAccount() {
        // 初始化请求参数
        String accountAddress = "did:bid:efwD895wkz42QTXKwoquw1RieEmzvfEz";
        BIFAccountGetInfoRequest request = new BIFAccountGetInfoRequest();
        request.setAddress(accountAddress);

        // 调用getAccount接口
        BIFAccountGetInfoResponse response = sdk.getBIFAccountService().getAccount(request);

        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * getNonce
     */
    @Test
    public void getNonce() {
        String accountAddress = "did:bid:efwD895wkz42QTXKwoquw1RieEmzvfEz";
        BIFAccountGetNonceRequest request = new BIFAccountGetNonceRequest();
        request.setAddress(accountAddress);

        BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(request);
        System.out.println(JSON.toJSONString(response, true));
        if (0 == response.getErrorCode()) {
            System.out.println("Account nonce:" + response.getResult().getNonce());
        }
    }

    /**
     * getAccountBalance
     */
    @Test
    public void getAccountBalance() {
        String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
        BIFAccountGetBalanceRequest request = new BIFAccountGetBalanceRequest();
        request.setAddress(accountAddress);

        BIFAccountGetBalanceResponse response = sdk.getBIFAccountService().getAccountBalance(request);

        System.out.println(JSON.toJSONString(response, true));
        if (0 == response.getErrorCode()) {
            System.out.println("Gas balance：" + ToBaseUnit.ToGas(response.getResult().getBalance().toString()) + "Gas");
        }
    }

    /**
     * getAccountMetadata
     */
    @Test
    public void getAccountMetadata() {
        // 初始化请求参数
        String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
        BIFAccountGetMetadataRequest request = new BIFAccountGetMetadataRequest();
        request.setAddress(accountAddress);
        request.setKey("20210820-01");

        // 调用getBIFMetadata接口
        BIFAccountGetMetadataResponse response =
                sdk.getBIFAccountService().getAccountMetadata(request);
        if (response.getErrorCode() == 0) {
            BIFAccountGetMetadataResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error:      " + response.getErrorDesc());
        }
    }

    /**
     * createAccount
     */
    @Test
    public void createAccount() {
        // 初始化参数
        String senderAddress = "did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
        String destAddress = "did:bid:efwD895wkz42QTXKwoquw1RieEmzvfEz";
       // String destAddress = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        Long initBalance = ToBaseUnit.ToUGas("0.01");

        BIFCreateAccountRequest request = new BIFCreateAccountRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setDestAddress(destAddress);
        request.setInitBalance(initBalance);
        request.setMetadata("init account");

        // 调用 createAccount 接口
        BIFCreateAccountResponse response = sdk.getBIFAccountService().createAccount(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error:      " + response.getErrorDesc());
        }
    }

    /**
     * setMetadata
     */
    @Test
    public void setMetadata() {
        // 初始化参数
        String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
        String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
        String key = "20210902-01";
        String value = "metadata-20210902-01";

        BIFAccountSetMetadataRequest request = new BIFAccountSetMetadataRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setKey(key);
        request.setValue(value);
        request.setMetadata("set metadata");

        // 调用 setMetadata 接口
        BIFAccountSetMetadataResponse response = sdk.getBIFAccountService().setMetadata(request);

        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error:      " + response.getErrorDesc());
        }
    }

    /**
     * getAccountPriv
     */
    @Test
    public void getAccountPriv() {
        // 初始化请求参数
        String accountAddress = "did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        BIFAccountPrivRequest request = new BIFAccountPrivRequest();
        request.setAddress(accountAddress);

        // 调用getAccountPriv接口
        BIFAccountPrivResponse response = sdk.getBIFAccountService().getAccountPriv(request);

        if (response.getErrorCode() == 0) {
            BIFAccountPrivResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
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
        request.setMetadata("set privilege");

        // 调用 setPrivilege 接口
        BIFAccountSetPrivilegeResponse response = sdk.getBIFAccountService().setPrivilege(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error:      " + response.getErrorDesc());
        }
    }
}

