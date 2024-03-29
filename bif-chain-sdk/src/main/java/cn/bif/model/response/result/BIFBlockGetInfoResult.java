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
package cn.bif.model.response.result;

import cn.bif.model.response.result.data.BIFBlockHeader;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BIFBlockGetInfoResult {

    @JsonProperty(value = "header")
    private BIFBlockHeader header;

    @JsonProperty(value = "ledger_length")
    private Long ledgerLength;

    @JsonProperty(value = "leader")
    private String leader;

    public Long getLedgerLength() {
        return ledgerLength;
    }

    public void setLedgerLength(Long ledgerLength) {
        this.ledgerLength = ledgerLength;
    }

    /**
     * @Method getHeader
     * @Params []
     * @Return BlockHeader
     */
    public BIFBlockHeader getHeader() {
        return header;
    }

    /**
     * @Method setHeader
     * @Params [header]
     * @Return void
     */
    public void setHeader(BIFBlockHeader header) {
        this.header = header;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
