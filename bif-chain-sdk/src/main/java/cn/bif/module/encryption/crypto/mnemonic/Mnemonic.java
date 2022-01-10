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
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.module.encryption.model.KeyType;
import org.bitcoinj.crypto.*;

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

    public static List<String> generatePrivateKeys(List<String> mnemonicCodes, List<String> hdPaths) throws EncException {
        if (null == mnemonicCodes || mnemonicCodes.size() == 0) {
            throw new EncException("The size of mnemonicCodes must be bigger than or equal to 0");
        }
        if (null == hdPaths || hdPaths.size() == 0) {
            throw new EncException("The size of hdPaths must be bigger than or equal to 0");
        }
        byte[] seed = MnemonicCode.toSeed(mnemonicCodes, "");
        DeterministicKey deterministicKey = HDKeyDerivation.createMasterPrivateKey(seed);

        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
        List<String> privateKeys = new ArrayList<>();
        for (String hdPath : hdPaths) {
            List<ChildNumber> keyPath = HDUtils.parsePath(hdPath);
            DeterministicKey childKey = deterministicHierarchy.get(keyPath, true, true);
            BigInteger privKey = childKey.getPrivKey();
            byte[] bytes = privKey.toByteArray();
            byte[] seeds = new byte[32];
            int startPos = 0;
            int length = 32;
            if (privKey.toByteArray().length == 33) {
                startPos = 1;
            }
            if (privKey.toByteArray().length == 31) {
                length = 31;
            }
            System.arraycopy(bytes, startPos, seeds, 0, length);
            PrivateKeyManager privateKey = new PrivateKeyManager(seeds);
            privateKeys.add(privateKey.getEncPrivateKey());
        }
        if (privateKeys.size() == 0) {
            throw new EncException("Failed to generate private key with mnemonicCodes");
        }
        return privateKeys;
    }

    public static List<String> generatePrivateKeysByCrypto(KeyType type,List<String> mnemonicCodes, List<String> hdPaths) throws EncException {
        if (null == mnemonicCodes || mnemonicCodes.size() == 0) {
            throw new EncException("The size of mnemonicCodes must be bigger than or equal to 0");
        }
        if (null == hdPaths || hdPaths.size() == 0) {
            throw new EncException("The size of hdPaths must be bigger than or equal to 0");
        }
        byte[] seed = MnemonicCode.toSeed(mnemonicCodes, "");
        DeterministicKey deterministicKey = HDKeyDerivation.createMasterPrivateKey(seed);

        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
        List<String> privateKeys = new ArrayList<>();
        for (String hdPath : hdPaths) {
            List<ChildNumber> keyPath = HDUtils.parsePath(hdPath);
            DeterministicKey childKey = deterministicHierarchy.get(keyPath, true, true);
            BigInteger privKey = childKey.getPrivKey();
            byte[] bytes = privKey.toByteArray();
            byte[] seeds = new byte[32];
            int startPos = 0;
            int length = 32;
            if (privKey.toByteArray().length == 33) {
                startPos = 1;
            }
            if (privKey.toByteArray().length == 31) {
                length = 31;
            }
            System.arraycopy(bytes, startPos, seeds, 0, length);
            PrivateKeyManager privateKey = new PrivateKeyManager(seeds,type);
            privateKeys.add(privateKey.getEncPrivateKey());
        }
        if (privateKeys.size() == 0) {
            throw new EncException("Failed to generate private key with mnemonicCodes");
        }
        return privateKeys;
    }
}
