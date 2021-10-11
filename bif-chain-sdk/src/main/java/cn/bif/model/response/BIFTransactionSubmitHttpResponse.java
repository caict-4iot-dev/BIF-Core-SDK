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
package cn.bif.model.response;

import cn.bif.model.response.result.BIFTransactionSubmitHttpResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BIFTransactionSubmitHttpResponse {

    @JsonProperty(value = "success_count")
    private Integer successCount;

    @JsonProperty(value = "results")
    private BIFTransactionSubmitHttpResult[] results;

    /**
     * @Method getSuccessCount
     * @Params []
     * @Return java.lang.Integer
     */
    public Integer getSuccessCount() {
        return successCount;
    }

    /**
     * @Method setSuccessCount
     * @Params [successCount]
     * @Return void
     */
    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    /**
     * @Method getResults
     * @Params []
     * @Return TransactionSubmitHttpResult[]
     */
    public BIFTransactionSubmitHttpResult[] getResults() {
        return results;
    }

    /**
     * @Method setResults
     * @Params [results]
     * @Return void
     */
    public void setResults(BIFTransactionSubmitHttpResult[] results) {
        this.results = results;
    }
}
