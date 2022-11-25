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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class BIFTransactionHistory {
     @JsonProperty(value =  "actual_fee")
    private String fee;

     @JsonProperty(value =  "close_time")
    private Long confirmTime;

     @JsonProperty(value =  "contract_tx_hashes")
    private String[] contractTxHashes;

     @JsonProperty(value =  "error_code")
    private Integer errorCode;

     @JsonProperty(value =  "error_desc")
    private String errorDesc;

     @JsonProperty(value =  "hash")
    private String hash;

     @JsonProperty(value =  "ledger_seq")
    private Long ledgerSeq;

     @JsonProperty(value =  "signatures")
    private BIFSignature[] signatures;

     @JsonProperty(value =  "transaction")
    private BIFTransactionInfo transaction;

     @JsonProperty(value =  "tx_size")
    private Long txSize;
    @JsonProperty(value = "ceil_ledger_seq")
    private String ceilLedgerSeq;

    public String getCeilLedgerSeq() {
        return ceilLedgerSeq;
    }

    public void setCeilLedgerSeq(String ceilLedgerSeq) {
        this.ceilLedgerSeq = ceilLedgerSeq;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Long getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Long confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * @return String[]
     * @Method getContractTxHashes
     * @Params []
     */
    public String[] getContractTxHashes() {
        return contractTxHashes;
    }

    /**
     * @param contractTxHashes String[]
     * @Method setContractTxHashes
     */
    public void setContractTxHashes(String[] contractTxHashes) {
        this.contractTxHashes = contractTxHashes;
    }

    /**
     * @Method getErrorCode
     * @Params []
     * @Return java.lang.Long
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * @Method setErrorCode
     * @Params [errorCode]
     * @Return void
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @Method getErrorDesc
     * @Params []
     * @Return java.lang.Long
     */
    public String getErrorDesc() {
        return errorDesc;
    }

    /**
     * @Method setErrorDesc
     * @Params [errorDesc]
     * @Return void
     */
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    /**
     * @Method getHash
     * @Params []
     * @Return java.lang.String
     */
    public String getHash() {
        return hash;
    }

    /**
     * @Method setHash
     * @Params [hash]
     * @Return void
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @Method getLedgerSeq
     * @Params []
     * @Return java.lang.Long
     */
    public Long getLedgerSeq() {
        return ledgerSeq;
    }

    /**
     * @Method setLedgerSeq
     * @Params [ledgerSeq]
     * @Return void
     */
    public void setLedgerSeq(Long ledgerSeq) {
        this.ledgerSeq = ledgerSeq;
    }

    /**
     * @Method getSignatures
     * @Params []
     * @Return Signature[]
     */
    public BIFSignature[] getSignatures() {
        return signatures;
    }

    /**
     * @Method setSignatures
     * @Params [signatures]
     * @Return void
     */
    public void setSignatures(BIFSignature[] signatures) {
        this.signatures = signatures;
    }

    /**
     * @Method getTransactionService
     * @Params []
     * @Return TransactionInfo
     */
    public BIFTransactionInfo getTransaction() {
        return transaction;
    }

    /**
     * @Method setTransaction
     * @Params [transaction]
     * @Return void
     */
    public void setTransaction(BIFTransactionInfo transaction) {
        this.transaction = transaction;
    }

    /**
     * @Method getTxSize
     * @Params []
     * @Return java.lang.Long
     */
    public Long getTxSize() {
        return txSize;
    }

    /**
     * @Method setTxSize
     * @Params [txSize]
     * @Return void
     */
    public void setTxSize(Long txSize) {
        this.txSize = txSize;
    }
}
