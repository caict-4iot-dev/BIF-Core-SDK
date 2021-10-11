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
package cn.bif.model.response.result.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 */
public class BIFContractStat {

    @JsonProperty(value = "apply_time")
    private Long applyTime;

    @JsonProperty(value = "memory_usage")
    private Long memoryUsage;

    @JsonProperty(value = "stack_usage")
    private Long stackUsage;

    @JsonProperty(value = "stack_usage")
    private Long step;

    /**
     *
     * @Method getStackUsage
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Long getStackUsage() {
        return stackUsage;
    }

    /**
     *
     * @Method setStackUsage
     * @Params [stackUsage]
     * @Return void
     *
     */
    public void setStackUsage(Long stackUsage) {
        this.stackUsage = stackUsage;
    }

    /**
     *
     * @Method getApplyTime
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Long getApplyTime() {
        return applyTime;
    }

    /**
     *
     * @Method setApplyTime
     * @Params [applyTime]
     * @Return void
     *
     */
    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    /**
     *
     * @Method getMemoryUsage
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Long getMemoryUsage() {
        return memoryUsage;
    }

    /**
     *
     * @Method setMemoryUsage
     * @Params [memoryUsage]
     * @Return void
     *
     */
    public void setMemoryUsage(Long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    /**
     *
     * @Method getStep
     * @Params []
     * @Return java.lang.Long
     *
     */
    public Long getStep() {
        return step;
    }

    /**
     *
     * @Method setStep
     * @Params [step]
     * @Return void
     *
     */
    public void setStep(Long step) {
        this.step = step;
    }
}
