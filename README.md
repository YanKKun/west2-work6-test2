# 存放west2-work6-test2作业的仓库

## 技术栈：

redis，lua

## 项目结构：

myRate-spring-boot-starter
    ├─src
    │  └─main
    │      ├─java
    │      │  └─cn
    │      │      └─yk
    │      │          └─starter
    │      │              ├─config
    │      │              ├─enums
    │      │              └─RateLimit
    │      └─resources
    │          ├─lua
    │          └─META-INF

## 项目功能：

- 使用redis实现了限流注解
- 通过lua脚本保证原子性
- 可自定义时间，时间单位，访问次数
- 可通过IP限流或全局限流