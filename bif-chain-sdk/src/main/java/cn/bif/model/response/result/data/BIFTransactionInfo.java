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
package cn.bif.model.response.result.data;

import cn.bif.utils.hex.HexFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class BIFTransactionInfo {
    @JsonProperty(value = "ceil_ledger_seq")
    private String ceilLedgerSeq;

    @JsonProperty(value = "source_address")
    private String sourceAddress;

    @JsonProperty(value = "fee_limit")
    private Long feeLimit;

    @JsonProperty(value = "gas_price")
    private Long gasPrice;

    @JsonProperty(value = "nonce")
    private Long nonce;

    @JsonProperty(value = "metadata")
    private String metadata;

    @JsonProperty(value = "operations")
    private BIFOperation[] operations;

    @JsonProperty(value = "chain_id")
    private Long chainId;
    @JsonProperty(value = "domain_id")
    private Long domainId;

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getCeilLedgerSeq() {
        return ceilLedgerSeq;
    }

    public void setCeilLedgerSeq(String ceilLedgerSeq) {
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
        if (metadata != null && metadata.length() != 0) {
           // this.metadata = new String(HexFormat.hexToByte(metadata));
            this.metadata = metadata;
        }
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
     * @Method getOperations
     * @Params []
     * @Return cn.bif.model.response.result.data.operation[]
     */
    public BIFOperation[] getOperations() {
        return operations;
    }

    /**
     * @Method setOperations
     * @Params [operations]
     * @Return void
     */
    public void setOperations(BIFOperation[] operations) {
        this.operations = operations;
    }

    /**
     * @Method getChainId
     * @Params []
     * @Return java.lang.Long
     */
    public Long getChainId() {
        return chainId;
    }

    /**
     * @Method setChainId
     * @Params [chainId]
     * @Return void
     */
    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }
}
