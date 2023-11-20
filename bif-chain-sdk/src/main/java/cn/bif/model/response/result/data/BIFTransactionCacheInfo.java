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

public class BIFTransactionCacheInfo {
    @JsonProperty(value =  "signatures")
    private BIFSignature[] signatures;

    @JsonProperty(value =  "hash")
    private String hash;

    @JsonProperty(value =  "incoming_time")
    private String incomingTime;

    @JsonProperty(value =  "status")
    private String status;

    @JsonProperty(value = "transaction")
    private BIFTransactionInfo transaction;

    public BIFSignature[] getSignatures() {
        return signatures;
    }

    public void setSignatures(BIFSignature[] signatures) {
        this.signatures = signatures;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getIncomingTime() {
        return incomingTime;
    }

    public void setIncomingTime(String incomingTime) {
        this.incomingTime = incomingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BIFTransactionInfo getTransaction() {
        return transaction;
    }

    public void setTransaction(BIFTransactionInfo transaction) {
        this.transaction = transaction;
    }
}
