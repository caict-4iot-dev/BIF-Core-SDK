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
package cn.bif.module.encryption.crypto.keystore.entity;

public class KeyStoreEty {

	private String address;
	private String aesctr_iv;
	private String cypher_text;
	private ScryptParamsEty scrypt_params;
	private int version;


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getAesctr_iv() {
		return aesctr_iv;
	}


	public void setAesctr_iv(String aesctr_iv) {
		this.aesctr_iv = aesctr_iv;
	}


	public String getCypher_text() {
		return cypher_text;
	}


	public void setCypher_text(String cypher_text) {
		this.cypher_text = cypher_text;
	}


	public ScryptParamsEty getScrypt_params() {
		return scrypt_params;
	}


	public void setScrypt_params(ScryptParamsEty scrypt_params) {
		this.scrypt_params = scrypt_params;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	@Override
	public String toString() {
		return "KeyStoreEty [address=" + address + ", aesctr_iv=" + aesctr_iv + ", cypher_text=" + cypher_text
				+ ", scrypt_params=" + scrypt_params + ", version=" + version + "]";
	}




}
