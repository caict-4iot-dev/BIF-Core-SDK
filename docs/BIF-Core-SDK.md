# BIF-Core-SDK

## 概述
本文档详细说明BIF-Core-SDK常用接口文档, 使开发者更方便地操作和查询星火链。

[TOC]


## 名词解析

账户服务： 提供账户相关的有效性校验、创建与查询接口

合约服务： 提供合约相关的有效性校验、创建与查询接口

交易服务： 提供构建交易及交易查询接口

区块服务： 提供区块的查询接口

账户nonce值： 每个账户都维护一个序列号，用于用户提交交易时标识交易执行顺序的

## 请求参数与响应数据格式

### 请求参数

接口的请求参数的类名，是[服务名][方法名]Request，比如: 账户服务下的getAccount接口的请求参数格式是BIFAccountGetInfoRequest。

请求参数的成员，是各个接口的入参的成员。例如：账户服务下的getAccount接口的入参成员是address，那么该接口的请求参数的完整结构如下：
```java
Class BIFAccountGetInfoRequest {
	String address;
}
```

### 响应数据

接口的响应数据的类名，是[服务名][方法名]Response，比如：账户服务下的getNonce接口的响应数据格式是BIFAccountGetNonceResponse。

响应数据的成员，包括错误码、错误描述和返回结果，响应数据的成员如下：
```java
Class BIFAccountGetNonceResponse {
	Integer errorCode;
	String errorDesc;
	BIFAccountGetNonceResult result;
}
```

说明：
1. errorCode: 错误码。错误码为0表示响应正常，其他错误码请查阅[错误码详情](#错误码)。
2. errorDesc: 错误描述。
3. result: 返回结果。一个结构体，其类名是[服务名][方法名]Result，其成员是各个接口返回值的成员，例如：账户服务下的getNonce接口的结果类名是BIFAccountGetNonceResult，成员有nonce, 完整结构如下：
```java
Class BIFAccountGetNonceResult {
	Long nonce;
}
```

## 使用方法

这里介绍SDK的使用流程，首先需要生成SDK实现，然后调用相应服务的接口，其中服务包括账户服务、合约服务、区块服务

### 生成SDK实例

调用SDK的接口getInstance来实现，调用如下：
```java
String url = "http://192.168.22.121:37002";
BIFSDK sdk = BIFSDK.getInstance(url);
```

### 生成公私钥地址

#### Ed25519算法生成

```java
KeyPairEntity keypair = KeyPairEntity.getBidAndKeyPair();
String encAddress = keypair.getEncAddress();
String encPublicKey = keypair.getEncPublicKey();
String encPrivateKey = keypair.getEncPrivateKey();
byte[] rawPublicKey = keypair.getRawPublicKey();
byte[] rawPrivateKey = keypair.getRawPrivateKey();
```

#### SM2算法生成

```java
KeyPairEntity keypair = Keypair.getBidAndKeyPairBySM2();
String encAddress = keypair.getEncAddress();
String encPublicKey = keypair.getEncPublicKey();
String encPrivateKey = keypair.getEncPrivateKey();
byte[] rawPublicKey = keypair.getRawPublicKey();
byte[] rawPrivateKey = keypair.getRawPrivateKey();
```

### 私钥对象使用

#### 构造对象

```java
//签名方式构造 
PrivateKeyManager privateKey = new PrivateKeyManager(KeyType.ED25519);
//私钥构造
String encPrivateKey = "privbsDGan4sA9ZYpEERhMe25k4K5tnJu1fNqfEHbyKfaV9XSYq7uMcy";
PrivateKeyManager privateKey = new PrivateKeyManager(encPrivateKey);
```

#### 解析对象

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

#### 私钥接口

##### 根据私钥获取公钥

```java
String encPrivateKey = "privbsDGan4sA9ZYpEERhMe25k4K5tnJu1fNqfEHbyKfaV9XSYq7uMcy";
String encPublicKey = PrivateKeyManager.getEncPublicKey(encPrivateKey);
```

##### 原生私钥转星火私钥

```java
String encPrivateKey = PrivateKeyManager.getEncPrivateKey(rawPrivateKey, KeyType.ED25519);
```

##### 原生公钥转星火公钥

```java
String encPublicKey = PrivateKeyManager.getEncPublicKey(rawPublicKey, KeyType.ED25519);
```

##### 签名

```java
#调用此方法需要构造PrivateKeyManage对象
PrivateKeyManager privateKey = PrivateKeyManager(KeyType.ED25519);
String src = "test";
byte[] signMsg = privateKey.sign(src.getBytes());

#调用此方法不需要构造PrivateKeyManage对象
String src = "test";
String privateKey = "privbsDGan4sA9ZYpEERhMe25k4K5tnJu1fNqfEHbyKfaV9XSYq7uMcy";
byte[] sign = PrivateKeyManager.sign(src.getBytes(), privateKey);
```

### 公钥对象使用

#### 构造对象

```java
#公钥创建对象  
String encPublicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
PublicKeyManager publicKey = new PublicKeyManager(encPublicKey);
```

#### 接口

##### 获取账号地址

```java
#调用此方法需要构造PublicKeyManager对象
String publicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
PublicKeyManager publicKey = new PublicKeyManager(encPublicKey);
String encAddress = publicKey.getEncAddress();

#调用此方法不需要构造PublicKeyManager对象
String publicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
String encAddress = PublicKeyManager.getEncAddress(encPublicKey);
```

##### 验签

```java
#调用此方法需要构造PublicKeyManager对象
String publicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
PublicKeyManager publicKey = new PublicKeyManager(publicKey);
String src = "test";
#签名后信息
String sign = "5EC1B9625D28906378E6ED364855295748A57CDB679F5A23C4EA1427228814B7D68BEDC9D1CCED4720630501C2EA15C9F73639936C95E903432E8E893234C402";
Boolean verifyResult = publicKey.verify(src.getBytes(), sign.getBytes());

#调用此方法不需要构造PublicKeyManager对象
String src = "test";
String publicKey = "b0014085888f15e6fdae80827f5ec129f7e9323cf60732e7f8259fa2d68a282e8eed51fad13f";
#签名后信息
String sign = "5EC1B9625D28906378E6ED364855295748A57CDB679F5A23C4EA1427228814B7D68BEDC9D1CCED4720630501C2EA15C9F73639936C95E903432E8E893234C402";
Boolean verifyResult = PublicKeyManager.verify(src.getBytes(), sign, publicKey);
```

### 密钥存储器

#### 生成密钥存储器

KeyStore.generateKeyStore(encPrivateKey, password, n, r, p, kerynSptore)

> 请求参数

| 参数          | 类型    | 描述                   |
| ------------- | ------- | ---------------------- |
| encPrivateKey | String  | 待存储的密钥，可为null |
| password      | String  | 口令                   |
| n             | Integer | CPU消耗参数，可为null  |
| r             | Integer | 内存消息参数，可为null |
| p             | Integer | 并行化参数，可为null   |

> 响应数据

| 参数          | 类型       | 描述             |
| ------------- | ---------- | ---------------- |
| encPrivateKey | String     | 解析出来的密钥   |
| keyStore      | JSONObject | 存储密钥的存储器 |

> 示例

```java
#私钥
String encPrivateKey = "privbtGQELqNswoyqgnQ9tcfpkuH8P1Q6quvoybqZ9oTVwWhS6Z2hi1B";
#口令
String password = "test1234";
JSONObject keyStore = new JSONObject();
String returEencPrivateKey = KeyStore.generateKeyStore(encPrivateKey, password, 16384, 8, 1, keyStore);
System.out.println(returEencPrivateKey);
System.out.println(keyStore.toJSONString());
```

#### 解析密钥存储器

KeyStore.from(keyStore, password)

> 请求参数

| 参数     | 类型       | 描述             |
| -------- | ---------- | ---------------- |
| password | String     | 口令             |
| keyStore | JSONObject | 存储密钥的存储器 |

> 响应数据

| 参数          | 类型   | 描述           |
| ------------- | ------ | -------------- |
| encPrivateKey | String | 解析出来的密钥 |

> 示例

```java
String password = "test1234";
JSONObject keyStore = JSONObject.parseObject("{\"cypher_text\":\"7E0892CAB60761CD8F73A21F0B040ACACAB694AF8C8CA25D4BE8549CCBD8E013AA4C2D338EA11F42596E0EEC05A158C20AE4B51E2B94D102\",\"aesctr_iv\":\"38C33D8E6E5911A0C3F715F5AC75A88A\",\"address\":\"did:bid:QdBdkvmAhnRrhLp4dmeCc2ft7RNE51c9EK\",\"scrypt_params\":{\"p\":1,\"r\":8,\"salt\":\"3070E64061711D39A382E23142B91DD9A6B3AB0B5AC1FD4D202191EAC2816661\",\"n\":16384},\"version\":2}");
encPrivateKey = KeyStore.from(keyStore, password);
System.out.println(encPrivateKey);
```

### 助记词

#### 生成助记词

> 请求参数

| 参数   | 类型   | 描述                     |
| ------ | ------ | ------------------------ |
| random | byte[] | 16位字节数组，必须是16位 |

> 响应数据

| 参数          | 类型         | 描述   |
| ------------- | ------------ | ------ |
| mnemonicCodes | List<String> | 助记词 |

>  示例

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

#### 根据助记词生成私钥

> 请求参数

| 参数          | 类型         | 描述   |
| ------------- | ------------ | ------ |
| mnemonicCodes | List<String> | 助记词 |
| hdPaths       | List<String> | 路径   |

>响应数据

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
List<String> privateKeys = Mnemonic.generatePrivateKeys(mnemonicCodes, hdPaths);
for (String privateKey : privateKeys) {
   System.out.print(privateKey + " ");
}
System.out.println();
```

## 账户服务

账户服务主要是账户相关的接口。

### 接口列表

| 序号 | 接口               | 说明                                 |
| ---- | ------------------ | ------------------------------------ |
| 1    | createAccount      | 生成主链数字身份                     |
| 2    | getAccount         | 该接口用于获取指定的账户信息         |
| 3    | getNonce           | 该接口用于获取指定账户的nonce值      |
| 4    | getAccountBalance  | 该接口用于获取指定账户的Gas的余额    |
| 5    | setMetadata        | 设置metadata                         |
| 6    | getAccountMetadata | 该接口用于获取指定账户的metadata信息 |
| 7    | setPrivilege       | 设置权限                             |
| 8    | getAccountPriv     | 获取账户权限                         |

#### createAccount

> 接口说明

   生成主链数字身份

> 调用方法

BIFCreateAccountResponse createAccount(BIFCreateAccountRequest);

> 请求参数

| 参数          | 类型   | 描述                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| senderAddress | string | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long   | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata      | String | 可选，用户自定义给交易的备注，16进制格式                     |
| destAddress   | String | 必填，目标账户地址                                           |
| initBalance   | Long   | 必填，初始化资产，单位MO，1 Gas = 10^8 UGas, 大小(0, Long.MAX_VALUE] |

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
request.setMetadata("init account");

// 调用 createAccount 接口
BIFCreateAccountResponse response = sdk.getBIFAccountService().createAccount(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

#### getAccount

> 接口说明

   该接口用于获取指定的账户信息

> 调用方法

BIFAccountGetInfoResponse getAccount(BIFAccountGetInfoRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
address     |   String     |  必填，待查询的区块链账户地址   

> 响应数据

   参数    |     类型      |        描述       
--------- | ------------- | ---------------- 
address	  |    String     |    账户地址       
balance	  |    Long       |    账户余额，单位MO，1 Gas = 10^8 UGas, 必须大于0
nonce	  |    Long       |    账户交易序列号，必须大于0

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_ADDRESS_ERROR| 11006 | Invalid address
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR| 11007| Failed to connect to the network
SYSTEM_ERROR |   20000     |  System error 

> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetInfoRequest request = new BIFAccountGetInfoRequest();
request.setAddress(accountAddress);

// 调用 getAccount 接口
BIFAccountGetInfoResponse response = sdk.getBIFAccountService().getAccount(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

#### getNonce

> 接口说明

   该接口用于获取指定账户的nonce值

> 调用方法

BIFAccountGetNonceResponse getNonce(BIFAccountGetNonceRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
address     |   String     |  必填，待查询的区块链账户地址   

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
nonce       |   Long       |  账户交易序列号   

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_ADDRESS_ERROR| 11006 | Invalid address
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR| 11007| Failed to connect to the network
SYSTEM_ERROR |   20000     |  System error 

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

#### getAccountBalance

> 接口说明

  该接口用于获取指定账户的余额。

> 调用方法

BIFAccountGetBalanceResponse getAccountBalance(BIFAccountGetBalanceRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
address     |   String     |  必填，待查询的区块链账户地址   

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
balance     |   Long       | 余额 

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_ADDRESS_ERROR| 11006 | Invalid address
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR| 11007| Failed to connect to the network
SYSTEM_ERROR |   20000     |  System error 

> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetBalanceRequest request = new BIFAccountGetBalanceRequest();
request.setAddress(accountAddress);

// 调用 getAccountBalance 接口
BIFAccountGetBalanceResponse response = sdk.getBIFAccountService().getAccountBalance(request);
System.out.println(JSON.toJSONString(response, true));
if (0 == response.getErrorCode()) {
    System.out.println("Gas balance：" + ToBaseUnit.ToGas(response.getResult().getBalance().toString()) + "Gas");
}
```

#### setMetadata

> 接口说明

   修改账户的metadata信息

> 调用方法

BIFAccountSetMetadataResponse setMetadata(BIFAccountSetMetadataRequest);

> 请求参数

| 参数          | 类型    | 描述                                                         |
| ------------- | ------- | ------------------------------------------------------------ |
| senderAddress | string  | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String  | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long    | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata      | String  | 可选，用户自定义给交易的备注，16进制格式                     |
| key           | String  | 必填，metadata的关键词，长度限制[1, 1024]                    |
| value         | String  | 必填，metadata的内容，长度限制[0, 256000]                    |
| version       | Long    | 选填，metadata的版本                                         |
| deleteFlag    | Boolean | 选填，是否删除metadata                                       |

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

BIFAccountSetMetadataRequest request = new BIFAccountSetMetadataRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setKey(key);
request.setValue(value);
request.setMetadata("set metadata");

// 调用 setMetadata 接口
BIFAccountSetMetadataResponse response = sdk.getBIFAccountService().setMetadata(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

#### getAccountMetadata

> 接口说明

   该接口用于获取指定账户的metadata信息

> 调用方法

BIFAccountGetMetadataResponse getAccountMetadata(BIFAccountGetMetadataRequest);

> 请求参数

   参数   |   类型   |        描述       
-------- | -------- | ---------------- 
address  |  String  |  必填，待查询的账户地址  
key      |  String  | 选填，metadata关键字，长度限制[1, 1024]，有值为精确查找，无值为全部查找 

> 响应数据

| 参数              | 类型   | 描述             |
| ----------------- | ------ | ---------------- |
| metadatas         | object | 账户             |
| metadatas.key     | String | metadata的关键词 |
| metadatas.value   | String | metadata的内容   |
| metadatas.version | Long   | metadata的版本   |


> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_ADDRESS_ERROR | 11006 | Invalid address
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR | 11007 | Failed to connect to the network
NO_METADATA_ERROR|11010|The account does not have the metadata
INVALID_DATAKEY_ERROR | 11011 | The length of key must be between 1 and 1024
SYSTEM_ERROR | 20000| System error


> 示例

```java
// 初始化请求参数
String accountAddress = "did:bid:ef26wZymU7Vyc74S5TBrde8rAu6rnLJwN";
BIFAccountGetMetadataRequest request = new BIFAccountGetMetadataRequest();
request.setAddress(accountAddress);
request.setKey("20210820-01");

// 调用getAccountMetadata接口
BIFAccountGetMetadataResponse response =
sdk.getBIFAccountService().getAccountMetadata(request);
if (response.getErrorCode() == 0) {
    BIFAccountGetMetadataResult result = response.getResult();
    System.out.println(JSON.toJSONString(result, true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

#### setPrivilege

> 接口说明

   设置权限

> 调用方法

BIFAccountSetPrivilegeResponse setPrivilege(BIFAccountSetPrivilegeRequest);

> 请求参数

| 参数                    | 类型   | 描述                                                         |
| ----------------------- | ------ | ------------------------------------------------------------ |
| senderAddress           | string | 必填，交易源账号，即交易的发起方                             |
| privateKey              | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq           | Long   | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata                | String | 可选，用户自定义给交易的备注，16进制格式                     |
| signers                 | list   | 选填，签名者权重列表                                         |
| signers.address         | String | 签名者区块链账户地址                                         |
| signers.weight          | Long   | 选填，metadata的版本                                         |
| txThreshold             | String | 选填，交易门限，大小限制[0, Long.MAX_VALUE]                  |
| typeThreshold           | list   | 选填，指定类型交易门限                                       |
| typeThreshold.type      | Long   | 操作类型，必须大于0                                          |
| typeThreshold.threshold | Long   | 门限值，大小限制[0, Long.MAX_VALUE]                          |
| masterWeight            | String | 选填                                                         |

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
request.setMetadata("set privilege");

// 调用 setPrivilege 接口
BIFAccountSetPrivilegeResponse response = sdk.getBIFAccountService().setPrivilege(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

#### getAccountPriv

> 接口说明

   该接口用于获取指定的账户权限信息。

> 调用方法

BIFAccountPrivResponse getAccountPriv(BIFAccountPrivRequest);

> 请求参数

| 参数    | 类型   | 描述                         |
| ------- | ------ | ---------------------------- |
| address | String | 必填，待查询的区块链账户地址 |

> 响应数据

| 参数                                        | 类型   | 描述                   |
| ------------------------------------------- | ------ | ---------------------- |
| address                                     | String | 账户地址               |
| priv                                        | Object | 账户权限               |
| Priv.masterWeight                           | Object | 账户自身权重，大小限制 |
| Priv.signers                                | Object | 签名者权重列表         |
| Priv.signers[i].address                     | String | 签名者区块链账户地址   |
| Priv.signers[i].weight                      | Long   | 签名者权重，大小限制   |
| Priv.Thresholds                             | Object |                        |
| Priv.Thresholds.txThreshold                 | Long   | 交易默认门限，大小限制 |
| Priv.Thresholds.typeThresholds              | Object | 不同类型交易的门限     |
| Priv.Thresholds.typeThresholds[i].type      | Long   | 操作类型，必须大于0    |
| Priv.Thresholds.typeThresholds[i].threshold | Long   | 门限值，大小限制       |

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
    System.out.println(JSON.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

## 合约服务

合约服务主要是合约相关的接口，目前有6个接口：checkContractAddress, getContractInfo, getContractAddress, contractQuery,contractCreate,contractInvoke

### 接口列表

| 序号 | 接口                 | 说明                               |
| ---- | -------------------- | ---------------------------------- |
| 1    | checkContractAddress | 该接口用于检测合约账户的有效性     |
| 2    | contractCreate       | 创建合约                           |
| 3    | getContractInfo      | 该接口用于查询合约代码             |
| 4    | getContractAddress   | 该接口用于根据交易Hash查询合约地址 |
| 5    | contractQuery        | 该接口用于调试合约代码             |
| 6    | contractInvoke       | 合约调用                           |

#### checkContractAddress

> 接口说明

   该接口用于检测合约账户的有效性

> 调用方法

BIFContractCheckValidResponse checkContractAddress(BIFContractCheckValidRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
contractAddress     |   String     |  待检测的合约账户地址   

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
isValid     |   Boolean     |  是否有效   

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_CONTRACTADDRESS_ERROR|11037|Invalid contract address
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
SYSTEM_ERROR |   20000     |  System error 

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

#### contractCreate

> 接口说明

   创建合约

> 调用方法

BIFContractCreateResponse contractCreate(BIFContractCreateRequest);

> 请求参数

| 参数          | 类型    | 描述                                                         |
| ------------- | ------- | ------------------------------------------------------------ |
| senderAddress | string  | 必填，交易源账号，即交易的发起方                             |
| feeLimit      | Long    | 可选，交易花费的手续费，默认1000000L                         |
| privateKey    | String  | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long    | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata      | String  | 可选，用户自定义给交易的备注，16进制格式                     |
| initBalance   | Long    | 必填，给合约账户的初始化资产，单位MO，1 Gas = 10^8 UGas, 大小限制[1, Long.MAX_VALUE] |
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
request.setMetadata("create contract");
request.setType(0);
request.setFeeLimit(1000000000L);

// 调用 contractCreate 接口
BIFContractCreateResponse response = sdk.getBIFContractService().contractCreate(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

#### getContractInfo

> 接口说明

   该接口用于查询合约代码

> 调用方法

BIFContractGetInfoResponse getContractInfo(BIFContractGetInfoRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
contractAddress     |   String     |  待查询的合约账户地址   

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
 contract             | object  | 合约信息        
 contractInfo.type    | Integer |合约类型，默认0
contractInfo.payload|String|合约代码

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_CONTRACTADDRESS_ERROR|11037|Invalid contract address
CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR|11038|contractAddress is not a contract account
NO_SUCH_TOKEN_ERROR|11030|No such token
GET_TOKEN_INFO_ERROR|11066|Failed to get token info
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
SYSTEM_ERROR|20000|System error

> 示例

```java
// 初始化请求参数
BIFContractGetInfoRequest request = new BIFContractGetInfoRequest();
request.setContractAddress("did:bid:efiBacNvVSnr5QxgB282XGWkg4RXLLxL");

// 调用 getContractInfo 接口
BIFContractGetInfoResponse response = sdk.getBIFContractService().getContractInfo(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

#### getContractAddress

> 接口说明

该接口用于根据交易Hash查询合约地址。

> 调用方法

BIFContractGetAddressResponse getContractAddress(BIFContractGetAddressRequest);

> 请求参数

参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
hash     |   String     |  创建合约交易的hash   

> 响应数据

参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
 contractAddressInfos                                        | List<ContractAddressInfo> | 合约地址列表   
 contractAddressInfos[i].ContractAddressInfo                 | object                    | 成员           
 contractAddressInfos[i].ContractAddressInfo.contractAddress | String                    |合约地址
contractAddressInfos[i].ContractAddressInfo.operationIndex|Integer|所在操作的下标

> 错误码

异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_HASH_ERROR|11055|Invalid transaction hash
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
SYSTEM_ERROR|20000|System error

> 示例

```java
// 初始化请求参数
String hash = "4bb232fbe86e33b956ad5338103d4610b2b31d5bf6af742d7e55b9c6182abfee";
BIFContractGetAddressRequest request = new BIFContractGetAddressRequest();
request.setHash(hash);

// 调用 getContractAddress 接口
BIFContractGetAddressResponse response = sdk.getBIFContractService().getContractAddress(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

#### contractQuery 

> 接口说明

   该接口用于调用合约查询接口。

> 调用方法

BIFContractCallResponse contractQuery(BIFContractCallRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
sourceAddress|String|选填，合约触发账户地址
contractAddress|String|必填，合约账户地址
input|String|选填，合约入参


> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
queryRets|JSONArray|查询结果集

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_SOURCEADDRESS_ERROR|11002|Invalid sourceAddress
INVALID_CONTRACTADDRESS_ERROR|11037|Invalid contract address
CONTRACTADDRESS_CODE_BOTH_NULL_ERROR|11063|ContractAddress and code cannot be empty at the same time
SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR|11040|SourceAddress cannot be equal to contractAddress
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

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
    System.out.println(JSON.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

#### contractInvoke

> 接口说明

   合约调用

> 调用方法

BIFContractInvokeResponse contractInvoke(BIFContractInvokeRequest);

> 请求参数

| 参数            | 类型   | 描述                                                         |
| --------------- | ------ | ------------------------------------------------------------ |
| senderAddress   | string | 必填，交易源账号，即交易的发起方                             |
| feeLimit        | Long   | 可选，交易花费的手续费，默认1000000L                         |
| privateKey      | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq   | Long   | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata        | String | 可选，用户自定义给交易的备注，16进制格式                     |
| contractAddress | String | 必填，合约账户地址                                           |
| bifAmount       | Long   | 必填，转账金额                                               |
| input           | String | 选填，待触发的合约的main()入参                               |

> 响应数据

| 参数 | 类型   | 描述     |
| ---- | ------ | -------- |
| hash | string | 交易hash |


> 错误码

| 异常                          | 错误码 | 描述                                             |
| ----------------------------- | ------ | ------------------------------------------------ |
| INVALID_ADDRESS_ERROR         | 11006  | Invalid address                                  |
| REQUEST_NULL_ERROR            | 12001  | Request parameter cannot be null                 |
| PRIVATEKEY_NULL_ERROR         | 11057  | PrivateKeys cannot be empty                      |
| INVALID_CONTRACTADDRESS_ERROR | 11037  | Invalid contract address                         |
| INVALID_AMOUNT_ERROR          | 11024  | Amount must be between 0 and Long.MAX_VALUE |
| INVALID_FEELIMIT_ERROR        | 11050  | FeeLimit must be between 0 and Long.MAX_VALUE    |
| SYSTEM_ERROR                  | 20000  | System error                                     |


> 示例

```java
// 初始化请求参数
String senderAddress = "did:bid:efVmotQW28QDtQyupnKTFvpjKQYs5bxf";
String contractAddress = "did:bid:ef2gAT82SGdnhj87wQWb9suPKLbnk9NP";
String senderPrivateKey = "priSPKnDue7AJ42gt7acy4AVaobGJtM871r1eukZ2M6eeW5LxG";
Long amount = 0L;

BIFContractInvokeRequest request = new BIFContractInvokeRequest();
request.setSenderAddress(senderAddress);
request.setPrivateKey(senderPrivateKey);
request.setContractAddress(contractAddress);
request.setBIFAmount(amount);
request.setMetadata("contract invoke");

// 调用 contractInvoke 接口
BIFContractInvokeResponse response = sdk.getBIFContractService().contractInvoke(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

## 交易服务

交易服务主要是交易相关的接口，目前有4个接口：getTransactionInfo, gasSend, privateContractCall, privateContractCreate。

### 接口列表

| 序号 | 接口                  | 说明                               |
| ---- | --------------------- | ---------------------------------- |
| 1    | gasSend               | 交易                               |
| 2    | privateContractCreate | 私有化交易-合约创建                |
| 3    | privateContractCall   | 私有化交易-合约调用                |
| 4    | getTransactionInfo    | 该接口用于实现根据交易hash查询交易 |

#### gasSend

> 接口说明

   交易

> 调用方法

BIFTransactionGasSendResponse gasSend(BIFTransactionGasSendRequest);

> 请求参数

| 参数          | 类型   | 描述                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| senderAddress | string | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long   | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata      | String | 可选，用户自定义给交易的备注，16进制格式                     |
| destAddress   | String | 必填，发起方地址                                             |
| amount        | Long   | 必填，转账金额                                               |

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
| INVALID_GAS_AMOUNT_ERROR  | 11026  | bifAmount must be between 0 and Long.MAX_VALUE |
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
request.setMetadata("gas send");

// 调用 gasSend 接口
BIFTransactionGasSendResponse response = sdk.getBIFTransactionService().gasSend(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + response.getErrorDesc());
}
```

#### privateContractCreate

> 接口说明

   私有化交易-合约创建

> 调用方法

BIFTransactionPrivateContractCreateResponse privateContractCreate(BIFTransactionPrivateContractCreateRequest);

> 请求参数

| 参数          | 类型     | 描述                                                         |
| ------------- | -------- | ------------------------------------------------------------ |
| senderAddress | string   | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String   | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long     | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata      | String   | 可选，用户自定义给交易的备注，16进制格式                     |
| type          | Integer  | 必填，合约的语种                                             |
| payload       | String   | 必填，对应语种的合约代码                                     |
| from          | String   | 必填，发起方加密机公钥                                       |
| to            | String[] | 必填，接收方加密机公钥                                       |

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
request.setMetadata("init account");

// 调用 privateContractCreate 接口
BIFTransactionPrivateContractCreateResponse response = sdk.getBIFTransactionService().privateContractCreate(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error:      " + JSON.toJSONString(response));
    return;
}

Thread.sleep(5000);
BIFTransactionGetInfoRequest transactionRequest = new BIFTransactionGetInfoRequest();
transactionRequest.setHash(response.getResult().getHash());
// 调用getTransactionInfo接口
BIFTransactionGetInfoResponse transactionResponse = sdk.getBIFTransactionService().getTransactionInfo(transactionRequest);
if (transactionResponse.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(transactionResponse.getResult(), true));
} else {
    System.out.println("error: " + transactionResponse.getErrorDesc());
}
```

#### privateContractCall

> 接口说明

   私有化交易-合约调用

> 调用方法

BIFTransactionPrivateContractCallResponse privateContractCall(BIFTransactionPrivateContractCallRequest);

> 请求参数

| 参数          | 类型     | 描述                                                         |
| ------------- | -------- | ------------------------------------------------------------ |
| senderAddress | string   | 必填，交易源账号，即交易的发起方                             |
| privateKey    | String   | 必填，交易源账户私钥                                         |
| ceilLedgerSeq | Long     | 可选，区块高度限制, 如果大于0，则交易只有在该区块高度之前（包括该高度）才有效 |
| metadata      | String   | 可选，用户自定义给交易的备注，16进制格式                     |
| destAddress   | String   | 必填，发起方地址                                             |
| type          | Integer  | 必填，合约的语种                                             |
| input         | String   | 必填，待触发的合约的main()入参                               |
| from          | String   | 必填，发起方加密机公钥                                       |
| to            | String[] | 必填，接收方加密机公钥                                       |

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
request.setMetadata("private Contract Call");

// 调用 privateContractCall 接口
BIFTransactionPrivateContractCallResponse response = sdk.getBIFTransactionService().privateContractCall(request);
if (response.getErrorCode() == 0) {
    System.out.println(JSON.toJSONString(response.getResult(), true));
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
    System.out.println(JSON.toJSONString(transactionResponse.getResult(), true));
} else {
    System.out.println("error: " + transactionResponse.getErrorDesc());
}
```

#### getTransactionInfo

> 接口说明

   该接口用于实现根据交易hash查询交易

> 调用方法

BIFTransactionGetInfoResponse getTransactionInfo(BIFTransactionGetInfoRequest);

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
    System.out.println(JSON.toJSONString(response.getResult(), true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

## 区块服务

区块服务主要是区块相关的接口，目前有6个接口：getBlockNumber, getTransactions, getBlockInfo , getBlockLatestInfo, getValidators, getLatestValidators。

### 接口列表

| 序号 | 接口                | 说明                                    |
| ---- | ------------------- | --------------------------------------- |
| 1    | getBlockNumber      | 该接口用于查询最新的区块高度            |
| 2    | getTransactions     | 该接口用于查询指定区块高度下的所有交易3 |
| 3    | getBlockInfo        | 该接口用于获取区块信息                  |
| 4    | getBlockLatestInfo  | 该接口用于获取最新区块信息              |
| 5    | getValidators       | 该接口用于获取指定区块中所有验证节点数  |
| 6    | getLatestValidators | 该接口用于获取最新区块中所有验证节点数  |

#### getBlockNumber

> 接口说明

   该接口用于查询最新的区块高度

> 调用方法

BIFBlockGetNumberResponse getBlockNumber();

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
header|BlockHeader|区块头
header.blockNumber|Long|最新的区块高度，对应底层字段seq

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

> 示例

```java
// 调用getBlockNumber接口
BIFBlockGetNumberResponse response = sdk.getBIFBlockService().getBlockNumber();
if(0 == response.getErrorCode()){
   System.out.println(JSON.toJSONString(response.getResult(), true));
}else{
   System.out.println("error: " + response.getErrorDesc());
}
```

#### getTransactions

> 接口说明

   该接口用于查询指定区块高度下的所有交易

> 调用方法

BIFBlockGetTransactionsResponse getTransactions(BIFBlockGetTransactionsRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
blockNumber|Long|必填，最新的区块高度，对应底层字段seq

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
totalCount|Long|返回的总交易数
transactions|BifTransactionHistory[]|交易内容

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_BLOCKNUMBER_ERROR|11060|BlockNumber must bigger than 0
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

> 示例

```java
// 初始化请求参数
Long blockNumber = 1L;
BIFBlockGetTransactionsRequest request = new BIFBlockGetTransactionsRequest();
request.setBlockNumber(blockNumber);
// 调用 getTransactions 接口
BIFBlockGetTransactionsResponse response = sdk.getBIFBlockService().getTransactions(request);
if (0 == response.getErrorCode()) {
    System.out.println(JSON.toJSONString(response, true));
} else {
    System.out.println("失败\n" + JSON.toJSONString(response, true));
}
```

#### getBlockInfo

> 接口说明

   该接口用于获取区块信息

> 调用方法

BIFBlockGetInfoResponse getBlockInfo(BIFBlockGetInfoRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
blockNumber|Long|必填，待查询的区块高度

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
 header             | BifBlockHeader | 区块信息     
 header.confirmTime | Long           | 区块确认时间 
 header.number      | Long           | 区块高度     
 header.txCount     | Long           | 交易总量     
header.version|String|区块版本

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_BLOCKNUMBER_ERROR|11060|BlockNumber must bigger than 0
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

> 示例

```java
// 初始化请求参数
BIFBlockGetInfoRequest blockGetInfoRequest = new BIFBlockGetInfoRequest();
blockGetInfoRequest.setBlockNumber(10L);
// 调用 getBlockInfo 接口
BIFBlockGetInfoResponse lockGetInfoResponse = sdk.getBIFBlockService().getBlockInfo(blockGetInfoRequest);
if (lockGetInfoResponse.getErrorCode() == 0) {
    BIFBlockGetInfoResult lockGetInfoResult = lockGetInfoResponse.getResult();
    System.out.println(JSON.toJSONString(lockGetInfoResult, true));
} else {
    System.out.println("error: " + lockGetInfoResponse.getErrorDesc());
}
```

#### getBlockLatestInfo

> 接口说明

   该接口用于获取最新区块信息

> 调用方法

BIFBlockGetLatestInfoResponse getBlockLatestInfo();

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
 header | BifBlockHeader | 区块信息                  
 header.confirmTime | Long           | 区块确认时间              
 header.number      | Long           | 区块高度，对应底层字段seq 
 header.txCount     | Long           | 交易总量                  
 header.version     | String         | 区块版本                  


> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

> 示例

```java
// 调用 getBlockLatestInfo 接口
BIFBlockGetLatestInfoResponse lockGetLatestInfoResponse = sdk.getBIFBlockService().getBlockLatestInfo();
if (lockGetLatestInfoResponse.getErrorCode() == 0) {
    BIFBlockGetLatestInfoResult lockGetLatestInfoResult = lockGetLatestInfoResponse.getResult();
    System.out.println(JSON.toJSONString(lockGetLatestInfoResult, true));
} else {
    System.out.println(lockGetLatestInfoResponse.getErrorDesc());
}
```

#### getValidators

> 接口说明

   该接口用于获取指定区块中所有验证节点数

> 调用方法

BIFBlockGetValidatorsResponse getValidators(BIFBlockGetValidatorsRequest);

> 请求参数

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
blockNumber|Long|必填，待查询的区块高度，必须大于0

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
 validators         | String[] | 验证节点列表 
validators.address|String|共识节点地址

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
INVALID_BLOCKNUMBER_ERROR|11060|BlockNumber must bigger than 0
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

> 示例

```java
// 初始化请求参数
BIFBlockGetValidatorsRequest request = new BIFBlockGetValidatorsRequest();
request.setBlockNumber(1L);

// 调用 getValidators 接口
BIFBlockGetValidatorsResponse response = sdk.getBIFBlockService().getValidators(request);
if (response.getErrorCode() == 0) {
    BIFBlockGetValidatorsResult result = response.getResult();
    System.out.println(JSON.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

#### getLatestValidators

> 接口说明

   该接口用于获取最新区块中所有验证节点数

> 调用方法

BIFBlockGetLatestValidatorsResponse getLatestValidators();

> 响应数据

   参数      |     类型     |        描述       
----------- | ------------ | ---------------- 
 validators         | String[] | 验证节点列表 
 validators.address | String   |共识节点地址

> 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
SYSTEM_ERROR|20000|System error

> 示例

```java
// 调用 getLatestValidators 接口
BIFBlockGetLatestValidatorsResponse response = sdk.getBIFBlockService().getLatestValidators();
if (response.getErrorCode() == 0) {
    BIFBlockGetLatestValidatorsResult result = response.getResult();
    System.out.println(JSON.toJSONString(result, true));
} else {
    System.out.println("error: " + response.getErrorDesc());
}
```

## 错误码

   异常       |     错误码   |   描述   
-----------  | ----------- | -------- 
ACCOUNT_CREATE_ERROR|11001|Failed to create the account 
INVALID_SOURCEADDRESS_ERROR|11002|Invalid sourceAddress
INVALID_DESTADDRESS_ERROR|11003|Invalid destAddress
INVALID_INITBALANCE_ERROR|11004|InitBalance must be between 1 and Long.MAX_VALUE 
SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR|11005|SourceAddress cannot be equal to destAddress
INVALID_ADDRESS_ERROR|11006|Invalid address
CONNECTNETWORK_ERROR|11007|Failed to connect to the network
INVALID_ISSUE_AMOUNT_ERROR|11008|Amount of the token to be issued must be between 1 and Long.MAX_VALUE
NO_METADATA_ERROR|11010|The account does not have the metadata
INVALID_DATAKEY_ERROR|11011|The length of key must be between 1 and 1024
INVALID_DATAVALUE_ERROR|11012|The length of value must be between 0 and 256000
INVALID_DATAVERSION_ERROR|11013|The version must be equal to or greater than 0 
INVALID_MASTERWEIGHT_ERROR|11015|MasterWeight must be between 0 and (Integer.MAX_VALUE * 2L + 1)
INVALID_SIGNER_ADDRESS_ERROR|11016|Invalid signer address
INVALID_SIGNER_WEIGHT_ERROR|11017|Signer weight must be between 0 and (Integer.MAX_VALUE * 2L + 1)
INVALID_TX_THRESHOLD_ERROR|11018|TxThreshold must be between 0 and Long.MAX_VALUE
INVALID_OPERATION_TYPE_ERROR|11019|Operation type must be between 1 and 100
INVALID_TYPE_THRESHOLD_ERROR|11020|TypeThreshold must be between 0 and Long.MAX_VALUE
INVALID_AMOUNT_ERROR|11024|Amount must be between 0 and Long.MAX_VALUE
INVALID_GAS_AMOUNT_ERROR|11026|bifAmount must be between 0 and Long.MAX_VALUE
INVALID_ISSUER_ADDRESS_ERROR|11027|Invalid issuer address
INVALID_CONTRACTADDRESS_ERROR|11037|Invalid contract address
CONTRACTADDRESS_NOT_CONTRACTACCOUNT_ERROR|11038|contractAddress is not a contract account
SOURCEADDRESS_EQUAL_CONTRACTADDRESS_ERROR|11040|SourceAddress cannot be equal to contractAddress
INVALID_FROMADDRESS_ERROR|11041|Invalid fromAddress
FROMADDRESS_EQUAL_DESTADDRESS_ERROR|11042|FromAddress cannot be equal to destAddress
INVALID_SPENDER_ERROR|11043|Invalid spender
PAYLOAD_EMPTY_ERROR|11044|Payload cannot be empty
INVALID_CONTRACT_TYPE_ERROR|11047|Invalid contract type
INVALID_NONCE_ERROR|11048|Nonce must be between 1 and Long.MAX_VALUE
INVALID_GASPRICE_ERROR|11049|GasPrice must be between 0 and Long.MAX_VALUE
INVALID_FEELIMIT_ERROR|11050|FeeLimit must be between 0 and Long.MAX_VALUE
OPERATIONS_EMPTY_ERROR|11051|Operations cannot be empty
INVALID_CEILLEDGERSEQ_ERROR|11052|CeilLedgerSeq must be equal to or greater than 0
OPERATIONS_ONE_ERROR|11053|One of the operations cannot be resolved
INVALID_SIGNATURENUMBER_ERROR|11054|SignagureNumber must be between 1 and Integer.MAX_VALUE
INVALID_HASH_ERROR|11055|Invalid transaction hash
INVALID_BLOB_ERROR|11056|Invalid blob
PRIVATEKEY_NULL_ERROR|11057|PrivateKeys cannot be empty
PRIVATEKEY_ONE_ERROR|11058|One of privateKeys is invalid
SIGNDATA_NULL_ERROR|11059|SignData cannot be empty
INVALID_BLOCKNUMBER_ERROR|11060|BlockNumber must be bigger than 0
PUBLICKEY_NULL_ERROR|11061|PublicKey cannot be empty
URL_EMPTY_ERROR|11062|Url cannot be empty
CONTRACTADDRESS_CODE_BOTH_NULL_ERROR|11063|ContractAddress and code cannot be empty at the same time
INVALID_OPTTYPE_ERROR|11064|OptType must be between 0 and 2
GET_ALLOWANCE_ERROR|11065|Failed to get allowance
SIGNATURE_EMPTY_ERROR|11067|The signatures cannot be empty
REQUEST_NULL_ERROR|12001|Request parameter cannot be null
CONNECTN_BLOCKCHAIN_ERROR|19999|Failed to connect to the blockchain 
SYSTEM_ERROR|20000|System error
INVALID_CONTRACTBALANCE_ERROR|12002|ContractBalance must be between 1 and Long.MAX_VALUE
INVALID_PRITX_FROM_ERROR|12003|Invalid Private Transaction Sender
INVALID_PRITX_PAYLAOD_ERROR|12004|Invalid Private Transaction payload
INVALID_PRITX_TO_ERROR|12005|Invalid Private Transaction recipient list
INVALID_PRITX_HASH_ERROR|12006|Invalid Private Transaction Hash
