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

public class BIFTransactionPrivateContractCallRequest {
    private String senderAddress;
    private String privateKey;
    private Long ceilLedgerSeq;
    private String metadata;

    private Integer type;
    private String input;
    private String from;
    private String[] to;
    private String destAddress;

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

    /**
     * @Method getMetadata
     * @Params []
     * @Return String
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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }
}
