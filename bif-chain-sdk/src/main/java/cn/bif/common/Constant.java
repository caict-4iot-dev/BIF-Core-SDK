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


public class Constant {

    public static final Long METADATA_KEY_MAX = 1024L;
    public static final Long METADATA_VALUE_MAX = 256000L;
    public static final Long UINT_MAX = Integer.MAX_VALUE * 2L + 1;
    public static final Integer HASH_HEX_LENGTH = 64;
    public static final Integer OPT_TYPE_MIN = 0;
    public static final Integer OPT_TYPE_MAX = 2;
    //交易默认值
    public static final Long GAS_PRICE = 100L;
    public static final Long FEE_LIMIT = 1000000L;
    //合约查询类型
    public static final Integer CONTRACT_QUERY_OPT_TYPE = 2;


    //账号参数
    public static final Long VERSION = 0L;
    public static final Integer SUCCESS = 0;
    public static final Integer ERRORCODE= 4;
    public static final Long INIT_NONCE = 0L;
    public static final Integer INIT_ZERO = 0;
    public static final Integer INIT_ONE = 1;

    public static final Long INIT_ZERO_L = 0L;
    public static final Long INIT_ONE_L = 1L;
}
