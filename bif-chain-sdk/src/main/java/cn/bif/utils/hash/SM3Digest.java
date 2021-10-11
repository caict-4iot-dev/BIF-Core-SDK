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

public class SM3Digest
{
	private static final int BYTE_LENGTH = 32;
	private static final int BLOCK_LENGTH = 64;
	private static final int BUFFER_LENGTH = BLOCK_LENGTH * 1;
	private byte[] xBuf = new byte[BUFFER_LENGTH];
	private int xBufOff;

	private byte[] V = SM3.iv.clone();
	private int cntBlock = 0;

	public SM3Digest() {
	}

	public SM3Digest(SM3Digest t)
	{
		System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);
		this.xBufOff = t.xBufOff;
		System.arraycopy(t.V, 0, this.V, 0, t.V.length);
	}

	public int doFinal(byte[] out, int outOff)
	{
		byte[] tmp = doFinal();
		System.arraycopy(tmp, 0, out, 0, tmp.length);
		return BYTE_LENGTH;
	}

	public void reset()
	{
		xBufOff = 0;
		cntBlock = 0;
		V = SM3.iv.clone();
	}

	public void update(byte[] in, int inOff, int len)
	{
		int partLen = BUFFER_LENGTH - xBufOff;
		int inputLen = len;
		int dPos = inOff;
		if (partLen < inputLen)
		{
			System.arraycopy(in, dPos, xBuf, xBufOff, partLen);
			inputLen -= partLen;
			dPos += partLen;
			doUpdate();
			while (inputLen > BUFFER_LENGTH)
			{
				System.arraycopy(in, dPos, xBuf, 0, BUFFER_LENGTH);
				inputLen -= BUFFER_LENGTH;
				dPos += BUFFER_LENGTH;
				doUpdate();
			}
		}

		System.arraycopy(in, dPos, xBuf, xBufOff, inputLen);
		xBufOff += inputLen;
	}

	private void doUpdate()
	{
		byte[] B = new byte[BLOCK_LENGTH];
		for (int i = 0; i < BUFFER_LENGTH; i += BLOCK_LENGTH)
		{
			System.arraycopy(xBuf, i, B, 0, B.length);
			doHash(B);
		}
		xBufOff = 0;
	}

	private void doHash(byte[] B)
	{
		byte[] tmp = SM3.CF(V, B);
		System.arraycopy(tmp, 0, V, 0, V.length);
		cntBlock++;
	}

	private byte[] doFinal()
	{
		byte[] B = new byte[BLOCK_LENGTH];
		byte[] buffer = new byte[xBufOff];
		System.arraycopy(xBuf, 0, buffer, 0, buffer.length);
		byte[] tmp = SM3.padding(buffer, cntBlock);
		for (int i = 0; i < tmp.length; i += BLOCK_LENGTH)
		{
			System.arraycopy(tmp, i, B, 0, B.length);
			doHash(B);
		}
		return V;
	}

	public void update(byte in)
	{
		byte[] buffer = new byte[] { in };
		update(buffer, 0, 1);
	}

	public int getDigestSize()
	{
		return BYTE_LENGTH;
	}

	public static byte[] Hash(byte[] input) {
		SM3Digest sm3Digest =new SM3Digest();
		//int len = 0;
		sm3Digest.update(input,0, input.length);


		return sm3Digest.doFinal();
	}
}
