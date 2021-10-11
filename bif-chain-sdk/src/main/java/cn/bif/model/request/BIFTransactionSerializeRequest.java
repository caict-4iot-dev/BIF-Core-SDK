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
package cn.bif.model.request;

import cn.bif.model.request.operation.BIFBaseOperation;

public class BIFTransactionSerializeRequest {
    private String sourceAddress;
    private Long nonce;
    private Long gasPrice;
    private Long feeLimit;
    private BIFBaseOperation operation;
    private Long ceilLedgerSeq;
    private String metadata;

    /**
     * @Method getAddress
     * @Params []
     * @Return java.lang.String
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @Method setAddress
     * @Params [sourceAddress]
     * @Return void
     */
    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * @Method getNonce
     * @Params []
     * @Return java.lang.Long
     */
    public Long getNonce() {
        return nonce;
    }

    /**
     * @Method setNonce
     * @Params [nonce]
     * @Return void
     */
    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    /**
     * @Method getGasPrice
     * @Params []
     * @Return java.lang.Long
     */
    public Long getGasPrice() {
        return gasPrice;
    }

    /**
     * @Method setGasPrice
     * @Params [gasPrice]
     * @Return void
     */
    public void setGasPrice(Long gasPrice) {
        this.gasPrice = gasPrice;
    }

    /**
     * @Method getFeeLimit
     * @Params []
     * @Return java.lang.Long
     */
    public Long getFeeLimit() {
        return feeLimit;
    }

    /**
     * @Method setFeeLimit
     * @Params [feeLimit]
     * @Return void
     */
    public void setFeeLimit(Long feeLimit) {
        this.feeLimit = feeLimit;
    }

    public BIFBaseOperation getOperation() {
        return operation;
    }

    public void setOperation(BIFBaseOperation operation) {
        this.operation = operation;
    }

    /**
     * @Method getCeilLedgerSeq
     * @Params []
     * @Return java.lang.String
     */
    public Long getCeilLedgerSeq() {
        return ceilLedgerSeq;
    }

    /**
     * @Method setCeilLedgerSeq
     * @Params [ceilLedgerSeq]
     * @Return void
     */
    public void setCeilLedgerSeq(Long ceilLedgerSeq) {
        this.ceilLedgerSeq = ceilLedgerSeq;
    }

    /**
     * @Method getMetadata
     * @Params []
     * @Return java.lang.String
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @Method setMetadata
     * @Params [metadata]
     * @Return void
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
