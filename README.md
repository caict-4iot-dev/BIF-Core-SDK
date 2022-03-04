## 简介

BIF-Core-SDK通过API调用的方式提供了星火链网-底层区块链平台公私钥对生成、星火链网-底层区块链平台私钥签名公钥验签、账号服务、区块服务、交易服务等接口，同时还提供了接口使用示例说明，开发者可以调用该SDK方便快捷的生成星火链网主链的快速接入。中国信通院秉持开源开放的理念，将星火“BID-Core-SDK”面向社区和公众完全开源，助力全行业伙伴提升数据价值流通的效率，实现数据价值转化。

<img src=".\images\image-20211012184224056.png" alt="image-20211012184224056" style="zoom:80%;" />

​                                                                                   **图1 BIF-Core-SDK 逻辑架构图**

## 环境要求 
- JDK1.8以上的版本(本SDK使用256位密钥加解密,需去官方下载JCE无限制权限策略文件，替换local_policy.jar，US_export_policy.jar文件）。更多信息，请参见[安装JDK](https://www.oracle.com/java/technologies/javase-downloads.html)。

- 安装Maven。更多信息，请参见[安装Maven](https://maven.apache.org/download.cgi)。

- 查看Java版本

  执行命令`java -version`查看Java版本

## BIF-Core-SDK使用  
- bif-chain-sdk目录：BIF-Core-SDK开源代码  
- bif-chain-sdk-example目录：BIF-Core-SDK开发包演示example 

## 文档

- [CHANGELOG](./CHANGELOG.md)
- [LICENSE](./LICENSE)
- [Documentation](doc/SDK_CN.md)

## 问题反馈

欢迎参与“星火·链网”主链服务的生态建设：

1. 如项目对您有帮助，欢迎点亮我们的小星星(点击项目上方Star按钮)。

2. 欢迎提交代码(Pull requests)。

3. 提问和提交BUG。

4. 邮件反馈：guoshijie@caict.ac.cn

   我们将尽快给予回复。
   
## 发行说明

各版本更新将记录在[CHANGELOG](./CHANGELOG.md)中。

## 许可证

[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

版权所有 2021 中国信息通信研究院工业互联网与物联网研究所
