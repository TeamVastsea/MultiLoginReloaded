# 安全策略

## 受支持的版本

我们只对最新的正式发布版本提供安全修复。请在提交安全问题前先升级到 [最新版本](https://github.com/TeamVastsea/MultiLoginReloaded/releases/latest) 复现。

| 版本 | 是否受支持 |
| --- | --- |
| 最新正式版 | :white_check_mark: |
| Weekly 构建 | :warning: 尽力支持，不保证时效 |
| 其他历史版本 | :x: |

## 报告漏洞

MultiLogin 处于玩家登录链路上，直接接触正版会话校验、外置验证服务器凭据和玩家档案数据。**请不要通过公开 Issue 报告安全漏洞**，以免在修复发布前被利用。

请使用 GitHub 的私密漏洞报告通道：

1. 打开仓库的 [Security 页面](https://github.com/TeamVastsea/MultiLoginReloaded/security/advisories)
2. 点击 `Report a vulnerability`
3. 填写复现步骤

如果无法使用该通道，可以在 Issue 中只写"存在安全问题，请联系我"，不要附带任何细节，维护者会主动联系你。

### 请在报告中包含

- 受影响的插件版本（`/multilogin info` 或启动日志中的版本号）
- 代理端类型与版本（Velocity 构建号）
- 涉及的验证服务类型（正版、Blessing Skin、自定义 Yggdrasil、Floodgate）
- 复现步骤，以及漏洞造成的实际影响（越权登录、档案劫持、凭据泄露等）
- 相关日志片段，**请自行删除其中的 accessToken、serverId、数据库密码等敏感值**

### 处理流程

| 阶段 | 预期时间 |
| --- | --- |
| 确认收到 | 7 天内 |
| 初步评估结论 | 14 天内 |
| 修复发布 | 视严重程度而定，高危问题优先 |

漏洞被确认后，修复版本发布时会在 Release Notes 中致谢报告者（如你希望匿名请在报告中说明）。若经评估认为不构成安全问题，我们会说明理由，你可以转为公开 Issue 继续讨论。

## 不属于安全漏洞的情形

以下情况请直接提交普通 Issue：

- 服务端未开启 `online-mode` 或未开启玩家信息转发导致的异常（插件会主动拒绝启动）
- 外置验证服务器自身的漏洞或配置错误
- 服务器管理员主动配置导致的行为，例如关闭白名单校验、放宽 `nameAllowedRegular`
- 需要控制台权限或数据库直连权限才能触发的问题
