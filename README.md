# 项目名称

BIF-Core-SDK通过API调用的方式提供了星火链网-底层区块链平台相关接口。

## 功能特性

BIF-Core-SDK通过API调用的方式提供了星火链网-底层区块链平台公私钥对生成、星火链网-底层区块链平台私钥签名公钥验签、账号服务、区块服务、交易服务等接口，同时还提供了接口使用示例说明，开发者可以调用该SDK方便快捷的生成星火链网主链的快速接入。

## 快速开始

### 依赖检查 

- JDK1.8以上的版本(本SDK使用256位密钥加解密,需去官方下载JCE无限制权限策略文件，替换local_policy.jar，US_export_policy.jar文件）。更多信息，请参见[安装JDK](https://www.oracle.com/java/technologies/javase-downloads.html)。

- 安装Maven。更多信息，请参见[安装Maven](https://maven.apache.org/download.cgi)。

- 查看Java版本

  执行命令`java -version`查看Java版本
  
  ```
  $ java -version
  java version "1.8.0_202"
  ```
### SDK Jar包引用方式

#### 构建

使用Maven 构建项目

```maven
## 1.清理
mvn clean： 清除maven工程下的target文件夹，由compile生成的文件夹
## 2.编译
mvn compile ：编译当前的工程，将Java文件编译成.class文件（真正在jvm里面运行的文件）
## 3.打包并安装
mvn install： 构建当前的maven工程，并且安装到本地中，可以直接被本地的机器的其他工程引用。（执行这个操作，默认会执行compile操作）
```

#### maven中央仓库

```
<dependency>
    <groupId>bif.chain</groupId>
    <artifactId>bif-chain-sdk</artifactId>
    <version>1.0.2</version>
</dependency>
```

#### 引用

在要使用sdk的源文件里使用import引用sdk包，如下：

```
import cn.bif.*;
```

#### 应用demo

java sdk应用示例，请参考[ bif-chain-sdk-example](bif-chain-sdk-example)

## 使用指南
- bif-chain-sdk目录：BIF-Core-SDK开源代码  
- bif-chain-sdk-example目录：BIF-Core-SDK演示example 

详见[BIF-Core-SDK](./docs/BIF-Core-SDK.md)

## 文档

- [CHANGELOG](./CHANGELOG.md)
- [LICENSE](./LICENSE)
- [BIF-Core-SDK](https://bif-core-dev-doc.readthedocs.io/zh_CN/v1.0.0/index.html)

## 如何贡献

欢迎参与“星火·链网”主链服务的生态建设：

1. 如项目对您有帮助，欢迎点亮我们的小星星(点击项目上方Star按钮)。

2. 欢迎提交代码(Pull requests)。

3. 提问和提交BUG。

4. 邮件反馈：guoshijie@caict.ac.cn

   我们将尽快给予回复。
   
## 关于作者

中国信通院秉持开源开放的理念，将星火“BID-Core-SDK”面向社区和公众完全开源，助力全行业伙伴提升数据价值流通的效率，实现数据价值转化。

## 许可证

[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

版权所有 2023 中国信息通信研究院工业互联网与物联网研究所
