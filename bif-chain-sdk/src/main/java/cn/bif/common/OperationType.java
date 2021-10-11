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
package cn.bif.common;

import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;

public enum OperationType {
    // Unknown operation
    UNKNOWN(0),

    // Activate an account
    ACCOUNT_ACTIVATE(1),

    // Set metadata
    ACCOUNT_SET_METADATA(2),

    // Set privilege
    ACCOUNT_SET_PRIVILEGE(3),

    // Send gas
    GAS_SEND(6),

    // Create contract
    CONTRACT_CREATE(13),

    // Invoke contract by sending
    CONTRACT_INVOKE(15),

    // Create Private Contract
    PRIVATE_CONTRACT_CREATE(17),

    // Call Private Contract
    PRIVATE_CONTRACT_CALL(18),
    ;

    private final int value;

    private OperationType(int value) {
        this.value = value;
    }

    public final int getNumber() {
        return value;
    }

    public static OperationType getOperationTypeByValue(Integer value) {
        for (OperationType operationType : OperationType.values()) {
            if (value.equals(operationType.getNumber())) {
                return operationType;
            }
        }
        throw new SDKException(SdkError.OPERATION_TYPE_ERROR);
    }
}
