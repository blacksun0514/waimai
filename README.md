
# 社区鲜食快送系统 - Community Fresh Food Delivery

社区鲜食快送系统是一款基于 Spring Boot 构建的社区生鲜食品配送管理系统，包含后台管理端和移动端用户端功能，为社区居民提供新鲜食材的快速配送服务。

## 技术栈

| 分类 | 技术 | 版本 |
| :--- | :--- | :--- |
| 语言 | Java | 8+ |
| 框架 | Spring Boot | 2.7.3 |
| ORM | MyBatis | 2.2.0 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 6.0+ |
| 认证 | JWT | 0.9.1 |
| 文件存储 | 阿里云 OSS | 3.10.2 |
| API文档 | Knife4j | 3.0.2 |
| 支付 | 微信支付 | API v3 |
| 实时通信 | WebSocket | - |

## 项目架构

### 模块划分

```
sky-take-out/
├── sky-common/          # 通用模块
│   ├── constant/        # 常量定义
│   ├── context/         # 上下文工具
│   ├── exception/       # 异常处理
│   ├── json/            # JSON处理
│   ├── properties/      # 配置属性
│   ├── result/          # 返回结果封装
│   └── utils/           # 工具类
├── sky-pojo/            # 数据模型模块
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 数据库实体
│   └── vo/              # 视图对象
└── sky-server/          # 业务服务模块
    ├── annotation/      # 自定义注解
    ├── aspect/          # AOP切面
    ├── config/          # 配置类
    ├── controller/      # 控制层
    ├── handler/         # 全局处理器
    ├── interceptor/     # 拦截器
    ├── mapper/          # 数据访问层
    ├── service/         # 业务逻辑层
    └── task/            # 定时任务
```

### 架构特点

- **模块化设计**：按功能划分为独立模块，降低耦合度
- **分层架构**：Controller → Service → Mapper → Database
- **RESTful API**：统一的接口风格
- **JWT认证**：前后端分离的无状态认证
- **AOP切面**：统一处理日志、事务、参数校验

## 核心功能

### 后台管理端 (`/admin/**`)

| 模块 | 功能 |
| :--- | :--- |
| 员工管理 | 登录、新增、分页查询、启用/禁用、编辑 |
| 菜品管理 | 菜品CRUD、分类管理、口味管理 |
| 套餐管理 | 套餐CRUD、关联菜品 |
| 订单管理 | 订单列表、状态变更、取消、派送 |
| 报表统计 | 营业额统计、订单统计、用户统计 |
| 店铺管理 | 营业状态管理 |

### 用户移动端 (`/user/**`)

| 模块 | 功能 |
| :--- | :--- |
| 用户登录 | 微信小程序登录 |
| 菜品浏览 | 分类浏览、菜品详情 |
| 购物车 | 添加、修改、删除、清空 |
| 订单管理 | 下单、支付、取消、评价 |
| 地址管理 | 地址增删改查 |
| 店铺状态 | 查看营业状态 |

## 快速开始

### 环境要求

- JDK 1.8+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 数据库配置

创建数据库并导入初始化数据：

```sql
CREATE DATABASE sky_take_out CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 配置文件

修改 `sky-server/src/main/resources/application-dev.yml`：

```yaml
sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_password
    database: 10
```

### 启动方式

**开发态运行**：

```bash
cd sky-server
mvn spring-boot:run
```

**打包构建**：

```bash
mvn clean package
java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
```

### 访问地址

| 服务 | 地址 |
| :--- | :--- |
| API服务 | http://localhost:8080 |
| Swagger文档 | http://localhost:8080/doc.html |

## API 接口示例

### 员工登录

**请求**：
```http
POST /admin/employee/login
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

**响应**：
```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "id": 1,
        "userName": "admin",
        "name": "管理员",
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
}
```

### 用户微信登录

**请求**：
```http
POST /user/user/login
Content-Type: application/json

{
    "code": "08123456"
}
```

**响应**：
```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "id": 1,
        "openid": "o123456...",
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
}
```

## 数据模型

### 核心实体

| 实体 | 说明 |
| :--- | :--- |
| `Employee` | 员工信息 |
| `User` | 用户信息 |
| `Dish` | 菜品信息 |
| `Category` | 分类信息 |
| `Setmeal` | 套餐信息 |
| `Orders` | 订单信息 |
| `ShoppingCart` | 购物车 |
| `AddressBook` | 收货地址 |

### 实体关系

```
Category ──< Dish ──< DishFlavor
        └──< Setmeal ──< SetmealDish

User ──< AddressBook
    ──< Orders ──< OrderDetail
    ──< ShoppingCart
```

## 安全机制

### JWT 认证流程

1. 用户登录获取 Token
2. 后续请求在 Header 中携带 Token
3. 拦截器验证 Token 有效性
4. 解析 Token 获取用户ID
5. 将用户ID存入 ThreadLocal

### Token 配置

```yaml
sky:
  jwt:
    admin-secret-key: itcast        # 管理员密钥
    admin-ttl: 7200000              # 管理员Token有效期(毫秒)
    admin-token-name: token          # 管理员Token名称
    user-secret-key: itheima         # 用户密钥
    user-ttl: 7200000               # 用户Token有效期(毫秒)
    user-token-name: authentication  # 用户Token名称
```

## 定时任务

| 任务 | 说明 |
| :--- | :--- |
| `OrderTask` | 处理超时未支付订单 |
| `WebSocketTask` | 推送订单状态更新 |

## 配置说明

### 支付配置（微信支付）

```yaml
sky:
  wechat:
    appid: your_appid
    secret: your_secret
    mchid: your_mchid
    mchSerialNo: your_serial_no
    privateKeyFilePath: /path/to/apiclient_key.pem
    apiV3Key: your_api_v3_key
    weChatPayCertFilePath: /path/to/wechatpay_cert.pem
    notifyUrl: https://your-domain/notify/paySuccess
```

### 文件存储配置（阿里云OSS）

```yaml
sky:
  alioss:
    endpoint: oss-cn-beijing.aliyuncs.com
    access-key-id: your_access_key
    access-key-secret: your_secret_key
    bucket-name: your_bucket_name
```

## 开发规范

### 代码结构

```
controller/          # REST API 控制层
  ├── admin/         # 后台管理接口
  └── user/          # 用户端接口

service/             # 业务逻辑层
  ├── impl/          # 接口实现
  └── *.java         # 接口定义

mapper/              # 数据访问层
  └── *.java         # Mapper接口

entity/              # 数据库实体
dto/                 # 请求参数封装
vo/                  # 响应结果封装
```

### 命名规范

- **类名**：采用 PascalCase
- **方法名**：采用 camelCase
- **变量名**：采用 camelCase
- **常量名**：采用 UPPER_CASE_WITH_UNDERSCORE

## 许可证

MIT License

## 联系方式

如有问题或建议，请通过以下方式联系：

- 邮箱：support@example.com
- 项目地址：https://github.com/example/sky-take-out
