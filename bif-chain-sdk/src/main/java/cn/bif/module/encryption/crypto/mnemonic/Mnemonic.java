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
package cn.bif.module.encryption.crypto.mnemonic;

import cn.bif.exception.EncException;
import cn.bif.module.encryption.crypto.slip10.HDKey;
import cn.bif.module.encryption.crypto.slip10.Keys;
import cn.bif.module.encryption.crypto.slip10.Slip10Curve;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.module.encryption.model.KeyType;
import org.bitcoinj.crypto.*;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Mnemonic {
    public static List<String> generateMnemonicCode(byte[] random) throws EncException {
        if (random.length != 16) {
            throw new EncException("The length of random must be 16");
        }

        List<String> mnemonicCodes;
        try {
            mnemonicCodes = MnemonicCode.INSTANCE.toMnemonic(random);
        } catch (MnemonicException.MnemonicLengthException e) {
            throw new EncException(e.getMessage());
        }

        if (null == mnemonicCodes || mnemonicCodes.size() == 0) {
            throw new EncException("Failed to generate mnemonic codes");
        }

        return mnemonicCodes;
    }
    public static String generatePrivateKeyByMnemonicCodeAndKeyTypeAndHDPath(List<String> mnemonicCodes, KeyType keyType, String hdPath) throws Exception {
        if (null == mnemonicCodes || mnemonicCodes.size() == 0) {
            throw new EncException("The size of mnemonicCodes must be bigger than or equal to 0");
        }
        if (null == hdPath) {
            throw new EncException("The size of hdPaths must be bigger than or equal to 0");
        }
        byte[] seed = MnemonicCode.toSeed(mnemonicCodes, "");
        Slip10Curve curveSeed;
        switch(keyType) {
            case ED25519:
                curveSeed = Slip10Curve.ED25519;
                break;
            case SM2:
                curveSeed = Slip10Curve.SM2;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + keyType);
        }
        HDKey hdKey = new HDKey();
        Keys resultKeys = hdKey.deriveKeyByPath(curveSeed, hdPath, Hex.toHexString(seed));
        PrivateKeyManager privateKey = new PrivateKeyManager(Hex.decode(resultKeys.getKey()), keyType);
        return privateKey.getEncPrivateKey();
    }
}
