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

public class BIFAccountSetMetadatasRequest {
    private String senderAddress;
    private String key;
    private String value;
    private Long version;
    private Boolean deleteFlag;
    private Long ceilLedgerSeq;
    private String remarks;
    private String privateKey;
    private Long feeLimit;
    private Long gasPrice;
    private Integer domainId;

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public Long getFeeLimit() {
        return feeLimit;
    }

    public void setFeeLimit(Long feeLimit) {
        this.feeLimit = feeLimit;
    }

    public Long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @Method getSenderAddress
     * @Params []
     * @Return java.lang.String
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * @Method setSenderAddress
     * @Params [senderAddress]
     * @Return void
     */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @Method getCeilLedgerSeq
     * @Params []
     * @Return Long
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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
