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
public class BIFContractAddressInfo {
     @JsonProperty(value =  "contract_address")
    private String contractAddress;

     @JsonProperty(value =  "operation_index")
    private Integer operationIndex;

    /**
     *
     * @Method getContractAddress
     * @Params []
     * @Return java.lang.String
     *
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     *
     * @Method setContractAddress
     * @Params [contractAddress]
     * @Return void
     *
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     *
     * @Method getOperationIndex
     * @Params []
     * @Return java.lang.Integer
     *
     */
    public Integer getOperationIndex() {
        return operationIndex;
    }

    /**
     *
     * @Method setOperationIndex
     * @Params [operationIndex]
     * @Return void
     *
     */
    public void setOperationIndex(Integer operationIndex) {
        this.operationIndex = operationIndex;
    }
}