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
 *
 */
public class BIFTypeThreshold {
     @JsonProperty(value =  "type")
    private Integer type;
     @JsonProperty(value =  "threshold")
    private Long threshold;

    public BIFTypeThreshold() {

    }

    /**
     *
     * @Method TypeThreshold
     * @Params [type, threshold]
     * @Return
     */
    public BIFTypeThreshold(Integer type, Long threshold) {
        this.type = type;
        this.threshold = threshold;
    }

    /**
     *
     * @Method getType
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Integer getType() {
        return type;
    }

    /**
     *
     * @Method setType
     * @Params [type]
     * @Return void
     *
     */
    public void setType(Integer type) {
        this.type = type;
    }
    public void setType(String type) {
        switch (type){
            case "UNKNOWN": this.type = 0;
            case "CREATE_ACCOUNT": this.type = 1;break;
            case "SET_METADATA":  this.type = 4;break;
            case "SET_THRESHOLD": this.type = 6;break;
            case "PAY_COIN":   this.type = 7;break;
            case "LOG":   this.type = 8;break;
            case "SET_PRIVILEGE": this.type = 9;break;
            default: this.type = 0;
        }
    }
    /**
     *
     * @Method getThreshold
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Long getThreshold() {
        return threshold;
    }

    /**
     *
     * @Method setThreshold
     * @Params [threshold]
     * @Return void
     *
     */
    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }
}
