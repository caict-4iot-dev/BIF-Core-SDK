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
public class BIFAccountActiviateInfo {

    @JsonProperty(value = "dest_address")
    private String destAddress;

    @JsonProperty(value = "contract")
    private BIFContractInfo contract;

    @JsonProperty(value = "priv")
    private BIFPriv priv;

    @JsonProperty(value = "metadatas")
    private BIFMetadataInfo[] metadatas;

    @JsonProperty(value = "init_balance")
    private Long initBalance;

    @JsonProperty(value = "init_input")
    private String initInput;

    /**
     *
     * @Method getDestAddress
     * @Params []
     * @Return java.lang.String
     *
     */
    public String getDestAddress() {
        return destAddress;
    }

    /**
     *
     * @Method setDestAddress
     * @Params [destAddress]
     * @Return void
     *
     */
    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    /**
     *
     * @Method getContractService
     * @Params []
     * @Return ContractInfo
     *
     */
    public BIFContractInfo getContract() {
        return contract;
    }

    /**
     *
     * @Method setContract
     * @Params [contract]
     * @Return void
     *
     */
    public void setContract(BIFContractInfo contract) {
        this.contract = contract;
    }

    /**
     *
     * @Method getPriv
     * @Params []
     * @Return Priv
     *
     */
    public BIFPriv getPriv() {
        return priv;
    }

    /**
     *
     * @Method setPriv
     * @Params [priv]
     * @Return void
     *
     */
    public void setPriv(BIFPriv priv) {
        this.priv = priv;
    }

    /**
     *
     * @Method getMetadatas
     * @Params []
     * @Return MetadataInfo[]
     *
     */
    public BIFMetadataInfo[] getMetadatas() {
        return metadatas;
    }

    /**
     *
     * @Method setMetadatas
     * @Params [metadatas]
     * @Return void
     *
     */
    public void setMetadatas(BIFMetadataInfo[] metadatas) {
        this.metadatas = metadatas;
    }

    /**
     *
     * @Method getInitBalance
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Long getInitBalance() {
        return initBalance;
    }

    /**
     *
     * @Method setInitBalance
     * @Params [initBalance]
     * @Return void
     *
     */
    public void setInitBalance(Long initBalance) {
        this.initBalance = initBalance;
    }

    /**
     *
     * @Method getInitInput
     * @Params []
     * @Return java.lang.String
     *
     */
    public String getInitInput() {
        return initInput;
    }

    /**
     *
     * @Method setInitInput
     * @Params [initInput]
     * @Return void
     *
     */
    public void setInitInput(String initInput) {
        this.initInput = initInput;
    }
}
