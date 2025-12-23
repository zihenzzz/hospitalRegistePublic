# 医院预约挂号系统 - JDK 17 + Spring AI Alibaba 升级指南

## 🎯 升级概述

本次升级将项目从 **JDK 8 + Spring Boot 2.5** 升级到 **JDK 17 + Spring Boot 3.2**，并集成 **Spring AI Alibaba** 框架。

## ✅ 已完成的升级内容

### 1. 核心框架升级
- ✅ Java 版本：1.8 → 17
- ✅ Spring Boot：2.5.14 → 3.2.0
- ✅ Maven 编译器插件：3.1 → 3.11.0
- ✅ Druid：1.2.15 → 1.2.20
- ✅ FastJSON：2.0.20 → 2.0.43
- ✅ MyBatis PageHelper：1.4.6 → 2.1.0
- ✅ Commons IO：2.11.0 → 2.15.1
- ✅ POI：4.1.2 → 5.2.5
- ✅ JWT：0.9.1 → 0.12.3
- ✅ OSHI：6.4.0 → 6.4.10

### 2. API文档框架替换
- ❌ 移除：Swagger 3 (springfox-boot-starter)
- ✅ 新增：SpringDoc OpenAPI 2.3.0

**原因**：Swagger 3不支持Spring Boot 3，SpringDoc是官方推荐替代方案

### 3. 数据库驱动升级
- ❌ 旧驱动：mysql-connector-java
- ✅ 新驱动：mysql-connector-j

### 4. 新增 Spring AI Alibaba
- ✅ Spring AI Alibaba BOM：1.0.0-M3.2
- ✅ Spring AI Alibaba Starter
- ✅ 通义千问（DashScope）集成
- ✅ AI智能对话控制器
- ✅ 医疗咨询助手功能

## 🚀 安装 JDK 17

### Windows 系统

#### 方式1：使用 Microsoft OpenJDK（推荐）
```bash
# 1. 下载 Microsoft OpenJDK 17
访问：https://learn.microsoft.com/zh-cn/java/openjdk/download

# 2. 选择 Windows x64 MSI 安装包下载

# 3. 双击安装包，按提示安装

# 4. 验证安装
java -version
# 应该显示：openjdk version "17.x.x"
```

#### 方式2：使用 Eclipse Temurin
```bash
# 1. 访问 https://adoptium.net/
# 2. 选择 JDK 17，下载 Windows x64 安装包
# 3. 安装并配置环境变量
```

### Linux 系统
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel

# 验证
java -version
```

### macOS 系统
```bash
# 使用 Homebrew
brew install openjdk@17

# 配置环境变量
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# 验证
java -version
```

## 🔧 IDEA 配置 JDK 17

### 1. 设置项目 SDK
1. 打开 **File** → **Project Structure** (`Ctrl+Alt+Shift+S`)
2. 左侧选择 **Project**
3. **SDK**: 
   - 如果列表中有 `17`，直接选择
   - 如果没有，点击 **Add SDK** → **Download JDK**
   - 选择 Version: `17`，Vendor: `Microsoft` 或 `Eclipse Temurin`
4. **Language level**: 选择 `17 - Sealed types, always-strict floating-point semantics`
5. 点击 **Apply** → **OK**

### 2. 设置 Maven 配置
1. **File** → **Settings** (`Ctrl+Alt+S`)
2. 搜索 **Maven** → **Importing**
3. **JDK for importer**: 选择 `17`
4. 搜索 **Java Compiler**
5. **Project bytecode version**: 选择 `17`
6. 点击 **OK**

### 3. 刷新 Maven 项目
1. 打开右侧 **Maven** 面板
2. 点击刷新按钮 🔄 （Reload All Maven Projects）
3. 等待依赖下载完成

## 📦 获取 Spring AI Alibaba API Key

### 1. 注册阿里云账号
访问：https://www.aliyun.com/

### 2. 开通通义千问服务
1. 访问：https://dashscope.aliyun.com/
2. 登录后，进入控制台
3. 点击 **API Key 管理**
4. 创建新的 API Key
5. 复制保存 API Key（只显示一次）

### 3. 配置 API Key

#### 方式1：环境变量（推荐，安全）
```bash
# Windows (PowerShell)
$env:DASHSCOPE_API_KEY="sk-xxxxxxxxxxxxx"

# Windows (命令提示符)
set DASHSCOPE_API_KEY=sk-xxxxxxxxxxxxx

# Linux/Mac
export DASHSCOPE_API_KEY=sk-xxxxxxxxxxxxx
```

#### 方式2：配置文件
编辑 `application.yml`：
```yaml
spring:
  ai:
    dashscope:
      api-key: sk-xxxxxxxxxxxxx  # 替换为你的API Key
```

**⚠️ 注意**：不要将API Key提交到Git仓库！

## 🔄 重新编译项目

### 1. 清理旧编译文件
```bash
cd hospital-registration-back-end
mvn clean
```

### 2. 编译项目
```bash
mvn clean install -DskipTests
```

### 3. 启动项目
```bash
cd pipilin-admin
mvn spring-boot:run
```

或在 IDEA 中直接运行 `PipilinApplication.java`

## 🎨 API 文档访问

### 旧版（Swagger）已移除
❌ `http://localhost:8080/swagger-ui/`

### 新版（SpringDoc OpenAPI）
✅ `http://localhost:8080/swagger-ui.html`

或访问：
- API文档 JSON：`http://localhost:8080/v3/api-docs`
- Swagger UI：`http://localhost:8080/swagger-ui/index.html`

## 🤖 Spring AI Alibaba 使用示例

### 1. 简单对话
```bash
# GET 请求
curl http://localhost:8080/ai/chat?message=你好

# 返回
{
  "code": 200,
  "msg": "操作成功",
  "data": "你好！我是通义千问，很高兴为你服务..."
}
```

### 2. 详细对话（带元数据）
```bash
# POST 请求
curl -X POST http://localhost:8080/ai/chat/detail \
  -H "Content-Type: application/json" \
  -d '{
    "message": "什么是Spring Boot？",
    "temperature": 0.7,
    "maxTokens": 2000
  }'

# 返回
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "content": "Spring Boot是...",
    "model": "qwen-turbo",
    "usage": {
      "promptTokens": 15,
      "completionTokens": 120,
      "totalTokens": 135
    }
  }
}
```

### 3. 医疗咨询助手
```bash
# 医疗问答
curl http://localhost:8080/ai/medical/consult?question=头痛应该挂什么科

# 症状分析
curl -X POST http://localhost:8080/ai/medical/analyze \
  -H "Content-Type: text/plain" \
  -d "头痛、发烧、咳嗽三天了"
```

### 4. 在前端使用
```javascript
// 简单对话
async function chat(message) {
  const response = await fetch(`/ai/chat?message=${encodeURIComponent(message)}`);
  const result = await response.json();
  console.log(result.data);
}

// 医疗咨询
async function medicalConsult(question) {
  const response = await fetch(`/ai/medical/consult?question=${encodeURIComponent(question)}`);
  const result = await response.json();
  console.log(result.data);
}
```

## 📋 主要代码变化

### 1. javax → jakarta
Spring Boot 3 使用 Jakarta EE，所有 `javax.*` 包需要改为 `jakarta.*`

**如果遇到编译错误，需要手动替换：**
```java
// 旧的
import javax.servlet.*;
import javax.validation.*;
import javax.persistence.*;

// 新的
import jakarta.servlet.*;
import jakarta.validation.*;
import jakarta.persistence.*;
```

### 2. Swagger 注解替换
```java
// 旧的 Swagger 3
@Api(tags = "用户管理")
@ApiOperation("获取用户列表")
@ApiParam("用户ID")

// 新的 SpringDoc
@Tag(name = "用户管理")
@Operation(summary = "获取用户列表")
@Parameter(description = "用户ID")
```

### 3. 配置类变化
```java
// 旧的
@EnableSwagger2

// 新的（不需要特别注解，自动配置）
// SpringDoc 自动配置
```

## 🐛 常见问题

### 1. 编译错误：找不到 javax 包
```
解决：替换为 jakarta 包
import jakarta.servlet.*;
```

### 2. Swagger UI 无法访问
```
解决：访问新地址
http://localhost:8080/swagger-ui.html
```

### 3. Spring AI 调用失败
```
错误：API Key 未配置
解决：设置环境变量 DASHSCOPE_API_KEY 或在配置文件中配置
```

### 4. MySQL 驱动找不到
```
错误：com.mysql.jdbc.Driver ClassNotFoundException
解决：新驱动类名已改为 com.mysql.cj.jdbc.Driver
配置文件会自动识别，无需手动修改
```

## 🎁 新功能特性

### 1. AI 智能助手
- ✅ 通用对话功能
- ✅ 医疗咨询助手
- ✅ 症状分析与科室推荐
- ✅ 可扩展的AI功能接口

### 2. 现代化 API 文档
- ✅ 更好的界面体验
- ✅ 支持 OAuth2
- ✅ 自动生成客户端代码
- ✅ 更完善的类型定义

### 3. 性能提升
- ✅ JDK 17 性能优化
- ✅ Spring Boot 3 原生支持
- ✅ 更快的启动速度
- ✅ 更低的内存占用

## 📚 学习资源

### Spring Boot 3
- 官方文档：https://spring.io/projects/spring-boot
- 迁移指南：https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide

### Spring AI Alibaba
- GitHub：https://github.com/alibaba/spring-ai-alibaba
- 官方文档：https://sca.aliyun.com/ai/
- 通义千问文档：https://help.aliyun.com/zh/dashscope/

### JDK 17
- 新特性：https://openjdk.org/projects/jdk/17/
- 学习教程：https://www.baeldung.com/java-17-new-features

## 🎉 升级完成！

现在你的项目已经升级到：
- ✅ **JDK 17** - 最新长期支持版本
- ✅ **Spring Boot 3.2** - 最新稳定版
- ✅ **Spring AI Alibaba** - AI能力集成
- ✅ **现代化技术栈** - 更好的性能和开发体验

开始探索 AI 驱动的医疗应用吧！🚀



