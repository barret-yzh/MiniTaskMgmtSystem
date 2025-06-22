# Mini Task Management System - API 接口文档

## 概述

Mini Task Management System 是一个任务管理系统，提供用户管理、项目管理和任务管理功能。

- **基础URL**: `http://localhost:8080`
- **数据格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

所有API接口都返回统一的响应格式：

```json
{
  "success": true,
  "message": "操作成功",
  "data": {
  }
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| success | boolean | 操作是否成功 |
| message | string | 响应消息 |
| data | object | 响应数据（可选） |

## 错误码说明

| HTTP状态码 | 说明 |
|------------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 1. 用户管理模块

### 1.1 用户注册

**接口地址**: `POST /api/user/register`

**请求参数**:
```json
{
  "username": "testuser",
  "email": "test@example.com", 
  "password": "password123"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "用户注册成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
}
```

### 1.2 用户登录

**接口地址**: `POST /api/user/login`

**请求参数**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
}
```

---

## 2. 项目管理模块

### 2.1 创建项目

**接口地址**: `POST /api/project?userId={userId}`

**请求参数**:
```json
{
  "name": "项目名称",
  "description": "项目描述"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "项目创建成功",
  "data": {
    "id": 1,
    "name": "项目名称",
    "description": "项目描述",
    "ownerId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T10:30:00"
  }
}
```

### 2.2 获取项目列表

**接口地址**: `GET /api/project?userId={userId}`

**响应示例**:
```json
{
  "success": true,
  "message": "获取项目列表成功",
  "data": [
    {
      "id": 1,
      "name": "项目1",
      "description": "项目1描述",
      "ownerId": 1,
      "createdAt": "2025-06-21T10:30:00",
      "updatedAt": "2025-06-21T10:30:00"
    }
  ]
}
```

### 2.3 获取项目详情

**接口地址**: `GET /api/project/{id}?userId={userId}`

**响应示例**:
```json
{
  "success": true,
  "message": "获取项目详情成功",
  "data": {
    "id": 1,
    "name": "项目名称",
    "description": "项目描述",
    "ownerId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T10:30:00"
  }
}
```

### 2.4 更新项目

**接口地址**: `PUT /api/project/{id}?userId={userId}`

**请求参数**:
```json
{
  "name": "更新后的项目名称",
  "description": "更新后的项目描述"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "项目更新成功",
  "data": {
    "id": 1,
    "name": "更新后的项目名称",
    "description": "更新后的项目描述",
    "ownerId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T11:00:00"
  }
}
```

### 2.5 删除项目

**接口地址**: `DELETE /api/project/{id}?userId={userId}`

**响应示例**:
```json
{
  "success": true,
  "message": "项目删除成功"
}
```

---

## 3. 任务管理模块

### 3.1 创建任务

**接口地址**: `POST /api/project/{projectId}/tasks?userId={userId}`

**请求参数**:
```json
{
  "title": "任务标题",
  "description": "任务描述",
  "status": "TODO",
  "priority": "MEDIUM"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "任务创建成功",
  "data": {
    "id": 1,
    "title": "任务标题",
    "description": "任务描述",
    "status": "待办",
    "priority": "中",
    "projectId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T10:30:00"
  }
}
```

### 3.2 获取任务列表

**接口地址**: `GET /api/project/{projectId}/tasks?userId={userId}`

**响应示例**:
```json
{
  "success": true,
  "message": "获取任务列表成功",
  "data": [
    {
      "id": 1,
      "title": "任务1",
      "description": "任务1描述",
      "status": "待办",
      "priority": "高",
      "projectId": 1,
      "createdAt": "2025-06-21T10:30:00",
      "updatedAt": "2025-06-21T10:30:00"
    }
  ]
}
```

### 3.3 获取任务详情

**接口地址**: `GET /api/project/{projectId}/tasks/{id}?userId={userId}`

**响应示例**:
```json
{
  "success": true,
  "message": "获取任务详情成功",
  "data": {
    "id": 1,
    "title": "任务标题",
    "description": "任务描述",
    "status": "进行中",
    "priority": "高",
    "projectId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T11:00:00"
  }
}
```

### 3.4 更新任务

**接口地址**: `PUT /api/project/{projectId}/tasks/{id}?userId={userId}`

**请求参数**:
```json
{
  "title": "更新后的任务标题",
  "description": "更新后的任务描述",
  "status": "IN_PROGRESS",
  "priority": "HIGH"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "任务更新成功",
  "data": {
    "id": 1,
    "title": "更新后的任务标题",
    "description": "更新后的任务描述",
    "status": "进行中",
    "priority": "高",
    "projectId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T11:30:00"
  }
}
```

### 3.5 更新任务状态

**接口地址**: `PATCH /api/project/{projectId}/tasks/{id}/status?userId={userId}&status={status}`

**响应示例**:
```json
{
  "success": true,
  "message": "任务状态更新成功",
  "data": {
    "id": 1,
    "title": "任务标题",
    "description": "任务描述",
    "status": "已完成",
    "priority": "高",
    "projectId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T12:00:00"
  }
}
```

### 3.6 更新任务优先级

**接口地址**: `PATCH /api/project/{projectId}/tasks/{id}/priority?userId={userId}&priority={priority}`

**响应示例**:
```json
{
  "success": true,
  "message": "任务优先级更新成功",
  "data": {
    "id": 1,
    "title": "任务标题",
    "description": "任务描述",
    "status": "进行中",
    "priority": "低",
    "projectId": 1,
    "createdAt": "2025-06-21T10:30:00",
    "updatedAt": "2025-06-21T12:30:00"
  }
}
```

### 3.7 删除任务

**接口地址**: `DELETE /api/project/{projectId}/tasks/{id}?userId={userId}`

**响应示例**:
```json
{
  "success": true,
  "message": "任务删除成功"
}
```

---

## 4. 数据字典

### 4.1 任务状态枚举

| 枚举值 | 显示值 | 说明 |
|--------|--------|------|
| TODO | 待办 | 任务待开始 |
| IN_PROGRESS | 进行中 | 任务进行中 |
| COMPLETED | 已完成 | 任务已完成 |

### 4.2 任务优先级枚举

| 枚举值 | 显示值 | 说明 |
|--------|--------|------|
| LOW | 低 | 低优先级 |
| MEDIUM | 中 | 中优先级 |
| HIGH | 高 | 高优先级 |

---