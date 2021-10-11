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

import cn.bif.model.request.BIFPrivateTransactionReceiveRawRequest;
import cn.bif.model.request.BIFPrivateTransactionReceiveRequest;
import cn.bif.model.request.BIFPrivateTransactionSendRequest;
import cn.bif.model.request.BIFPrivateTransactionStoreRawRequest;
import cn.bif.model.response.BIFPrivateTransactionReceiveRawResponse;
import cn.bif.model.response.BIFPrivateTransactionReceiveResponse;
import cn.bif.model.response.BIFPrivateTransactionSendResponse;
import cn.bif.model.response.BIFPrivateTransactionStoreRawResponse;

public interface BIFPrivateTransactionService {
    /**
     * @Method storeRaw
     * @Params [privateTransactionStoreRawRequest]
     * @Return PrivateTransactionStoreRawResponse
     */
    public BIFPrivateTransactionStoreRawResponse storeRaw(BIFPrivateTransactionStoreRawRequest privateTransactionStoreRawRequest);

    /**
     * @Method receiveRaw
     * @Params [privateTransactionReceiveRawRequest]
     * @Return PrivateTransactionReceiveRawResponse
     */
    public BIFPrivateTransactionReceiveRawResponse receiveRaw(BIFPrivateTransactionReceiveRawRequest privateTransactionReceiveRawRequest);

    /**
     * @Method send
     * @Params [privateTransactionSendRequest]
     * @Return PrivateTransactionSendResponse
     */
    public BIFPrivateTransactionSendResponse send(BIFPrivateTransactionSendRequest privateTransactionSendRequest);

    /**
     * @Method receive
     * @Params [privateTransactionReceiveRequest]
     * @Return PrivateTransactionReceiveResponse
     */
    public BIFPrivateTransactionReceiveResponse receive(BIFPrivateTransactionReceiveRequest privateTransactionReceiveRequest);
}
