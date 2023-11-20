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
import cn.bif.module.encryption.model.KeyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class TestMnemonic {
    static String mnemonicCode = "attitude coyote negative library clerk copy portion bus combine gospel topic typical";
    static String hdPath = "m/44'/2022'/0'/0'/0'";
    static String hdPathForSM2 = "m/44'/2022'/0'/0/0";
    static List<String> mnemonicCodes = null;
    @BeforeAll
    static  void  setUp() {
        mnemonicCodes = Arrays.asList(mnemonicCode.split(" "));
    }
    @Test
    public void generateMnemonicCode(){
        //生成助记词
        byte[] aesIv = new byte[16];
        SecureRandom randomIv = new SecureRandom();
        randomIv.nextBytes(aesIv);

        List<String> mnemonicCodes1 = Mnemonic.generateMnemonicCode(aesIv);
        for (String mnemonicCode : mnemonicCodes1) {
            System.out.println(mnemonicCode + " ");
        }
    }
    @Test
    public void generatePrivateKeyForAccount() throws Exception {
        String privateKeyByED25519 = Mnemonic.generatePrivateKeyByMnemonicCodeAndKeyTypeAndHDPath(mnemonicCodes, KeyType.ED25519, hdPath);
        System.out.println(privateKeyByED25519);
        String privateKeyBySM2 = Mnemonic.generatePrivateKeyByMnemonicCodeAndKeyTypeAndHDPath(mnemonicCodes, KeyType.SM2, hdPathForSM2);
        System.out.println(privateKeyBySM2);
    }
}
