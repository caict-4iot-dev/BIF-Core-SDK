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
package cn.bif.module.blockchain;

import cn.bif.model.request.BIFBlockGetInfoRequest;
import cn.bif.model.request.BIFBlockGetTransactionsRequest;
import cn.bif.model.request.BIFBlockGetValidatorsRequest;
import cn.bif.model.response.*;

public interface BIFBlockService {
    /**
     * @Method getNumber
     * @Params []
     * @Return BlockGetNumberResponse
     */
    BIFBlockGetNumberResponse getBlockNumber();

    /**
     * @Method getTransactions
     * @Params [blockGetTransactionsRequest]
     * @Return BlockGetTransactionsResponse
     */
    BIFBlockGetTransactionsResponse getTransactions(BIFBlockGetTransactionsRequest blockGetTransactionsRequest);

    /**
     * @Method getInfo
     * @Params [blockGetInfoRequest]
     * @Return BlockGetInfoResponse
     */
    BIFBlockGetInfoResponse getBlockInfo(BIFBlockGetInfoRequest blockGetInfoRequest);

    /**
     * @Method getLatestInfo
     * @Params []
     * @Return BlockGetLatestInfoResponse
     */
    BIFBlockGetLatestInfoResponse getBlockLatestInfo();

    /**
     * @Method getValidators
     * @Params [blockGetValidatorsRequest]
     * @Return BlockGetValidatorsResponse
     */
    BIFBlockGetValidatorsResponse getValidators(BIFBlockGetValidatorsRequest blockGetValidatorsRequest);

    /**
     * @Method getLatestValidators
     * @Params []
     * @Return BlockGetLatestValidatorsResponse
     */
    BIFBlockGetLatestValidatorsResponse getLatestValidators();
}
