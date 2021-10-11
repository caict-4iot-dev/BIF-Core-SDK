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
package cn.bif.utils.hash;

import cn.bif.exception.EncException;
import cn.bif.module.encryption.model.KeyType;
import cn.bif.utils.hash.model.HashType;
import cn.bif.utils.hex.HexFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    /**
     * @param src
     * @param type
     * @return
     */
    public static String GenerateHashHex(byte[] src, HashType type) {
        String hashHex = null;
        switch (type) {
            case SHA256: {
                Sha256 sha256 = new Sha256(src);
                byte[] hash = sha256.finish();
                hashHex = HexFormat.byteToHex(hash).toLowerCase();
                break;
            }
            case SM3: {
                byte[] hash = SM3Digest.Hash(src);
                hashHex = HexFormat.byteToHex(hash).toLowerCase();
                break;
            }
            default:
                throw new EncException("type does not exist");
        }
        return hashHex;
    }

    /**
     * get hash
     *
     * @param type KeyType.ED25519 or KeyType.ECCSM2
     * @param data Data before hash
     * @return data after hash
     */
    public static byte[] CalHash(KeyType type, byte[] data) {
        byte[] result = null;
        if (type == KeyType.ED25519) {
            MessageDigest sha256 = null;
            try {
                sha256 = MessageDigest.getInstance("SHA-256");

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            sha256.update(data);
            result = sha256.digest();
        } else {
            result = SM3Digest.Hash(data);
        }
        return result;
    }
}
