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
package cn.bif.utils.aes;

import cn.bif.exception.EncException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCtr {

    public static byte[] encrypt(byte[] plainText, byte[] key, byte[] iv) throws EncException {
    	byte[] encrypted = null;
    	try {
    		byte[] clean = plainText;
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encrypted = cipher.doFinal(clean);
    	}
    	catch (Exception e) {
    		throw new EncException(e.getMessage());
    	}
        return encrypted;
    }

    public static byte[] decrypt(byte[] encryptedIvTextBytes, byte[] key, byte[] iv) throws EncException {
    	byte[] decrypted = null;
    	try {
    		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipherDecrypt = Cipher.getInstance("AES/CTR/NoPadding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            decrypted = cipherDecrypt.doFinal(encryptedIvTextBytes);
    	}
    	catch (Exception e) {
            throw new EncException(e.getMessage());
    	}

        return decrypted;
    }
}
