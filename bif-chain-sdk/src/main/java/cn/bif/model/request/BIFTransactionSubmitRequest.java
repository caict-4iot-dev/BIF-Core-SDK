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

import cn.bif.model.response.result.data.BIFSignature;

import java.util.Arrays;

public class BIFTransactionSubmitRequest {
    private String serialization;
    private String signData;
    private String publicKey;
    private BIFSignature[] signatures;

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public BIFSignature[] getSignatures() {
        return signatures;
    }

    public void addSignature(String publicKey, String signData) {
        if (null == signatures) {
            signatures = new BIFSignature[1];
        } else {
            signatures = Arrays.copyOf(signatures, signatures.length + 1);
        }
        BIFSignature signature = new BIFSignature();
        signature.setPublicKey(publicKey);
        signature.setSignData(signData);
        signatures[signatures.length - 1] = signature;
    }
}
