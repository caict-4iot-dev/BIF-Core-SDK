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
public class BIFOperationFormat {
     @JsonProperty(value =  "type")
    private Integer type;

     @JsonProperty(value =  "metadata")
    private String metadata;

     @JsonProperty(value =  "create_account")
    private BIFAccountActiviateInfo createAccount;

     @JsonProperty(value =  "pay_coin")
    private BIFGasSendInfo sendGas;

     @JsonProperty(value =  "set_metadata")
    private BIFAccountSetMetadataInfo setMetadata;

     @JsonProperty(value =  "set_privilege")
    private BIFAccountSetPrivilegeInfo setPrivilege;

    @JsonProperty(value = "log")
    private BIFLogInfo log;

    public BIFLogInfo getLog() {
        return log;
    }

    public void setLog(BIFLogInfo log) {
        this.log = log;
    }

    /**
     *
     * @Method getType
     * @Params []
     * @Return int
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
    public void setType(String type) {
        switch (type){
            case "UNKNOWN": this.type = 0;break;
            case "CREATE_ACCOUNT": this.type = 1;break;
            case "SET_METADATA":  this.type = 4;break;
            case "SET_SIGNER_WEIGHT":  this.type = 5;break;
            case "SET_THRESHOLD": this.type = 6;break;
            case "PAY_COIN":   this.type = 7;break;
            case "LOG":   this.type = 8;break;
            case "SET_PRIVILEGE": this.type = 9;break;
            case "UPGRADE_CONTRACT": this.type = 10;break;
            case "SET_CONTROLLED_AREA": this.type = 11;break;
            case "AUTHORIZE_TRANSFER": this.type = 12;break;
            case "1": this.type = 1;break;
            case "4":  this.type = 4;break;
            case "5":  this.type = 5;break;
            case "6": this.type = 6;break;
            case "7":   this.type = 7;break;
            case "8":   this.type = 8;break;
            case "9": this.type = 9;break;
            case "10": this.type = 10;break;
            case "11": this.type = 11;break;
            case "12": this.type = 12;break;
            default: this.type = 0;break;
        }
    }


    /**
     *
     * @Method getMetadata
     * @Params []
     * @Return java.lang.String
     *
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     *
     * @Method setMetadata
     * @Params [metadata]
     * @Return void
     *
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    /**
     *
     * @Method getCreateAccount
     * @Params []
     * @Return AccountActiviateInfo
     *
     */
    public BIFAccountActiviateInfo getCreateAccount() {
        return createAccount;
    }

    /**
     *
     * @Method setCreateAccount
     * @Params [createAccount]
     * @Return void
     *
     */
    public void setCreateAccount(BIFAccountActiviateInfo createAccount) {
        this.createAccount = createAccount;
    }

    /**
     *
     * @Method getSendGas
     * @Params []
     * @Return GasSendInfo
     *
     */
    public BIFGasSendInfo getSendGas() {
        return sendGas;
    }

    /**
     *
     * @Method setSendGas
     * @Params [sendGas]
     * @Return void
     *
     */
    public void setSendGas(BIFGasSendInfo sendGas) {
        this.sendGas = sendGas;
    }

    /**
     *
     * @Method getSetMetadata
     * @Params []
     * @Return AccountSetMetadataInfo
     *
     */
    public BIFAccountSetMetadataInfo getSetMetadata() {
        return setMetadata;
    }

    /**
     *
     * @Method setSetMetadata
     * @Params [setMetadata]
     * @Return void
     *
     */
    public void setSetMetadata(BIFAccountSetMetadataInfo setMetadata) {
        this.setMetadata = setMetadata;
    }

    /**
     *
     * @Method getSetPrivilege
     * @Params []
     * @Return AccountSetPrivilegeInfo
     *
     */
    public BIFAccountSetPrivilegeInfo getSetPrivilege() {
        return setPrivilege;
    }

    /**
     *
     * @Method setSetPrivilege
     * @Params [setPrivilege]
     * @Return void
     *
     */
    public void setSetPrivilege(BIFAccountSetPrivilegeInfo setPrivilege) {
        this.setPrivilege = setPrivilege;
    }

}
