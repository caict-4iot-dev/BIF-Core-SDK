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
package cn.bif.utils.sm2;

import cn.bif.exception.EncException;
import cn.bif.module.encryption.model.KeyMember;
import cn.bif.utils.hash.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * SM2公钥加密算法实现 包括 -密钥对生成 -签名,验签
 *
 * @author Yangwei
 */
public class SM2 {
    private static BigInteger n = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);
    private static BigInteger p = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
    private static BigInteger a = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
    private static BigInteger b = new BigInteger(
            "28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);
    private static BigInteger gx = new BigInteger(
            "32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);
    private static BigInteger gy = new BigInteger(
            "BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);
    private static ECDomainParameters ecc_bc_spec;
    private static int w = (int) Math.ceil(n.bitLength() * 1.0 / 2) - 1;
    private static BigInteger _2w = new BigInteger("2").pow(w);
    private static final int DIGEST_LENGTH = 32;
    private static SM3Digest SM3 = new SM3Digest();

    private static SecureRandom random = new SecureRandom();
    public static ECCurve.Fp curve = new ECCurve.Fp(p, // q
            a, // a
            b); // b;
    public static ECPoint G = curve.createPoint(gx, gy);

    /**
     * 随机数生成器
     *
     * @param max
     * @return
     */
    private static BigInteger random(BigInteger max) {

        BigInteger r = new BigInteger(256, random);

        while (r.compareTo(max) >= 0) {
            r = new BigInteger(128, random);
        }

        return r;
    }

    /**
     * 判断是否在范围内
     *
     * @param param
     * @param min
     * @param max
     * @return
     */
    private static boolean between(BigInteger param, BigInteger min, BigInteger max) {
        if (param.compareTo(min) >= 0 && param.compareTo(max) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断生成的公钥是否合法
     *
     * @param publicKey
     * @return
     */
    private boolean checkPublicKey(ECPoint publicKey) {

        if (!publicKey.isInfinity()) {

            BigInteger x = publicKey.getXCoord().toBigInteger();
            BigInteger y = publicKey.getYCoord().toBigInteger();

            if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {

                BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);
                BigInteger yResult = y.pow(2).mod(p);
                if (yResult.equals(xResult) && publicKey.multiply(n).isInfinity()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 生成密钥对
     *
     * @return
     */
    public SM2KeyPair generateKeyPair() {

        BigInteger d = random(n.subtract(new BigInteger("1")));
        SM2KeyPair keyPair = new SM2KeyPair(G.multiply(d).normalize(), d);

        if (checkPublicKey(keyPair.getPublicKey())) {
            return keyPair;
        } else {
            return null;
        }
    }

    public SM2() {
        ecc_bc_spec = new ECDomainParameters(curve, G, n);
    }

    /**
     * 字节数组拼接
     *
     * @param params
     * @return
     */
    private static byte[] join(byte[]... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] res = null;
        try {
            for (int i = 0; i < params.length; i++) {
                baos.write(params[i]);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * sm3摘要
     *
     * @param params
     * @return
     */
    private static byte[] sm3hash(byte[]... params) {
        byte[] res = null;
        try {
            res = SM3.Hash(join(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 取得用户标识字节数组
     *
     * @param IDA
     * @param aPublicKey
     * @return
     */
    private static byte[] ZA(String IDA, ECPoint aPublicKey) {
        byte[] idaBytes = IDA.getBytes();
        int entlenA = idaBytes.length * 8;
        byte[] ENTLA = new byte[]{(byte) (entlenA & 0xFF00), (byte) (entlenA & 0x00FF)};
        byte[] ZA = sm3hash(ENTLA, idaBytes, bytesLenTo32(a.toByteArray()), bytesLenTo32(b.toByteArray()),
                bytesLenTo32(gx.toByteArray()), bytesLenTo32(gy.toByteArray()),
                bytesLenTo32(aPublicKey.getXCoord().toBigInteger().toByteArray()),
                bytesLenTo32(aPublicKey.getYCoord().toBigInteger().toByteArray()));
        return ZA;
    }

    /**
     * 签名
     *
     * @param M       签名信息
     * @param IDA     签名方唯一标识
     * @param keyPair 签名方密钥对
     * @return 签名
     */
    public static Signature sign(String M, String IDA, SM2KeyPair keyPair) throws UnsupportedEncodingException {
        byte[] ZA = ZA(IDA, keyPair.getPublicKey());
        byte[] M_ = join(ZA, M.getBytes("ISO8859-1"));
        BigInteger e = new BigInteger(1, sm3hash(M_));
        BigInteger k;
        BigInteger r;
        do {
            k = random(n);
            ECPoint p1 = G.multiply(k).normalize();
            BigInteger x1 = p1.getXCoord().toBigInteger();
            r = e.add(x1);
            r = r.mod(n);
        } while (r.equals(BigInteger.ZERO) || r.add(k).equals(n));

        BigInteger s = ((keyPair.getPrivateKey().add(BigInteger.ONE).modInverse(n))
                .multiply((k.subtract(r.multiply(keyPair.getPrivateKey()))).mod(n))).mod(n);

        return new Signature(r, s);
    }

    /**
     * 签名
     *
     * @param M       签名信息
     * @param IDA     签名方唯一标识
     * @param keyPair 签名方密钥对
     * @return 签名字节数组
     */
    public static byte[] signWithBytes(String M, String IDA, SM2KeyPair keyPair) throws UnsupportedEncodingException {
        Signature sign = sign(M, IDA, keyPair);
        return sign.toByte();
    }


    /**
     * 验签
     *
     * @param M          签名信息
     * @param signature  签名
     * @param IDA        签名方唯一标识
     * @param aPublicKey 签名方公钥
     * @return true or false
     */
    public static boolean verify(String M, Signature signature, String IDA, ECPoint aPublicKey) throws UnsupportedEncodingException {
        if (!between(signature.r, BigInteger.ONE, n))
            return false;
        if (!between(signature.s, BigInteger.ONE, n))
            return false;

        byte[] M_ = join(ZA(IDA, aPublicKey), M.getBytes("ISO8859-1"));
        BigInteger e = new BigInteger(1, sm3hash(M_));
        BigInteger t = signature.r.add(signature.s).mod(n);

        if (t.equals(BigInteger.ZERO))
            return false;

        ECPoint p1 = G.multiply(signature.s).normalize();
        ECPoint p2 = aPublicKey.multiply(t).normalize();
        BigInteger x1 = p1.add(p2).normalize().getXCoord().toBigInteger();
        BigInteger R = e.add(x1).mod(n);
        if (R.equals(signature.r))
            return true;
        return false;
    }

    /**
     * 验签
     *
     * @param M          签名信息
     * @param signature  签名
     * @param IDA        签名方唯一标识
     * @param aPublicKey 签名方公钥
     * @return true or false
     */
    public static boolean verify(String M, byte[] signature, String IDA, ECPoint aPublicKey) throws UnsupportedEncodingException {

        byte[] rBytes = Arrays.copyOfRange(signature, 0, 32);
        byte[] sBytes = Arrays.copyOfRange(signature, 32, 64);

        rBytes = bytesLenPreHandle(rBytes);
        sBytes = bytesLenPreHandle(sBytes);

        BigInteger r = new BigInteger(rBytes);
        BigInteger s = new BigInteger(sBytes);


        if (!between(r, BigInteger.ONE, n))
            return false;
        if (!between(s, BigInteger.ONE, n))
            return false;

        byte[] M_ = join(ZA(IDA, aPublicKey), M.getBytes("ISO8859-1"));
        BigInteger e = new BigInteger(1, sm3hash(M_));
        BigInteger t = r.add(s).mod(n);

        if (t.equals(BigInteger.ZERO))
            return false;

        ECPoint p1 = G.multiply(s).normalize();
        ECPoint p2 = aPublicKey.multiply(t).normalize();
        BigInteger x1 = p1.add(p2).normalize().getXCoord().toBigInteger();
        BigInteger R = e.add(x1).mod(n);
        if (R.equals(r))
            return true;
        return false;
    }

    public static boolean verify(byte[] msg, byte[] sign, KeyMember member) throws UnsupportedEncodingException {
        byte[] pubKeyXBytes = new byte[32];
        byte[] pubKeyYBytes = new byte[32];
        System.arraycopy(member.getRawPKey(), 1, pubKeyXBytes, 0, 32);
        System.arraycopy(member.getRawPKey(), 33, pubKeyYBytes, 0, 32);

        pubKeyXBytes = SM2.bytesLenPreHandle(pubKeyXBytes);
        pubKeyYBytes = SM2.bytesLenPreHandle(pubKeyYBytes);

        BigInteger x = new BigInteger(pubKeyXBytes);
        BigInteger y = new BigInteger(pubKeyYBytes);

        ECPoint publicKey = SM2.curve.createPoint(x, y);
        return SM2.verify(new String(msg, "ISO8859-1"), sign, "1234567812345678", publicKey);
    }

    private static byte[] bytesLenPreHandle(byte[] bytes) {
        if (new BigInteger(bytes).signum() == -1) {
            byte[] rBytesWithLen33 = new byte[33];
            System.arraycopy(bytes, 0, rBytesWithLen33, 1, 32);
            bytes = rBytesWithLen33;
        }
        return bytes;
    }

    private static byte[] bytesLenTo32(byte[] bytes) {
        if (bytes.length > 33) {
            throw new EncException("The length of bytes must be less than or equal to 33");
        }else if (bytes.length == 33) {
            byte[] rBytesWithLen32 = new byte[32];
            System.arraycopy(bytes, 1, rBytesWithLen32, 0, 32);
            bytes = rBytesWithLen32;
        }else{
            byte[] rBytesWithLen32 = new byte[32];
            for(int i=0;i<rBytesWithLen32.length-bytes.length;i++){
                rBytesWithLen32[i]=0;
            }
            System.arraycopy(bytes, 0, rBytesWithLen32, (rBytesWithLen32.length-bytes.length), bytes.length);
            bytes = rBytesWithLen32;
        }
        return bytes;
    }
    public static BigInteger bigIntegerPreHandle(BigInteger bigInteger) {
        if (bigInteger.signum() == -1) {
            byte[] rBytesWithLen33 = new byte[33];
            System.arraycopy(bigInteger.toByteArray(), 0, rBytesWithLen33, 1, 32);
            bigInteger = new BigInteger(rBytesWithLen33);
        }
        return bigInteger;
    }

    public static byte[] getRawPubKey(ECPoint pubKey) {
        byte[] pubKeyBytes = new byte[65];
        pubKeyBytes[0] = (byte) 4;
        byte[] pubKeyXBytes = pubKey.getAffineXCoord().toBigInteger().toByteArray();
        byte[] pubKeyYBytes = pubKey.getAffineYCoord().toBigInteger().toByteArray();
        if (pubKeyXBytes.length == 33)
            System.arraycopy(pubKeyXBytes, 1, pubKeyBytes, 1, 32);
        else
            System.arraycopy(pubKeyXBytes, 0, pubKeyBytes, 1, 32);

        if (pubKeyYBytes.length == 33)
            System.arraycopy(pubKeyYBytes, 1, pubKeyBytes, 33, 32);
        else
           // System.arraycopy(pubKeyYBytes, 0, pubKeyBytes, 33, 32);
            System.arraycopy(pubKeyYBytes, 0, pubKeyBytes,  (pubKeyBytes.length-pubKeyYBytes.length),pubKeyYBytes.length);
        return pubKeyBytes;
    }

    public static SM2KeyPair getSM2KeyPair() {
        SM2 sm = new SM2();
        return sm.generateKeyPair();
    }

    public static byte[] getRawSkey(SM2KeyPair sm2KeyPair) {
        BigInteger priKey = sm2KeyPair.getPrivateKey();

        byte[] priKeyBytes = priKey.toByteArray();
        if (priKey.toByteArray().length == 33) {
            priKeyBytes = Arrays.copyOfRange(priKey.toByteArray(), 1, 33);
        }
        return priKeyBytes;
    }

    public static byte[] getRawPubKey(SM2KeyPair sm2KeyPair) {
        ECPoint pubKey = sm2KeyPair.getPublicKey();
        return getRawPubKey(pubKey);
    }


    public static class Signature {
        BigInteger r;
        BigInteger s;

        public Signature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }

        public String toString() {
            return r.toString(16) + s.toString(16);
        }

        public byte[] toByte() {
            byte[] sign = new byte[64];
            byte[] rBytes = r.toByteArray();
            byte[] sBytes = s.toByteArray();
            if (rBytes.length == 33)
                System.arraycopy(rBytes, 1, sign, 0, 32);
            else
                System.arraycopy(rBytes, 0, sign, 0, 32);

            if (sBytes.length == 33)
                System.arraycopy(sBytes, 1, sign, 32, 32);
            else
                System.arraycopy(sBytes, 0, sign, 32, 32);
            return sign;
        }
    }
}
