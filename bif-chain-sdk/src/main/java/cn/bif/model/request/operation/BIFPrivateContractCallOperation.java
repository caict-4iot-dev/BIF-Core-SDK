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
package cn.bif.model.request.operation;

import cn.bif.common.OperationType;

public class BIFPrivateContractCallOperation extends BIFBaseOperation {
    private Integer type;
    private String input;
    private String from;
    private String[] to;
    private String destAddress;

    public BIFPrivateContractCallOperation() {
        operationType = OperationType.PRIVATE_CONTRACT_CALL;
    }

    /**
     * @Method getOperationType
     * @Params []
     * @Return OperationType
     */
    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * @Method getType
     * @Params []
     * @Return java.lang.Integer
     */
    public Integer getType() {
        return type;
    }

    /**
     * @Method setType
     * @Params [type]
     * @Return void
     */
    public void setType(Integer type) {
        this.type = type;
    }


    /**
     * @Method getInput
     * @Params []
     * @Return java.lang.String
     */
    public String getInput() {
        return input;
    }

    /**
     * @Method setInitInput
     * @Params [initInput]
     * @Return void
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * @Method getFrom
     * @Params []
     * @Return java.lang.String
    */
    public String getFrom() {
        return from;
    }

    /**
     * @Method setFrom
     * @Params [from]
     * @Return void
    */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @Method getTo
     * @Params []
     * @Return java.lang.String[]
     */
    public String[] getTo(){
        return to;
    }

    /**
     * @Method setTo
     * @Params [to]
     * @Return void
    */
    public void setTo(String[] to){
        this.to = to;
    }

    /**
     * @Method getdestAddress
     * @Params []
     * @Return java.lang.String
     */
    public String getdestAddress() {
        return destAddress;
    }

    /**
     * @Method setdestAddress
     * @Params [addr]
     * @Return void
     */
    public void setdestAddress(String addr) {
        this.destAddress = addr;
    }

}

