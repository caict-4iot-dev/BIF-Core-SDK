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
package cn.bif.sdkSamples.encryption.example;

import cn.bif.module.encryption.crypto.mnemonic.Mnemonic;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.module.encryption.model.KeyType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class TestMnemonic {
    public static void main(String[] argv) {
        //生成助记词
        byte[] aesIv = new byte[16];
        SecureRandom randomIv = new SecureRandom();
        randomIv.nextBytes(aesIv);

        List<String> mnemonicCodes1 = Mnemonic.generateMnemonicCode(aesIv);
        for (String mnemonicCode : mnemonicCodes1) {
            System.out.println(mnemonicCode + " ");
        }
        System.out.println();

        //根据助记词生成私钥
        // field resemble board rain amazing gap aisle debris clay frequent usage industry
        List<String> mnemonicCodes = new ArrayList<>();
        mnemonicCodes.add("style");
        mnemonicCodes.add("orchard");
        mnemonicCodes.add("science");
        mnemonicCodes.add("puppy");
        mnemonicCodes.add("place");
        mnemonicCodes.add("differ");
        mnemonicCodes.add("benefit");
        mnemonicCodes.add("thing");
        mnemonicCodes.add("wrap");
        mnemonicCodes.add("type");
        mnemonicCodes.add("build");
        mnemonicCodes.add("scare");


        List<String> hdPaths = new ArrayList<>();
        hdPaths.add("M/44H/526H/1H/0/0");
        List<String> privateKeysBySM2 = Mnemonic.generatePrivateKeysByCrypto(KeyType.SM2,mnemonicCodes, hdPaths);
        for (String privateKey : privateKeysBySM2) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.println("SM2 { privateKey : "+privateKey + " \n encAddress : " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey))+" \n }");
        }

        List<String> privateKeysByED25519 = Mnemonic.generatePrivateKeysByCrypto(KeyType.ED25519,mnemonicCodes, hdPaths);
        for (String privateKey : privateKeysByED25519) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.println("ED25519  { privateKey : "+privateKey + " \n encAddress : " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey))+" \n }");
        }

        List<String> privateKeys = Mnemonic.generatePrivateKeys(mnemonicCodes, hdPaths);
        for (String privateKey : privateKeys) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.println(privateKey + " " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey)));
        }
        System.out.println();
    }
}
