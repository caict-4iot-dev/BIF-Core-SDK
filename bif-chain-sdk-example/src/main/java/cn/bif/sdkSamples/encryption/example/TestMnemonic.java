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

import java.util.ArrayList;
import java.util.List;

public class TestMnemonic {
    public static void main(String[] argv) {
//        byte[] aesIv = new byte[16];
//        SecureRandom randomIv = new SecureRandom();
//        randomIv.nextBytes(aesIv);
//
//        List<String> mnemonicCodes = Mnemonic.generateMnemonicCode(aesIv);
//        for (String mnemonicCode : mnemonicCodes) {
//            System.out.print(mnemonicCode + " ");
//        }
//        System.out.println();

        // field resemble board rain amazing gap aisle debris clay frequent usage industry
        List<String> mnemonicCodes = new ArrayList<>();
        mnemonicCodes.add("field");
        mnemonicCodes.add("resemble");
        mnemonicCodes.add("board");
        mnemonicCodes.add("rain");
        mnemonicCodes.add("amazing");
        mnemonicCodes.add("gap");
        mnemonicCodes.add("aisle");
        mnemonicCodes.add("debris");
        mnemonicCodes.add("clay");
        mnemonicCodes.add("frequent");
        mnemonicCodes.add("usage");
        mnemonicCodes.add("industry");


        List<String> hdPaths = new ArrayList<>();
        hdPaths.add("M/44H/526H/1H/0/0");
        List<String> privateKeys = Mnemonic.generatePrivateKeys(mnemonicCodes, hdPaths);
        for (String privateKey : privateKeys) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.print(privateKey + " " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey)));
        }
        System.out.println();
    }
}
