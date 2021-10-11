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
package cn.bif.model.response.result;

import cn.bif.model.response.result.data.BIFContractStat;
import cn.bif.model.response.result.data.BIFTransactionEnvs;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;


public class BIFContractCallPrivateResult<T> {

    @JsonProperty(value = "logs")
    private Map<String, Object> logs;


    @JsonProperty(value = "query_rets")
    private List<Object> queryRets;

    @JsonProperty(value = "stat")
    private BIFContractStat stat;

    @JsonProperty(value = "txs")
    private BIFTransactionEnvs[] txs;

    /**
     * @Method getLogs
     * @Params []
     * @Return java.lang.Object
     */
    public Map<String, Object> getLogs() {
        return logs;
    }

    /**
     * @Method setLogs
     * @Params [logs]
     * @Return void
     */
    public void setLogs(Map<String, Object> logs) {
        this.logs = logs;
    }

    /**
     * @Method getQueryRets
     * @Params []
     * @Return java.lang.Object[]
     */
    public List<Object> getQueryRets() {
        return queryRets;
    }

    /**
     * @Method setQueryRets
     * @Params [queryRets]
     * @Return void
     */
    public void setQueryRets(List<Object> queryRets) {
        this.queryRets = queryRets;
    }

    /**
     * @Method getStat
     * @Params []
     * @Return ContractStat
     */
    public BIFContractStat getStat() {
        return stat;
    }

    /**
     * @Method setStat
     * @Params [stat]
     * @Return void
     */
    public void setStat(BIFContractStat stat) {
        this.stat = stat;
    }

    /**
     * @Method getTxs
     * @Params []
     * @Return TransactionEnv[]
     */
    public BIFTransactionEnvs[] getTxs() {
        return txs;
    }

    /**
     * @Method setTxs
     * @Params [txs]
     * @Return void
     */
    public void setTxs(BIFTransactionEnvs[] txs) {
        this.txs = txs;
    }
}
