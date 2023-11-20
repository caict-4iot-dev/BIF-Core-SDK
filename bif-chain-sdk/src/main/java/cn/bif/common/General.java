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

import cn.bif.api.BIFSDK;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class General {
    private static General general = null;
    private String url;

    private General() {
    }

    public synchronized static General getInstance() {
        if (general == null) {
            general = new General();
        }
        general.url = BIFSDK.getSdk().getUrl();
        return general;
    }

    public String getUrl() {
        return url;
    }

    public String accountGetInfoUrl(String address,Integer domainId) throws UnsupportedEncodingException {
        return url + "/getAccountBase?address=" + URLEncoder.encode(address, "utf8")
                + (Tools.isEmpty(domainId) ? "" : "&domainid=" +  domainId);
    }
    public String accountGetMetadataUrl(String address, String key,Integer domainId) throws UnsupportedEncodingException {
        return url + "/getAccount?address=" + URLEncoder.encode(address, "utf8") + (Tools.isEmpty(key) ? "" : "&key=" + URLEncoder.encode(key, "utf8"))
                + (Tools.isEmpty(domainId) ? "" : "&domainid=" + domainId);
    }

    public String contractCallUrl() {
        return url + "/callContract";
    }

    public String transactionSubmitUrl() {
        return url + "/submitTransaction";
    }

    public String transactionGetInfoUrl(String hash,Integer domainId) throws UnsupportedEncodingException {
        return url + "/getTransactionHistory?hash=" + URLEncoder.encode(hash, "utf8")+ (Tools.isEmpty(domainId) ? "" : "&domainid=" + domainId);
    }

    public String blockGetNumberUrl(Integer domainId) {
        return url + "/getLedger"+ (Tools.isEmpty(domainId) ? "" : "?domainid=" +  domainId);
    }

    public String blockGetTransactionsUrl(Long blockNumber,Integer domainId)  {
        return url + "/getTransactionHistory?ledger_seq=" + blockNumber + (Tools.isEmpty(domainId) ? "" : "&domainid=" + domainId);
    }

    public String blockGetInfoUrl(Long blockNumber,Integer domainId,Boolean withLeader) {
        return url + "/getLedger?seq=" + blockNumber  + (Tools.isEmpty(domainId) ? "" : "&domainid=" +  domainId) + (withLeader ? "&with_leader=" +  withLeader : "");
    }

    public String blockGetLatestInfoUrl(Integer domainId) {
        return url + "/getLedger" + (Tools.isEmpty(domainId) ? "" : "?domainid=" +  domainId);
    }

    public String blockGetValidatorsUrl(Long blockNumber,Integer domainId) {
        return url + "/getLedger?seq=" + blockNumber + "&with_validator=true" + (Tools.isEmpty(domainId) ? "" : "&domainid=" + domainId);
    }

    public String blockGetLatestValidatorsUrl(Integer domainId) {
        return url + "/getLedger?with_validator=true"+ (Tools.isEmpty(domainId) ? "" : "&domainid=" +  domainId);
    }

    public String transactionEvaluationFee() {
        return url + "/testTransaction";
    }

    public String getTxCacheSize(Integer domainId) {
        return url + "/getTxCacheSize"+ (Tools.isEmpty(domainId) ? "" : "?domainid=" +  domainId);
    }

    public String getTxCacheData(Integer domainId,String hash) throws UnsupportedEncodingException {
        return url + "/getTransactionCache?domainid="+domainId + (Tools.isEmpty(hash) ? "" : "&hash=" +  URLEncoder.encode(hash, "utf8"));
    }
}
