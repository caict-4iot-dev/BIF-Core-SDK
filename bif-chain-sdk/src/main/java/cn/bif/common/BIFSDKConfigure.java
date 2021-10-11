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
package cn.bif.common;

public class BIFSDKConfigure {
    private String url;
    private int httpConnectTimeOut;
    private int httpReadTimeOut;
    private long chainId = 0;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getChainId() {
        return chainId;
    }

    public void setChainId(long chainId) {
        this.chainId = chainId;
    }

    public int getHttpConnectTimeOut() {
        return httpConnectTimeOut;
    }

    public void setHttpConnectTimeOut(int httpConnectTimeOut) {
        this.httpConnectTimeOut = httpConnectTimeOut;
    }

    public int getHttpReadTimeOut() {
        return httpReadTimeOut;
    }

    public void setHttpReadTimeOut(int httpReadTimeOut) {
        this.httpReadTimeOut = httpReadTimeOut;
    }
}
