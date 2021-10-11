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
import cn.bif.model.response.result.data.BIFSigner;
import cn.bif.model.response.result.data.BIFTypeThreshold;

import java.util.Arrays;

public class BIFAccountSetPrivilegeOperation extends BIFBaseOperation {
    private String masterWeight;
    private BIFSigner[] signers;
    private String txThreshold;
    private BIFTypeThreshold[] typeThresholds;

    public BIFAccountSetPrivilegeOperation() {
        operationType = OperationType.ACCOUNT_SET_PRIVILEGE;
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
     * @Method getMasterWeight
     * @Params []
     * @Return java.lang.String
     */
    public String getMasterWeight() {
        return masterWeight;
    }

    /**
     * @Method setMasterWeight
     * @Params [masterWeight]
     * @Return void
     */
    public void setMasterWeight(String masterWeight) {
        this.masterWeight = masterWeight;
    }

    /**
     * @Method getSigners
     * @Params []
     * @Return Signer[]
     */
    public BIFSigner[] getSigners() {
        return signers;
    }

    /**
     * @Method setSigners
     * @Params [signers]
     * @Return void
     */
    public void setSigners(BIFSigner[] signers) {
        this.signers = signers;
    }

    /**
     * @Method addSigner
     * @Params [signer]
     * @Return void
     */
    public void addSigner(BIFSigner signer) {
        if (null == signers) {
            signers = new BIFSigner[1];
        } else {
            signers = Arrays.copyOf(signers, signers.length + 1);
        }
        signers[signers.length - 1] = signer;
    }

    /**
     * @Method getTxThreshold
     * @Params []
     * @Return java.lang.String
     */
    public String getTxThreshold() {
        return txThreshold;
    }

    /**
     * @Method setTxThreshold
     * @Params [txThreshold]
     * @Return void
     */
    public void setTxThreshold(String txThreshold) {
        this.txThreshold = txThreshold;
    }

    /**
     * @Method getTypeThresholds
     * @Params []
     * @Return TypeThreshold[]
     */
    public BIFTypeThreshold[] getTypeThresholds() {
        return typeThresholds;
    }

    /**
     * @Method setTypeThresholds
     * @Params [typeThresholds]
     * @Return void
     */
    public void setTypeThresholds(BIFTypeThreshold[] typeThresholds) {
        this.typeThresholds = typeThresholds;
    }

    /**
     * @Method addTypeThreshold
     * @Params [typeThreshold]
     * @Return void
     */
    public void addTypeThreshold(BIFTypeThreshold typeThreshold) {
        if (null == typeThresholds) {
            typeThresholds = new BIFTypeThreshold[1];
        } else {
            typeThresholds = Arrays.copyOf(typeThresholds, typeThresholds.length + 1);
        }
        typeThresholds[typeThresholds.length - 1] = typeThreshold;
    }
}
