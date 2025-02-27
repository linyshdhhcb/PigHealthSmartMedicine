# 面向生猪健康管理的智慧医药系统

## 项目简介
本项目是一个面向生猪健康管理的智慧医药系统，有AI兽医功能、文章管理功能、药物管理功能、政策资讯管理功能，旨在通过数字化手段提升生猪养殖的医疗管理水平。系统集成 **SpringBoot3 + Ollama + Spring AI + DeepSeek + Mysql8.0 + Mybatis-Plus**，提供智能兽医诊断服务，帮助养殖户或兽医快速诊断生猪病情并推荐相应的治疗方案。

## 技术栈
- **后端**：Java、SpringBoot、Spring AI、SaToken
- **AI 模型**：Ollama + DeepSeek 
- **数据库**：MySQL
- **缓存**：Redis
- **API 文档**：knife4j
## 

| 依赖项                         | 版本          | 描述                |
| ------------------------------ | ------------- | ------------------- |
| JDK                            | 21            |                     |
| SpringBoot                     | 3.3.5         | 框架                |
| Mysql                          | 8.0.33        | 数据库连接器        |
| Druid                          | 1.2.24        | 数据库连接池        |
| MyBatis Plus                   | 3.5.8     | ORM 框架            |
| Hutool                         | 5.7.17        | 工具类库            |
| Lombok                         | 1.18.36       | 简化代码的库        |
| OkHttp                         | 4.9.3         | HTTP 客户端         |
| Minio                          | 8.5.14        | 对象存储客户端      |
| Spring Security Crypto         | 5.3.8.RELEASE | 安全加密库          |
| Sa-Token Redis                 | 1.40.0        | Sa-Token 整合 Redis |
| Sa-Token Spring Boot Starter   | 1.39.0        | Sa-Token 权限认证   |
| Sa-Token Core                  | 1.39.0        | Sa-Token 核心库     |
| Knife4j                        | 4.4.0         | API 文档生成工具    |
| Spring Boot Starter Data Redis | 3.1.0         | Redis 支持          |
| Spring Boot Starter Mail       |               | 邮件服务            |
| Apache HttpClient              | 4.5.13        | HTTP 客户端         |
| FastJson                       | 2.0.54        | JSON 解析库         |
- **前端**：开发中。。。
## 核心功能
### 1. **用户管理**
- 用户注册、登录
- 养殖户 / 兽医 / 管理员身份权限区分
- 账号密码登录/账号邮箱验证码登录

### 2. **智能兽医诊断**
- 使用 **Ollama + DeepSeek** 实现 AI 兽医问诊
- 通过自然语言描述病症，AI 推荐可能的疾病并提供治疗建议
- 查询疾病的相关药品信息

### 3. **疾病管理**
- 养殖户可查询疾病信息、症状、对应药物
- 疾病分类管理（疾病种类）
- 疾病与药物的关联管理

### 4. **药品管理**
- 药品信息录入、查询、修改、删除
- 养殖户可查询药品使用方法、适应症、注意事项

### 5. **文章与资讯**
- 文章分类管理（文章类型）
- 养殖知识、医疗知识文章发布
- 新闻资讯管理

### 6. **系统文件与反馈**
- 用户反馈（feedback）
- 文件管理（files）

## 安装与部署
### 1. **环境要求**
- **JDK 17+**
- **MySQL 8+**
- **Redis 6+**
- **Docker（可选，用于部署 AI 大模型）**
- **Maven 3.8+**

### 2. **数据库初始化**
数据库建表的SQL文件在项目的SQL文件夹
```sql
CREATE DATABASE pig_health_smart_medicine;
```
然后在 `application.yml` 配置数据库连接信息,根据图片的红标修改自己的

![mysql](img\mysql.png)

配置邮箱：

![mail](img\mail.png)

配置Redis：

![redis](img\redis.png)

配置Minio

![minio](img\minio.png)

配置Ollama：

![ollama](img\ollama.png)

### 3. **启动后端服务**

```sh
git clone https://gitee.com/hsdchb/pig-health-smart-medicine.git
cd pig-health-smart-medicine
mvn clean package
java -jar target/pig-health-smart-medicine.jar
```
## 测试账号
```
管理员：username: admin | password: 123123
普通用户：username: linyi | password: 123123
```

## 数据库设计
### 主要数据表
| 表名 | 说明 |
|------|------|
| `user` | 用户表，存储用户信息 |
| `article_types` | 文章分类表 |
| `articles` | 文章表，存储养殖知识文章 |
| `conversation` | AI 兽医问诊记录表 |
| `feedback` | 用户反馈表 |
| `files` | 文件存储信息表 |
| `history` | 操作日志记录表 |
| `illness` | 疾病表 |
| `illness_kind` | 疾病种类表 |
| `illness_medicine` | 疾病-药品关联表 |
| `medicine` | 药品信息表 |
| `news_articles` | 新闻资讯表 |
| `pageview` | 浏览量统计表 |

## 数据库：pig_health_smart_medicine

## article_types[文章类型]


| 序号 | 字段名      | 类型        | 长度 | 是否为空 | 默认值            | 小数位 | 注释         |
| :--: | ----------- | ----------- | ---- | -------- | ----------------- | ------ | ------------ |
|  1   | type_id     | int         | --   | NO       | --                | 0      | 文章类型ID   |
|  2   | type_name   | varchar(20) | 20   | NO       | --                | --     | 文章类型名称 |
|  3   | create_time | datetime    | --   | YES      | CURRENT_TIMESTAMP | --     | 创建时间     |
|  4   | update_time | datetime    | --   | YES      | CURRENT_TIMESTAMP | --     | 更新时间     |


## articles[文章]


| 序号 | 字段名      | 类型         | 长度  | 是否为空 | 默认值            | 小数位 | 注释                                |
| :--: | ----------- | ------------ | ----- | -------- | ----------------- | ------ | ----------------------------------- |
|  1   | id          | int          | --    | NO       | --                | 0      | 文章ID                              |
|  2   | title       | varchar(255) | 255   | NO       | --                | --     | 文章标题                            |
|  3   | content     | text         | 65535 | NO       | --                | --     | 文章内容                            |
|  4   | author      | varchar(100) | 100   | YES      | --                | --     | 作者                                |
|  5   | create_time | datetime     | --    | YES      | CURRENT_TIMESTAMP | --     | 创建时间                            |
|  6   | update_time | datetime     | --    | YES      | CURRENT_TIMESTAMP | --     | 更新时间                            |
|  7   | type_id     | int          | --    | YES      | --                | 0      | 文章类型ID，外键关联article_types表 |


## conversation[对话]


| 序号 | 字段名            | 类型          | 长度  | 是否为空 | 默认值            | 小数位 | 注释             |
| :--: | ----------------- | ------------- | ----- | -------- | ----------------- | ------ | ---------------- |
|  1   | id                | bigint        | --    | NO       | --                | 0      | 主键ID           |
|  2   | user_id           | int           | --    | NO       | --                | 0      | 用户ID           |
|  3   | user_input        | text          | 65535 | NO       | --                | --     | 用户输入         |
|  4   | ai_response       | text          | 65535 | NO       | --                | --     | AI回复           |
|  5   | conversation_time | datetime      | --    | NO       | CURRENT_TIMESTAMP | --     | 对话时间         |
|  6   | model_name        | varchar(255)  | 255   | YES      | --                | --     | AI模型名称       |
|  7   | response_time     | decimal(10,2) | --    | YES      | --                | 2      | AI响应时间（秒） |


## feedback[反馈]


| 序号 | 字段名      | 类型         | 长度  | 是否为空 | 默认值            | 小数位 | 注释     |
| :--: | ----------- | ------------ | ----- | -------- | ----------------- | ------ | -------- |
|  1   | id          | int          | --    | NO       | --                | 0      | 主键ID   |
|  2   | name        | varchar(11)  | 11    | YES      | --                | --     | 反馈用户 |
|  3   | email       | varchar(255) | 255   | YES      | --                | --     | 邮箱地址 |
|  4   | title       | varchar(255) | 255   | YES      | --                | --     | 反馈标题 |
|  5   | content     | text         | 65535 | YES      | --                | --     | 反馈内容 |
|  6   | create_time | datetime     | --    | YES      | CURRENT_TIMESTAMP | --     | 创建时间 |
|  7   | update_time | datetime     | --    | YES      | CURRENT_TIMESTAMP | --     | 更新时间 |


## files[文件信息]


| 序号 | 字段名       | 类型         | 长度 | 是否为空 | 默认值            | 小数位 | 注释                    |
| :--: | ------------ | ------------ | ---- | -------- | ----------------- | ------ | ----------------------- |
|  1   | id           | int          | --   | NO       | --                | 0      | 主键id                  |
|  2   | file_name    | varchar(255) | 255  | NO       | --                | --     | 文件名                  |
|  3   | file_path    | varchar(255) | 255  | NO       | --                | --     | 文件在 MinIO 中的路径   |
|  4   | file_size    | bigint       | --   | NO       | --                | 0      | 文件大小，单位为字节    |
|  5   | content_type | varchar(100) | 100  | YES      | --                | --     | 文件的 MIME 类型        |
|  6   | url          | varchar(500) | 500  | NO       | --                | --     | 文件的url               |
|  7   | upload_time  | timestamp    | --   | YES      | CURRENT_TIMESTAMP | --     | 文件上传时间            |
|  8   | bucket_name  | varchar(100) | 100  | NO       | --                | --     | 文件存储的 MinIO 桶名称 |


## history[操作记录]


| 序号 | 字段名       | 类型         | 长度 | 是否为空 | 默认值            | 小数位 | 注释                      |
| :--: | ------------ | ------------ | ---- | -------- | ----------------- | ------ | ------------------------- |
|  1   | id           | int          | --   | NO       | --                | 0      | 用户搜索历史主键id        |
|  2   | user_id      | int          | --   | YES      | --                | 0      | 用户ID                    |
|  3   | keyword      | varchar(255) | 255  | YES      | --                | --     | 搜索关键字                |
|  4   | operate_type | int          | --   | YES      | --                | 0      | 类型：1搜索，2科目，3药品 |
|  5   | create_time  | datetime     | --   | YES      | CURRENT_TIMESTAMP | --     | 创建时间                  |
|  6   | update_time  | datetime     | --   | YES      | CURRENT_TIMESTAMP | --     | 更新时间                  |


## illness[疾病]


| 序号 | 字段名          | 类型         | 长度     | 是否为空 | 默认值            | 小数位 | 注释       |
| :--: | --------------- | ------------ | -------- | -------- | ----------------- | ------ | ---------- |
|  1   | id              | int          | --       | NO       | --                | 0      | 疾病id     |
|  2   | kind_id         | int          | --       | YES      | --                | 0      | 疾病分类ID |
|  3   | illness_name    | varchar(100) | 100      | YES      | --                | --     | 疾病名字   |
|  4   | include_reason  | mediumtext   | 16777215 | YES      | --                | --     | 诱发因素   |
|  5   | illness_symptom | mediumtext   | 16777215 | YES      | --                | --     | 疾病症状   |
|  6   | special_symptom | mediumtext   | 16777215 | YES      | --                | --     | 特殊症状   |
|  7   | create_time     | datetime     | --       | YES      | CURRENT_TIMESTAMP | --     | 创建时间   |
|  8   | update_time     | datetime     | --       | YES      | CURRENT_TIMESTAMP | --     | 更新时间   |


## illness_kind[疾病种类]


| 序号 | 字段名      | 类型         | 长度 | 是否为空 | 默认值            | 小数位 | 注释     |
| :--: | ----------- | ------------ | ---- | -------- | ----------------- | ------ | -------- |
|  1   | id          | int          | --   | NO       | --                | 0      | 主键ID   |
|  2   | name        | varchar(255) | 255  | YES      | --                | --     | 分类名称 |
|  3   | info        | varchar(255) | 255  | YES      | --                | --     | 描述     |
|  4   | create_time | datetime     | --   | YES      | CURRENT_TIMESTAMP | --     | 创建时间 |
|  5   | update_time | datetime     | --   | YES      | CURRENT_TIMESTAMP | --     | 更新时间 |


## illness_medicine[疾病-药物]


| 序号 | 字段名      | 类型     | 长度 | 是否为空 | 默认值            | 小数位 | 注释           |
| :--: | ----------- | -------- | ---- | -------- | ----------------- | ------ | -------------- |
|  1   | id          | int      | --   | NO       | --                | 0      | 病和药品关联id |
|  2   | illness_id  | int      | --   | YES      | --                | 0      | 病id           |
|  3   | medicine_id | int      | --   | YES      | --                | 0      | 药品id         |
|  4   | create_time | datetime | --   | YES      | CURRENT_TIMESTAMP | --     | 创建时间       |
|  5   | update_time | datetime | --   | YES      | CURRENT_TIMESTAMP | --     | 更新时间       |


## medicine[药品]


| 序号 | 字段名          | 类型          | 长度     | 是否为空 | 默认值            | 小数位 | 注释                            |
| :--: | --------------- | ------------- | -------- | -------- | ----------------- | ------ | ------------------------------- |
|  1   | id              | int           | --       | NO       | --                | 0      | 药品主键ID                      |
|  2   | medicine_name   | varchar(100)  | 100      | YES      | --                | --     | 药的名字                        |
|  3   | keyword         | varchar(255)  | 255      | YES      | --                | --     | 关键字搜索                      |
|  4   | medicine_effect | mediumtext    | 16777215 | YES      | --                | --     | 药的功效                        |
|  5   | medicine_brand  | varchar(255)  | 255      | YES      | --                | --     | 药的品牌                        |
|  6   | interaction     | mediumtext    | 16777215 | YES      | --                | --     | 药的相互作用                    |
|  7   | taboo           | mediumtext    | 16777215 | YES      | --                | --     | 禁忌                            |
|  8   | us_age          | mediumtext    | 16777215 | YES      | --                | --     | 用法用量                        |
|  9   | medicine_type   | int           | --       | YES      | --                | 0      | 药的类型，0西药，1中药，2中成药 |
|  10  | img_path        | varchar(255)  | 255      | YES      | --                | --     | 相关图片路径                    |
|  11  | medicine_price  | decimal(10,2) | --       | YES      | --                | 2      | 药的价格                        |
|  12  | create_time     | datetime      | --       | YES      | CURRENT_TIMESTAMP | --     | 创建时间                        |
|  13  | update_time     | datetime      | --       | YES      | CURRENT_TIMESTAMP | --     | 更新时间                        |


## news_articles[新闻资讯]


| 序号 | 字段名       | 类型         | 长度  | 是否为空 | 默认值            | 小数位 | 注释                     |
| :--: | ------------ | ------------ | ----- | -------- | ----------------- | ------ | ------------------------ |
|  1   | id           | int          | --    | NO       | --                | 0      | 新闻ID                   |
|  2   | url          | varchar(500) | 500   | YES      | --                | --     | 转载url                  |
|  3   | title        | varchar(255) | 255   | NO       | --                | --     | 新闻标题                 |
|  4   | content      | text         | 65535 | NO       | --                | --     | 新闻内容                 |
|  5   | author       | varchar(100) | 100   | YES      | --                | --     | 作者                     |
|  6   | publish_time | timestamp    | --    | YES      | CURRENT_TIMESTAMP | --     | 发布时间，默认为当前时间 |
|  7   | source       | varchar(255) | 255   | YES      | --                | --     | 新闻来源                 |
|  8   | summary      | text         | 65535 | YES      | --                | --     | 新闻摘要                 |
|  9   | create_time  | datetime     | --    | YES      | CURRENT_TIMESTAMP | --     | 创建时间                 |
|  10  | update_time  | datetime     | --    | YES      | CURRENT_TIMESTAMP | --     | 更新时间                 |


## pageview[浏览量]


| 序号 | 字段名     | 类型 | 长度 | 是否为空 | 默认值 | 小数位 | 注释   |
| :--: | ---------- | ---- | ---- | -------- | ------ | ------ | ------ |
|  1   | id         | int  | --   | NO       | --     | 0      | 主键id |
|  2   | pageviews  | int  | --   | YES      | --     | 0      | 浏览量 |
|  3   | illness_id | int  | --   | YES      | --     | 0      | 病的id |


## user[用户]


| 序号 | 字段名       | 类型         | 长度 | 是否为空 | 默认值            | 小数位 | 注释                         |
| :--: | ------------ | ------------ | ---- | -------- | ----------------- | ------ | ---------------------------- |
|  1   | id           | int          | --   | NO       | --                | 0      | 用户主键id                   |
|  2   | user_account | varchar(255) | 255  | YES      | --                | --     | 用户账号                     |
|  3   | user_name    | varchar(255) | 255  | YES      | --                | --     | 用户的真实名字               |
|  4   | user_pwd     | varchar(255) | 255  | YES      | --                | --     | 用户密码                     |
|  5   | user_age     | int          | --   | YES      | --                | 0      | 用户年龄                     |
|  6   | user_sex     | varchar(1)   | 1    | YES      | --                | --     | 用户性别                     |
|  7   | user_email   | varchar(255) | 255  | YES      | --                | --     | 用户邮箱                     |
|  8   | user_tel     | varchar(50)  | 50   | YES      | --                | --     | 手机号                       |
|  9   | role_status  | int          | --   | YES      | --                | 0      | 角色状态，1管理员，0普通用户 |
|  10  | img_path     | varchar(255) | 255  | YES      | --                | --     | 用户头像                     |
|  11  | create_time  | datetime     | --   | YES      | CURRENT_TIMESTAMP | --     | 创建时间                     |
|  12  | update_time  | datetime     | --   | YES      | CURRENT_TIMESTAMP | --     | 更新时间                     |

## API 文档
启动后访问 `http://localhost:9999/doc.html` 查看完整 API 文档。

## 未来计划
- **优化 AI 兽医问诊模型，提高准确率**
- **增加微信小程序端支持**
- **提供疾病流行趋势预测功能**
- **兽医线上问诊**

## 贡献指南
欢迎提交 PR 或 Issue 来优化本项目。

## 许可证
本项目采用 MIT 许可证。

---
如有问题，可以有些邮箱联系我，也可以进行交流，项目不足之处，还请多多担待。
> **作者**: linyi
> **邮箱**: jingshuihuayue@qq.com  
> **GitHub**: [PigHealthSmartMedicine](https://github.com/linyshdhhcb/PigHealthSmartMedicine.git)

