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
import cn.bif.model.request.BIFBlockGetInfoRequest;
import cn.bif.model.request.BIFBlockGetTransactionsRequest;
import cn.bif.model.request.BIFBlockGetValidatorsRequest;
import cn.bif.model.response.*;
import cn.bif.model.response.result.BIFBlockGetInfoResult;
import cn.bif.model.response.result.BIFBlockGetLatestInfoResult;
import cn.bif.model.response.result.BIFBlockGetLatestValidatorsResult;
import cn.bif.model.response.result.BIFBlockGetValidatorsResult;
import org.junit.Test;

public class BlockDemo {
    BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);

    /**
     * 查询区块高度
     */
    @Test
    public void getBlockNumber() {
        BIFBlockGetNumberResponse response = sdk.getBIFBlockService().getBlockNumber();
        System.out.println(JsonUtils.toJSONString(response));
    }

    /**
     * 探测用户充值
     * <p>
     * 通过解析区块下的交易，来探测用户的充值动作
     */
    @Test
    public void getTransactions() {
        Long blockNumber = 1L;// 第617247区块
        BIFBlockGetTransactionsRequest request = new BIFBlockGetTransactionsRequest();
        request.setBlockNumber(blockNumber);
        BIFBlockGetTransactionsResponse response = sdk.getBIFBlockService().getTransactions(request);
        if (0 == response.getErrorCode()) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * 查询指定区块高度的区块信息
     */
    @Test
    public void getBlockInfo() {
        BIFBlockGetInfoRequest blockGetInfoRequest = new BIFBlockGetInfoRequest();
        blockGetInfoRequest.setBlockNumber(10L);
        BIFBlockGetInfoResponse lockGetInfoResponse = sdk.getBIFBlockService().getBlockInfo(blockGetInfoRequest);
        if (lockGetInfoResponse.getErrorCode() == 0) {
            BIFBlockGetInfoResult lockGetInfoResult = lockGetInfoResponse.getResult();
            System.out.println(JsonUtils.toJSONString(lockGetInfoResult));
        } else {
            System.out.println(JsonUtils.toJSONString(lockGetInfoResponse));
        }
    }

    /**
     * 查询最新的区块信息
     */
    @Test
    public void getBlockLatestInfo() {
        BIFBlockGetLatestInfoResponse lockGetLatestInfoResponse = sdk.getBIFBlockService().getBlockLatestInfo();
        if (lockGetLatestInfoResponse.getErrorCode() == 0) {
            BIFBlockGetLatestInfoResult lockGetLatestInfoResult = lockGetLatestInfoResponse.getResult();
            System.out.println(JsonUtils.toJSONString(lockGetLatestInfoResult));
        } else {
            System.out.println(JsonUtils.toJSONString(lockGetLatestInfoResponse));
        }
    }

    /**
     * 获取指定区块中所有验证节点数
     */
    @Test
    public void getValidators() {
        // 初始化请求参数
        BIFBlockGetValidatorsRequest request = new BIFBlockGetValidatorsRequest();
        request.setBlockNumber(1L);

        // 调用getBIFValidators接口
        BIFBlockGetValidatorsResponse response = sdk.getBIFBlockService().getValidators(request);
        if (response.getErrorCode() == 0) {
            BIFBlockGetValidatorsResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }

    /**
     * 获取最新区块中所有验证节点数
     */
    @Test
    public void getLatestValidators() {
        // 调用getBIFLatestValidators接口
        BIFBlockGetLatestValidatorsResponse response = sdk.getBIFBlockService().getLatestValidators();
        if (response.getErrorCode() == 0) {
            BIFBlockGetLatestValidatorsResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
    }
}

