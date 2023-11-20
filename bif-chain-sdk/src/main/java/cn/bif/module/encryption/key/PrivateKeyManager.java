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
package cn.bif.module.encryption.key;

import cn.bif.exception.EncException;
import cn.bif.module.encryption.model.KeyMember;
import cn.bif.module.encryption.model.KeyType;
import cn.bif.utils.base.Base58;
import cn.bif.utils.hex.HexFormat;
import cn.bif.utils.sm2.SM2;
import cn.bif.utils.sm2.SM2KeyPair;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;

public class PrivateKeyManager {
    private PublicKeyManager publicKey = new PublicKeyManager();
    private KeyMember keyMember = new KeyMember();

    /**
     * generate key pair (default: ed25519)
     *
     * @throws EncException
     */
    public PrivateKeyManager() throws EncException {
        this(KeyType.ED25519);
    }

    public PrivateKeyManager(boolean isDefault, String chainCode) throws EncException {
        this(KeyType.ED25519, chainCode);
    }

    /**
     * generate key pair
     *
     * @param type the type of key
     * @throws EncException
     */
    public PrivateKeyManager(KeyType type) throws EncException {
        switch (type) {
            case ED25519: {
                KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                EdDSAPrivateKey priKey = (EdDSAPrivateKey) keyPair.getPrivate();
                EdDSAPublicKey pubKey = (EdDSAPublicKey) keyPair.getPublic();
                keyMember.setRawSKey(priKey.getSeed());
                publicKey.setRawPublicKey(pubKey.getAbyte());
                break;
            }
            case SM2: {
                SM2KeyPair keyPair = SM2.getSM2KeyPair();
                byte[] rawSkey = SM2.getRawSkey(keyPair);
                byte[] rawPubKey = SM2.getRawPubKey(keyPair);
                keyMember.setRawSKey(rawSkey);
                publicKey.setRawPublicKey(rawPubKey);
                break;
            }
            default:
                throw new EncException("type does not exist");
        }
        setKeyType(type);
        publicKey.setKeyType(type);
    }

    /**
     * generate key pair
     *
     * @param type the type of key
     * @throws EncException
     */
    public PrivateKeyManager(KeyType type, String chainCode) throws EncException {
        switch (type) {
            case ED25519: {
                KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                EdDSAPrivateKey priKey = (EdDSAPrivateKey) keyPair.getPrivate();
                EdDSAPublicKey pubKey = (EdDSAPublicKey) keyPair.getPublic();
                keyMember.setRawSKey(priKey.getSeed());
                publicKey.setRawPublicKey(pubKey.getAbyte());
                publicKey.setChainCode(chainCode);
                break;
            }
            case SM2: {
                SM2KeyPair keyPair = SM2.getSM2KeyPair();
                byte[] rawSkey = SM2.getRawSkey(keyPair);
                byte[] rawPubKey = SM2.getRawPubKey(keyPair);
                keyMember.setRawSKey(rawSkey);
                publicKey.setRawPublicKey(rawPubKey);
                break;
            }
            default:
                throw new EncException("type does not exist");
        }
        setKeyType(type);
        publicKey.setKeyType(type);
    }

    /**
     * generate key pair
     *
     * @param seed the seed
     */
    public PrivateKeyManager(byte[] seed, KeyType type) {
        switch (type) {
            case ED25519: {
                EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
                EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(seed, spec);
                EdDSAPublicKeySpec spec2 = new EdDSAPublicKeySpec(privKey.getA(), spec);
                EdDSAPublicKey pDsaPublicKey = new EdDSAPublicKey(spec2);
                publicKey.setRawPublicKey(pDsaPublicKey.getAbyte());
                keyMember.setRawSKey(seed);
                setKeyType(KeyType.ED25519);
                publicKey.setKeyType(KeyType.ED25519);
                break;
            }
            case SM2: {
                keyMember.setRawSKey(seed);
                setKeyType(KeyType.SM2);
                break;
            }
            default:
                throw new EncException("type does not exist");
        }
    }

    public PrivateKeyManager(byte[] seed) {
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(seed, spec);
        EdDSAPublicKeySpec spec2 = new EdDSAPublicKeySpec(privKey.getA(), spec);
        EdDSAPublicKey pDsaPublicKey = new EdDSAPublicKey(spec2);
        publicKey.setRawPublicKey(pDsaPublicKey.getAbyte());
        keyMember.setRawSKey(seed);
        setKeyType(KeyType.ED25519);
        publicKey.setKeyType(KeyType.ED25519);
    }

    /**
     * generate key pair
     *
     * @param skey private key
     * @throws EncException
     */
    public PrivateKeyManager(String skey) throws EncException {
        getPrivateKey(skey, keyMember);
        publicKey.setKeyType(keyMember.getKeyType());
        byte[] rawPKey = getPublicKey(keyMember);
        publicKey.setRawPublicKey(rawPKey);
        keyMember.setRawPKey(rawPKey);
    }

    public PrivateKeyManager(String skey, String chainCode) throws EncException {
        getPrivateKey(skey, keyMember);
        publicKey.setKeyType(keyMember.getKeyType());
        byte[] rawPKey = getPublicKey(keyMember);
        publicKey.setRawPublicKey(rawPKey);
        keyMember.setRawPKey(rawPKey);
        publicKey.setChainCode(chainCode);
    }

    /**
     * set key type
     *
     * @param keyType key type
     */
    public void setKeyType(KeyType keyType) {
        keyMember.setKeyType(keyType);
    }

    /**
     * get key type
     *
     * @return key type
     */
    public KeyType getKeyType() {
        return keyMember.getKeyType();
    }

    /**
     * set raw private key
     *
     * @param rawSKey private key
     */
    public void setRawPrivateKey(byte[] rawSKey) {
        keyMember.setRawSKey(rawSKey);
    }

    /**
     * get raw private key
     *
     * @return raw private key
     */
    public byte[] getRawPrivateKey() {
        return keyMember.getRawSKey();
    }

    /**
     * 获取原生公钥
     *
     * @return
     */
    public byte[] getRawPublicKey() {
        return publicKey.getRawPublicKey();
    }

    /**
     * get public key
     *
     * @return public key
     */
    public PublicKeyManager getPublicKey() {
        return publicKey;
    }

    /**
     * @return encode private key
     * @throws EncException
     */
    public String getEncPrivateKey() throws EncException {
        byte[] rawSKey = keyMember.getRawSKey();
        if (rawSKey == null) {
            throw new EncException("raw private key is null");
        }
        return getEncPrivateKey(keyMember.getRawSKey(), keyMember.getKeyType());
    }

    /**
     * @param encPrivateKey encode private key
     * @return true or false
     */
    public static boolean isPrivateKeyValid(String encPrivateKey) {
        return encPrivateKeyValid(encPrivateKey);
    }


    /**
     * @return encode public key
     * @throws EncException
     */
    public String getEncPublicKey() throws EncException {
        byte[] rawPKey = publicKey.getRawPublicKey();
        if (rawPKey == null) {
            throw new EncException("raw public key is null");
        }
        return encPublicKey(keyMember.getKeyType(), rawPKey).toLowerCase();
    }

    /**
     * @param skey encode private key
     * @return encode public key
     * @throws EncException
     */
    public static String getEncPublicKey(String skey) throws EncException {
        KeyMember member = new KeyMember();
        getPrivateKey(skey, member);
        byte[] rawPKey = getPublicKey(member);
        return encPublicKey(member.getKeyType(), rawPKey).toLowerCase();
    }

    /**
     * 原生转星火公钥
     *
     * @param rawPKey
     * @param type
     * @return
     */
    public static String getEncPublicKey(byte[] rawPKey, KeyType type) {
        return encPublicKey(type, rawPKey).toLowerCase();
    }

    /**
     * @param encPublicKey encode public key
     * @return true or false
     */
    public static boolean isPublicKeyValid(String encPublicKey) {
        return PublicKeyManager.isPublicKeyValid(encPublicKey);
    }

    /**
     * @return encode address
     * @throws EncException getEncAddress
     */
    public String getEncAddress() throws EncException {
        return publicKey.getEncAddress();
    }

    /**
     * @param pKey encode public key
     * @return encode address
     * @throws EncException
     */
    public static String getEncAddress(String pKey, String chainCode) throws EncException {
        return PublicKeyManager.getEncAddress(pKey, chainCode);
    }

    /**
     * @param pKey encode public key
     * @return encode address
     * @throws EncException
     */
    public static String getEncAddress(String pKey) throws EncException {
        return PublicKeyManager.getEncAddress(pKey);
    }

    /**
     * @param encAddress encode address
     * @return true or false
     */
    public static boolean isAddressValid(String encAddress) {
        return PublicKeyManager.isAddressValid(encAddress);
    }

    /**
     * sign message
     *
     * @param msg message
     * @return sign data
     * @throws EncException
     */
    public byte[] sign(byte[] msg) throws EncException {
        return signMessage(msg, keyMember);
    }

    /**
     * sign message
     *
     * @param msg  message
     * @param skey private key
     * @return sign data
     * @throws EncException
     */
    public static byte[] sign(byte[] msg, String skey) throws EncException {
        KeyMember member = new KeyMember();
        getPrivateKey(skey, member);
        byte[] rawPKey = getPublicKey(member);
        member.setRawPKey(rawPKey);
        return signMessage(msg, member);
    }

    private static void getPrivateKey(String bSkey, KeyMember member) throws EncException {
        try {
            if (null == bSkey) {
                throw new EncException("Private key cannot be null");
            }

            byte[] skeyTmp = Base58.decode(bSkey);
            if (skeyTmp.length <= 5) {
                throw new EncException("Private key (" + bSkey + ") is invalid");
            }

            KeyType type;
            switch (skeyTmp[3]) {
                case KeyType.ED25519_VALUE: {
                    type = KeyType.values()[0];
                    break;
                }
                case KeyType.SM2_VALUE: {
                    type = KeyType.values()[1];
                    break;
                }
                default:
                    throw new EncException("Private key (" + bSkey + ") is invalid");

            }

            if (skeyTmp[4] != Base58.BASE_58_VALUE) {
                throw new EncException("Private key (" + bSkey + ") is invalid");
            }

            byte[] rawSKey = new byte[skeyTmp.length - 5];
            System.arraycopy(skeyTmp, 5, rawSKey, 0, rawSKey.length);

            member.setKeyType(type);
            member.setRawSKey(rawSKey);
        } catch (Exception e) {
            throw new EncException("Invalid privateKey");
        }

    }

    private static byte[] getPublicKey(KeyMember member) throws EncException {
        byte[] rawPKey = null;
        switch (member.getKeyType()) {
            case ED25519: {
                EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
                EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(member.getRawSKey(), spec);
                EdDSAPublicKeySpec spec2 = new EdDSAPublicKeySpec(privKey.getA(), spec);
                EdDSAPublicKey pDsaPublicKey = new EdDSAPublicKey(spec2);
                rawPKey = pDsaPublicKey.getAbyte();
                break;
            }
            case SM2: {
                BigInteger priKey = new BigInteger(member.getRawSKey());
                priKey = SM2.bigIntegerPreHandle(priKey);
                ECPoint pubKey = SM2.G.multiply(priKey).normalize();
                rawPKey = SM2.getRawPubKey(pubKey);
                break;
            }
            default:
                throw new EncException("Type does not exist");
        }
        return rawPKey;
    }

    /**
     * 原生转星火私钥
     *
     * @param type
     * @param raw_skey
     * @return
     * @throws EncException
     */
    public static String getEncPrivateKey(byte[] raw_skey, KeyType type) throws EncException {
        if (null == raw_skey) {
            throw new EncException("Private key is null");
        }
        byte[] buff = new byte[raw_skey.length + 5];
        buff[0] = (byte) 0x18;
        buff[1] = (byte) 0x9E;
        buff[2] = (byte) 0x99;
        System.arraycopy(raw_skey, 0, buff, 5, raw_skey.length);

        switch (type) {
            case ED25519: {
                buff[3] = KeyType.ED25519_VALUE;
                break;
            }
            case SM2: {
                buff[3] = KeyType.SM2_VALUE;
                break;
            }
            default:
                throw new EncException("type does not exist");
        }

        buff[4] = Base58.BASE_58_VALUE;

        return Base58.encode(buff);
    }

    private static boolean encPrivateKeyValid(String encPrivateKey) {
        boolean valid;
        try {
            if (null == encPrivateKey) {
                throw new EncException("Invalid privateKey");
            }

            byte[] privateKeyTemp = Base58.decode(encPrivateKey);

            if (privateKeyTemp.length != 37 || privateKeyTemp[0] != (byte) 0x18 || privateKeyTemp[1] != (byte) 0x9E ||
                    privateKeyTemp[2] != (byte) 0x99 || privateKeyTemp[4] != Base58.BASE_58_VALUE) {
                throw new EncException("Invalid privateKey");
            }

            switch (privateKeyTemp[3]) {
                // Ed25519算法 && SM2算法
                case KeyType.ED25519_VALUE:
                case KeyType.SM2_VALUE: {
                    break;
                }
                default:
                    throw new EncException("Invalid privateKey");
            }

            valid = true;
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    private static String encPublicKey(KeyType type, byte[] raw_pkey) throws EncException {
        if (null == raw_pkey) {
            throw new EncException("Public key is null");
        }
        int length = raw_pkey.length + 3;
        byte[] buff = new byte[length];
        buff[0] = (byte) 0xB0;

        switch (type) {
            case ED25519: {
                buff[1] = KeyType.ED25519_VALUE;
                break;
            }
            case SM2: {
                buff[1] = KeyType.SM2_VALUE;
                break;
            }
            default:
                throw new EncException("type does not exist");
        }

        buff[2] = Base58.BASE_58_VALUE;

        System.arraycopy(raw_pkey, 0, buff, 3, raw_pkey.length);

        return HexFormat.byteToHex(buff);
    }

    private static byte[] signMessage(byte[] msg, KeyMember member) throws EncException {
        if (null == member.getRawSKey()) {
            throw new EncException("Raw private key is null");
        }
        byte[] signMsg = null;

        try {
            switch (member.getKeyType()) {
                case ED25519: {
                    Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
                    EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
                    EdDSAPrivateKeySpec sKeySpec = new EdDSAPrivateKeySpec(member.getRawSKey(), spec);
                    EdDSAPrivateKey sKey = new EdDSAPrivateKey(sKeySpec);
                    sgr.initSign(sKey);
                    sgr.update(msg);

                    signMsg = sgr.sign();
                    break;
                }
                case SM2: {
                    BigInteger sKey = new BigInteger(member.getRawSKey());
                    sKey = SM2.bigIntegerPreHandle(sKey);
                    SM2KeyPair keyPair = new SM2KeyPair(SM2.G.multiply(sKey).normalize(), sKey);
                    signMsg = SM2.signWithBytes(new String(msg, "ISO_8859_1"), "1234567812345678", keyPair);
                    break;
                }
                default:
                    throw new EncException("Type does not exist");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new EncException("System error");
        } catch (InvalidKeyException e) {
            throw new EncException("Invalid privateKey");
        } catch (SignatureException e) {
            throw new EncException("Sign message failed");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return signMsg;
    }
}
