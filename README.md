# 高校教师成长档案管理系统

**彭伟龙** | Faculty Teacher Development System

## 项目说明

本项目是高校教师成长档案管理系统，原始版本为微信小程序，已转换为 H5 Web 应用部署。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.x + MyBatis-Plus + MySQL 8 |
| 前端（H5） | Vue 2 + Element UI + ECharts |
| 原始前端 | 微信小程序（WXML/WXSS/JS） |
| 部署 | Docker + Nginx + Cloudflare Tunnel |

## 目录结构

```
├── src/                    # H5 Web 前端源码（Vue2）
├── original-backend/       # 原始 Spring Boot 后端源码
├── original-miniapp/       # 原始微信小程序源码
├── sql/                    # 数据库初始化脚本
└── package.json            # 前端依赖配置
```

## 演示地址

https://certified-coupon-perfect-waves.trycloudflare.com

## 登录账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 教师 | zhangwei | 123456 |
| 教师 | lifang | 123456 |

## 功能模块

- 首页概览：档案统计、类型分布图、最近档案列表
- 档案管理：新增/编辑/删除/查看档案，支持筛选和搜索
- 统计分析：年度趋势图、类型分布饼图、教师排行榜
- 个人中心：修改个人信息
