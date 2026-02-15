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

The minimum requirement is `Java 21`, no need to install `authlib-injector`, no prerequisite plugins, and no need to add or change `JVM` parameters

1. [Download](https://github.com/TeamVastsea/MultiLoginReloaded/releases/latest) the plugin
1. Download the latest version from this repository's Releases
3. Start the server

## Config

See details in the [Wiki](https://github.com/TeamVastsea/MultiLoginReloaded/wiki)

## Build

1. Clone this project
2. Execute `./gradlew shadowJar` / `gradlew shadowJar`
3. Find what you need under `*/build/libs`

Or you can

1. [Fork](https://github.com/TeamVastsea/MultiLoginReloaded/fork) this project
1. Commit any file in the forked project

## BUG Report

[Weekly Ver](https://github.com/TeamVastsea/MultiLoginReloaded/releases/tag/weekly) Click here, perhaps the issue you encountered has been fixed
Please submit feedback and suggestions via Issues.

<a href="https://github.com/TeamVastsea/MultiLoginReloaded/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=TeamVastsea/MultiLoginReloaded"  alt="Contributor's head"/>
</a>

[I also want to be one of the contributors?](https://github.com/TeamVastsea/MultiLoginReloaded/pulls)
