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
package cn.bif.exception;

import cn.bif.common.Tools;
import cn.bif.model.response.BIFBaseResponse;


public enum SdkError {
    /*
     * SUCCESS
     */
    SUCCESS(0, "Success"),

    /*
     * ACCOUNT_CREATE_ERROR
     */
    ACCOUNT_CREATE_ERROR(11001, "Failed to create the account"),
    /*
     * INVALID_AMOUNT_ERROR
     */
    INVALID_AMOUNT_ERROR(11024, "Amount must be between 0 and Long.MAX_VALUE"),
    /*
     * INVALID_SOURCEADDRESS_ERROR
     */
    INVALID_SOURCEADDRESS_ERROR(11002, "Invalid sourceAddress"),
    /*
     * INVALID_DESTADDRESS_ERROR
     */
    INVALID_DESTADDRESS_ERROR(11003, "Invalid destAddress"),
    /*
     * INVALID_INITBALANCE_ERROR
     */
    INVALID_INITBALANCE_ERROR(11004, "InitBalance must be between 1 and Long.MAX_VALUE"),
    /*
     * SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR
     */
    SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR(11005, "SourceAddress cannot be equal to destAddress"),
    /*
     * INVALID_ADDRESS_ERROR
     */
    INVALID_ADDRESS_ERROR(11006, "Invalid address"),
    /*
     * CONNECTNETWORK_ERROR
     */
    CONNECTNETWORK_ERROR(11007, "Failed to connect to the network"),

    /*
     * NO_METADATAS_ERROR
     */
    NO_METADATAS_ERROR(11010, "The account does not have this metadatas"),
    /*
     * INVALID_DATAKEY_ERROR
     */
    INVALID_DATAKEY_ERROR(11011, "The length of key must be between 1 and 1024"),
    /*
     * INVALID_DATAVALUE_ERROR
     */
    INVALID_DATAVALUE_ERROR(11012, "The length of value must be between 0 and 256000"),
    /*
     * INVALID_DATAVERSION_ERROR
     */
    INVALID_DATAVERSION_ERROR(11013, "The version must be equal to or greater than 0"),
    /*
     * INVALID_MASTERWEIGHT_ERROR
     */
    INVALID_MASTERWEIGHT_ERROR(11015, "MasterWeight must be between 0 and (Integer.MAX_VALUE * 2L + 1)"),
    /*
     * INVALID_SIGNER_ADDRESS_ERROR
     */
    INVALID_SIGNER_ADDRESS_ERROR(11016, "Invalid signer address"),
    /*
     * INVALID_SIGNER_WEIGHT_ERROR
     */
    INVALID_SIGNER_WEIGHT_ERROR(11017, "Signer weight must be between 0 and (Integer.MAX_VALUE * 2L + 1)"),
    /*
     * INVALID_TX_THRESHOLD_ERROR
     */
    INVALID_TX_THRESHOLD_ERROR(11018, "TxThreshold must be between 0 and Long.MAX_VALUE"),
    /*
     * INVALID_TYPETHRESHOLD_TYPE_ERROR
     */
    INVALID_TYPETHRESHOLD_TYPE_ERROR(11019, "Type of TypeThreshold is invalid"),
    /*
     * INVALID_TYPE_THRESHOLD_ERROR
     */
    INVALID_TYPE_THRESHOLD_ERROR(11020, "TypeThreshold must be between 0 and Long.MAX_VALUE"),

    /*
     * INVALID_CONTRACT_HASH_ERROR
     */
    INVALID_CONTRACT_HASH_ERROR(11025, "Invalid transaction hash to create contract"),
    /*
     * INVALID_GAS_AMOUNT_ERROR
     */
    INVALID_GAS_AMOUNT_ERROR(11026, "bifAmount must be between 0 and Long.MAX_VALUE"),

   /*
     * INVALID_CONTRACTADDRESS_ERROR
     */
    INVALID_CONTRACTADDRESS_ERROR(11037, "Invalid contract address"),
    /*
     * CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR
     */
    CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR(11038, "contractAddress is not a contract account"),

    /*
     * SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR
     */
    SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR(11040, "SourceAddress cannot be equal to contractAddress"),
    /*
     * INVALID_FROMADDRESS_ERROR
     */
    INVALID_FROMADDRESS_ERROR(11041, "Invalid fromAddress"),
    /*
     * FROMADDRESS_EQUAL_DESTADDRESS_ERROR
     */
    FROMADDRESS_EQUAL_DESTADDRESS_ERROR(11042, "FromAddress cannot be equal to destAddress"),
    /*
     * INVALID_SPENDER_ERROR
     */
    INVALID_SPENDER_ERROR(11043, "Invalid spender"),
    /*
     * PAYLOAD_EMPTY_ERROR
     */
    PAYLOAD_EMPTY_ERROR(11044, "Payload cannot be empty"),

    /*
     * INVALID_CONTRACT_TYPE_ERROR
     */
    INVALID_CONTRACT_TYPE_ERROR(11047, "Invalid contract type"),
    /*
     * INVALID_NONCE_ERROR
     */
    INVALID_NONCE_ERROR(11048, "Nonce must be between 1 and Long.MAX_VALUE"),
    /*
     * INVALID_GASPRICE_ERROR
     */
    INVALID_GASPRICE_ERROR(11049, "GasPrice must be between 0 and Long.MAX_VALUE"),
    /*
     * INVALID_FEELIMIT_ERROR
     */
    INVALID_FEELIMIT_ERROR(11050, "FeeLimit must be between 0 and Long.MAX_VALUE"),
    /*
     * OPERATIONS_EMPTY_ERROR
     */
    OPERATIONS_EMPTY_ERROR(11051, "Operations cannot be empty"),
    /*
     * INVALID_CEILLEDGERSEQ_ERROR
     */
    INVALID_CEILLEDGERSEQ_ERROR(11052, "CeilLedgerSeq must be equal to or greater than 0"),
    /*
     * OPERATIONS_ONE_ERROR
     */
    OPERATIONS_ONE_ERROR(11053, "One of the operations cannot be resolved"),
    /*
     * INVALID_SIGNATURENUMBER_ERROR
     */
    INVALID_SIGNATURENUMBER_ERROR(11054, "SignagureNumber must be between 1 and Integer.MAX_VALUE"),
    /*
     * INVALID_HASH_ERROR
     */
    INVALID_HASH_ERROR(11055, "Invalid transaction hash"),
    /*
     * INVALID_SERIALIZATION_ERROR
     */
    INVALID_SERIALIZATION_ERROR(11056, "Invalid serialization"),
    /*
     * PRIVATEKEY_NULL_ERROR
     */
    PRIVATEKEY_NULL_ERROR(11057, "PrivateKeys cannot be empty"),
    /*
     * PRIVATEKEY_ONE_ERROR
     */
    PRIVATEKEY_ONE_ERROR(11058, "One of privateKeys is invalid"),
    /*
     * SIGNDATA_NULL_ERROR
     */
    SIGNDATA_NULL_ERROR(11059, "SignData cannot be empty"),
    /*
     * INVALID_BLOCKNUMBER_ERROR
     */
    INVALID_BLOCKNUMBER_ERROR(11060, "BlockNumber must be bigger than 0"),
    /*
     * PUBLICKEY_NULL_ERROR
     */
    PUBLICKEY_NULL_ERROR(11061, "PublicKey cannot be empty"),
    /*
     * URL_EMPTY_ERROR
     */
    URL_EMPTY_ERROR(11062, "Url cannot be empty"),
    /*
     * CONTRACTADDRESS_CODE_BOTH_NULL_ERROR
     */
    CONTRACTADDRESS_CODE_BOTH_NULL_ERROR(11063, "ContractAddress and code cannot be empty at the same time"),
    /*
     * INVALID_OPTTYPE_ERROR
     */
    INVALID_OPTTYPE_ERROR(11064, "OptType must be between 0 and 2"),
    /*
     * GET_ALLOWANCE_ERROR
     */
    GET_ALLOWANCE_ERROR(11065, "Failed to get allowance"),

    /*
     * SIGNATURE_EMPTY_ERROR
     */
    SIGNATURE_EMPTY_ERROR(11067, "The signatures cannot be empty"),
    OPERATIONS_INVALID_ERROR(11068, "Operations length must be between 1 and 100"),
    /*
     * 操作类型为空
     */
    OPERATION_TYPE_ERROR(11077, "Operation type cannot be empty"),
    /*
     * CONNECTN_BLOCKCHAIN_ERROR
     */
    CONNECTN_BLOCKCHAIN_ERROR(19999, "Failed to connect blockchain"),
    /*
     * SYSTEM_ERROR
     */
    SYSTEM_ERROR(20000, "System error"),
    /*
     * REQUEST_NULL_ERROR
     */
    REQUEST_NULL_ERROR(12001, "Request parameter cannot be null"),
    /*
     * INVALID_CONTRACTBALANCE_ERROR
     */
    INVALID_CONTRACTBALANCE_ERROR(12002, "ContractBalance must be between 1 and Long.MAX_VALUE"),

    /*
     * INVALID_PRITX_FROM_ERROR
     */
    INVALID_PRITX_FROM_ERROR(12003, "Invalid Private Transaction Sender"),

    /*
     * INVALID_PRITX_PAYLAOD_ERROR
     */
    INVALID_PRITX_PAYLAOD_ERROR(12004, "Invalid Private Transaction payload"),

    /*
     * INVALID_PRITX_TO_ERROR
     */
    INVALID_PRITX_TO_ERROR(12005, "Invalid Private Transaction recipient list"),
    /*
     * INVALID_PROTOCOL_ERROR
     */
    INVALID_PROTOCOL_ERROR(12007, "Invalid protocol"),

    /*
     * INVALID_PRITX_HASH_ERROR
     */
    INVALID_PRITX_HASH_ERROR(12006, "Invalid Private Transaction Hash"),
    ;



    private final Integer code;
    private final String description;

    SdkError(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static void checkErrorCode(BIFBaseResponse baseResponse) throws SDKException {
        Integer errorCode = baseResponse.getErrorCode();
        if (Tools.isEmpty(errorCode)) {
            throw new SDKException(SdkError.CONNECTNETWORK_ERROR);
        }
        if (errorCode != 0) {
            String errorDesc = baseResponse.getErrorDesc();
            throw new SDKException(errorCode, (null == errorDesc ? "error" : errorDesc));
        }
    }

    public static void checkErrorCode(Integer errorCode, String errorDesc) throws SDKException {
        if (Tools.isEmpty(errorCode)) {
            throw new SDKException(SdkError.CONNECTNETWORK_ERROR);
        }
        if (errorCode != 0) {
            throw new SDKException(errorCode, (null == errorDesc ? "error" : errorDesc));
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
