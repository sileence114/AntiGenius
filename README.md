# AntiGenius反小天才

###### [English]( #AntiGenius ) / 中文

Fabric 版的 [Prism-Bukkit]( https://github.com/AddstarMC/Prism-Bukkit )（棱镜），名曰 "AntiGenius"。通过记录和回滚来防止小天才引发杯具。

## 把棱镜带到Fabric的原因

南航的朋友开了个技术生存的服务器，用的 Fabric 和地毯模组。当时需要一个能在 Fabric 上运行的记录 MOD 来防小天才。因为 Fabric 很年轻，所以自然是没找到，那就只能咋自己去整一个了。

本打算自己从头编写，但可惜的是我对于高并发的数据库操作一窍不通。正头疼呢，就在 EssentialsX 的 Discord 服务器 (M.O.S.S.) 上发现了开源的 Prism。所以我决定将其适配到 Fabric。

## 反小天才mod的开发计划

> **开发绝赞进行中**

1. [Bukkit 端棱镜动作列表]( https://github.com/AddstarMC/Prism-Bukkit )里的事件全部都能监听，并记录到数据库。
2. 能把数据从数据库里面查出来。
3. 回滚/恢复操作，及其预览。
4. 支持其他类型的 SQL 数据库（SQLite之类的）。

## 关于棱镜

* ### 棱镜 1.x 版本和 2.x 版本

最早版本的棱镜诞生于 Cauldron 时代，基于 Bukkit API。主要的贡献者是 **nasonfish**。
> 除了海绵端棱镜（或棱镜 3.x 版本）仓库中的 [README.md]( https://github.com/prism/Prism#prism-1x---2x ) 文件外，我找不到其他关于棱镜 1 和 2 的信息。
>
> 如果你知道的话，欢迎 PR。

* ### [海绵端棱镜（或棱镜 3.x 版本）]( https://github.com/prism/Prism )

在2016年，因为要在海绵端重构棱镜，Bukkit 端棱镜被抛弃了。重构由 **viveleroi** 完成。

* ### [Bukkit 端棱镜]( https://github.com/AddstarMC/Prism-Bukkit )

在GitHub贡献者的添砖加瓦下，@viveleroi努力的延续，现在由 [AddstarMC]( https://github.com/AddstarMC ) 团队维护。

Bukkit 端棱镜之支持在高于 1.15 版本的 Spigot 端或者 Paper 端 运行。

# AntiGenius

###### English / [中文]( #AntiGenius反小天才 )

A fabric-supported fork of [Prism-Bukkit]( https://github.com/AddstarMC/Prism-Bukkit ) named AntiGenius. Prevent grief
from genius by logging and rollback.
> My English is bad, and the following is basically done with the help of Google Translate.
>
> If you have grammar or vocabulary questions, just pr.

## Reason of Carrying Prism to Fabric

Friends from NUAA seted up a technology survival server with Fabric and Carpet mod. A logging mod on Fabric is needed
but cannot be found. So we can only write a record module ourselves.

I planned to write it from scratch, but unfortunately I don't know anything about highly concurrent database operations.
When I was troubled by this, I found the open source Prism in the Discord server(M.O.S.S.) of EssentialsX. So I decided
to adapt it to Fabric.

## Plan of the AntiGenius

> **The project is still developing.**

1. Log all events
   in [Prism-Bukkit Actions List](https://prism-bukkit.readthedocs.io/en/latest/commands/parameters.html#actions-list)
   to database.
2. Query data in database.
3. Rollback/Restore and their preview.
4. Other SQL database support(sqlite, or other).

## About Prism

* ### Prism version 1.x and 2.x

The early versions Prism work on Bukkit API in the Cauldron ages. The main contributor was **nasonfish**.
> I couldn't found more information about Prism 1&2 besides the [README.md]( https://github.com/prism/Prism#prism-1x---2x ) from repositories of Prism-Sponge(or Prism 3).
>
> Welcome PRs if you know something more about Prism 1&2.

* ### [Prism-Sponge (or Prism 3)]( https://github.com/prism/Prism )

In 2016, Prism-Bukkit was abandoned in favor of the Sponge rewrite by **viveleroi**.

* ### [Prism-Bukkit]( https://github.com/AddstarMC/Prism-Bukkit )

With various fixes and feature additions by a few GitHub contributors, a continuation of @viveleroi's work, now
maintained by [AddstarMC]( https://github.com/AddstarMC ).

Prism-Bukkit only support for Spigot/Paper 1.15+.