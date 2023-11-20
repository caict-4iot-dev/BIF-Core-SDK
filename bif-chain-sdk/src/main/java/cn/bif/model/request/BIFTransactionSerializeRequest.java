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

import java.util.Arrays;

public class BIFTransactionSerializeRequest {
    private String sourceAddress;
    private Long nonce;
    private Long gasPrice;
    private Long feeLimit;
    private BIFBaseOperation[] operations;
    private Long ceilLedgerSeq;
    private String metadata;
    private Integer domainId;

    public BIFBaseOperation[] getOperations() {
        return operations;
    }
    /**
     * @Method setOperations
     * @Params [operations]
     * @Return void
     */
    public void setOperation(BIFBaseOperation operation) {
        if (null == this.operations) {
            this.operations = new BIFBaseOperation[1];
        } else {
            operations = Arrays.copyOf(operations, 1);
        }
        this.operations[0] = operation;
    }

    /**
     * @Method addOperation
     * @Params [operation]
     * @Return void
     */
    public void addOperation(BIFBaseOperation operation) {
        if (null == operations) {
            operations = new BIFBaseOperation[1];
        } else {
            operations = Arrays.copyOf(operations, operations.length + 1);
        }
        operations[operations.length - 1] = operation;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

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
