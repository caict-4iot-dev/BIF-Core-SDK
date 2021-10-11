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
package cn.bif.module.encryption.crypto.keystore;

import cn.bif.module.encryption.crypto.keystore.entity.KeyStoreEty;
import cn.bif.module.encryption.crypto.keystore.entity.ScryptParamsEty;
import cn.bif.exception.EncException;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.module.encryption.model.KeyType;
import cn.bif.utils.aes.AesCtr;
import cn.bif.utils.hash.HashUtil;
import cn.bif.utils.hash.model.HashType;
import cn.bif.utils.hex.HexFormat;
import com.lambdaworks.crypto.SCrypt;

import java.security.SecureRandom;

public class KeyStore {

	public static KeyStoreEty generateKeyStore(String password, String privateKeyStr, int version) throws EncException {
		int n = 16384;
		int r = 8;
		int p = 1;
		return generateKeyStore(password, privateKeyStr, n, r, p, version);
	}

	public static KeyStoreEty generateKeyStore(String password, String privateKeyStr,int n,int r,int p,int version) throws EncException{

		String address = "";
		if (privateKeyStr == null || privateKeyStr.isEmpty() || "".equals(privateKeyStr)) {
			PrivateKeyManager privateKey = new PrivateKeyManager(KeyType.ED25519);
			privateKeyStr = privateKey.getEncPrivateKey();
			address = privateKey.getEncAddress();
		}else {
			PrivateKeyManager privateKey = new PrivateKeyManager(privateKeyStr);
			address = privateKey.getEncAddress();
		}
		KeyStoreEty keyStoreEty = keyStoreEncipher(password.getBytes(), privateKeyStr.getBytes(), n, r, p, version);
		keyStoreEty.setAddress(address);
		return keyStoreEty;
	}

	public static KeyStoreEty generateKeyStoreFromData(byte[] password, byte[] message, int version) throws EncException{
		int n = 16384;
		int r = 8;
		int p = 1;
		KeyStoreEty keyStoreEty = keyStoreEncipher(password, message, n, r, p, version);
		keyStoreEty.setAddress(HashUtil.GenerateHashHex(message,HashType.SHA256));
		return keyStoreEty;
	}

	public static KeyStoreEty generateKeyStoreFromData(byte[] password,byte[] message,int n,int r,int p,int version) throws EncException{
		KeyStoreEty keyStoreEty = keyStoreEncipher(password, message, n, r, p, version);
		keyStoreEty.setAddress(HashUtil.GenerateHashHex(message, HashType.SHA256));
		return keyStoreEty;
	}

	public static String decipherKeyStore(String password,KeyStoreEty keyStoreEty) throws EncException{
		String encPrivateKey = new String(keyStoreDecipher(password.getBytes(), keyStoreEty));
		String address = keyStoreEty.getAddress();
		PrivateKeyManager privateKey = new PrivateKeyManager(encPrivateKey);
		if (!privateKey.getEncAddress().equals(address)) {
			throw new EncException("The password was wrong");
		}
		return encPrivateKey;
	}

	public static byte[] decipherKeyStoreFromData(byte[] password,KeyStoreEty keyStoreEty) throws EncException{
		byte[] message = keyStoreDecipher(password, keyStoreEty);
		String address = keyStoreEty.getAddress();
		;
		if (address.compareTo(HashUtil.GenerateHashHex(message,HashType.SHA256)) != 0) {
			throw new EncException("The password was wrong");
		}
		return message;
	}

	private static KeyStoreEty keyStoreEncipher(byte[] password, byte[] message, int n, int r, int p, int version) throws EncException{
		KeyStoreEty keyStoreEty = new KeyStoreEty();
		int dkLen = 32;
		byte[] salt = new byte[32];
		SecureRandom randomSalt = new SecureRandom();
		randomSalt.nextBytes(salt);

		byte[] aesIv = new byte[16];
		SecureRandom randomIv = new SecureRandom();
		randomIv.nextBytes(aesIv);
		byte[] dk = null;
		try {
			dk = SCrypt.scrypt(password, salt, n, r, p, dkLen);
		} catch (Exception e) {
			throw new EncException(e.getMessage());
		}
		byte[] cyperText = AesCtr.encrypt(message, dk, aesIv);

		keyStoreEty.setVersion(version);
		keyStoreEty.setAesctr_iv(HexFormat.byteToHex(aesIv));
		keyStoreEty.setCypher_text(HexFormat.byteToHex(cyperText));
		ScryptParamsEty scryptParams = new ScryptParamsEty();
		scryptParams.setN(n);
		scryptParams.setP(p);
		scryptParams.setR(r);
		scryptParams.setSalt(HexFormat.byteToHex(salt));
		keyStoreEty.setScrypt_params(scryptParams);
		return keyStoreEty;
	}

	private static byte[] keyStoreDecipher(byte[] password,KeyStoreEty keyStoreEty) throws EncException{

		ScryptParamsEty scryptParams = keyStoreEty.getScrypt_params();
		int n = scryptParams.getN();
		int r = scryptParams.getR();
		int p = scryptParams.getP();
		byte[] salt = HexFormat.hexToByte(scryptParams.getSalt());

		int keyLen = 32;
		byte[] aesIv = HexFormat.hexToByte(keyStoreEty.getAesctr_iv());
		byte[] cypherText =  HexFormat.hexToByte(keyStoreEty.getCypher_text());

		byte[] dk = null;
		try {
			dk = SCrypt.scrypt(password, salt, n, r, p, keyLen);
		} catch (Exception e) {
			throw new EncException(e.getMessage());
		}
		byte[] encPrivateKey = AesCtr.decrypt(cypherText, dk, aesIv);

		return encPrivateKey;
	}
}
