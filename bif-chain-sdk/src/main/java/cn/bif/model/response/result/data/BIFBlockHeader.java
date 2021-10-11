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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class BIFBlockHeader {

    @JsonProperty(value = "close_time")
    private Long confirmTime;

    @JsonProperty(value = "seq")
    private Long number;

    @JsonProperty(value = "tx_count")
    private Long txCount;

    @JsonProperty(value = "version")
    private String version;

    public Long getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Long confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * @Method getNumber
     * @Params []
     * @Return java.lang.Long
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @Method setNumber
     * @Params [number]
     * @Return void
     */
    public void setNumber(Long number) {
        this.number = number;
    }

    /**
     * @Method getTxCount
     * @Params []
     * @Return java.lang.Long
     */
    public Long getTxCount() {
        return txCount;
    }

    /**
     * @Method setTxCount
     * @Params [txCount]
     * @Return void
     */
    public void setTxCount(Long txCount) {
        this.txCount = txCount;
    }

    /**
     * @Method getVersion
     * @Params []
     * @Return java.lang.String
     */
    public String getVersion() {
        return version;
    }

    /**
     * @Method setVersion
     * @Params [version]
     * @Return void
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
