[English](./README.en.md)
<div align="center">

# MultiLogin Reloaded

_✨ 正版与多种外置登录共存 ✨_

[![GitHub license](https://img.shields.io/github/license/CaaMoe/MultiLogin?style=flat-square)](LICENSE)
<!-- [![bStats](https://img.shields.io/bstats/servers/21890?color=brightgreen&label=bStats&logo=bs&style=flat-square)](https://bstats.org/plugin/velocity/MultiLogin/21890) -->

</div>

> [!NOTE]
> 原仓库 https://github.com/CaaMoe/MultiLogin 已归档。  
> 本仓库为基于个人使用场景的延续维护版本，将跟进问题并进行修复。  
> 新的问题反馈请在本仓库 [Issues](https://github.com/TeamVastsea/MultiLoginReloaded/issues) 提交。

## 概述

MultiLogin 是一款主要为 Minecraft 代理端设计的插件，旨在实现对正版与多种外置登录共存的支持，用于连接两个或多个外置验证服务器下的玩家，使他们能够在同一个服务器上一起游戏。

## 特性

* 支持多达 128 个不同来源的 Yggdrasil 同时共存
* 鉴权代理、重试机制
* 游戏内档案管理系统
* 异步/同步皮肤修复机制
* 支持接管 Floodgate

## 安装

最低需要 `Java 21`，不需要安装 `authlib-injector`，没有任何前置插件，也不需要添加和更改 `JVM` 参数。

1. 在 [Releases](https://github.com/TeamVastsea/MultiLoginReloaded/releases/latest) 下载最新版本的插件
2. 放入代理端的 `plugins` 目录
3. 启动服务器，插件会自动下载运行期依赖并生成配置文件

想抢先试用未发布的修复，可以下载 [Weekly](https://github.com/TeamVastsea/MultiLoginReloaded/releases/tag/weekly) 构建。

## 配置

详见 [Wiki](https://github.com/TeamVastsea/MultiLoginReloaded/wiki)。

## 构建

1. 克隆这个项目
2. 执行 `./gradlew shadowJar`（Windows 用 `gradlew.bat shadowJar`）
3. 产物在 `velocity/build/libs` 下

运行测试：`./gradlew test`

或者你也可以 [Fork](https://github.com/TeamVastsea/MultiLoginReloaded/fork) 此项目，随便提交一个改动，由 CI 自动构建。

## BUG 汇报

先确认你用的是最新版本，[Weekly](https://github.com/TeamVastsea/MultiLoginReloaded/releases/tag/weekly) 构建里也许已经修好了你遇到的问题。

确认仍然存在后，请通过 [Issues](https://github.com/TeamVastsea/MultiLoginReloaded/issues) 提交反馈与建议，模板里的版本号、核心类型和日志请尽量填全。

安全漏洞请不要公开提交，参见 [SECURITY.md](SECURITY.md)。

## 贡献

欢迎提交 PR，开始之前请阅读 [贡献指南](CONTRIBUTING.md)。

<a href="https://github.com/TeamVastsea/MultiLoginReloaded/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=TeamVastsea/MultiLoginReloaded"  alt="作者头像"/>
</a>
