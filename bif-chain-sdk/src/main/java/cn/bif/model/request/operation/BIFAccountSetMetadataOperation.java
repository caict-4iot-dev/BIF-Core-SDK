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
package cn.bif.model.request.operation;

import cn.bif.common.OperationType;

public class BIFAccountSetMetadataOperation extends BIFBaseOperation {
    private String key;
    private String value;
    private Long version;
    private Boolean deleteFlag;

    public BIFAccountSetMetadataOperation() {
        operationType = OperationType.ACCOUNT_SET_METADATA;
    }

    /**
     * @Method getOperationType
     * @Params []
     * @Return OperationType
     */
    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * @Method getKey
     * @Params []
     * @Return java.lang.String
     */
    public String getKey() {
        return key;
    }

    /**
     * @Method setKey
     * @Params [key]
     * @Return void
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @Method getValue
     * @Params []
     * @Return java.lang.String
     */
    public String getValue() {
        return value;
    }

    /**
     * @Method setValue
     * @Params [value]
     * @Return void
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @Method getVersion
     * @Params []
     * @Return java.lang.Long
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @Method setVersion
     * @Params [version]
     * @Return void
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * @Method getDeleteFlag
     * @Params []
     * @Return java.lang.Boolean
     */
    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @Method setDeleteFlag
     * @Params [deleteFlag]
     * @Return void
     */
    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
