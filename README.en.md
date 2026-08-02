[中文](./README.md)
<div align="center">

# MultiLogin Reloaded

_✨ Genuine and multiple external logins coexist ✨_

[![GitHub license](https://img.shields.io/github/license/CaaMoe/MultiLogin?style=flat-square)](LICENSE)
<!-- [![bStats](https://img.shields.io/bstats/servers/21890?color=brightgreen&label=bStats&logo=bs&style=flat-square)](https://bstats.org/plugin/velocity/MultiLogin/21890) -->

</div>

> [!NOTE]
> The original repository https://github.com/CaaMoe/MultiLogin has been archived.  
> This repository is a continuation maintained based on personal usage scenarios and will follow up on issues and make fixes.  
> New issue feedback can be submitted in this repository's [Issues](https://github.com/TeamVastsea/MultiLoginReloaded/issues).

## Overview

MultiLogin is a plugin mainly designed for the Minecraft proxy, aiming to support the coexistence of genuine and multiple external logins. It is used to connect players under two or more external authentication servers, allowing them to play together on the same server.

## Features

* Supports up to 128 Yggdrasils from different sources coexisting simultaneously
* Authentication proxy and retry mechanism
* In-game profile management system
* Asynchronous/synchronous skin repair mechanism
* Supports takeover of Floodgate

## Install

The minimum requirement is `Java 21`, no need to install `authlib-injector`, no prerequisite plugins, and no need to add or change `JVM` parameters.

1. Download the latest plugin from [Releases](https://github.com/TeamVastsea/MultiLoginReloaded/releases/latest)
2. Put it into the `plugins` directory of your proxy
3. Start the server. The plugin downloads its runtime libraries and generates config files on first launch

To try fixes that have not been released yet, grab a [Weekly](https://github.com/TeamVastsea/MultiLoginReloaded/releases/tag/weekly) build.

## Config

See details in the [Wiki](https://github.com/TeamVastsea/MultiLoginReloaded/wiki).

## Build

1. Clone this project
2. Execute `./gradlew shadowJar` (or `gradlew.bat shadowJar` on Windows)
3. Find the artifact under `velocity/build/libs`

Run the tests with `./gradlew test`.

Or you can [Fork](https://github.com/TeamVastsea/MultiLoginReloaded/fork) this project and push any change, then let CI build it for you.

## BUG Report

First make sure you are on the latest version. The issue you hit may already be fixed in a [Weekly](https://github.com/TeamVastsea/MultiLoginReloaded/releases/tag/weekly) build.

If it still reproduces, please submit feedback and suggestions via [Issues](https://github.com/TeamVastsea/MultiLoginReloaded/issues), filling in the version, proxy type and logs as completely as you can.

Please do not report security vulnerabilities publicly. See [SECURITY.md](SECURITY.md) instead.

## Contributing

Pull requests are welcome. Please read the [contributing guide](CONTRIBUTING.md) before you start.

<a href="https://github.com/TeamVastsea/MultiLoginReloaded/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=TeamVastsea/MultiLoginReloaded"  alt="Contributor's head"/>
</a>
