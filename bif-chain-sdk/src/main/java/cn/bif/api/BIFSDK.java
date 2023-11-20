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
package cn.bif.api;

import cn.bif.common.Tools;
import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;
import cn.bif.utils.http.HttpUtils;
import cn.bif.common.BIFSDKConfigure;
import cn.bif.module.account.BIFAccountService;
import cn.bif.module.account.impl.BIFAccountServiceImpl;
import cn.bif.module.blockchain.BIFBlockService;
import cn.bif.module.blockchain.BIFTransactionService;
import cn.bif.module.blockchain.impl.BIFBlockServiceImpl;
import cn.bif.module.blockchain.impl.BIFTransactionServiceImpl;
import cn.bif.module.contract.BIFContractService;
import cn.bif.module.contract.impl.BIFContractServiceImpl;

public class BIFSDK {
    private static BIFSDK sdk = null;
    private String url;
    private long chainId = 0;
    private String cacheIp;
    private int cachePort;
    private String protocol="UDP";

    public String getCacheIp() {
        return cacheIp;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * @Method Structure
     * @Params [url]
     */
    private BIFSDK() {
    }

    /**
     * @Method getInstance
     * @Params [url]
     * @Return SDK
     */
    public synchronized static BIFSDK getInstance(String url) throws SDKException {
        if (sdk == null) {
            sdk = new BIFSDK();
        }
        sdk.init(url);
        return sdk;
    }


    /**
     * @Method getAccountService
     * @Params []
     * @Return AccountService
     */
    public BIFAccountService getBIFAccountService() {
        return new BIFAccountServiceImpl();
    }



    /**
     * @Method getTransactionService
     * @Params []
     * @Return TransactionService
     */
    public BIFTransactionService getBIFTransactionService() {
        return new BIFTransactionServiceImpl();
    }

    /**
     * @Method getBlockService
     * @Params []
     * @Return BlockService
     */
    public BIFBlockService getBIFBlockService() {
        return new BIFBlockServiceImpl();
    }

    /**
     * @Method getContractService
     * @Params []
     * @Return ContractService
     */
    public BIFContractService getBIFContractService() {
        return new BIFContractServiceImpl();
    }

    /**
     * @Method getSdk
     * @Return SDK
     */
    public static BIFSDK getSdk() {
        return sdk;
    }

    /**
     * @Method getUrl
     * @Params []
     * @Return java.lang.String
     */
    public String getUrl() {
        return url;
    }
    /**
     * @Method getCachePort
     * @Params []
     * @Return java.lang.int
     */
    public int getCachePort() {
        return cachePort;
    }
    /**
     * @Method getChainId
     * @Params []
     * @Return long
     */
    public long getChainId() {
        return chainId;
    }

    /**
     * @Method init
     * @Params [url]
     * @Return void
     */
    private void init(BIFSDKConfigure sdkConfigure) throws SDKException {
        if (Tools.isEmpty(sdkConfigure.getUrl())) {
            throw new SDKException(SdkError.URL_EMPTY_ERROR);
        }
        sdk.url = sdkConfigure.getUrl();
        int httpConnectTimeOut = sdkConfigure.getHttpConnectTimeOut();
        if (httpConnectTimeOut > 0) {
            HttpUtils.connectTimeOut = httpConnectTimeOut;
        }
        int readTimeOut = sdkConfigure.getHttpReadTimeOut();
        if (readTimeOut > 0) {
            HttpUtils.readTimeOut = readTimeOut;
        }
        long chainId = sdkConfigure.getChainId();
        if (chainId > 0) {
            sdk.chainId = chainId;
        }
    }

    /**
     * @Method init
     * @Params [url]
     * @Return void
     */
    private void init(String url) throws SDKException {
        if (Tools.isEmpty(url)) {
            throw new SDKException(SdkError.URL_EMPTY_ERROR);
        }
        sdk.url = url;
    }
}
