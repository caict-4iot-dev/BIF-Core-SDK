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

import cn.bif.common.JsonUtils;
import cn.bif.module.encryption.crypto.keystore.KeyStore;
import cn.bif.module.encryption.crypto.keystore.entity.KeyStoreEty;

import java.util.HashMap;
import java.util.Map;


public class TestCrypto {
    public static void main(String argv[]) {
        String encPrivateKey = "priSPKqru2zMzeb14XWxPNM1sassFeqyyUZotCAYcvCjhNof7t";
        String password = "bif8888";
        TestKeyStoreWithPrivateKey(encPrivateKey, password);

    }

    public static void TestKeyStoreWithPrivateKey(String encPrivateKey, String password) {
        try {
            int n = (int) Math.pow(2, 16);
            //生成密钥存储器-1
            KeyStoreEty returEencPrivateKey = KeyStore.generateKeyStore(password,encPrivateKey, 2, 1, 1, n);
            System.out.println(JsonUtils.toJSONString(returEencPrivateKey));
            //生成密钥存储器-2
            KeyStoreEty keyStore1 = KeyStore.generateKeyStore(password, encPrivateKey, n);
            System.out.println(JsonUtils.toJSONString(keyStore1));

            //解析密钥存储器
            String keyStoreStr="{\"address\":\"did:bid:efEScJgGPf54vfU8ciEjjugkJLB4xYzp\",\"aesctr_iv\":\"EEDDD37CEB6864030124142CEB081BCD\",\"cypher_text\":\"7274705F65388E30338A2D69AE2241DBABCF66550C0453BEE30CFA45F02E04D08FAC551B46171531CA067B6E85BC342F43C8\",\"scrypt_params\":{\"n\":16384,\"p\":1,\"r\":8,\"salt\":\"82D37133C13525EDE4EF19DCD692592FC1685B5EDAABA8C943EA2C1AD4596FB3\"},\"version\":2}";
            String privateKey = KeyStore.decipherKeyStore(password, JsonUtils.toJavaObject(keyStoreStr,KeyStoreEty.class));
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
