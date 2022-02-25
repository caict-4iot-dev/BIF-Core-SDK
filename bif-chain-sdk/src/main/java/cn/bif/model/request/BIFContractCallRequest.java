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

public class BIFContractCallRequest {
    private String sourceAddress;
    private String contractAddress;
    private String input;
    private Long feeLimit;
    private Long gasPrice;

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
     * @Method getContractAddress
     * @Params []
     * @Return java.lang.String
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * @Method setContractAddress
     * @Params [contractAddress]
     * @Return void
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     * @Method getInput
     * @Params []
     * @Return java.lang.String
     */
    public String getInput() {
        return input;
    }

    /**
     * @Method setInput
     * @Params [input]
     * @Return void
     */
    public void setInput(String input) {
        this.input = input;
    }
}
