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

import cn.bif.module.encryption.crypto.keystore.KeyStore;
import cn.bif.module.encryption.crypto.keystore.entity.KeyStoreEty;
import com.alibaba.fastjson.JSON;

public class TestCrypto {
    public static void main(String argv[]) {
        String encPrivateKey = "priSPKqru2zMzeb14XWxPNM1sassFeqyyUZotCAYcvCjhNof7t";
        String password = "test";
        TestKeyStoreWithPrivateKey(encPrivateKey, password);

    }

    public static void TestKeyStoreWithPrivateKey(String encPrivateKey, String password) {
        try {
            //KeyStoreEty keyStore = KeyStore.generateKeyStore(password, encPrivateKey);
            //
            int n = (int) Math.pow(2, 16);
            KeyStoreEty keyStore = KeyStore.generateKeyStore(password, encPrivateKey, 2);
            System.out.println(JSON.toJSONString(keyStore));
            String privateKey = KeyStore.decipherKeyStore(password, keyStore);
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
