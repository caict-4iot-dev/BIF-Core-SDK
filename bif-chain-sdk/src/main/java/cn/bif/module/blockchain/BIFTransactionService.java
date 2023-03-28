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

import cn.bif.model.request.*;
import cn.bif.model.request.operation.BIFBaseOperation;
import cn.bif.model.request.operation.BIFContractInvokeOperation;
import cn.bif.model.request.operation.BIFGasSendOperation;
import cn.bif.model.response.*;

import java.util.List;

public interface BIFTransactionService {
    /**
     * @Method BIFSerializable
     * @Params [bifTransactionSerializeRequest]
     * @Return BIFTransactionSerializeResponse
     */
    BIFTransactionSerializeResponse BIFSerializable(BIFTransactionSerializeRequest bifTransactionSerializeRequest);


    /**
     * @Method submit
     * @Params [transactionSubmitRequest]
     * @Return TransactionSubmitResponse
     */
    BIFTransactionSubmitResponse BIFSubmit(BIFTransactionSubmitRequest bifTransactionSubmitRequest);

    /**
     * radioTransaction
     *
     * @return
     */
    String radioTransaction(String senderAddress, Long feeLimit, Long gasPrice, BIFBaseOperation operation, Long ceilLedgerSeq, String remarks, String privateKey);
    String radioTransaction(String senderAddress, Long feeLimit, Long gasPrice, List<BIFContractInvokeOperation> operations, Long ceilLedgerSeq, String remarks, String privateKey);
    String radioGasTransaction(String senderAddress, Long feeLimit, Long gasPrice, List<BIFGasSendOperation> operations, Long ceilLedgerSeq, String remarks, String privateKey);
    /**
     * @Method getInfo
     * @Params [transactionGetInfoRequest]
     * @Return TransactionGetInfoResponse
     */
    BIFTransactionGetInfoResponse getTransactionInfo(BIFTransactionGetInfoRequest transactionGetInfoRequest);

    /**
     * 交易
     * @param request
     * @return
     */
    BIFTransactionGasSendResponse gasSend(BIFTransactionGasSendRequest request);
    /**
     * 批量转账
     * @param request
     * @return
     */
    BIFTransactionGasSendResponse batchGasSend(BIFBatchGasSendRequest request);
    /**
     * 私有化交易-合约调用
     * @param request
     * @return
     */
    @Deprecated
    BIFTransactionPrivateContractCallResponse privateContractCall(BIFTransactionPrivateContractCallRequest request);

    /**
     * 私有化交易-合约创建
     * @param request
     * @return
     */
    @Deprecated
    BIFTransactionPrivateContractCreateResponse privateContractCreate(BIFTransactionPrivateContractCreateRequest request);

    /**
     * 交易的费用评估
     * @param BIFTransactionEvaluateFeeRequest
     * @return
     */
    BIFTransactionEvaluateFeeResponse evaluateFee(BIFTransactionEvaluateFeeRequest BIFTransactionEvaluateFeeRequest);

    /**
     * 获取交易池中交易条数
     * @return
     */
    BIFTransactionGetTxCacheSizeResponse getTxCacheSize();
    /**
     * 获取交易池交易数据
     * @return
     */
    BIFTransactionCacheResponse  getTxCacheData(BIFTransactionCacheRequest request);

    /**
     * blob数据解析
     * @param blob
     * @return
     */
    BIFTransactionParseBlobResponse parseBlob(String blob);
    BIFTransactionGetBidResponse getBidByHash(String hash);

}
