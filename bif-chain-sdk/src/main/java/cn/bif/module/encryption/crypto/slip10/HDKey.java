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
package cn.bif.module.encryption.crypto.slip10;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;

import java.math.BigInteger;
import java.security.Security;

public class HDKey {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final long HARDENED_OFFSET = 0x80000000L;

    public Keys getMasterKeyFromSeed(Slip10Curve curve, String seedHex) {
        byte[] seed = hexStringToByteArray(seedHex);
        byte[] I = hmacSha512(curve.key.getBytes(), seed);

        byte[] IL = Arrays.copyOfRange(I, 0, 32);
        byte[] IR = Arrays.copyOfRange(I, 32, 64);

        BigInteger n = getCurveNValue(curve);
        BigInteger privateKeyInt = new BigInteger(1, IL);

        if (curve != Slip10Curve.ED25519 && (privateKeyInt.equals(BigInteger.ZERO) || privateKeyInt.compareTo(n) >= 0)) {
            // Retry with IR as the new seed
            return getMasterKeyFromSeed(curve, byteArrayToHexString(IR));
        } else {
            return new Keys(byteArrayToHexString(IL), byteArrayToHexString(IR));
        }
    }

    public Keys deriveKeyByPath(Slip10Curve curve, String path, String seedHex) throws Exception {
        Keys masterKeys = getMasterKeyFromSeed(curve, seedHex);
        String[] segments = getPathSegments(path);
        for (String segment : segments) {
            boolean isHardened = segment.endsWith("'");
            long index = Long.valueOf(isHardened ? String.valueOf(Long.valueOf(segment.substring(0, segment.length() - 1)) + HARDENED_OFFSET) : segment);
            masterKeys = getCKDPriv(masterKeys, index, curve);
        }

        return masterKeys;
    }

    private Keys getCKDPriv(Keys keys, Long index, Slip10Curve curve) throws Exception {
        byte[] kpar = hexStringToByteArray(keys.getKey());
        byte[] cpar = hexStringToByteArray(keys.getChainCode());
        byte[] data;
        byte[] I;
        Boolean isHardened = index >= HARDENED_OFFSET;
        if (isHardened) {
            data = Arrays.concatenate(new byte[]{(byte) 0}, kpar, ser32(index));
        } else {
            if (curve == Slip10Curve.ED25519) {
                throw new UnsupportedOperationException("Non-hardened key derivation is not supported for ED25519");
            }
            ECPoint point = point(hexStringToByteArray(keys.getKey()), curve);
            data = Arrays.concatenate(point.getEncoded(true), ser32(index));
        }
        I = hmacSha512(cpar, data);

        return handleCKDPriv(keys, index, curve, I);
    }

    private Keys handleCKDPriv(Keys keys, Long index, Slip10Curve curve, byte[] I) {
        byte[] IL = Arrays.copyOfRange(I, 0, 32);
        byte[] IR = Arrays.copyOfRange(I, 32, 64);

        if (curve == Slip10Curve.ED25519) {
            return new Keys(byteArrayToHexString(IL), byteArrayToHexString(IR));
        }

        BigInteger n = getCurveNValue(curve);
        BigInteger ki = new BigInteger(1, IL);
        BigInteger kpar = new BigInteger(1, hexStringToByteArray(keys.getKey()));

        BigInteger newKeyBigInt = ki.add(kpar).mod(n);
        String newKey = newKeyBigInt.toString(16);
        newKey = padHexString(newKey, 64);

        if (ki.compareTo(n) >= 0 || newKeyBigInt.equals(BigInteger.ZERO)) {
            byte[] data = Arrays.concatenate(new byte[]{(byte) 0x01}, IR, ser32(index));
            byte[] IPrime = hmacSha512(hexStringToByteArray(keys.getChainCode()), data);
            return handleCKDPriv(keys, index, curve, IPrime);
        }

        return new Keys(newKey, byteArrayToHexString(IR));
    }

    private byte[] hmacSha512(byte[] key, byte[] data) {
        HMac hmac = new HMac(new SHA512Digest());
        hmac.init(new KeyParameter(key));
        hmac.update(data, 0, data.length);
        byte[] output = new byte[hmac.getMacSize()];
        hmac.doFinal(output, 0);
        return output;
    }

    private ECNamedCurveParameterSpec getCurveParams(Slip10Curve curve) {
        switch (curve) {
            case SECP256K1:
                return ECNamedCurveTable.getParameterSpec("secp256k1");
            case SM2:
                return ECNamedCurveTable.getParameterSpec("sm2p256v1");
            default:
                throw new UnsupportedOperationException("Curve not supported");
        }
    }

    private ECPoint point(byte[] kpar, Slip10Curve curve) throws Exception {

        if(curve == Slip10Curve.ED25519){
            throw new Exception("Non-hardened key derivation not supported for Ed25519");
        }

        ECNamedCurveParameterSpec curveParams = getCurveParams(curve);
        ECDomainParameters domainParams = new ECDomainParameters(
                curveParams.getCurve(),   // 曲线
                curveParams.getG(),       // 基点 G
                curveParams.getN(),       // 曲线的阶（order）
                curveParams.getH()        // 曲线的余因子（cofactor）
        );
        FixedPointCombMultiplier multiplier = new FixedPointCombMultiplier();
        return multiplier.multiply(domainParams.getG(), new BigInteger(1, kpar));
    }

    private String[] getPathSegments(String path) {
        // 使用 split() 方法分割路径字符串
        String[] segments = path.split("/");

        String[] pathSegments = new String[segments.length - 1];

        for (int i = 1; i < segments.length; i++) {
            pathSegments[i - 1] = segments[i];
        }
        return pathSegments;
    }


    private BigInteger getCurveNValue(Slip10Curve curve) {
        switch (curve) {
            case SECP256K1:
                return new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
            case ED25519:
                return new BigInteger("1000000000000000000000000000000014DEF9DEA2F79CD65812631A5CF5D3ED", 16);
            case SM2:
                return new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);
            default:
                throw new UnsupportedOperationException("Invalid curve");
        }
    }

    private byte[] ser32(long i) {
        if (i < 0 || i > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("Invalid integer value");
        }
        return new byte[]{
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)
        };
    }

    private byte[] hexStringToByteArray(String hex) {
        hex = hex.replaceAll("0x", "");
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    private String byteArrayToHexString(byte[] bytes) {
        String hexStr = String.format("%02X", new BigInteger(1, bytes)).toLowerCase();
        return padHexString(hexStr, 64);
    }

    public static String padHexString(String hexString, int fixedLength) {
        int currentLength = hexString.length();
        if (currentLength >= fixedLength) {
            return hexString; // 不需要补零
        } else {
            int numberOfZerosToPad = fixedLength - currentLength;
            StringBuilder paddedHex = new StringBuilder(hexString);
            for (int i = 0; i < numberOfZerosToPad; i++) {
                paddedHex.insert(0, '0'); // 在字符串的开头插入零
            }
            return paddedHex.toString();
        }
    }
}
