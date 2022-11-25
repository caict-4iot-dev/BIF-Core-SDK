# 1. BIF-Core-SDK使用说明

​		本节详细说明BIF-Core-SDK常用接口文档。星火链提供 JAVA SDK供开发者使用。

​        **github**代码库地址：https://github.com/CAICT-DEV/BIF-Core-SDK

## 1.1 SDK概述

### 1.1.1 名词解析

+ 账户服务： 提供账户相关的有效性校验、创建与查询接口

+ 合约服务： 提供合约相关的有效性校验、创建与查询接口

+ 交易服务： 提供构建交易及交易查询接口

+ 区块服务： 提供区块的查询接口

+ 账户nonce值： 每个账户都维护一个序列号，用于用户提交交易时标识交易执行顺序的

### 1.1.2 请求参数与相应数据格式

+ **请求参数**

​		接口的请求参数的类名，是\[服务名][方法名]Request，例如: 账户服务下的getAccount接口的请求参数格式是BIFAccountGetInfoRequest。

​		请求参数的成员，是各个接口的入参的成员。例如：账户服务下的getAccount接口的入参成员是address，那么该接口的请求参数的完整结构如下：

```java
Class BIFAccountGetInfoRequest {
	String address;
}
```

+ **响应数据**

​		接口的响应数据的类名，是\[服务名][方法名]Response，例如：账户服务下的getNonce接口的响应数据格式是BIFAccountGetNonceResponse。

​		响应数据的成员，包括错误码、错误描述和返回结果。响应数据的成员如下：

```java
Class BIFAccountGetNonceResponse {
	Integer errorCode;
	String errorDesc;
	BIFAccountGetNonceResult result;
}
```

1. errorCode: 错误码。错误码为0表示响应正常，其他错误码请查阅[错误码详情](# 4.7 错误码)。
2. errorDesc: 错误描述。
3. result: 返回结果。一个结构体，其类名是\[服务名][方法名]Result，其成员是各个接口返回值的成员，例如：账户服务下的getNonce接口的结果类名是BIFAccountGetNonceResult，成员有nonce, 完整结构如下：

```java
Class BIFAccountGetNonceResult {
	Long nonce;
}
```

## 1.2 SDK使用方法

​		本节介绍SDK的使用流程。

​		首先需要生成SDK实现，然后调用相应服务的接口，其中服务包括账户服务、合约服务、交易服务和区块服务。

### 1.2.1 生成SDK实例

​		调用SDK的接口getInstance来实现，调用如下：

```java
String url = "http://test-bif-core.xinghuo.space";
BIFSDK sdk = BIFSDK.getInstance(url);
```

### 1.2.2 生成公私钥地址

+ **Ed25519算法生成**

```java
KeyPairEntity keypair = KeyPairEntity.getBidAndKeyPair();
String encAddress = keypair.getEncAddress();
String encPublicKey = keypair.getEncPublicKey();
String encPrivateKey = keypair.getEncPrivateKey();
byte[] rawPublicKey = keypair.getRawPublicKey();
byte[] rawPrivateKey = keypair.getRawPrivateKey();
```

+ **SM2算法生成**

```java
KeyPairEntity keypair = KeyPairEntity.getBidAndKeyPairBySM2();
String encAddress = keypair.getEncAddress();
String encPublicKey = keypair.getEncPublicKey();
String encPrivateKey = keypair.getEncPrivateKey();
byte[] rawPublicKey = keypair.getRawPublicKey();
byte[] rawPrivateKey = keypair.getRawPrivateKey();
```

### 1.2.3 私钥对象使用

+ **构造对象**

```java
//签名方式构造 
PrivateKeyManager privateKey = new PrivateKeyManager(KeyType.ED25519);
//私钥构造
String encPrivateKey = "privbsDGan4sA9ZYpEERhMe25k4K5tnJu1fNqfEHbyKfaV9XSYq7uMcy";
PrivateKeyManager privateKey = new PrivateKeyManager(encPrivateKey);
```

+ **解析对象**

```java
//构造对象
PrivateKeyManager privateKey = new PrivateKeyManager(KeyType.ED25519);
//获取私钥
String encPrivateKey = privateKey.getEncPrivateKey();
//address
String encAddress= privateKey.getEncAddress();
//公钥
String encPublicKey=privateKey.getEncPrivateKey();
//获取原生私钥
byte[] rawPrivateKey=privateKey.getRawPrivateKey();
//获取原生公钥
byte[] rawPublicKey=privateKey.getRawPublicKey();
```

+ **根据私钥获取公钥**

```java
String encPrivateKey = "privbsDGan4sA9ZYpEERhMe25k4K5tnJu1fNqfEHbyKfaV9XSYq7uMcy";
String encPublicKey = PrivateKeyManager.getEncPublicKey(encPrivateKey);
```

+ **原生私钥转星火私钥**

```java
String encPrivateKey = PrivateKeyManager.getEncPrivateKey(rawPrivateKey, KeyType.ED25519);
```

+ **原生公钥转星火公钥**

```java
String encPublicKey = PrivateKeyManager.getEncPublicKey(rawPublicKey, KeyType.ED25519);
```

+ **签名**

```java
#调用此方法需要构造PrivateKeyManage对象
PrivateKeyManager privateKey = PrivateKeyManager(KeyType.ED25519);
String src = "test";
byte[] signMsg = privateKey.sign(HexFormat.hexStringToBytes(src));

#调用此方法不需要构造PrivateKeyManage对象
String src = "test";
String privateKey = "privbsDGan4sA9ZYpEERhMe25k4K5tnJu1fNqfEHbyKfaV9XSYq7uMcy";
byte[] sign = PrivateKeyManager.sign(HexFormat.hexStringToBytes(src), privateKey);
```

### 1.2.4 公钥对象使用

+ **构造对象**

```java
#公钥创建对象
String encPublicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
PublicKeyManager publicKey = new PublicKeyManager(encPublicKey);
```

+ **获取账号地址**

```java
#调用此方法需要构造PublicKeyManager对象
String publicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
PublicKeyManager publicKey = new PublicKeyManager(encPublicKey);
String encAddress = publicKey.getEncAddress();

#调用此方法不需要构造PublicKeyManager对象
String publicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
String encAddress = PublicKeyManager.getEncAddress(encPublicKey);
```

+ **账号地址校验**

```java
String address = "did:bid:efLrFZCn3wqSrozTG9MkxXbriRmwUHs5";
boolean isAddress = PublicKeyManager.isAddressValid(address);
```

+ **验签** 

```java
#调用此方法需要构造PublicKeyManager对象
String publicKey = "b07a6604f00d7a00da61f975048a40db1568a3befe3eb4e69fa2d14bf3e44833db58f4761c293efcd48a912b8ee2693b4f9ae0c9a4d03ffe4fb54bb7c2a5afd758df78dd";
PublicKeyManager publicKey = new PublicKeyManager(publicKey);
String src = "test";
#签名后信息
String sign = "59bda0c85e354ba4690b9bd8079a8e97dd18461c5d67128e46b693aef71d391ad965464c2db2c88610b3266899392703f11d047c696d17867985d0e057018450";
Boolean verifyResult = publicKey.verify(HexFormat.hexStringToBytes(src), HexFormat.hexToByte(sign));

#调用此方法不需要构造PublicKeyManager对象
String src = "test";
String publicKey = "b07a6604f00d7a00da61f975048a40db1568a3befe3eb4e69fa2d14bf3e44833db58f4761c293efcd48a912b8ee2693b4f9ae0c9a4d03ffe4fb54bb7c2a5afd758df78dd";
#签名后信息
String sign = "59bda0c85e354ba4690b9bd8079a8e97dd18461c5d67128e46b693aef71d391ad965464c2db2c88610b3266899392703f11d047c696d17867985d0e057018450";
Boolean verifyResult = PublicKeyManager.verify(HexFormat.hexStringToBytes(src), HexFormat.hexToByte(sign), publicKey);
```

### 1.2.5 密钥存储器

+ **生成密钥存储器**

```java
KeyStore.generateKeyStore(encPrivateKey, password, n, r, p,version)
```

>  请求参数

| 参数          | 类型    | 描述                     |
| ------------- | ------- | ------------------------ |
| encPrivateKey | String  | 待存储的密钥，可为null   |
| password      | String  | 口令                     |
| n             | Integer | CPU消耗参数，必填且大于1 |
| r             | Integer | 内存消息参数，必填       |
| p             | Integer | 并行化参数，必填         |
| version       | Integer | 版本号，必填             |

> 响应数据

| 参数     | 类型        | 描述             |
| -------- | ----------- | ---------------- |
| keyStore | KeyStoreEty | 存储密钥的存储器 |

> 示例

```java
//私钥
String encPrivateKey = "privbtGQELqNswoyqgnQ9tcfpkuH8P1Q6quvoybqZ9oTVwWhS6Z2hi1B";
//口令
String password = "test1234";
//版本
int version = (int) Math.pow(2, 16);
//方法一
String keyStore = KeyStore.generateKeyStore(password,encPrivateKey, 16384, 8, 1, version);
System.out.println(JsonUtils.toJSONString(keyStore));
//方法二
 KeyStoreEty keyStore1 = KeyStore.generateKeyStore(password, encPrivateKey, version);
System.out.println(JsonUtils.toJSONString(keyStore1));

```

+ **解析密钥存储器**

```
KeyStore.decipherKeyStore(keyStore, password)
```

>  请求参数

| 参数     | 类型   | 描述             |
| -------- | ------ | ---------------- |
| password | String | 口令             |
| keyStore | String | 存储密钥的存储器 |

> 响应数据

| 参数          | 类型   | 描述           |
| ------------- | ------ | -------------- |
| encPrivateKey | String | 解析出来的密钥 |

> 示例

```java
String password = "test1234";
String keyStore="{\"address\":\"did:bid:efqhQu9YWEWpUKQYkAyGevPGtAdD1N6p\",\"aesctr_iv\":\"5A2515820FBA1B6527A9C406E694B7AC\",\"cypher_text\":\"46DDF466290280AAFA889A78F35943C28E9CA262C86D01FBA075C243C029E772B5241FDDCBFEC21ADA48A8A8A1D55CE3F71F\",\"scrypt_params\":{\"n\":16384,\"p\":1,\"r\":8,\"salt\":\"B9370D6D7E22361870F5A1CB05658A9D2A9CA11BA8DBC8B582331DAAAF4F6C8D\"},\"version\":65536}";

String encPrivateKey =             KeyStore.decipherKeyStore(password,JsonUtils.toJavaObject(keyStoreStr,KeyStoreEty.class));
System.out.println(encPrivateKey);

```

### 1.2.6 助记词

+ **生成助记词**

> 请求参数

| 参数   | 类型   | 描述                     |
| ------ | ------ | ------------------------ |
| random | byte[] | 16位字节数组，必须是16位 |

> 响应数据

| 参数          | 类型         | 描述   |
| ------------- | ------------ | ------ |
| mnemonicCodes | List<String> | 助记词 |

> 示例

```java
byte[] aesIv = new byte[16];
SecureRandom randomIv = new SecureRandom();
randomIv.nextBytes(aesIv);

List<String> mnemonicCodes = Mnemonic.generateMnemonicCode(aesIv);
for (String mnemonicCode : mnemonicCodes) {
   System.out.print(mnemonicCode + " ");
}
System.out.println();
```

+ **根据助记词生成私钥**

> 请求参数

| 参数          | 类型         | 描述                      |
| ------------- | ------------ | ------------------------- |
| KeyType       | String       | 选填，加密类型ED25519/SM2 |
| mnemonicCodes | List<String> | 必填，助记词              |
| hdPaths       | List<String> | 必填，路径                |

> 响应数据

| 参数        | 类型         | 描述 |
| ----------- | ------------ | ---- |
| privateKeys | List<String> | 私钥 |

> 示例

```java
List<String> mnemonicCodes = new ArrayList<>();
mnemonicCodes.add("wood");
mnemonicCodes.add("floor");
mnemonicCodes.add("submit");
mnemonicCodes.add("traffic");
mnemonicCodes.add("obvious");
mnemonicCodes.add("indoor");
mnemonicCodes.add("rocket");
mnemonicCodes.add("lunch");
mnemonicCodes.add("melt");
mnemonicCodes.add("park");
mnemonicCodes.add("regular");
mnemonicCodes.add("vessel");

List<String> hdPaths = new ArrayList<>();
hdPaths.add("M/44/80/0/0/1");
//方式一
List<String> privateKeys = Mnemonic.generatePrivateKeys(mnemonicCodes, hdPaths);
        for (String privateKey : privateKeys) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.println(privateKey + " " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey)));
        }
//SM2
 List<String> privateKeysBySM2 = Mnemonic.generatePrivateKeysByCrypto(KeyType.SM2,mnemonicCodes, hdPaths);
        for (String privateKey : privateKeysBySM2) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.println("SM2 { privateKey : "+privateKey + " \n encAddress : " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey))+" \n }");
        }

//ED25519
 List<String> privateKeysByED25519 = Mnemonic.generatePrivateKeysByCrypto(KeyType.ED25519,mnemonicCodes, hdPaths);
        for (String privateKey : privateKeysByED25519) {
            if (!PrivateKeyManager.isPrivateKeyValid(privateKey)) {
                System.out.println("private is invalid");
                return;
            }
            System.out.println("ED25519  { privateKey : "+privateKey + " \n encAddress : " + PrivateKeyManager.getEncAddress(PrivateKeyManager.getEncPublicKey(privateKey))+" \n }");
        }
System.out.println();
```

## 1.3 账户服务接口列表

​		账户服务接口主要是账户相关的接口，目前有8个接口：

| 序号 | 接口                | 说明                                  |
| ---- | ------------------- | ------------------------------------- |
| 1    | createAccount       | 生成主链数字身份                      |
| 2    | getAccount          | 该接口用于获取指定的账户信息          |
| 3    | getNonce            | 该接口用于获取指定账户的nonce值       |
| 4    | getAccountBalance   | 该接口用于获取指定账户的星火令的余额  |
| 5    | setMetadatas        | 设置metadatas                         |
| 6    | getAccountMetadatas | 该接口用于获取指定账户的metadatas信息 |
| 7    | setPrivilege        | 设置权限                              |
| 8    | getAccountPriv      | 获取账户权限                          |

### 1.3.1 createAccount

> 接口说明

```
该接口用于生成主链数字身份。
```

> 调用方法

```java
BIFCreateAccountResponse createAccount(BIFCreateAccountRequest);
```

> 请求参数

| 参数          | 类型   | 描述                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| senderAddress | string | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long   | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String | 选填，用户自定义给交易的备注                                 |
| destAddress   | String | 必填，目标账户地址                                           |
| initBalance   | Long   | 必填，初始化星火令，单位PT，1 星火令 = 10^8 PT, 大小(0, Long.MAX_VALUE] |
| gasPrice      | Long   | 选填，打包费用 (单位是PT)，默认100L                          |
| feeLimit      | Long   | 选填，交易花费的手续费(单位是PT)，默认1000000L               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                      | 错误码 | 描述                                             |
| ------------------------- | ------ | ------------------------------------------------ |
| INVALID_ADDRESS_ERROR     | 11006  | Invalid address                                  |
| REQUEST_NULL_ERROR        | 12001  | Request parameter cannot be null                 |
| PRIVATEKEY_NULL_ERROR     | 11057  | PrivateKeys cannot be empty                      |
| INVALID_DESTADDRESS_ERROR | 11003  | Invalid destAddress                              |
| INVALID_INITBALANCE_ERROR | 11004  | InitBalance must be between 1 and Long.MAX_VALUE |
| SYSTEM_ERROR              | 20000  | System error                                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
String destAddress = "did:bid:efLnyv1Cw2aN3NJBepus55EahPcq24dH";
Long initBalance = ToBaseUnit.ToUGas("0.01");

BIFCreateAccountRequest request = new BIFCreateAccountRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setDestAddress(destAddress);
request.setInitBalance(initBalance);
request.setRemarks("init account");

// 调用 createAccount 接口
BIFCreateAccountResponse response = sdk.getBIFAccountService().createAccount(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

### 1.3.2 getAccount

> 接口说明

   	该接口用于获取指定的账户信息。

> 调用方法

```java
BIFAccountGetInfoResponse getAccount(BIFAccountGetInfoRequest);
```

> 请求参数

| 参数    | 类型   | 描述                         |
| ------- | ------ | ---------------------------- |
| address | String | 必填，待查询的区块链账户地址 |

> 响应数据

| 参数    | 类型   | 描述                                           |
| ------- | ------ | ---------------------------------------------- |
| address | String | 账户地址                                       |
| balance | Long   | 账户余额，单位PT，1 星火令 = 10^8 PT, 必须大于0 |
| nonce   | Long   | 账户交易序列号，必须大于0                      |

> 错误码

| 异常                  | 错误码 | 描述                             |
| --------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR    | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR  | 11007  | Failed to connect to the network |
| SYSTEM_ERROR          | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetInfoRequest request = new BIFAccountGetInfoRequest();
request.setAddress(accountAddress);

// 调用 getAccount 接口
BIFAccountGetInfoResponse response = sdk.getBIFAccountService().getAccount(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.3.3 getNonce

> 接口说明

   	该接口用于获取指定账户的nonce值。

> 调用方法

```java
BIFAccountGetNonceResponse getNonce(BIFAccountGetNonceRequest);
```

> 请求参数

| 参数    | 类型   | 描述                         |
| ------- | ------ | ---------------------------- |
| address | String | 必填，待查询的区块链账户地址 |

> 响应数据

| 参数  | 类型 | 描述           |
| ----- | ---- | -------------- |
| nonce | Long | 账户交易序列号 |

> 错误码

| 异常                  | 错误码 | 描述                             |
| --------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR    | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR  | 11007  | Failed to connect to the network |
| SYSTEM_ERROR          | 20000  | System error                     |

> 示例

```java
// 初始化请求参数     
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetNonceRequest request = new BIFAccountGetNonceRequest();
request.setAddress(accountAddress);

// 调用 getNonce 接口
BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(request);
if (0 == response.getErrorCode()) {
    System.out.println("Account nonce:" + response.getResult().getNonce());
}
```

### 1.3.4 getAccountBalance

> 接口说明

  	该接口用于获取指定账户的余额。

> 调用方法

```java
BIFAccountGetBalanceResponse getAccountBalance(BIFAccountGetBalanceRequest);
```

> 请求参数

| 参数    | 类型   | 描述                         |
| ------- | ------ | ---------------------------- |
| address | String | 必填，待查询的区块链账户地址 |

> 响应数据

| 参数    | 类型 | 描述 |
| ------- | ---- | ---- |
| balance | Long | 余额 |

> 错误码

| 异常                  | 错误码 | 描述                             |
| --------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR    | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR  | 11007  | Failed to connect to the network |
| SYSTEM_ERROR          | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetBalanceRequest request = new BIFAccountGetBalanceRequest();
request.setAddress(accountAddress);

// 调用 getAccountBalance 接口
BIFAccountGetBalanceResponse response = sdk.getBIFAccountService().getAccountBalance(request);
System.out.println(JsonUtils.toJSONString(response, true));
if (0 == response.getErrorCode()) {
    System.out.println("PT balance：" + ToBaseUnit.ToGas(response.getResult().getBalance().toString()) + "PT");
}
```

### 1.3.5 setMetadatas

> 接口说明

   	该接口用于修改账户的metadatas信息。

> 调用方法

```java
BIFAccountSetMetadatasResponse setMetadatas(BIFAccountSetMetadatasRequest);
```

> 请求参数

| 参数          | 类型    | 描述                                                         |
| ------------- | ------- | ------------------------------------------------------------ |
| senderAddress | string  | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String  | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long    | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String  | 选填，用户自定义给交易的备注                                 |
| key           | String  | 必填，metadatas的关键词，长度限制[1, 1024]                   |
| value         | String  | 必填，metadatas的内容，长度限制[0, 256000]                   |
| version       | Long    | 选填，metadatas的版本                                        |
| deleteFlag    | Boolean | 选填，是否删除remarks                                        |
| gasPrice      | Long    | 选填，打包费用 (单位是PT)，默认100L                          |
| feeLimit      | Long    | 选填，交易花费的手续费(单位是PT)，默认1000000L               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                    | 错误码 | 描述                                             |
| ----------------------- | ------ | ------------------------------------------------ |
| INVALID_ADDRESS_ERROR   | 11006  | Invalid address                                  |
| REQUEST_NULL_ERROR      | 12001  | Request parameter cannot be null                 |
| PRIVATEKEY_NULL_ERROR   | 11057  | PrivateKeys cannot be empty                      |
| INVALID_DATAKEY_ERROR   | 11011  | The length of key must be between 1 and 1024     |
| INVALID_DATAVALUE_ERROR | 11012  | The length of value must be between 0 and 256000 |
| SYSTEM_ERROR            | 20000  | System error                                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
String key = "20210902-01";
String value = "metadata-20210902-01";

BIFAccountSetMetadatasRequest request = new BIFAccountSetMetadatasRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setKey(key);
request.setValue(value);
request.setRemarks("set metadata");

// 调用 setMetadatas 接口
BIFAccountSetMetadatasResponse response = sdk.getBIFAccountService().setMetadatas(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

### 1.3.6 getAccountMetadatas

> 接口说明

   	该接口用于获取指定账户的metadatas信息。

> 调用方法

```java
BIFAccountGetMetadatasResponse getAccountMetadatas(BIFAccountGetMetadatasRequest);
```

> 请求参数

| 参数    | 类型   | 描述                                                         |
| ------- | ------ | ------------------------------------------------------------ |
| address | String | 必填，待查询的账户地址                                       |
| key     | String | 选填，metadatas关键字，长度限制[1, 1024]，有值为精确查找，无值为全部查找 |

> 响应数据

| 参数                 | 类型     | 描述              |
| -------------------- | -------- | ----------------- |
| metadatas            | object[] | 账户              |
| metadatas[i].key     | String   | metadatas的关键词 |
| metadatas[i].value   | String   | metadatas的内容   |
| metadatas[i].version | Long     | metadatas的版本   |


> 错误码

| 异常                  | 错误码 | 描述                                         |
| --------------------- | ------ | -------------------------------------------- |
| INVALID_ADDRESS_ERROR | 11006  | Invalid address                              |
| REQUEST_NULL_ERROR    | 12001  | Request parameter cannot be null             |
| CONNECTNETWORK_ERROR  | 11007  | Failed to connect to the network             |
| NO_METADATAS_ERROR    | 11010  | The account does not have the metadatas      |
| INVALID_DATAKEY_ERROR | 11011  | The length of key must be between 1 and 1024 |
| SYSTEM_ERROR          | 20000  | System error                                 |


> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetMetadatasRequest request = new BIFAccountGetMetadatasRequest();
request.setAddress(accountAddress);
request.setKey("20210820-01");

// 调用getAccountMetadatas接口
BIFAccountGetMetadatasResponse response =
sdk.getBIFAccountService().getAccountMetadatas(request);
if (response.getErrorCode() == 0) {
    BIFAccountGetMetadatasResult result = response.getResult();
    System.out.println(JsonUtils.toJSONString(result, true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

### 1.3.7 setPrivilege

> 接口说明

   	该接口用于设置权限。

> 调用方法

```java
BIFAccountSetPrivilegeResponse setPrivilege(BIFAccountSetPrivilegeRequest);
```

> 请求参数

| 参数                    | 类型   | 描述                                                         |
| ----------------------- | ------ | ------------------------------------------------------------ |
| senderAddress           | string | 必填，交易源账号，即交易的发起方                             |
| privateKey              | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq           | Long   | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks                 | String | 选填，用户自定义给交易的备注                                 |
| signers                 | list   | 选填，签名者权重列表                                         |
| signers.address         | String | 签名者区块链账户地址                                         |
| signers.weight          | Long   | 为签名者设置权重值                                           |
| txThreshold             | String | 选填，交易门限，大小限制[0, Long.MAX_VALUE]                  |
| typeThreshold           | list   | 选填，指定类型交易门限                                       |
| typeThreshold.type      | Long   | 操作类型，必须大于0                                          |
| typeThreshold.threshold | Long   | 门限值，大小限制[0, Long.MAX_VALUE]                          |
| masterWeight            | String | 选填                                                         |
| gasPrice                | Long   | 选填，打包费用 (单位是PT)，默认100L                          |
| feeLimit                | Long   | 选填，交易花费的手续费(单位是PT)，默认1000000L               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                  | 错误码 | 描述                             |
| --------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR    | 12001  | Request parameter cannot be null |
| PRIVATEKEY_NULL_ERROR | 11057  | PrivateKeys cannot be empty      |
| SYSTEM_ERROR          | 20000  | System error                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
String masterWeight = null;
BIFSigner[] signers = null;
String txThreshold = null;
BIFTypeThreshold[] typeThresholds = null;

BIFAccountSetPrivilegeRequest request = new BIFAccountSetPrivilegeRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setSigners(signers);
request.setTxThreshold(txThreshold);
request.setMasterWeight(masterWeight);
request.setTypeThresholds(typeThresholds);
request.setRemarks("set privilege");

// 调用 setPrivilege 接口
BIFAccountSetPrivilegeResponse response = sdk.getBIFAccountService().setPrivilege(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

### 1.3.8 getAccountPriv

> 接口说明

   	该接口用于获取指定的账户权限信息。

> 调用方法

```java
BIFAccountPrivResponse getAccountPriv(BIFAccountPrivRequest);
```

> 请求参数

| 参数    | 类型   | 描述                         |
| ------- | ------ | ---------------------------- |
| address | String | 必填，待查询的区块链账户地址 |

> 响应数据

| 参数                                        | 类型     | 描述                   |
| ------------------------------------------- | -------- | ---------------------- |
| address                                     | String   | 账户地址               |
| priv                                        | Object   | 账户权限               |
| Priv.masterWeight                           | Object   | 账户自身权重，大小限制 |
| Priv.signers                                | Object   | 签名者权重列表         |
| Priv.signers[i].address                     | String   | 签名者区块链账户地址   |
| Priv.signers[i].weight                      | Long     | 签名者权重，大小限制   |
| Priv.Thresholds                             | Object   |                        |
| Priv.Thresholds.txThreshold                 | Long     | 交易默认门限，大小限制 |
| Priv.Thresholds.typeThresholds              | Object[] | 不同类型交易的门限     |
| Priv.Thresholds.typeThresholds[i].type      | Long     | 操作类型，必须大于0    |
| Priv.Thresholds.typeThresholds[i].threshold | Long     | 门限值，大小限制       |

> 错误码

| 异常                  | 错误码 | 描述                             |
| --------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR    | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR  | 11007  | Failed to connect to the network |
| SYSTEM_ERROR          | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountPrivRequest request = new BIFAccountPrivRequest();
request.setAddress(accountAddress);

// 调用getAccountPriv接口
BIFAccountPrivResponse response = sdk.getBIFAccountService().getAccountPriv(request);
if (response.getErrorCode() == 0) {
    BIFAccountPrivResult result = response.getResult();
    System.out.println(JsonUtils.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

## 1.4 合约服务接口列表

​		合约服务接口主要是合约相关的接口，目前有6个接口：

| 序号 | 接口                 | 说明                               |
| ---- | -------------------- | ---------------------------------- |
| 1    | checkContractAddress | 该接口用于检测合约账户的有效性     |
| 2    | contractCreate       | 创建合约                           |
| 3    | getContractInfo      | 该接口用于查询合约代码             |
| 4    | getContractAddress   | 该接口用于根据交易Hash查询合约地址 |
| 5    | contractQuery        | 该接口用于调试合约代码             |
| 6    | contractInvoke       | 合约调用                           |

### 1.4.1 checkContractAddress

> 接口说明

   	该接口用于检测合约账户的有效性。

> 调用方法

```java
BIFContractCheckValidResponse checkContractAddress(BIFContractCheckValidRequest);
```

> 请求参数

| 参数            | 类型   | 描述                 |
| --------------- | ------ | -------------------- |
| contractAddress | String | 待检测的合约账户地址 |

> 响应数据

| 参数    | 类型    | 描述     |
| ------- | ------- | -------- |
| isValid | Boolean | 是否有效 |

> 错误码

| 异常                          | 错误码 | 描述                             |
| ----------------------------- | ------ | -------------------------------- |
| INVALID_CONTRACTADDRESS_ERROR | 11037  | Invalid contract address         |
| REQUEST_NULL_ERROR            | 12001  | Request parameter cannot be null |
| SYSTEM_ERROR                  | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
BIFContractCheckValidRequest request = new BIFContractCheckValidRequest();
request.setContractAddress("did:bid:efiBacNvVSnr5QxgB282XGWkg4RXLLxL");

// 调用 checkContractAddress 接口
BIFContractCheckValidResponse response = sdk.getBIFContractService().checkContractAddress(request);
if (response.getErrorCode() == 0) {
    BIFContractCheckValidResult result = response.getResult();
    System.out.println(result.getValid());
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.4.2 contractCreate

> 接口说明

   	该接口用于创建合约。

> 调用方法

```java
BIFContractCreateResponse contractCreate(BIFContractCreateRequest);
```

> 请求参数

| 参数          | 类型    | 描述                                                         |
| ------------- | ------- | ------------------------------------------------------------ |
| senderAddress | string  | 必填，交易源账号，即交易的发起方                             |
| gasPrice      | Long    | 选填，打包费用 (单位是PT)默认，默认100L                      |
| feeLimit      | Long    | 选填，交易花费的手续费(单位是PT)，默认1000000L               |
| privateKey    | String  | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long    | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String  | 选填，用户自定义给交易的备注                                 |
| initBalance   | Long    | 选填，给合约账户的初始化星火令，单位PT，1 星火令 = 10^8 PT, 大小限制[1, Long.MAX_VALUE] |
| type          | Integer | 选填，合约的类型，默认是0 , 0: javascript，1 :evm 。         |
| payload       | String  | 必填，对应语种的合约代码                                     |
| initInput     | String  | 选填，合约代码中init方法的入参                               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                      | 错误码 | 描述                                             |
| ------------------------- | ------ | ------------------------------------------------ |
| INVALID_ADDRESS_ERROR     | 11006  | Invalid address                                  |
| REQUEST_NULL_ERROR        | 12001  | Request parameter cannot be null                 |
| PRIVATEKEY_NULL_ERROR     | 11057  | PrivateKeys cannot be empty                      |
| INVALID_INITBALANCE_ERROR | 11004  | InitBalance must be between 1 and Long.MAX_VALUE |
| PAYLOAD_EMPTY_ERROR       | 11044  | Payload cannot be empty                          |
| INVALID_FEELIMIT_ERROR    | 11050  | FeeLimit must be between 0 and Long.MAX_VALUE    |
| SYSTEM_ERROR              | 20000  | System error                                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
String senderPrivateKey = "priSPKqvAwSG3cp63GAuWfXASGXUSokYeA5nNkuWxKeBF54yEC";
String payload = "\"use strict\";function init(bar){/*init whatever you want*/return;}function main(input){let para = JSON.parse(input);if (para.do_foo)\n    {\n      let x = {\n\'hello\' : \'world\'\n      };\n    }\n  }\n  \n  function query(input)\n  { \n    return input;\n  }\n";
Long initBalance = ToBaseUnit.ToUGas("0.01");

BIFContractCreateRequest request = new BIFContractCreateRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setInitBalance(initBalance);
request.setPayload(payload);
request.setRemarks("create contract");
request.setType(0);
request.setFeeLimit(1000000000L);

// 调用 contractCreate 接口
BIFContractCreateResponse response = sdk.getBIFContractService().contractCreate(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

### 1.4.3 getContractInfo

> 接口说明

   	该接口用于查询合约代码。

> 调用方法

```java
BIFContractGetInfoResponse getContractInfo(BIFContractGetInfoRequest);
```

> 请求参数

| 参数            | 类型   | 描述                 |
| --------------- | ------ | -------------------- |
| contractAddress | String | 待查询的合约账户地址 |

> 响应数据

| 参数                 | 类型    | 描述            |
| -------------------- | ------- | --------------- |
| contract             | object  | 合约信息        |
| contractInfo.type    | Integer | 合约类型，默认0 |
| contractInfo.payload | String  | 合约代码        |

> 错误码

| 异常                                      | 错误码 | 描述                                      |
| ----------------------------------------- | ------ | ----------------------------------------- |
| INVALID_CONTRACTADDRESS_ERROR             | 11037  | Invalid contract address                  |
| CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR | 11038  | contractAddress is not a contract account |
| NO_SUCH_TOKEN_ERROR                       | 11030  | No such token                             |
| GET_TOKEN_INFO_ERROR                      | 11066  | Failed to get token info                  |
| REQUEST_NULL_ERROR                        | 12001  | Request parameter cannot be null          |
| SYSTEM_ERROR                              | 20000  | System error                              |

> 示例

```java
// 初始化请求参数
BIFContractGetInfoRequest request = new BIFContractGetInfoRequest();
request.setContractAddress("did:bid:efiBacNvVSnr5QxgB282XGWkg4RXLLxL");

// 调用 getContractInfo 接口
BIFContractGetInfoResponse response = sdk.getBIFContractService().getContractInfo(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.4.4 getContractAddress

> 接口说明

```
该接口用于根据交易Hash查询合约地址。
```

> 调用方法

```java
BIFContractGetAddressResponse getContractAddress(BIFContractGetAddressRequest);
```

> 请求参数

| 参数 | 类型   | 描述               |
| ---- | ------ | ------------------ |
| hash | String | 创建合约交易的hash |

> 响应数据

| 参数                                                        | 类型                      | 描述           |
| ----------------------------------------------------------- | ------------------------- | -------------- |
| contractAddressInfos                                        | List<ContractAddressInfo> | 合约地址列表   |
| contractAddressInfos[i].ContractAddressInfo                 | object                    | 成员           |
| contractAddressInfos[i].ContractAddressInfo.contractAddress | String                    | 合约地址       |
| contractAddressInfos[i].ContractAddressInfo.operationIndex  | Integer                   | 所在操作的下标 |

> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| INVALID_HASH_ERROR   | 11055  | Invalid transaction hash         |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| REQUEST_NULL_ERROR   | 12001  | Request parameter cannot be null |
| SYSTEM_ERROR         | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
String hash = "4bb232fbe86e33b956ad5338103d4610b2b31d5bf6af742d7e55b9c6182abfee";
BIFContractGetAddressRequest request = new BIFContractGetAddressRequest();
request.setHash(hash);

// 调用 getContractAddress 接口
BIFContractGetAddressResponse response = sdk.getBIFContractService().getContractAddress(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.4.5 contractQuery

> 接口说明

   	该接口用于调用合约查询接口。

> 调用方法

```java
BIFContractCallResponse contractQuery(BIFContractCallRequest);
```

> 请求参数

| 参数            | 类型   | 描述                                           |
| --------------- | ------ | ---------------------------------------------- |
| sourceAddress   | String | 选填，合约触发账户地址                         |
| contractAddress | String | 必填，合约账户地址                             |
| input           | String | 选填，合约入参                                 |
| gasPrice        | Long   | 选填，打包费用 (单位是PT)默认，默认100L        |
| feeLimit        | Long   | 选填，交易花费的手续费(单位是PT)，默认1000000L |


> 响应数据

| 参数      | 类型      | 描述       |
| --------- | --------- | ---------- |
| queryRets | JSONArray | 查询结果集 |

> 错误码

| 异常                                      | 错误码 | 描述                                             |
| ----------------------------------------- | ------ | ------------------------------------------------ |
| INVALID_SOURCEADDRESS_ERROR               | 11002  | Invalid sourceAddress                            |
| INVALID_CONTRACTADDRESS_ERROR             | 11037  | Invalid contract address                         |
| SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR | 11040  | SourceAddress cannot be equal to contractAddress |
| REQUEST_NULL_ERROR                        | 12001  | Request parameter cannot be null                 |
| CONNECTNETWORK_ERROR                      | 11007  | Failed to connect to the network                 |
| SYSTEM_ERROR                              | 20000  | System error                                     |

> 示例

```java
// 初始化请求参数
String contractAddress = "did:bid:ef2gAT82SGdnhj87wQWb9suPKLbnk9NP";
BIFContractCallRequest request = new BIFContractCallRequest();
request.setContractAddress(contractAddress);

// 调用 contractQuery 接口
BIFContractCallResponse response = sdk.getBIFContractService().contractQuery(request);
if (response.getErrorCode() == 0) {
    BIFContractCallResult result = response.getResult();
    System.out.println(JsonUtils.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.4.6 contractInvoke

> 接口说明

   	该接口用于合约调用。

> 调用方法

```java
BIFContractInvokeResponse contractInvoke(BIFContractInvokeRequest);
```

> 请求参数

| 参数            | 类型                           | 描述                                                         |
| --------------- | ------------------------------ | ------------------------------------------------------------ |
| senderAddress   | string                         | 必填，交易源账号，即交易的发起方                             |
| gasPrice        | Long                           | 选填，打包费用 (单位是PT)默认，默认100L                      |
| feeLimit        | Long                           | 选填，交易花费的手续费(单位是PT)，默认1000000L               |
| privateKey      | String                         | 必填，交易源账户私钥                                         |
| ceilLedgerSeq   | Long                           | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks         | String                         | 选填，用户自定义给交易的备注                                 |
| contractAddress | String                         | 必填，合约账户地址                                           |
| BIFAmount       | Long                           | 必填，转账金额                                               |
| input           | 选填，待触发的合约的main()入参 |                                                              |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                          | 错误码 | 描述                                          |
| ----------------------------- | ------ | --------------------------------------------- |
| INVALID_ADDRESS_ERROR         | 11006  | Invalid address                               |
| REQUEST_NULL_ERROR            | 12001  | Request parameter cannot be null              |
| PRIVATEKEY_NULL_ERROR         | 11057  | PrivateKeys cannot be empty                   |
| INVALID_CONTRACTADDRESS_ERROR | 11037  | Invalid contract address                      |
| INVALID_AMOUNT_ERROR          | 11024  | Amount must be between 0 and Long.MAX_VALUE   |
| INVALID_FEELIMIT_ERROR        | 11050  | FeeLimit must be between 0 and Long.MAX_VALUE |
| SYSTEM_ERROR                  | 20000  | System error                                  |


> 示例

```java
// 初始化参数
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String contractAddress = "did:bid:eftzENB3YsWymQnvsLyF4T2ENzjgEg41";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";
        Long amount = 0L;
        String destAddress = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        String input = "{\"method\":\"creation\",\"params\":{\"document\":{\"@context\": [\"https://w3.org/ns/did/v1\"],\"context\": \"https://w3id.org/did/v1\"," +
                "\"id\": \""+destAddress+"\", \"version\": \"1\"}}}";
        BIFContractInvokeRequest request = new BIFContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setContractAddress(contractAddress);
        request.setBIFAmount(amount);
        request.setRemarks("contract invoke");
        request.setInput(input);
        // 调用 bifContractInvoke 接口
        BIFContractInvokeResponse response = sdk.getBIFContractService().contractInvoke(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
```

### 1.4.7 batchContractInvoke

> 接口说明

   	该接口用于批量合约调用。

> 调用方法

```java
BIFContractInvokeResponse batchContractInvoke(BIFBatchContractInvokeRequest);
```

> 请求参数

| 参数          | 类型                             | 描述                                                         |
| ------------- | -------------------------------- | ------------------------------------------------------------ |
| senderAddress | string                           | 必填，交易源账号，即交易的发起方                             |
| gasPrice      | Long                             | 选填，打包费用 (单位是PT)默认，默认100L                      |
| feeLimit      | Long                             | 选填，交易花费的手续费(单位是PT)，默认1000000L               |
| privateKey    | String                           | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long                             | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String                           | 选填，用户自定义给交易的备注                                 |
| operations    | List<BIFContractInvokeOperation> | 必填，合约调用集合                                           |

| BIFContractInvokeOperation |        |                                |
| -------------------------- | ------ | ------------------------------ |
| contractAddress            | String | 必填，合约账户地址             |
| BIFAmount                  | Long   | 必填，转账金额                 |
| input                      | String | 选填，待触发的合约的main()入参 |



> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                          | 错误码 | 描述                                          |
| ----------------------------- | ------ | --------------------------------------------- |
| INVALID_ADDRESS_ERROR         | 11006  | Invalid address                               |
| REQUEST_NULL_ERROR            | 12001  | Request parameter cannot be null              |
| PRIVATEKEY_NULL_ERROR         | 11057  | PrivateKeys cannot be empty                   |
| INVALID_CONTRACTADDRESS_ERROR | 11037  | Invalid contract address                      |
| INVALID_AMOUNT_ERROR          | 11024  | Amount must be between 0 and Long.MAX_VALUE   |
| INVALID_FEELIMIT_ERROR        | 11050  | FeeLimit must be between 0 and Long.MAX_VALUE |
| SYSTEM_ERROR                  | 20000  | System error                                  |


> 示例

```java
// 初始化参数
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String contractAddress = "did:bid:eftzENB3YsWymQnvsLyF4T2ENzjgEg41";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";
        Long amount = 0L;
        String destAddress1 = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        String destAddress2 = KeyPairEntity.getBidAndKeyPair().getEncAddress();
        String input1 = "{\"method\":\"creation\",\"params\":{\"document\":{\"@context\": [\"https://w3.org/ns/did/v1\"],\"context\": \"https://w3id.org/did/v1\"," +
                "\"id\": \""+destAddress1+"\", \"version\": \"1\"}}}";
        String input2 = "{\"method\":\"creation\",\"params\":{\"document\":{\"@context\": [\"https://w3.org/ns/did/v1\"],\"context\": \"https://w3id.org/did/v1\"," +
                "\"id\": \""+destAddress2+"\", \"version\": \"1\"}}}";

        List<BIFContractInvokeOperation> operations = new ArrayList<BIFContractInvokeOperation>();
        //操作对象1
        BIFContractInvokeOperation operation1=new BIFContractInvokeOperation();
        operation1.setContractAddress(contractAddress);
        operation1.setBIFAmount(amount);
        operation1.setInput(input1);
        //操作对象2
        BIFContractInvokeOperation operation2=new BIFContractInvokeOperation();
        operation2.setContractAddress(contractAddress);
        operation2.setBIFAmount(amount);
        operation2.setInput(input2);

        operations.add(operation1);
        operations.add(operation2);

        BIFBatchContractInvokeRequest request = new BIFBatchContractInvokeRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setOperations(operations);
        request.setRemarks("contract invoke");

        // 调用 bifContractInvoke 接口
        BIFContractInvokeResponse response = sdk.getBIFContractService().batchContractInvoke(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
```

## 1.5 交易服务接口列表

​		交易服务接口主要是交易相关的接口，目前有6个接口：

| 序号 | 接口                  | 说明                               |
| ---- | --------------------- | ---------------------------------- |
| 1    | gasSend               | 交易                               |
| 2    | privateContractCreate | 私有化交易-合约创建                |
| 3    | privateContractCall   | 私有化交易-合约调用                |
| 4    | getTransactionInfo    | 该接口用于实现根据交易hash查询交易 |
| 5    | evaluateFee           | 该接口实现交易的费用评估           |
| 6    | BIFSubmit             | 提交交易                           |

### 1.5.1 gasSend

> 接口说明

   	该接口用于发起交易。

> 调用方法

```java
BIFTransactionGasSendResponse gasSend(BIFTransactionGasSendRequest);
```

> 请求参数

| 参数          | 类型   | 描述                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| senderAddress | string | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long   | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String | 选填，用户自定义给交易的备注                                 |
| destAddress   | String | 必填，发起方地址                                             |
| amount        | Long   | 必填，转账金额                                               |
| gasPrice      | Long   | 选填，打包费用 (单位是PT)，默认100L                          |
| feeLimit      | Long   | 选填，交易花费的手续费(单位是PT)，默认1000000L               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                      | 错误码 | 描述                                           |
| ------------------------- | ------ | ---------------------------------------------- |
| INVALID_ADDRESS_ERROR     | 11006  | Invalid address                                |
| REQUEST_NULL_ERROR        | 12001  | Request parameter cannot be null               |
| PRIVATEKEY_NULL_ERROR     | 11057  | PrivateKeys cannot be empty                    |
| INVALID_DESTADDRESS_ERROR | 11003  | Invalid destAddress                            |
| INVALID_GAS_AMOUNT_ERROR  | 11026  | BIFAmount must be between 0 and Long.MAX_VALUE |
| SYSTEM_ERROR              | 20000  | System error                                   |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
String destAddress = "did:bid:efrAgXe6NvmNwWmtdBs1iKGThzBqHNwH";
Long amount = ToBaseUnit.ToUGas("1");

BIFTransactionGasSendRequest request = new BIFTransactionGasSendRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setDestAddress(destAddress);
request.setAmount(amount);
request.setRemarks("PT send");

// 调用 gasSend 接口
BIFTransactionGasSendResponse response = sdk.getBIFTransactionService().gasSend(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

### 1.5.2 privateContractCreate

> 接口说明

```
该接口用于私有化交易的合约创建。
```

> 调用方法

```java
BIFTransactionPrivateContractCreateResponse privateContractCreate(BIFTransactionPrivateContractCreateRequest);
```

> 请求参数

| 参数          | 类型     | 描述                                                         |
| ------------- | -------- | ------------------------------------------------------------ |
| senderAddress | string   | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String   | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long     | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String   | 选填，用户自定义给交易的备注                                 |
| type          | Integer  | 选填，合约的语种                                             |
| payload       | String   | 必填，对应语种的合约代码                                     |
| from          | String   | 必填，发起方加密机公钥                                       |
| to            | String[] | 必填，接收方加密机公钥                                       |
| gasPrice      | Long     | 选填，打包费用 (单位是PT)默认，默认100L                      |
| feeLimit      | Long     | 选填，交易花费的手续费(单位是PT)，默认1000000L               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                        | 错误码 | 描述                             |
| --------------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR       | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR          | 12001  | Request parameter cannot be null |
| PRIVATEKEY_NULL_ERROR       | 11057  | PrivateKeys cannot be empty      |
| INVALID_CONTRACT_TYPE_ERROR | 11047  | Invalid contract type            |
| PAYLOAD_EMPTY_ERROR         | 11044  | Payload cannot be empty          |
| SYSTEM_ERROR                | 20000  | System error                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
String senderPrivateKey = "priSPKqvAwSG3cp63GAuWfXASGXUSokYeA5nNkuWxKeBF54yEC";
String payload = "\"use strict\";function queryBanance(address)\r\n{return \" test query private contract sdk_3\";}\r\nfunction sendTx(to,amount)\r\n{return Chain.payCoin(to,amount);}\r\nfunction init(input)\r\n{return;}\r\nfunction main(input)\r\n{let args=JSON.parse(input);if(args.method===\"sendTx\"){return sendTx(args.params.address,args.params.amount);}}\r\nfunction query(input)\r\n{let args=JSON.parse(input);if(args.method===\"queryBanance\"){return queryBanance(args.params.address);}}";
String from = "bDRE8iIfGdwDeQOcJqZabZQH5Nd6cfTOMOorudtgXjQ=";
String[] to = {"0VEtPRytTaDEf0g62KyAVeEHnwfd6ZM59/YQXfngaRs="};

BIFTransactionPrivateContractCreateRequest request = new BIFTransactionPrivateContractCreateRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setPayload(payload);
request.setFrom(from);
request.setTo(to);
request.setRemarks("init account");

// 调用 privateContractCreate 接口
BIFTransactionPrivateContractCreateResponse response = sdk.getBIFTransactionService().privateContractCreate(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + JsonUtils.toJSONString(response));
    return;
}

Thread.sleep(5000);
BIFTransactionGetInfoRequest transactionRequest = new BIFTransactionGetInfoRequest();
transactionRequest.setHash(response.getResult().getHash());
// 调用getTransactionInfo接口
BIFTransactionGetInfoResponse transactionResponse = sdk.getBIFTransactionService().getTransactionInfo(transactionRequest);
if (transactionResponse.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(transactionResponse.getResult(), true));
} else {
    System.out.println("error: " + transactionResponse.getErrorDesc());
}
```

### 1.5.3 privateContractCall

> 接口说明

   	该接口用于私有化交易的合约调用。

> 调用方法

```java
BIFTransactionPrivateContractCallResponse privateContractCall(BIFTransactionPrivateContractCallRequest);
```

> 请求参数

| 参数          | 类型     | 描述                                                         |
| ------------- | -------- | ------------------------------------------------------------ |
| senderAddress | string   | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String   | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long     | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String   | 选填，用户自定义给交易的备注                                 |
| destAddress   | String   | 必填，发起方地址                                             |
| type          | Integer  | 选填，合约的语种（待用）                                     |
| input         | String   | 必填，待触发的合约的main()入参                               |
| from          | String   | 必填，发起方加密机公钥                                       |
| to            | String[] | 必填，接收方加密机公钥                                       |
| gasPrice      | Long     | 选填，打包费用 (单位是PT)默认，默认100L                      |
| feeLimit      | Long     | 选填，交易花费的手续费(单位是PT)，默认1000000L               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                        | 错误码 | 描述                             |
| --------------------------- | ------ | -------------------------------- |
| INVALID_ADDRESS_ERROR       | 11006  | Invalid address                  |
| REQUEST_NULL_ERROR          | 12001  | Request parameter cannot be null |
| PRIVATEKEY_NULL_ERROR       | 11057  | PrivateKeys cannot be empty      |
| INVALID_CONTRACT_TYPE_ERROR | 11047  | Invalid contract type            |
| SYSTEM_ERROR                | 20000  | System error                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
String senderPrivateKey = "priSPKqvAwSG3cp63GAuWfXASGXUSokYeA5nNkuWxKeBF54yEC";
String input = "{\"method\":\"queryBanance\",\"params\":{\"address\":\"567890哈哈=======\"}}";
String from = "bDRE8iIfGdwDeQOcJqZabZQH5Nd6cfTOMOorudtgXjQ=";
String[] to = {"0VEtPRytTaDEf0g62KyAVeEHnwfd6ZM59/YQXfngaRs="};
//设置调用的私有合约地址
String destAddress = "did:bid:efTuswkPE1HP9Uc7vpNbRVokuQqhxaCE";

BIFTransactionPrivateContractCallRequest request = new BIFTransactionPrivateContractCallRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setInput(input);
request.setFrom(from);
request.setTo(to);
request.setDestAddress(destAddress);
request.setRemarks("private Contract Call");

// 调用 privateContractCall 接口
BIFTransactionPrivateContractCallResponse response = sdk.getBIFTransactionService().privateContractCall(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
    return;
}

Thread.sleep(5000);
BIFTransactionGetInfoRequest transactionRequest = new BIFTransactionGetInfoRequest();
transactionRequest.setHash(response.getResult().getHash());
// 调用getTransactionInfo接口
BIFTransactionGetInfoResponse transactionResponse = sdk.getBIFTransactionService().getTransactionInfo(transactionRequest);
if (transactionResponse.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(transactionResponse.getResult(), true));
} else {
    System.out.println("error: " + transactionResponse.getErrorDesc());
}
```

### 1.5.4 getTransactionInfo

> 接口说明

   	该接口用于实现根据交易hash查询交易。

> 调用方法

```java
BIFTransactionGetInfoResponse getTransactionInfo(BIFTransactionGetInfoRequest);
```

> 请求参数

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | String | 交易hash |

> 响应数据

| 参数                              | 类型               | 描述           |
| --------------------------------- | ------------------ | -------------- |
| totalCount                        | Long               | 返回的总交易数 |
| transactions                      | TransactionHistory | 交易内容       |
| transactions.fee                  | String             | 交易实际费用   |
| transactions.confirmTime          | Long               | 交易确认时间   |
| transactions.errorCode            | Long               | 交易错误码     |
| transactions.errorDesc            | String             | 交易描述       |
| transactions.hash                 | String             | 交易hash       |
| transactions.ledgerSeq            | Long               | 区块序列号     |
| transactions.transaction          | TransactionInfo    | 交易内容列表   |
| transactions.signatures           | Signature          | 签名列表       |
| transactions.signatures.signData  | Long               | 签名后数据     |
| transactions.signatures.publicKey | Long               | 公钥           |
| transactions.txSize               | Long               | 交易大小       |

> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| INVALID_HASH_ERROR   | 11055  | Invalid transaction hash         |
| REQUEST_NULL_ERROR   | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| SYSTEM_ERROR         | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
BIFTransactionGetInfoRequest request = new BIFTransactionGetInfoRequest();
request.setHash("6fd10128e0f1e3f6565542303ca308d26f70c7638ec3885141c5cdb72583d182");

// 调用 getTransactionInfo 接口
BIFTransactionGetInfoResponse response = sdk.getBIFTransactionService().getTransactionInfo(request);
if (response.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.5.5 evaluateFee

> 接口说明

   	该接口实现交易的费用评估。

> 调用方法

```java
BIFTransactionGetInfoResponse evaluateFee(BIFTransactionEvaluateFeeRequest);
```

> 请求参数

| 参数            | 类型                            | 描述                                                         |
| --------------- | ------------------------------- | ------------------------------------------------------------ |
| signatureNumber | Integer                         | 选填，待签名者的数量，默认是1，大小限制[1, Integer.MAX_VALUE] |
| remarks         | String                          | 选填，用户自定义给交易的备注                                 |
| operation       | [BaseOperation](#BaseOperation) | 必填，待提交的操作，不能为空                                 |
| gasPrice        | Long                            | 选填，打包费用 (单位是PT)                                    |
| feeLimit        | Long                            | 选填，交易花费的手续费(单位是PT)                             |

#### BaseOperation

| 序号 | 操作                              | 描述                         |
| ---- | --------------------------------- | ---------------------------- |
| 1    | BIFAccountActivateOperation       | 生成主链数字身份             |
| 2    | BIFAccountSetMetadataOperation    | 修改账户的metadatas信息      |
| 3    | BIFAccountSetPrivilegeOperation   | 设置权限                     |
| 4    | BIFContractCreateOperation        | 创建合约（暂不支持EVM 合约） |
| 5    | BIFContractInvokeOperation        | 合约调用（暂不支持EVM 合约） |
| 6    | BIFGasSendOperation               | 发起交易                     |
| 7    | BIFPrivateContractCallOperation   | 私有化交易的合约创建         |
| 8    | BIFPrivateContractCreateOperation | 私有化交易的合约调用         |

> 响应数据

| 参数 | 类型                | 描述       |
| ---- | ------------------- | ---------- |
| txs  | [TestTx](#TestTx)[] | 评估交易集 |

#### TestTx

| 成员变量       | 类型                                        | 描述         |
| -------------- | ------------------------------------------- | ------------ |
| transactionEnv | [TestTransactionFees](#TestTransactionFees) | 评估交易费用 |

#### TestTransactionFees

| 成员变量        | 类型                                | 描述     |
| --------------- | ----------------------------------- | -------- |
| transactionFees | [TransactionFees](#TransactionFees) | 交易费用 |

#### TransactionFees

| 成员变量 | 类型 | 描述               |
| -------- | ---- | ------------------ |
| feeLimit | Long | 交易要求的最低费用 |
| gasPrice | Long | 交易燃料单价       |

> 错误码

| 异常                          | 错误码 | 描述                                                    |
| ----------------------------- | ------ | ------------------------------------------------------- |
| INVALID_SOURCEADDRESS_ERROR   | 11002  | Invalid sourceAddress                                   |
| OPERATIONS_EMPTY_ERROR        | 11051  | Operations cannot be empty                              |
| OPERATIONS_ONE_ERROR          | 11053  | One of the operations cannot be resolved                |
| INVALID_SIGNATURENUMBER_ERROR | 11054  | SignagureNumber must be between 1 and Integer.MAX_VALUE |
| REQUEST_NULL_ERROR            | 12001  | Request parameter cannot be null                        |
| SYSTEM_ERROR                  | 20000  | System error                                            |

> 示例

```java
       // 初始化变量
        String senderAddresss = "did:bid:efAsXt5zM2Hsq6wCYRMZBS5Q9HvG2EmK";
        String destAddress = "did:bid:ef14uPsX7XYLzsU4t2rnRrsK2zfUbW3r";
        Long bifAmount = ToBaseUnit.ToUGas("10.9");

        // 构建sendGas操作
        BIFGasSendOperation gasSendOperation = new BIFGasSendOperation();
        gasSendOperation.setSourceAddress(senderAddresss);
        gasSendOperation.setDestAddress(destAddress);
        gasSendOperation.setAmount(bifAmount);

        // 初始化评估交易请求参数
        BIFTransactionEvaluateFeeRequest request = new BIFTransactionEvaluateFeeRequest();
        request.setOperation(gasSendOperation);
        request.setSourceAddress(senderAddresss);
        request.setSignatureNumber(1);
        request.setRemarks(HexFormat.byteToHex("evaluate fees".getBytes()));

       // 调用evaluateFee接口
        BIFTransactionEvaluateFeeResponse response = sdk.getBIFTransactionService().evaluateFee(request);
        if (response.getErrorCode() == 0) {
            BIFTransactionEvaluateFeeResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
```



### 1.5.6 BIFSubmit

> 接口说明

   	该接口用于交易提交。

> 调用方法

```java
BIFTransactionSubmitResponse BIFSubmit(BIFTransactionSubmitRequest);
```

> 请求参数

| 参数          | 类型   | 描述               |
| ------------- | ------ | ------------------ |
| serialization | String | 必填，交易序列化值 |
| signData      | String | 必填，签名数据     |
| privateKey    | String | 必填，签名者私钥   |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | String | 交易hash |

> 错误码

| 异常                        | 错误码 | 描述                             |
| --------------------------- | ------ | -------------------------------- |
| INVALID_SERIALIZATION_ERROR | 11056  | Invalid serialization            |
| SIGNATURE_EMPTY_ERROR       | 11067  | The signatures cannot be empty   |
| SIGNDATA_NULL_ERROR         | 11059  | SignData cannot be empty         |
| PUBLICKEY_NULL_ERROR        | 11061  | PublicKey cannot be empty        |
| REQUEST_NULL_ERROR          | 12001  | Request parameter cannot be null |
| SYSTEM_ERROR                | 20000  | System error                     |

> 示例

```Java
  // 初始化参数
  String senderPrivateKey = "priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";
  //序列化交易
  String serialization ="";
  //签名
  byte[] signBytes = PrivateKeyManager.sign(HexFormat.hexToByte(serialization), senderPrivateKey);
  String publicKey = PrivateKeyManager.getEncPublicKey(senderPrivateKey);
  //提交交易
  BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
     submitRequest.setSerialization(serialization);
     submitRequest.setPublicKey(publicKey);
     submitRequest.setSignData(HexFormat.byteToHex(signBytes));
  // 调用bifSubmit接口
  BIFTransactionSubmitResponse response = sdk.getBIFTransactionService().BIFSubmit(submitRequest);
     if (response.getErrorCode() == 0) {
          System.out.println(JsonUtils.toJSONString(response.getResult()));
      } else {
          System.out.println("error: " + response.getErrorDesc());
      }

```

### 1.5.7 getTxCacheSize

> 接口说明

   	该接口用于获取交易池中交易条数。

> 调用方法

```java
BIFTransactionGetTxCacheSizeResponse getTxCacheSize();
```

> 响应数据

| 参数       | 类型 | 描述                 |
| ---------- | ---- | -------------------- |
| queue_size | Long | 返回交易池中交易条数 |

> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| SYSTEM_ERROR         | 20000  | System error                     |

> 示例

```java
     BIFTransactionGetTxCacheSizeResponse response = sdk.getBIFTransactionService().getTxCacheSize();
     if (response.getErrorCode() == 0) {
            System.out.println("txCacheSize: "+JsonUtils.toJSONString(response.getQueueSize()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
```

### 1.5.8 getTxCacheData

> 接口说明

   	该接口用于获取交易池中交易数据。

> 调用方法

```java
BIFTransactionCacheResponse getTxCacheData(BIFTransactionCacheRequest);
```

> 请求参数

| 参数 | 类型   | 描述           |
| ---- | ------ | -------------- |
| hash | String | 选填，交易hash |

> 响应数据

| 参数                           | 类型     | 描述                 |
| ------------------------------ | -------- | -------------------- |
| transactions                   | Object[] | 返回交易池中交易数据 |
| transactionsp[i].hash          | String   | 交易hash             |
| transactionsp[i].incoming_time | String   | 进入时间             |
| transactionsp[i].status        | String   | 状态                 |
| transactionsp[i].transaction   | Object   |                      |

> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| SYSTEM_ERROR         | 20000  | System error                     |
| INVALID_HASH_ERROR   | 11055  | Invalid transaction hash         |

> 示例

```java
    //请求参数  
	BIFTransactionCacheRequest cacheRequest=new BIFTransactionCacheRequest();
        cacheRequest.setHash("8f3d53f0dfb5ae652d6ed93ca9512f57c2203fe0ffefdc7649908945ad96a730");

    BIFTransactionCacheResponse response = sdk.getBIFTransactionService().getTxCacheData(cacheRequest);
    if (response.getErrorCode() == 0) {
            System.out.println("txCacheData: "+JsonUtils.toJSONString(response.getResult().getTransactions()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
```

### 1.5.9 parseBlob

> 接口说明

   	该接口用于blob数据解析。

> 调用方法

```java
 BIFTransactionParseBlobResponse parseBlob(String blob);
```

> 请求参数

| 参数 | 类型   | 描述       |
| ---- | ------ | ---------- |
| blob | String | 必填，BLOB |

> 响应数据

| 参数          | 类型     | 描述                       |
| ------------- | -------- | -------------------------- |
| sourceAddress | String   | 交易源账号，即交易的发起方 |
| nonce         | String   | 账户交易序列号，必须大于0  |
| fee_limit     | String   | 交易要求的最低费用         |
| gas_price     | String   | 交易燃料单价               |
| remarks       | String   | 用户自定义给交易的备注     |
| operations    | Object[] | 操作对象数组               |

> 错误码

| 异常                        | 错误码 | 描述                             |
| --------------------------- | ------ | -------------------------------- |
| CONNECTNETWORK_ERROR        | 11007  | Failed to connect to the network |
| SYSTEM_ERROR                | 20000  | System error                     |
| INVALID_SERIALIZATION_ERROR | 11056  | Invalid serialization            |

> 示例

```java
      String transactionBlobResult = "0A276469643A6269643A324E4A4C46343931536431553434323270476B50715467686946664B3337751003225C080712276469643A6269643A324E4A4C46343931536431553434323270476B50715467686946664B333775522F0A276469643A6269643A32695277744E53666841753739754A73624C6B78694333374A554C437235791080A9E0870430C0843D38E807";
        // Parsing the transaction Blob
        BIFTransactionParseBlobResponse transaction = sdk.getBIFTransactionService().parseBlob(transactionBlobResult);
        if(transaction.getErrorCode()==0){
            System.out.println("transaction content: " + JsonUtils.toJSONString(transaction.getResult()));
        }else {
            System.out.println(JsonUtils.toJSONString(transaction));
        }
```

### 1.5.10 batchGasSend

> 接口说明

   	该接口用于批量转移星火令。

> 调用方法

```java
BIFTransactionGasSendResponse batchGasSend(BIFBatchGasSendRequest);
```

> 请求参数

| 参数          | 类型                      | 描述                                                         |
| ------------- | ------------------------- | ------------------------------------------------------------ |
| senderAddress | string                    | 必填，交易源账号，即交易的发起方                             |
| gasPrice      | Long                      | 选填，打包费用 (单位是PT)默认，默认100L                      |
| feeLimit      | Long                      | 选填，交易花费的手续费(单位是PT)，默认1000000L               |
| privateKey    | String                    | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long                      | 选填，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| remarks       | String                    | 选填，用户自定义给交易的备注                                 |
| operations    | List<BIFGasSendOperation> | 必填，转账操作集合                                           |

| BIFGasSendOperation |        |                    |
| ------------------- | ------ | ------------------ |
| destAddress         | String | 必填，目标账户地址 |
| amount              | Long   | 必填，转账金额     |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                     | 错误码 | 描述                                          |
| ------------------------ | ------ | --------------------------------------------- |
| INVALID_ADDRESS_ERROR    | 11006  | Invalid address                               |
| REQUEST_NULL_ERROR       | 12001  | Request parameter cannot be null              |
| PRIVATEKEY_NULL_ERROR    | 11057  | PrivateKeys cannot be empty                   |
| OPERATIONS_INVALID_ERROR | 11068  | Operations length must be between 1 and 100   |
| INVALID_AMOUNT_ERROR     | 11024  | Amount must be between 0 and Long.MAX_VALUE   |
| INVALID_FEELIMIT_ERROR   | 11050  | FeeLimit must be between 0 and Long.MAX_VALUE |
| SYSTEM_ERROR             | 20000  | System error                                  |


> 示例

```java
 		// 初始化参数
        String senderAddress = "did:bid:ef7zyvBtyg22NC4qDHwehMJxeqw6Mmrh";
        String senderPrivateKey = "priSPKr2dgZTCNj1mGkDYyhyZbCQhEzjQm7aEAnfVaqGmXsW2x";
        String destAddress1 = KeyPairEntity.getBidAndKeyPairBySM2().getEncAddress();
        String destAddress2 = KeyPairEntity.getBidAndKeyPairBySM2().getEncAddress();
        Long bifAmount1 = ToBaseUnit.ToUGas("1");
        Long bifAmount2 = ToBaseUnit.ToUGas("1");

        List<BIFGasSendOperation> operations = new ArrayList<BIFGasSendOperation>();
        BIFGasSendOperation operation1 = new BIFGasSendOperation();
        operation1.setDestAddress(destAddress1);
        operation1.setAmount(bifAmount1);


        BIFGasSendOperation operation2 = new BIFGasSendOperation();
        operation2.setDestAddress(destAddress2);
        operation2.setAmount(bifAmount2);

        operations.add(operation1);
        operations.add(operation2);

        // 初始化请求参数
        BIFBatchGasSendRequest request = new BIFBatchGasSendRequest();
        request.setSenderAddress(senderAddress);
        request.setPrivateKey(senderPrivateKey);
        request.setOperations(operations);
        request.setRemarks(HexFormat.byteToHex("batch gas send".getBytes()));
        request.setGasPrice(1L);
        request.setFeeLimit(500L);

        // 调用batchGasSend接口
        BIFTransactionGasSendResponse response = sdk.getBIFTransactionService().batchGasSend(request);
        if (response.getErrorCode() == 0) {
            BIFTransactionGasSendResult result = response.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }
```

##  

## 1.6 区块服务接口列表 

​		区块服务接口主要是区块相关的接口，目前有6个接口：

| 序号 | 接口                | 说明                                    |
| ---- | ------------------- | --------------------------------------- |
| 1    | getBlockNumber      | 该接口用于查询最新的区块高度            |
| 2    | getTransactions     | 该接口用于查询指定区块高度下的所有交易3 |
| 3    | getBlockInfo        | 该接口用于获取区块信息                  |
| 4    | getBlockLatestInfo  | 该接口用于获取最新区块信息              |
| 5    | getValidators       | 该接口用于获取指定区块中所有验证节点数  |
| 6    | getLatestValidators | 该接口用于获取最新区块中所有验证节点数  |

### 1.6.1 getBlockNumber

> 接口说明

   	该接口用于查询最新的区块高度。

> 调用方法

```java
BIFBlockGetNumberResponse getBlockNumber();
```

> 响应数据

| 参数               | 类型        | 描述                            |
| ------------------ | ----------- | ------------------------------- |
| header             | BlockHeader | 区块头                          |
| header.blockNumber | Long        | 最新的区块高度，对应底层字段seq |

> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| SYSTEM_ERROR         | 20000  | System error                     |

> 示例

```java
// 调用getBlockNumber接口
BIFBlockGetNumberResponse response = sdk.getBIFBlockService().getBlockNumber();
if(0 == response.getErrorCode()){
   System.out.println(JsonUtils.toJSONString(response.getResult(), true));
}else{
   System.out.println("error: " + response.getErrorDesc());
}
```

### 1.6.2 getTransactions

> 接口说明

   	该接口用于查询指定区块高度下的所有交易。

> 调用方法

```java
BIFBlockGetTransactionsResponse getTransactions(BIFBlockGetTransactionsRequest);
```

> 请求参数

| 参数        | 类型 | 描述                                  |
| ----------- | ---- | ------------------------------------- |
| blockNumber | Long | 必填，最新的区块高度，对应底层字段seq |

> 响应数据

| 参数         | 类型                    | 描述           |
| ------------ | ----------------------- | -------------- |
| totalCount   | Long                    | 返回的总交易数 |
| transactions | BIFTransactionHistory[] | 交易内容       |

> 错误码

| 异常                      | 错误码 | 描述                             |
| ------------------------- | ------ | -------------------------------- |
| INVALID_BLOCKNUMBER_ERROR | 11060  | BlockNumber must bigger than 0   |
| REQUEST_NULL_ERROR        | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR      | 11007  | Failed to connect to the network |
| SYSTEM_ERROR              | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
Long blockNumber = 1L;
BIFBlockGetTransactionsRequest request = new BIFBlockGetTransactionsRequest();
request.setBlockNumber(blockNumber);
// 调用 getTransactions 接口
BIFBlockGetTransactionsResponse response = sdk.getBIFBlockService().getTransactions(request);
if (0 == response.getErrorCode()) {
    System.out.println(JsonUtils.toJSONString(response, true));
} else {
    System.out.println("失败\n" + JsonUtils.toJSONString(response, true));
}
```

### 1.6.3 getBlockInfo

> 接口说明

   	该接口用于获取指定区块信息。

> 调用方法

```java
BIFBlockGetInfoResponse getBlockInfo(BIFBlockGetInfoRequest);
```

> 请求参数

| 参数        | 类型 | 描述                   |
| ----------- | ---- | ---------------------- |
| blockNumber | Long | 必填，待查询的区块高度 |

> 响应数据

| 参数                        | 类型           | 描述              |
| --------------------------- | -------------- | ----------------- |
| header                      | BIFBlockHeader | 区块信息          |
| header.close_time           | Long           | 区块确认时间      |
| header.seq                  | Long           | 区块高度          |
| header.tx_count             | Long           | 交易总量          |
| header.version              | String         | 区块版本          |
| header.account_tree_hash    | String         | 账户树哈希        |
| header.consensus_value_hash | String         | 共识信息哈希      |
| header.hash                 | String         | 区块哈希          |
| header.previous_hash        | String         | 上一区块哈希      |
| header.fees_hash            | String         | 费用hash          |
| header.validators_hash      | String         | 共识列表hash      |
| ledger_length               | Long           | 区块头大小(bytes) |

> 错误码

| 异常                      | 错误码 | 描述                             |
| ------------------------- | ------ | -------------------------------- |
| INVALID_BLOCKNUMBER_ERROR | 11060  | BlockNumber must bigger than 0   |
| REQUEST_NULL_ERROR        | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR      | 11007  | Failed to connect to the network |
| SYSTEM_ERROR              | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
BIFBlockGetInfoRequest blockGetInfoRequest = new BIFBlockGetInfoRequest();
blockGetInfoRequest.setBlockNumber(10L);
// 调用 getBlockInfo 接口
BIFBlockGetInfoResponse lockGetInfoResponse = sdk.getBIFBlockService().getBlockInfo(blockGetInfoRequest);
if (lockGetInfoResponse.getErrorCode() == 0) {
    BIFBlockGetInfoResult lockGetInfoResult = lockGetInfoResponse.getResult();
    System.out.println(JsonUtils.toJSONString(lockGetInfoResult, true));
} else {
    System.out.println("error: " + lockGetInfoResponse.getErrorDesc());
}
```

### 1.6.4 getBlockLatestInfo

> 接口说明

```
该接口用于获取最新区块信息。
```

> 调用方法

```java
BIFBlockGetLatestInfoResponse getBlockLatestInfo();
```

> 响应数据
>

| 参数                        | 类型           | 描述              |
| --------------------------- | -------------- | ----------------- |
| header                      | BIFBlockHeader | 区块信息          |
| header.close_time           | Long           | 区块确认时间      |
| header.seq                  | Long           | 区块高度          |
| header.tx_count             | Long           | 交易总量          |
| header.version              | String         | 区块版本          |
| header.account_tree_hash    | String         | 账户树哈希        |
| header.consensus_value_hash | String         | 共识信息哈希      |
| header.hash                 | String         | 区块哈希          |
| header.previous_hash        | String         | 上一区块哈希      |
| header.fees_hash            | String         | 费用hash          |
| header.validators_hash      | String         | 共识列表hash      |
| ledger_length               | Long           | 区块头大小(bytes) |


> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| SYSTEM_ERROR         | 20000  | System error                     |

> 示例

```java
// 调用 getBlockLatestInfo 接口
BIFBlockGetLatestInfoResponse lockGetLatestInfoResponse = sdk.getBIFBlockService().getBlockLatestInfo();
if (lockGetLatestInfoResponse.getErrorCode() == 0) {
    BIFBlockGetLatestInfoResult lockGetLatestInfoResult = lockGetLatestInfoResponse.getResult();
    System.out.println(JsonUtils.toJSONString(lockGetLatestInfoResult, true));
} else {
    System.out.println(lockGetLatestInfoResponse.getErrorDesc());
}
```

### 1.6.5 getValidators

> 接口说明

   	该接口用于获取指定区块中所有验证节点数。

> 调用方法

```java
BIFBlockGetValidatorsResponse getValidators(BIFBlockGetValidatorsRequest);
```

> 请求参数

| 参数        | 类型 | 描述                              |
| ----------- | ---- | --------------------------------- |
| blockNumber | Long | 必填，待查询的区块高度，必须大于0 |

> 响应数据

| 参数               | 类型     | 描述         |
| ------------------ | -------- | ------------ |
| validators         | String[] | 验证节点列表 |
| validators.address | String   | 共识节点地址 |

> 错误码

| 异常                      | 错误码 | 描述                             |
| ------------------------- | ------ | -------------------------------- |
| INVALID_BLOCKNUMBER_ERROR | 11060  | BlockNumber must bigger than 0   |
| REQUEST_NULL_ERROR        | 12001  | Request parameter cannot be null |
| CONNECTNETWORK_ERROR      | 11007  | Failed to connect to the network |
| SYSTEM_ERROR              | 20000  | System error                     |

> 示例

```java
// 初始化请求参数
BIFBlockGetValidatorsRequest request = new BIFBlockGetValidatorsRequest();
request.setBlockNumber(1L);

// 调用 getValidators 接口
BIFBlockGetValidatorsResponse response = sdk.getBIFBlockService().getValidators(request);
if (response.getErrorCode() == 0) {
    BIFBlockGetValidatorsResult result = response.getResult();
    System.out.println(JsonUtils.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

### 1.6.6 getLatestValidators

> 接口说明

   	该接口用于获取最新区块中所有验证节点数。

> 调用方法

```java
BIFBlockGetLatestValidatorsResponse getLatestValidators();
```

> 响应数据

| 参数               | 类型     | 描述         |
| ------------------ | -------- | ------------ |
| validators         | String[] | 验证节点列表 |
| validators.address | String   | 共识节点地址 |

> 错误码

| 异常                 | 错误码 | 描述                             |
| -------------------- | ------ | -------------------------------- |
| CONNECTNETWORK_ERROR | 11007  | Failed to connect to the network |
| SYSTEM_ERROR         | 20000  | System error                     |

> 示例

```java
// 调用 getLatestValidators 接口
BIFBlockGetLatestValidatorsResponse response = sdk.getBIFBlockService().getLatestValidators();
if (response.getErrorCode() == 0) {
    BIFBlockGetLatestValidatorsResult result = response.getResult();
    System.out.println(JsonUtils.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

## 1.7 SDK智能合约开发流程

本节给出一个基于Java SDK的完整智能合约开发流程。

**一定要灵活使用星火区块链浏览器 http://test-explorer.bitfactory.cn/, 账户，交易，合约hash都可以在上面搜索查询。**

### 1.7.1 概述

做合约开发，一般需要以下几个步骤：

1. 创建一个账号，并且获得XHT，才能发起后续交易
2. 编写合约，建议基于javascript编写
3. 编译和部署合约
4. 调用和读取合约

### 1.7.2 账号创建

通过调用getBidAndKeyPair()就可以离线创建一个随机地址。

```java
import cn.bif.model.crypto.KeyPairEntity;

entity = KeyPairEntity.getBidAndKeyPair();
System.out.printf("public BID %s\n", entity.getEncAddress());
System.out.printf("private key %s\n", entity.getEncPrivateKey());
```
建议将得到的地址和对应私钥都稳妥保存，之后就用这个地址开始后续的开发，私钥一定不能泄露。

### 1.7.3 初始化星火链SDK

**之后的操作都需要链网进行，需要初始化星火链SDK链接到星火链。**

```java
import cn.bif.api.BIFSDK;

public static final String NODE_URL = "http://test-bif-core.xinghuo.space";

public staitc BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
```

sdk初始化之后，我们可以通过sdk对象调用链上方法进行开发。

### 1.7.4 查看账户状态

1. 首先我们记录了最开始生成的账户地址和私钥

```java
public static final String publicKey = "did:bid:efKkF5uKsopAishxkYja4ULRJhrhrJQU";
public static final String privateKey = "priSPKqB8wCf8GtiKCG1yN3RHPVLbfcXLmkFfHLGjSgrMRD7AJ";
```

2. 通过星火SDK查看账户状态

```java
BIFAccountGetInfoRequest infoReq = new BIFAccountGetInfoRequest();
infoReq.setAddress(publicKey);

BIFAccountGetInfoResponse infoRsp = sdk.getBIFAccountService().getAccount(infoReq);

//current no info about this account
if (infoRsp.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(infoRsp.getResult()));
} else {
    System.out.println(infoRsp.getErrorDesc());
}
```
这里会报错，因为该账户还没有过任何操作，所以星火链上没有记录。

所有链上操作都需要耗费星火令(XHT)，因此您需要从其他地方获取XHT到这个账户供操作。

获取星火令之后再查看账户状态，得到正确返回如下：

```json
{"address":"did:bid:efKkF5uKsopAishxkYja4ULRJhrhrJQU","balance":10000000000,"nonce":0}
```

### 1.7.5 合约开发

做一个完整的链上合约开发主要包括以下几个部分：

1. 合约编写

合约具体编写可以参考[开发手册](https://bif-core-dev-doc.readthedocs.io/zh_CN/latest/)。这里直接列出写好的javascript智能合约。

```javascript
"use strict";

function queryById(id) {
    let data = Chain.load(id);
    return data;
}

function query(input) {
    input = JSON.parse(input);
    let id = input.id;
    let object = queryById(id);
    return object;
}

function main(input) {
    input = JSON.parse(input);
    Chain.store(input.id, input.data);
}

function init(input) {
    return;
}
```

该合约做的事情比较简单，就是实现了基于key的存储和读取。

2. 合约部署

写完合约后，需要将合约部署到链上(注意需要消耗XHT，确保账号有足够XHT)。示例代码如下：

```java
//部署合约

//合约代码，注意转义
String contractCode = "\"use strict\";function queryById(id) {    let data = Chain.load(id);    return data;}function query(input) {    input = JSON.parse(input);    let id = input.id;    let object = queryById(id);    return object;}function main(input) {    input = JSON.parse(input);    Chain.store(input.id, input.data);}function init(input) {    return;}";

BIFContractCreateRequest createCReq = new BIFContractCreateRequest();

//创建方地址和私钥
createCReq.setSenderAddress(publicKey);
createCReq.setPrivateKey(privateKey);

//合约初始balance，一般为0
createCReq.setInitBalance(0L);

//合约代码
createCReq.setPayload(contractCode);

//标记和type，javascript合约type为0
createCReq.setRemarks("create contract");
createCReq.setType(0);

//交易耗费上限
createCReq.setFeeLimit(300000000L);

BIFContractCreateResponse createCRsp = sdk.getBIFContractService().contractCreate(createCReq);
if (createCRsp.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(createCRsp.getResult()));
} else {
    System.out.println(createCRsp.getErrorDesc());
}
```

如果部署成功，返回里会拿到这个交易的HASH。

```json
{"hash":"b25567a482e674d79ac5f9b5f6601f27b676dde90a6a56539053ec882a99854f"}
```

这里我们记录下这个交易HASH，然后查询生成的合约地址。

3. 合约地址查询

基于刚刚得到的交易HASH查询生成的合约地址:

```java
BIFContractGetAddressRequest cAddrReq = new BIFContractGetAddressRequest();
cAddrReq.setHash(cTxHash);

BIFContractGetAddressResponse cAddrRsp = sdk.getBIFContractService().getContractAddress(cAddrReq);
if (cAddrRsp.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(cAddrRsp.getResult()));
} else {
    System.out.println(cAddrRsp.getErrorDesc());
}
```

收到返回如下: 

```json
{"contract_address_infos":[{"contract_address":"did:bid:efSvDJivc2A4iqurRkUPzmpT5kB3nkNg","operation_index":0}]}
```

生成的合约地址即为: did:bid:efSvDJivc2A4iqurRkUPzmpT5kB3nkNg.

3. 合约调用

有了合约地址，我们就可以开始调用合约，这里我们set一个key value对到刚刚合约里，对照我们刚刚javascript合约的main函数，调用的input为:

```json
{"id":"test", "data": "test"}
```

也就是在key "test"下写入 "test"值。

合约调用的java代码如下:
```java

//转义后input
String input = "{\"id\":\"test\", \"data\": \"test\"}";

BIFContractInvokeRequest cIvkReq = new BIFContractInvokeRequest();

//调用者地址和私钥
cIvkReq.setSenderAddress(publicKey);
cIvkReq.setPrivateKey(privateKey);

//合约地址
cIvkReq.setContractAddress(cAddr);

//调用交易XHT金额
cIvkReq.setBIFAmount(0L);

//标记
cIvkReq.setRemarks("contract invoke");

//调用input
cIvkReq.setInput(input);

BIFContractInvokeResponse cIvkRsp = sdk.getBIFContractService().contractInvoke(cIvkReq);
if (cIvkRsp.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(cIvkRsp.getResult()));
} else {
    System.out.println(cIvkRsp.getErrorDesc());
}
```

调用成功后，我们又会得到调用交易的HASH：

```json
{"hash":"c79835265e908f7f06d4fc2c61ef3fd046ae5252675e4671271bd921ad8fde89"}
```

4. 合约读取

调用成功后，我们还需要读取链上数据，根据我们的javascript合约，读取的input为
```json
{"id":"test"}
```
表示我们需要读取id "test"下的内容，使用javaSDK的读取代码如下:

```java
BIFContractCallRequest cCallReq = new BIFContractCallRequest();

String callInput = "{\"id\":\"test\"}";
cCallReq.setContractAddress(cAddr);
cCallReq.setInput(callInput);

BIFContractCallResponse cCallRsp = sdk.getBIFContractService().contractQuery(cCallReq);

if (cCallRsp.getErrorCode() == 0) {
    System.out.println(JsonUtils.toJSONString(cCallRsp.getResult()));
} else {
    System.out.println(cCallRsp.getErrorDesc());
}

```

读取成功的结果如下:

```json
{"query_rets":[{"result":{"type":"string","value":"test"}}]}
```

至此，我们就完成了一个完整的合约编写，部署，调用和读取的过程。


## 1.8 错误码

| 异常                                      | 错误码 | 描述                                                         |
| ----------------------------------------- | ------ | ------------------------------------------------------------ |
| ACCOUNT_CREATE_ERROR                      | 11001  | Failed to create the account                                 |
| INVALID_SOURCEADDRESS_ERROR               | 11002  | Invalid sourceAddress                                        |
| INVALID_DESTADDRESS_ERROR                 | 11003  | Invalid destAddress                                          |
| INVALID_INITBALANCE_ERROR                 | 11004  | InitBalance must be between 1 and Long.MAX_VALUE             |
| SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR     | 11005  | SourceAddress cannot be equal to destAddress                 |
| INVALID_ADDRESS_ERROR                     | 11006  | Invalid address                                              |
| CONNECTNETWORK_ERROR                      | 11007  | Failed to connect to the network                             |
| INVALID_ISSUE_AMOUNT_ERROR                | 11008  | Amount of the token to be issued must be between 1 and Long.MAX_VALUE |
| NO_METADATAS_ERROR                        | 11010  | The account does not have the metadatas                      |
| INVALID_DATAKEY_ERROR                     | 11011  | The length of key must be between 1 and 1024                 |
| INVALID_DATAVALUE_ERROR                   | 11012  | The length of value must be between 0 and 256000             |
| INVALID_DATAVERSION_ERROR                 | 11013  | The version must be equal to or greater than 0               |
| INVALID_MASTERWEIGHT_ERROR                | 11015  | MasterWeight must be between 0 and (Integer.MAX_VALUE * 2L + 1) |
| INVALID_SIGNER_ADDRESS_ERROR              | 11016  | Invalid signer address                                       |
| INVALID_SIGNER_WEIGHT_ERROR               | 11017  | Signer weight must be between 0 and (Integer.MAX_VALUE * 2L + 1) |
| INVALID_TX_THRESHOLD_ERROR                | 11018  | TxThreshold must be between 0 and Long.MAX_VALUE             |
| INVALID_TYPETHRESHOLD_TYPE_ERROR          | 11019  | Type of TypeThreshold is invalid                             |
| INVALID_TYPE_THRESHOLD_ERROR              | 11020  | TypeThreshold must be between 0 and Long.MAX_VALUE           |
| INVALID_AMOUNT_ERROR                      | 11024  | Amount must be between 0 and Long.MAX_VALUE                  |
| INVALID_CONTRACT_HASH_ERROR               | 11025  | Invalid transaction hash to create contract                  |
| INVALID_GAS_AMOUNT_ERROR                  | 11026  | bifAmount must be between 0 and Long.MAX_VALUE               |
| INVALID_ISSUER_ADDRESS_ERROR              | 11027  | Invalid issuer address                                       |
| INVALID_CONTRACTADDRESS_ERROR             | 11037  | Invalid contract address                                     |
| CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR | 11038  | contractAddress is not a contract account                    |
| SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR | 11040  | SourceAddress cannot be equal to contractAddress             |
| INVALID_FROMADDRESS_ERROR                 | 11041  | Invalid fromAddress                                          |
| FROMADDRESS_EQUAL_DESTADDRESS_ERROR       | 11042  | FromAddress cannot be equal to destAddress                   |
| INVALID_SPENDER_ERROR                     | 11043  | Invalid spender                                              |
| PAYLOAD_EMPTY_ERROR                       | 11044  | Payload cannot be empty                                      |
| INVALID_CONTRACT_TYPE_ERROR               | 11047  | Invalid contract type                                        |
| INVALID_NONCE_ERROR                       | 11048  | Nonce must be between 1 and Long.MAX_VALUE                   |
| INVALID_GASPRICE_ERROR                    | 11049  | GasPrice must be between 0 and Long.MAX_VALUE                |
| INVALID_FEELIMIT_ERROR                    | 11050  | FeeLimit must be between 0 and Long.MAX_VALUE                |
| OPERATIONS_EMPTY_ERROR                    | 11051  | Operations cannot be empty                                   |
| INVALID_CEILLEDGERSEQ_ERROR               | 11052  | CeilLedgerSeq must be equal to or greater than 0             |
| OPERATIONS_ONE_ERROR                      | 11053  | One of the operations cannot be resolved                     |
| INVALID_SIGNATURENUMBER_ERROR             | 11054  | SignagureNumber must be between 1 and Integer.MAX_VALUE      |
| INVALID_HASH_ERROR                        | 11055  | Invalid transaction hash                                     |
| INVALID_SERIALIZATION_ERROR               | 11056  | Invalid serialization                                        |
| PRIVATEKEY_NULL_ERROR                     | 11057  | PrivateKeys cannot be empty                                  |
| PRIVATEKEY_ONE_ERROR                      | 11058  | One of privateKeys is invalid                                |
| SIGNDATA_NULL_ERROR                       | 11059  | SignData cannot be empty                                     |
| INVALID_BLOCKNUMBER_ERROR                 | 11060  | BlockNumber must be bigger than 0                            |
| PUBLICKEY_NULL_ERROR                      | 11061  | PublicKey cannot be empty                                    |
| URL_EMPTY_ERROR                           | 11062  | Url cannot be empty                                          |
| INVALID_OPTTYPE_ERROR                     | 11064  | OptType must be between 0 and 2                              |
| GET_ALLOWANCE_ERROR                       | 11065  | Failed to get allowance                                      |
| SIGNATURE_EMPTY_ERROR                     | 11067  | The signatures cannot be empty                               |
| REQUEST_NULL_ERROR                        | 12001  | Request parameter cannot be null                             |
| CONNECTN_BLOCKCHAIN_ERROR                 | 19999  | Failed to connect to the blockchain                          |
| SYSTEM_ERROR                              | 20000  | System error                                                 |
| INVALID_CONTRACTBALANCE_ERROR             | 12002  | ContractBalance must be between 1 and Long.MAX_VALUE         |
| INVALID_PRITX_FROM_ERROR                  | 12003  | Invalid Private Transaction Sender                           |
| INVALID_PRITX_PAYLAOD_ERROR               | 12004  | Invalid Private Transaction payload                          |
| INVALID_PRITX_TO_ERROR                    | 12005  | Invalid Private Transaction recipient list                   |
| INVALID_PRITX_HASH_ERROR                  | 12006  | Invalid Private Transaction Hash                             |