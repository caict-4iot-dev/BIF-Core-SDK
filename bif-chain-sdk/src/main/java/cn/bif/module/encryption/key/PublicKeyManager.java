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
import cn.bif.utils.hash.HashUtil;
import cn.bif.utils.hex.HexFormat;
import cn.bif.utils.sm2.SM2;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.MessageDigest;
import java.security.Signature;

public class PublicKeyManager {
    private KeyMember keyMember = new KeyMember();
    private String chainCode = "";

    public PublicKeyManager() {
    }

    /**
     * structure with encrytion public key
     */
    public PublicKeyManager(String encPublicKey) throws EncException {
        getPublicKey(encPublicKey, keyMember);
    }

    /**
     * set enc public key
     *
     * @param encPublicKey encryption public key
     * @throws EncException
     */
    public void setEncPublicKey(String encPublicKey) throws EncException {
        getPublicKey(encPublicKey, keyMember);
    }

    /**
     * set raw public key
     *
     * @param rawPKey raw public key
     */
    public void setRawPublicKey(byte[] rawPKey) {
        keyMember.setRawPKey(rawPKey);
    }

    /**
     * get raw public key
     *
     * @return raw public key
     */
    public byte[] getRawPublicKey() {
        return keyMember.getRawPKey();
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
    private KeyType getKeyType() {
        return keyMember.getKeyType();
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    /**
     * @return encode address
     * @throws EncException
     */
    public String getEncAddress() throws EncException {
        byte[] raw_pkey = keyMember.getRawPKey();
        if (null == raw_pkey) {
            throw new EncException("public key is null");
        }

        return encAddress(keyMember.getKeyType(), raw_pkey, chainCode);
    }

    /**
     * @param pKey encode public key
     * @return encode address
     * @throws EncException
     */
    public static String getEncAddress(String pKey, String chainCode) throws EncException {
        KeyMember member = new KeyMember();
        getPublicKey(pKey, member);

        return encAddress(member.getKeyType(), member.getRawPKey(), chainCode);
    }

    /**
     * @param pKey encode public key
     * @return encode address
     * @throws EncException
     */
    public static String getEncAddress(String pKey) throws EncException {
        KeyMember member = new KeyMember();
        getPublicKey(pKey, member);

        return encAddress(member.getKeyType(), member.getRawPKey(), "");
    }

    /**
     * @param encAddress encode address
     * @return true or false
     */
    public static boolean isAddressValid(String encAddress) {
        return encAddressValid(encAddress);
    }

    /**
     * @param encPublicKey encode public key
     * @return true or false
     */
    public static boolean isPublicKeyValid(String encPublicKey) {
        return encPublicKeyValid(encPublicKey);
    }

    /**
     * check sign datas
     *
     * @param msg     source message
     * @param signMsg signed message
     * @return true or false
     * @throws EncException
     */
    public boolean verify(byte[] msg, byte[] signMsg) throws EncException {
        boolean verifySuccess = verifyMessage(msg, signMsg, keyMember);

        return verifySuccess;
    }

    /**
     * check sign datas
     *
     * @param msg          source message
     * @param signMsg      signed message
     * @param encPublicKey enc public key
     * @return true or false
     * @throws EncException
     */
    public static boolean verify(byte[] msg, byte[] signMsg, String encPublicKey) throws EncException {
        boolean verifySuccess = false;
        KeyMember member = new KeyMember();
        getPublicKey(encPublicKey, member);
        verifySuccess = verifyMessage(msg, signMsg, member);

        return verifySuccess;
    }

    private static void getPublicKey(String bPkey, KeyMember member) throws EncException {
        if (null == bPkey) {
            throw new EncException("public key cannot be null");
        }

        if (!HexFormat.isHexString(bPkey)) {
            throw new EncException("public key (" + bPkey + ") is invalid, please check");
        }

        KeyType type = null;
        byte[] buffPKey = HexFormat.hexToByte(bPkey);

        if (buffPKey.length < 3) {
            throw new EncException("public key (" + bPkey + ") is invalid, please check");
        }

        if (buffPKey[0] != (byte) 0xB0) {
            throw new EncException("public key (" + bPkey + ") is invalid, please check");
        }

        // 判断算法类型
        if (buffPKey[1] == KeyType.ED25519_VALUE) {
            type = KeyType.ED25519;
        } else if (buffPKey[1] == KeyType.SM2_VALUE) {
            type = KeyType.SM2;
        } else {
            throw new EncException("public key (" + bPkey + ") is invalid, please check");
        }

        if (buffPKey[2] != Base58.BASE_58_VALUE) {
            throw new EncException("public key (" + bPkey + ") is invalid, please check");
        }

        byte[] rawPKey = new byte[buffPKey.length - 3];
        System.arraycopy(buffPKey, 3, rawPKey, 0, rawPKey.length);
        member.setRawPKey(rawPKey);
        member.setKeyType(type);
    }

    private static boolean encPublicKeyValid(String encPublicKey) {
        boolean valid;
        try {
            if (null == encPublicKey) {
                throw new EncException("Invalid publicKey");
            }
            if (!HexFormat.isHexString(encPublicKey)) {
                throw new EncException("Invalid publicKey");
            }

            KeyType type = null;
            byte[] buffPKey = HexFormat.hexToByte(encPublicKey);

            if (buffPKey.length < 3 || buffPKey[0] != (byte)0xB0 || (buffPKey[1] != (byte)0x65 && buffPKey[1] != (byte)0x7A) || buffPKey[2] != (byte)0x66) {
                throw new EncException("Invalid publicKey");
            }

            valid = true;
        } catch (Exception exception) {
            valid = false;
        }
        return valid;
    }

    private static String encAddress(KeyType type, byte[] raw_pkey, String chainCode) {
        byte[] buff = new byte[22];
        //buff[0] = (byte) 0xF8;
        //buff[1] = (byte) 0xE1;
        //buff[0] = (byte) (type.ordinal() + 1);

        byte[] hashPkey = HashUtil.CalHash(type, raw_pkey);
        System.arraycopy(hashPkey, 10, buff, 0, 22);

        String encAddress = Base58.encode(buff);

        if (chainCode.isEmpty()) {
            switch (type) {
                case ED25519: {
                    return "did:bid:" + "ef" + encAddress;
                }
                case SM2: {
                    return "did:bid:" + "zf" + encAddress;
                }
                default:
                    throw new EncException("type does not exist");
            }
        } else {
            return "did:bid:" + chainCode + ":" + "ef" + encAddress;
        }
    }

    private static boolean encAddressValid(String encAddress) {
        boolean valid;
        try {
            if (null == encAddress) {
                throw new EncException("Invalid address");
            }
            boolean startsWithDidBid = encAddress.startsWith("did:bid:");
            if (!startsWithDidBid) {
                throw new EncException("Invalid address");
            }
            String[] items = encAddress.split(":");
            if (items.length != 3 && items.length != 4) {
                throw new EncException("Invalid address");
            }
            if (items.length == 3) {
                encAddress = items[2];
            } else {
                encAddress = items[3];
            }
            String prifx = encAddress.substring(0, 2);

            if (!prifx.equals("ef") && !prifx.equals("zf")) {
                throw new EncException("Invalid address");
            }

            String address = encAddress.substring(2, encAddress.length());
            byte[] base58_address = Base58.decode(address);
            if (base58_address.length != 22) {
                throw new EncException("Invalid address");
            }
            valid = true;
        } catch (Exception e) {
            valid = false;
        }

        return valid;
    }

    private static boolean verifyMessage(byte[] msg, byte[] sign, KeyMember member) {
        boolean verifySuccess;
        try {
            switch (member.getKeyType()) {
                case ED25519: {
                    Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
                    EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
                    EdDSAPublicKeySpec eddsaPubKey = new EdDSAPublicKeySpec(member.getRawPKey(), spec);
                    EdDSAPublicKey vKey = new EdDSAPublicKey(eddsaPubKey);
                    sgr.initVerify(vKey);
                    sgr.update(msg);
                    verifySuccess = sgr.verify(sign);
                    break;
                }
                case SM2: {
                    verifySuccess = SM2.verify(msg, sign, member);
                    break;
                }
                default:
                    throw new EncException("type does not exist");
            }
        } catch (Exception e) {
            verifySuccess = false;
        }

        return verifySuccess;
    }
}

