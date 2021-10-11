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

import cn.bif.exception.SdkError;
import cn.bif.model.response.result.BIFContractInvokeResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BIFContractInvokeResponse extends BIFBaseResponse {

     @JsonProperty(value =  "result")
    private BIFContractInvokeResult result;

    /**
     * @Method getResult
     * @Params []
     * @Return BIFContractInvokeByGasResult
     */
    public BIFContractInvokeResult getResult() {
        return result;
    }

    /**
     * @Method setResult
     * @Params [result]
     * @Return void
     */
    public void setResult(BIFContractInvokeResult result) {
        this.result = result;
    }

    /**
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     */
    public void buildResponse(SdkError sdkError, BIFContractInvokeResult result) {
        this.errorCode = sdkError.getCode();
        this.errorDesc = sdkError.getDescription();
        this.result = result;
    }

    /**
     * @Method buildResponse
     * @Params [errorCode, errorDesc, result]
     * @Return void
     */
    public void buildResponse(int errorCode, String errorDesc, BIFContractInvokeResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
