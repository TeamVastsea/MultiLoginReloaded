# 贡献指南

欢迎为 MultiLogin Reloaded 出力。无论是修 bug、补协议映射、写文档还是报告问题，都很有帮助。

## 开始之前

- 报告问题请走 [Issues](https://github.com/TeamVastsea/MultiLoginReloaded/issues)，模板里的版本号、核心类型和日志请尽量填全。
- 发现安全漏洞请不要公开提交，参见 [SECURITY.md](SECURITY.md)。
- 打算做较大改动（新增平台支持、改数据库结构、调整验证流程）建议先开 Issue 讨论方案，避免白做。

## 构建环境

需要 `Java 21` 或更高版本，其余依赖由 Gradle 自动处理。

```bash
git clone https://github.com/TeamVastsea/MultiLoginReloaded.git
cd MultiLoginReloaded
./gradlew shadowJar          # Windows 用 gradlew.bat shadowJar
```

产物在 `velocity/build/libs/MultiLogin-Velocity-Build_<commit>.jar`。

带 `-Denv=final` 参数构建时会用 `gradle.properties` 里的 `plugin_version` 作为版本号，否则用 `Build_<commit>` 标记为开发版。开发版启动时会打印稳定性警告，并且跳过更新检查。

## 运行测试

```bash
./gradlew test
```

纯函数逻辑（版本号比较、名称冲突处理、消息变量替换、协议映射）都有单元测试覆盖，改到这些地方请一并更新或补充测试。修 bug 时建议先写一个能复现该 bug 的测试，再改实现。

## 项目结构

| 模块 | 职责 |
| --- | --- |
| `api` | 对外 API 与内部抽象接口（`IPlayer`、`IServer`、`Injector`、`GameProfile` 等） |
| `core` | 主体逻辑：验证流程、数据库、指令、皮肤修复、配置读取 |
| `flows` | 自研工作流引擎（顺序、并行、委托三种编排方式） |
| `loader` | 运行时依赖下载与类加载器隔离，依赖清单见根目录 `libraries` / `repositories` / `.digests` |
| `velocity` | Velocity 平台实现层 |
| `velocity:injector` | 通过反射改写 Velocity 内部登录与聊天会话处理 |

`bungee` 和 `bukkit` 模块在 `settings.gradle` 中处于注释状态，目前只发布 Velocity 版本。

### 关于 injector

`velocity:injector` 直接操作 Velocity 的内部类（`StateRegistry`、`InitialLoginSessionHandler`、`AuthSessionHandler`），这些类不属于公开 API，随时可能在 Velocity 更新中改变。改动这个模块时请注意：

- `velocity` 与 `velocity:injector` 两个模块的 `velocity-api` / `velocity-proxy` 版本必须一致，否则反射目标会错位。
- 反射失败时要给出能让用户看懂的错误信息，最好带上当前 Velocity 版本。
- 改完必须在真实 Velocity 上验证登录流程，编译通过不等于可用。

## 添加依赖

运行期依赖不写在 `build.gradle` 里，而是加到根目录的 `libraries` 文件（格式为 `groupId  artifactId  version`，用空白分隔）。执行构建时 `loader` 模块会自动从 `repositories` 里列出的仓库下载并计算 SHA-256 摘要，写入根目录的 `.digests`。

`.digests` 不纳入版本控制，每次构建按 `libraries` 的内容自动补全缺失条目，所以新增依赖时只需要提交 `libraries` 的改动。插件在运行期会用这份摘要校验下载到的 jar，因此改完依赖后请完整跑一次构建，确认摘要能正常生成。

## 代码风格

- 跟随文件已有的风格，不要顺手重排无关代码。
- 注释和文档用中文，日志与异常信息保持英文（用户可见的文本统一走 `message.properties`）。
- 面向玩家或控制台的文本一律加到 `core/src/main/resources/message.properties`，不要硬编码。新增键必须同时补齐默认值，否则运行时取到 `null` 会抛异常。
- 周期性任务（`runTaskAsyncTimer`）内部必须捕获所有异常。底层用的是 `scheduleAtFixedRate`，任务一旦抛出异常就不会再被调度。
- 延迟执行的任务里获取玩家对象要判空，届时玩家可能已经离线。

## 提交与 PR

- 提交信息写清做了什么，中英文都可以。
- 一个 PR 只做一件事，便于评审和回滚。
- PR 描述里说明改了什么、怎么验证的、有没有已知限制。涉及登录流程的改动请说明在哪个 Velocity 版本和 Minecraft 版本上测过。
- CI 会自动构建，请确保通过。

## 新增协议版本支持

Minecraft 更新后如果聊天会话包 ID 变化，需要更新 `MapperConfig` 里的默认映射和 `core/src/main/resources/mapper.yml`。插件自带自动探测机制（`NewChatSessionPacketIDEvent`），能自行学习新的包 ID 并写入配置，但代价是该版本的第一个玩家会被踢一次，所以还是希望提前补上。
