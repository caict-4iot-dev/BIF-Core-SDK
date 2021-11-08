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
package cn.bif.module.account;

import cn.bif.model.request.*;
import cn.bif.model.response.*;

public interface BIFAccountService {

    /**
     * @Method getInfo
     * @Params [accountGetInfoRequest]
     * @Return AccountGetInfoResponse
     */
    BIFAccountGetInfoResponse getAccount(BIFAccountGetInfoRequest bifAccountGetInfoRequest);

    /**
     * @Method getNonce
     * @Params [accountGetNonceRequest]
     * @Return AccountGetNonceResponse
     */
    BIFAccountGetNonceResponse getNonce(BIFAccountGetNonceRequest bifAccountGetNonceRequest);

    /**
     * @Method getBalance
     * @Params [accountGetBalanceRequest]
     * @Return AccountGetBalanceResponse
     */
    BIFAccountGetBalanceResponse getAccountBalance(BIFAccountGetBalanceRequest bifAccountGetBalanceRequest);

    /**
     * @Method getMetadata
     * @Params [accountGetMetadataRequest]
     * @Return AccountGetMetadataResponse
     */
    BIFAccountGetMetadatasResponse getAccountMetadatas(BIFAccountGetMetadatasRequest bifAccountGetMetadatasRequest);

    /**
     * @Method BIFCreateAccount
     * @Params [request]
     * @Return BIFAccountCreateAccountResponse
     */
    BIFCreateAccountResponse createAccount(BIFCreateAccountRequest request);

    /**
     * @Method bifAccountSetMetadata
     * @Params [request]
     * @Return BIFAccountSetMetadataResponse
     */
    BIFAccountSetMetadatasResponse setMetadatas(BIFAccountSetMetadatasRequest request);

    /**
     * 获取账户权限
     *
     * @param request
     * @return
     */
    BIFAccountPrivResponse getAccountPriv(BIFAccountPrivRequest request);

    /**
     * 设置权限
     *
     * @param request
     * @return
     */
    BIFAccountSetPrivilegeResponse setPrivilege(BIFAccountSetPrivilegeRequest request);
}
