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
package cn.bif.module.blockchain.impl;

import cn.bif.common.Constant;
import cn.bif.common.Tools;
import cn.bif.exception.SDKException;
import cn.bif.exception.SdkError;
import cn.bif.model.request.operation.BIFGasSendOperation;
import cn.bif.module.encryption.key.PublicKeyManager;
import cn.bif.protobuf.Chain;
import com.google.protobuf.ByteString;


/**
 *
 *
 */
public class BIFGasServiceImpl {

    public static Chain.Operation send(BIFGasSendOperation gasSendOperation, String transSourceAddress) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = gasSendOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKeyManager.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String destAddress = gasSendOperation.getDestAddress();
            if (!PublicKeyManager.isAddressValid(destAddress)) {
                throw new SDKException(SdkError.INVALID_DESTADDRESS_ERROR);
            }
            boolean isNotValid = (!Tools.isEmpty(sourceAddress) && sourceAddress.equals(destAddress)) ||
                    (Tools.isEmpty(sourceAddress) && transSourceAddress.equals(destAddress));
            if (isNotValid) {
                throw new SDKException(SdkError.SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR);
            }
            Long amount = gasSendOperation.getAmount();
            if (Tools.isEmpty(amount) || amount < Constant.INIT_ZERO) {
                throw new SDKException(SdkError.INVALID_GAS_AMOUNT_ERROR);
            }
            String metadata = gasSendOperation.getMetadata();
            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.PAY_COIN);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            Chain.OperationPayCoin.Builder operationPayCoin = operation.getPayCoinBuilder();
            operationPayCoin.setDestAddress(destAddress);
            operationPayCoin.setAmount(amount);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }
}
