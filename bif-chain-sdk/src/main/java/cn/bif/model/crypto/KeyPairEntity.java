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
package cn.bif.model.crypto;


import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.module.encryption.model.KeyType;

public class KeyPairEntity {
    private String encAddress;
    private String encPublicKey;
    private String encPrivateKey;
    private byte[] rawPrivateKey;
    private byte[] rawPublicKey;

    public KeyPairEntity(String encAddress, String encPublicKey, String encPrivateKey, byte[] rawPrivateKey, byte[] rawPublicKey) {
        this.encAddress = encAddress;
        this.encPublicKey = encPublicKey;
        this.encPrivateKey = encPrivateKey;
        this.rawPrivateKey = rawPrivateKey;
        this.rawPublicKey = rawPublicKey;
    }

    public static KeyPairEntity getBidAndKeyPair() {
        try {
            PrivateKeyManager keyPair = new PrivateKeyManager();
            return new KeyPairEntity(keyPair.getEncAddress(), keyPair.getEncPublicKey(), keyPair.getEncPrivateKey(), keyPair.getRawPrivateKey(), keyPair.getRawPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPairEntity getBidAndKeyPairBySM2() {
        try {
            PrivateKeyManager keyPair = new PrivateKeyManager(KeyType.SM2);
            return new KeyPairEntity(keyPair.getEncAddress(), keyPair.getEncPublicKey(), keyPair.getEncPrivateKey(), keyPair.getRawPrivateKey(), keyPair.getRawPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEncAddress() {
        return encAddress;
    }

    public void setEncAddress(String encAddress) {
        this.encAddress = encAddress;
    }

    public String getEncPublicKey() {
        return encPublicKey;
    }

    public void setEncPublicKey(String encPublicKey) {
        this.encPublicKey = encPublicKey;
    }

    public String getEncPrivateKey() {
        return encPrivateKey;
    }

    public void setEncPrivateKey(String encPrivateKey) {
        this.encPrivateKey = encPrivateKey;
    }

    public byte[] getRawPrivateKey() {
        return rawPrivateKey;
    }

    public void setRawPrivateKey(byte[] rawPrivateKey) {
        this.rawPrivateKey = rawPrivateKey;
    }

    public byte[] getRawPublicKey() {
        return rawPublicKey;
    }

    public void setRawPublicKey(byte[] rawPublicKey) {
        this.rawPublicKey = rawPublicKey;
    }
}
