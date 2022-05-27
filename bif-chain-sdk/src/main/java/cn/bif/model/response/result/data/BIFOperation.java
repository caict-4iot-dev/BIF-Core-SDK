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

import cn.bif.utils.hex.HexFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class BIFOperation {
    @JsonProperty(value = "type")
    private int type;

    @JsonProperty(value = "source_address")
    private String sourceAddress;

    @JsonProperty(value = "metadata")
    private String metadata;

    @JsonProperty(value = "create_account")
    private BIFAccountActiviateInfo createAccount;

    @JsonProperty(value = "pay_coin")
    private BIFGasSendInfo sendGas;

    @JsonProperty(value = "set_metadata")
    private BIFAccountSetMetadataInfo setMetadata;

    @JsonProperty(value = "set_privilege")
    private BIFAccountSetPrivilegeInfo setPrivilege;
    @JsonProperty(value = "log")
    private BIFLogInfo log;


    @JsonProperty(value = "input")
    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public BIFLogInfo getLog() {
        return log;
    }

    public void setLog(BIFLogInfo log) {
        this.log = log;
    }

    /**
     * @Method getType
     * @Params []
     * @Return int
     */
    public int getType() {
        return type;
    }

    /**
     * @Method setType
     * @Params [type]
     * @Return void
     */
    public void setType(int type) {
        this.type = type;
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
     * @Method getMetadata
     * @Params []
     * @Return java.lang.String
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
        if (metadata != null && metadata.length() != 0) {
            this.metadata = new String(HexFormat.hexToByte(metadata));
        }
    }

    /**
     * @Method getCreateAccount
     * @Params []
     * @Return AccountActiviateInfo
     */
    public BIFAccountActiviateInfo getCreateAccount() {
        return createAccount;
    }

    /**
     * @Method setCreateAccount
     * @Params [createAccount]
     * @Return void
     */
    public void setCreateAccount(BIFAccountActiviateInfo createAccount) {
        this.createAccount = createAccount;
    }


    /**
     * @Method getSendGas
     * @Params []
     * @Return GasSendInfo
     */
    public BIFGasSendInfo getSendGas() {
        return sendGas;
    }

    /**
     * @Method setSendGas
     * @Params [sendGas]
     * @Return void
     */
    public void setSendGas(BIFGasSendInfo sendGas) {
        this.sendGas = sendGas;
    }

    /**
     * @Method getSetMetadata
     * @Params []
     * @Return AccountSetMetadataInfo
     */
    public BIFAccountSetMetadataInfo getSetMetadata() {
        return setMetadata;
    }

    /**
     * @Method setSetMetadata
     * @Params [setMetadata]
     * @Return void
     */
    public void setSetMetadata(BIFAccountSetMetadataInfo setMetadata) {
        this.setMetadata = setMetadata;
    }

    /**
     * @Method getSetPrivilege
     * @Params []
     * @Return AccountSetPrivilegeInfo
     */
    public BIFAccountSetPrivilegeInfo getSetPrivilege() {
        return setPrivilege;
    }

    /**
     * @Method setSetPrivilege
     * @Params [setPrivilege]
     * @Return void
     */
    public void setSetPrivilege(BIFAccountSetPrivilegeInfo setPrivilege) {
        this.setPrivilege = setPrivilege;
    }

}
