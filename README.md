# PigHealthSmartMedicine

基于 Spring Boot 和 React 的智能养猪健康管理与医疗系统，集成 RAG（检索增强生成）、AI 大模型、向量数据库等先进技术，提供智能化的养殖管理、疾病诊断、医疗记录等功能。

## 项目简介

PigHealthSmartMedicine 是一个综合性的智能养猪管理系统，结合了传统养殖管理与现代人工智能技术。系统采用前后端分离架构，后端基于 Spring Boot 3.x，前端使用 React + TypeScript，集成了 Milvus 向量数据库、多种 AI 大模型（通义千问、GLM、Ollama 等），实现了智能问答、知识库管理、会话记忆、深度思考等先进功能。

## 技术栈

### 后端技术

- **核心框架**: Spring Boot 3.5.7
- **JDK 版本**: Java 17
- **持久层**: MyBatis-Plus 3.5.14
- **数据库**: MySQL
- **缓存**: Redis
- **向量数据库**: Milvus 2.6.6
- **对象存储**: RustFS (S3 兼容)
- **权限认证**: Sa-Token 1.43.0
- **工具库**: Hutool 5.8.37, Lombok
- **文档处理**: Apache Tika 3.2.3
- **分布式 ID**: 雪花算法
- **代码格式化**: Spotless Maven Plugin

### AI 能力

- **大语言模型**: 
  - 通义千问 (Qwen-plus, Qwen3-max)
  - GLM-4.7 (SiliconFlow)
  - Ollama 本地模型 (Qwen2.5:7b)
- **嵌入模型**: 
  - Qwen3-Embedding-8B (4096 维度)
  - Nomic-embed-text (768 维度)
- **重排序模型**: Qwen3-rerank
- **多模型路由**: 支持优先级配置和自动切换

### 前端技术

- **核心框架**: React 18.3.1 + TypeScript
- **构建工具**: Vite 5.4.3
- **UI 组件**: Radix UI, Tailwind CSS
- **状态管理**: Zustand
- **路由**: React Router DOM 6.26.2
- **HTTP 客户端**: Axios
- **表单验证**: React Hook Form + Zod
- **Markdown 渲染**: React Markdown
- **图表**: Recharts

## 项目结构

```
PigHealthSmartMedicine/
├── bootstrap/              # 主应用模块（Spring Boot 启动模块）
│   ├── src/main/
│   │   ├── java/          # Java 源代码
│   │   └── resources/     # 配置文件
│   └── pom.xml
├── framework/              # 基础框架模块
│   ├── src/main/
│   │   ├── java/          # 核心框架代码
│   │   │   ├── config/    # 配置类
│   │   │   ├── context/   # 上下文管理
│   │   │   ├── database/  # 数据库相关
│   │   │   ├── exception/ # 异常处理
│   │   │   ├── idempotent/# 幂等性处理
│   │   │   └── web/       # Web 相关
│   │   └── resources/     # 资源文件
│   └── pom.xml
├── infra-ai/               # AI 基础设施模块
│   ├── src/main/java/
│   │   ├── chat/          # 聊天客户端
│   │   ├── embedding/     # 嵌入服务
│   │   ├── rerank/        # 重排序服务
│   │   └── model/         # AI 模型相关
│   └── pom.xml
├── mcp-server/             # MCP 服务器模块
│   └── pom.xml
├── frontend/               # 前端项目
│   ├── src/
│   │   ├── components/    # React 组件
│   │   ├── pages/         # 页面组件
│   │   ├── services/      # API 服务
│   │   ├── stores/        # 状态管理
│   │   └── hooks/         # 自定义 Hooks
│   └── package.json
└── pom.xml                 # 父 POM
```

## 核心功能

### 业务功能

- **猪场管理**: 猪场信息管理、存栏管理
- **生猪管理**: 生猪档案、生长监控
- **疾病诊疗**: 智能疾病诊断、治疗建议
- **医疗记录**: 治疗记录管理、病历档案
- **药品管理**: 药品库存、用药指导
- **健康监测**: 实时健康监控、预警通知
- **知识库**: 养殖知识、疾病防治文章

### AI 智能功能

- **智能问答**: 基于 RAG 的专业问答系统
- **语义搜索**: 向量检索 + 意图识别
- **会话管理**: 多轮对话、会话摘要、长期记忆
- **查询重写**: 智能查询优化和历史上下文理解
- **深度思考**: 支持复杂问题的逐步推理
- **知识入库**: 自动从对话中提取知识
- **限流保护**: 并发控制和请求限流

## 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 6.0+
- Milvus 2.4+
- Ollama（可选，用于本地 AI 模型）

### 后端启动

1. 克隆项目
```bash
git clone <repository-url>
cd PigHealthSmartMedicine
```

2. 导入 SQL 脚本
```bash
mysql -u root -p < PigHealthSmartMedicine.sql
```

3. 修改配置文件 `bootstrap/src/main/resources/application.yaml`
   - 数据库连接信息
   - Redis 连接信息
   - Milvus 地址
   - AI 模型配置

4. Maven 构建并启动
```bash
mvn clean install
cd bootstrap
mvn spring-boot:run
```

后端服务默认运行在：`http://localhost:8082/api/ragent`

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务默认运行在：`http://localhost:5173`

## 配置说明

### AI 模型配置

在 `application.yaml` 中配置 AI 提供商和模型：

```yaml
ai:
  providers:
    ollama:
      url: http://127.0.0.1:11434
      
  chat:
    default-model: qwen2.5:7b
    candidates:
      - id: qwen-plus
        provider: bailian
        model: qwen-plus-latest
        priority: 1
```

### RAG 配置

```yaml
rag:
  default:
    collection-name: rag_default_store
    dimension: 4096
    metric-type: COSINE
  
  query-rewrite:
    enabled: true
    max-history-messages: 4
  
  memory:
    history-keep-turns: 4
    summary-enabled: true
```

## API 接口

### 主要接口前缀

- 认证相关：`/api/ragent/auth/**`
- 用户管理：`/api/ragent/user/**`
- 聊天会话：`/api/ragent/chat/**`
- 知识库：`/api/ragent/knowledge/**`
- 文档上传：`/api/ragent/ingestion/**`
- 意图管理：`/api/ragent/intent/**`
- 样本问题：`/api/ragent/sample-question/**`
- 猪场管理：`/api/ragent/farm/**`
- 生猪管理：`/api/ragent/pig/**`
- 医疗记录：`/api/ragent/treatment-record/**`
- 药品管理：`/api/ragent/drug/**`
- 健康管理：`/api/ragent/health-monitor/**`
- 案例管理：`/api/ragent/case/**`
- 文章管理：`/api/ragent/article/**`

## 开发指南

### 代码规范

项目使用 Spotless Maven 插件进行代码格式化：

```bash
mvn spotless:apply
```

### 添加新的 AI 模型

1. 在 `infra-ai` 模块中添加新的客户端实现
2. 在 `application.yaml` 中配置新模型
3. 实现模型路由逻辑

### 扩展业务功能

1. 在 `bootstrap` 模块中添加新的 Controller
2. 创建对应的 Service 和 Mapper
3. 更新数据库表结构
4. 在前端添加对应的页面和组件

## 系统架构

```
┌─────────────┐         ┌──────────────┐
│   Frontend  │◄───────►│   Backend    │
│  (React TS) │  REST   │ (Spring Boot)│
└─────────────┘         └──────┬───────┘
                               │
                ┌──────────────┼──────────────┐
                │              │              │
           ┌────▼────┐   ┌────▼────┐   ┌────▼────┐
           │  MySQL  │   │  Redis  │   │ Milvus  │
           │         │   │         │   │ (Vector)│
           └─────────┘   └─────────┘   └─────────┘
                                               │
                                    ┌──────────▼──────────┐
                                    │  AI Model Providers │
                                    │  - Bailian          │
                                    │  - SiliconFlow      │
                                    │  - Ollama (Local)   │
                                    └─────────────────────┘
```

## 注意事项

1. 首次启动需要初始化数据库，执行 `PigHealthSmartMedicine.sql` 脚本
2. Milvus 需要提前部署并创建对应的 Collection
3. AI 模型需要配置有效的 API Key 或本地模型服务
4. Redis 用于缓存和会话管理，确保持久化配置正确
5. 文件上传功能依赖 RustFS 对象存储，需要正确配置访问凭证

## 许可证

本项目采用开源许可证，具体请参考 LICENSE 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件至项目维护者

## 致谢

感谢以下开源项目：
- Spring Boot
- React
- Milvus
- MyBatis-Plus
- Sa-Token
- Hutool
- Radix UI
- Tailwind CSS
