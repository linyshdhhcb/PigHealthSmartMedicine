# Pig Health Smart Medicine - RAG System

基于检索增强生成（RAG）的智能问答系统，面向生猪健康管理场景，集成多知识库、文档解析、向量检索与大模型对话；同时提供医疗内容（文章、资讯、疾病、药品等）的管理与用户端浏览能力。

## 项目简介

本系统在企业级 RAG 能力之上，扩展了**医疗内容数据管理**与**用户门户**：管理员可在后台维护文章类型、文章、新闻、疾病分类、疾病、药品及关联关系等；普通用户登录后可进入门户主页浏览列表与详情，或通过 AI 对话页基于知识库进行问答。

## 功能模块概览

| 模块 | 说明 | 主要入口（前端） |
|------|------|------------------|
| AI 对话 | 多轮会话、流式输出、会话列表与新建对话 | `/chat`、`/chat/:sessionId` |
| 用户门户 | 主页导航、文章/资讯/疾病/药品列表与详情 | `/app/home`、`/app/articles` 等 |
| 管理后台 | 知识库、意图、摄入、追踪、系统设置及医疗内容 CRUD | `/admin`（需管理员角色） |
| 用户反馈 | 提交反馈（按需启用路由） | `/feedback` |

门户顶部导航顺序：**主页、文章、资讯、疾病、药品、AI**（其中 AI 跳转至 `/chat`）。侧栏在「新建对话」下方提供「返回主页」，跳转 `/app/home`。

## 核心特性

### 知识库与检索

- 多知识库隔离；支持 PDF、Word、Excel、PPT、Markdown、TXT 等格式解析
- 文档分块、向量化与定时刷新远程文档
- 意图识别与多路召回（意图定向 + 全局检索）
- 向量检索：支持 PostgreSQL pgvector 与 Milvus
- 结果重排序（Cross-Encoder 等）

### 对话引擎

- 查询改写与多轮上下文
- 会话记忆与历史摘要
- 流式响应与深度思考模式

### 医疗内容（业务扩展）

- 文章类型、文章、新闻资讯的维护与检索
- 疾病种类、疾病条目（含富文本/HTML 字段）
- 药品库与疾病-药品关联
- 用户反馈、操作记录、浏览统计（管理端）

### 系统架构

- 前后端分离：React 18 + Spring Boot 3.x
- 消息队列：RocketMQ 异步处理
- 对象存储：RustFS（S3 兼容）
- 缓存：Redis
- 数据库：PostgreSQL（含 pgvector）

## 技术栈

### 后端

- Java 17
- Spring Boot 3.5.x
- MyBatis-Plus
- Apache Tika（文档解析）
- RocketMQ、PostgreSQL + pgvector、Redis、RustFS（或兼容 S3 的存储）
- 认证：Sa-Token（具体以代码配置为准）

### 前端

- React 18、TypeScript、Vite
- Tailwind CSS、Radix / Shadcn 风格组件
- React Router、Zustand、Axios

### AI 服务

- 对话与嵌入：阿里云百炼、硅基流动、Ollama 等（以 `application.yaml` 配置为准）
- 嵌入与 Rerank 模型按项目内配置选用

## 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- PostgreSQL 14+（含 pgvector）
- Redis 6+
- RocketMQ 5.x（按实际部署）
- 对象存储（RustFS / MinIO 等，按配置）

> 说明：若本仓库未附带 `docker-compose` 文件，请按上述组件自行部署或使用团队统一的基础设施；**请勿假设**存在固定的 Compose 路径。

### 后端

1. 在 `backend/phsm-rag-bootstrap` 下配置数据库、Redis、MQ、存储与 AI Key（可通过 `application.yaml` 与环境变量、`spring-dotenv` 加载的 `.env` 等，以项目实际为准）。

2. 编译并运行（示例）：

```bash
cd backend
mvn clean package -DskipTests
java -jar phsm-rag-bootstrap/target/phsm-rag-bootstrap-0.0.1-SNAPSHOT.jar
```

或使用 Spring Boot 插件：

```bash
cd backend
mvn spring-boot:run -pl phsm-rag-bootstrap
```

默认端口以 `application.yaml` 为准（常见为 `8080`）。

### 前端

1. 安装依赖：

```bash
cd frontend
npm install
```

2. 配置 API 基地址。项目使用 `VITE_API_BASE_URL`（示例见 `frontend/.env`）：

```env
VITE_API_BASE_URL=/api/ragent
```

开发环境下通常需在 `vite.config` 中配置代理，将 `/api` 转发到后端，避免跨域。

3. 启动：

```bash
npm run dev
```

4. 浏览器访问开发地址（默认 `http://localhost:5173`）。

### 登录与角色

- 登录页：`/login`
- 登录成功后默认进入 `/chat`（以当前路由逻辑为准）
- 管理员可访问 `/admin`；非管理员访问管理后台会被重定向到 `/chat`

## 前端路由说明

| 路径 | 说明 |
|------|------|
| `/` | 已登录跳转 `/chat`，否则 `/login` |
| `/login` | 登录 |
| `/chat`、`/chat/:sessionId` | AI 对话 |
| `/app` | 重定向到 `/app/home` |
| `/app/home` | 门户主页 |
| `/app/articles`、`/app/articles/:id` | 文章列表/详情 |
| `/app/news`、`/app/news/:id` | 资讯列表/详情 |
| `/app/illnesses`、`/app/illnesses/:id` | 疾病库列表/详情 |
| `/app/medicines`、`/app/medicines/:id` | 药品库列表/详情 |
| `/app/ai` | 重定向到 `/chat` |
| `/admin/*` | 管理后台（子路由见 `frontend/src/router.tsx`） |
| `/feedback` | 用户反馈提交 |

## 配置说明（摘要）

### AI 与 RAG

在 `backend/phsm-rag-bootstrap/src/main/resources/application.yaml` 中配置提供商、默认模型、嵌入维度、向量库类型（`pg` / `milvus`）及检索通道阈值等。以下为结构示例，**实际键名与默认值以仓库内文件为准**：

```yaml
ai:
  providers:
    bailian:
      url: https://dashscope.aliyuncs.com
      api-key: ${BAILIAN_API_KEY}
    siliconflow:
      url: https://api.siliconflow.cn
      api-key: ${SILICONFLOW_API_KEY}
    ollama:
      url: http://localhost:11434
  chat:
    default-model: qwen3-max
  embedding:
    default-model: qwen-emb-8b

rag:
  vector:
    type: pg
  search:
    channels:
      vector-global:
        confidence-threshold: 0.6
      intent-directed:
        min-intent-score: 0.4
```

### 环境变量示例（后端）

不同环境可能使用 `.env` 或部署平台变量，常见项包括：`DB_*`、`REDIS_*`、`ROCKETMQ_*`、对象存储访问密钥、各 AI `*_API_KEY` 等。请以 `application.yaml` 与基础设施文档为准进行填写。

## 使用指南

### 管理后台：知识库与文档

1. 使用管理员账号登录，进入 `/admin`
2. 在「知识库管理」中新建知识库并选择嵌入模型
3. 进入知识库上传文档或配置远程地址，等待解析与向量化完成

### 管理后台：医疗内容

在侧栏「医疗内容」或对应菜单中维护：

- 文章类型、文章、新闻资讯
- 疾病种类、疾病、药品、疾病-药品关联
- 用户反馈、操作记录、浏览统计

列表页支持检索、分页与详情查看（具体以界面为准）。

### 用户门户

1. 登录后访问 `/app/home`
2. 通过导航进入文章、资讯、疾病、药品等列表与详情
3. 点击「AI」或直接进入 `/chat` 进行 RAG 对话；侧栏「返回主页」可回到 `/app/home`

### 发起 AI 对话（RAG）

1. 进入 `/chat`
2. 在界面中选择知识库（若产品流程要求）
3. 输入问题，查看流式回答与引用；可开启深度思考等选项（以 UI 为准）

## 数据库与表结构

### RAG 核心表（示例）

- `t_knowledge_base`：知识库
- `t_knowledge_document`：文档
- `t_knowledge_chunk`：文本块
- `t_knowledge_vector`：向量（pgvector）
- 以及定时任务、分块日志等相关表

### 医疗业务表

医疗内容相关表名与字段以项目内 SQL 脚本及 MyBatis-Plus 实体为准（如 `pig.sql` 或迁移脚本）；与文章、资讯、疾病、药品、反馈、历史、浏览统计等模块对应。

详细字段定义请参考 `backend` 各模块 `domain` / `entity` 与 Mapper。

## API 说明

- 前端 Axios `baseURL` 一般为 `VITE_API_BASE_URL`（如 `/api/ragent`）
- 后端 REST 路径前缀与统一响应包装以 `rest-api` 模块及拦截器为准
- 典型能力：知识库与文档、对话流式接口、医疗内容 CRUD 等

完整接口列表建议使用 **Swagger / OpenAPI** 或团队维护的 **Apifox** 项目查看。

## 项目结构

```
phsm-rag/
├── backend/
│   ├── phsm-rag-bootstrap/       # 启动入口、配置、拦截器
│   ├── phsm-rag-application/     # 应用服务（knowledge-app、rag-app、ingestion-app 等）
│   ├── phsm-rag-domain/          # 领域模型
│   ├── phsm-rag-infrastructure/  # 基础设施实现
│   ├── phsm-rag-framework/       # 框架扩展
│   ├── phsm-rag-interfaces/      # REST 等接口层
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/           # 通用组件（含 chat、layout、admin 等）
│   │   ├── pages/
│   │   │   ├── admin/            # 管理后台页面
│   │   │   ├── portal/         # 用户门户（主页、文章/资讯/疾病/药品）
│   │   │   ├── ChatPage.tsx
│   │   │   └── ...
│   │   ├── services/             # API 封装
│   │   ├── stores/               # Zustand 状态
│   │   └── router.tsx            # 路由定义
│   └── package.json
└── README.md
```

## 开发指南

### 扩展文档解析

1. 实现统一的文档解析接口（以代码中的 `DocumentParser` 或同类抽象为准）
2. 注册为 Spring Bean
3. 在解析链路中注册支持的 MIME 类型或扩展名

### 扩展检索通道

1. 实现检索通道接口
2. 注册为 Spring Bean
3. 在配置中启用并调整阈值与 `top-k` 策略

### 前端规范

- 列表与表单尽量复用现有 `services/*` 与 UI 组件
- 门户与管理端样式可参考 `pages/admin/phsm/phsmTheme.ts` 等主题常量（若使用）

## 构建与检查

```bash
# 后端
cd backend && mvn -q -DskipTests package

# 前端
cd frontend && npm run build
cd frontend && npm run lint
```

## 常见问题

### 数据库连接失败

确认 PostgreSQL 已启动、库名与用户权限正确，防火墙与连接串一致。

### Ollama 或本地模型不可用

检查服务地址、模型是否已 `pull`，以及与 `application.yaml` 中名称是否一致。

### RocketMQ 发送超时

检查 NameServer、Broker 地址与网络；查看 Broker 日志与集群状态。

### 向量检索无结果

确认文档向量化成功、嵌入维度与集合一致，查询阈值是否过高。

### 前端接口 404 或跨域

确认 `VITE_API_BASE_URL` 与 Vite 代理目标指向正确后端上下文路径。

## 性能优化建议

1. 调整数据源连接池以匹配并发
2. pgvector 使用合适索引（如 HNSW，视版本与数据量而定）
3. 对热点配置与元数据使用 Redis
4. 耗时任务通过 MQ 异步化
5. 批量写入向量与分块以降低 RT

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件（若仓库根目录提供）。

## 贡献指南

欢迎提交 Issue 与 Pull Request。提交前请：

1. 遵循项目代码风格与 Spotless / ESLint 等检查
2. 为关键逻辑补充或更新测试
3. 更新本 README 或相关说明（若有行为变更）
4. Commit 信息清晰、可溯源

## 联系方式

- GitHub Issues
- Email: jingshuihuayue@qq.com

## 致谢

感谢 Spring Boot、React、Apache Tika、pgvector、RocketMQ、Ollama 等开源项目与本项目依赖生态。
