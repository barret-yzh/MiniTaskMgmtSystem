# Mini Task Management System - 项目开发思路记录

## 项目概述

本项目是一个迷你版任务管理系统，类似于简化版的Trello/Jira。采用Spring Boot + MyBatis-Plus + MySQL技术栈，提供用户管理、项目管理和任务管理的核心功能。

## 需求拆分思路

### 1. 核心功能模块识别
根据任务管理系统的特点，将系统拆分为三个核心模块：

#### 1.1 用户管理模块
- **功能范围**: 用户注册、登录、基本信息管理
- **核心实体**: User（用户）

#### 1.2 项目管理模块
- **功能范围**: 项目的增删改查等操作
- **设计考虑**: 项目归属于特定用户，支持权限控制
- **核心实体**: Project（项目）

#### 1.3 任务管理模块
- **功能范围**: 任务的增删改查操作
- **设计考虑**: 任务归属于项目，支持状态和优先级管理
- **核心实体**: Task（任务）

### 2. 功能优先级划分

#### 高优先级（MVP功能）
- 用户注册/登录
- 项目基本CRUD
- 任务基本CRUD
- 任务状态管理

#### 中优先级（增强功能）
- 任务优先级管理
- 任务筛选和排序
- 数据验证和错误处理

### 3. 技术选型考虑

#### 简化原则
- **数据库**: 不使用外键约束，通过应用层保证数据一致性

#### 可扩展性
- 使用MyBatis-Plus提供的基础功能
- 采用分层架构便于后续扩展
- 使用逻辑删除保证数据安全

---

## 数据库表设计思路

### 1. 实体关系分析

#### 1.1 核心实体识别
- **User（用户）**: 系统的基础实体
- **Project（项目）**: 任务的容器
- **Task（任务）**: 具体的工作项

#### 1.2 关系设计
```
User 1:N Project (一个用户可以有多个项目)
Project 1:N Task (一个项目可以有多个任务)
User 1:N Task (通过Project间接关联)
```

### 2. 表结构设计原则

#### 2.1 统一的基础字段
所有表都包含以下基础字段：
- `id`: 主键，使用BIGINT自增
- `created_at`: 创建时间，自动填充
- `updated_at`: 更新时间，自动更新
- `deleted`: 逻辑删除标记

#### 2.2 字段命名规范
- 使用下划线命名法（snake_case）
- 外键字段使用`_id`后缀
- 布尔字段使用清晰的语义

#### 2.3 数据类型选择
- **ID字段**: BIGINT，支持大数据量
- **字符串**: VARCHAR，根据实际需求设置长度
- **文本**: TEXT，用于描述等长文本
- **时间**: DATETIME，支持时区
- **枚举**: VARCHAR，存储枚举值

### 3. 索引策略

#### 3.1 主键索引
- 所有表使用自增主键
- 保证查询性能

#### 3.2 唯一索引
- `user.user_name`: 保证用户名唯一
- `user.email`: 保证邮箱唯一

#### 3.3 普通索引
- `deleted`: 逻辑删除查询优化
- `owner_id`: 用户项目查询
- `project_id`: 项目任务查询
- `status`, `priority`: 任务筛选优化

#### 3.4 复合索引
- `(project_id, status)`: 项目内状态筛选
- `(project_id, priority)`: 项目内优先级筛选

### 4. 设计决策记录

#### 4.1 不使用外键约束
**原因**:
- 简化数据库设计
- 避免级联删除的复杂性
- 通过应用层保证数据一致性

#### 4.2 使用逻辑删除
**原因**:
- 数据安全，支持恢复
- 保持数据完整性
- 便于审计和追踪

---

## API设计思路

### 1. RESTful设计原则

#### 1.1 资源定义
- `/api/user`: 用户资源
- `/api/project`: 项目资源
- `/api/project/{projectId}/tasks`: 任务资源（嵌套在项目下）

#### 1.2 HTTP方法映射
- `GET`: 查询操作
- `POST`: 创建操作
- `PUT`: 完整更新操作
- `PATCH`: 部分更新操作
- `DELETE`: 删除操作

### 2. URL设计模式

#### 2.1 层次化设计
```
/api/user/register          # 用户注册
/api/user/login             # 用户登录
/api/project                # 项目列表
/api/project/{id}           # 特定项目
/api/project/{id}/tasks     # 项目下的任务
/api/project/{id}/tasks/{taskId}  # 特定任务
```

#### 2.2 参数传递策略
- **路径参数**: 资源标识（如projectId, taskId）
- **查询参数**: 筛选条件和分页（如status, priority, userId）
- **请求体**: 创建和更新的数据

### 3. 响应格式统一

#### 3.1 统一响应结构
```json
{
  "success": "boolean",
  "message": "响应消息",
  "data": {} 
}
```

#### 3.2 HTTP状态码使用
- `200`: 操作成功
- `400`: 请求参数错误
- `404`: 资源不存在
- `500`: 服务器内部错误

## 代码实现关键点

### 1. 分层架构实现

#### 1.1 Controller层
**关键点**:
```java
@RestController
@RequestMapping("/api")
public class TaskController {
    
    // 统一异常处理
    // 参数验证
    // 响应格式统一
}
```

**实现要点**:
- 使用`@RestController`自动JSON序列化
- 统一使用`ApiResponse<T>`包装响应
- 参数验证在Controller层完成

#### 1.2 Service层
**关键点**:
```java
@Service
@Transactional
public class TaskService {
    
    // 业务逻辑实现
    // 权限验证
    // 数据校验
}
```

**实现要点**:
- 在Service层进行权限验证
- 业务异常使用RuntimeException

#### 1.3 Mapper层
**关键点**:
```java
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    // MyBatis-Plus提供基础CRUD
    // 自定义复杂查询
}
```

**实现要点**:
- 继承`BaseMapper<T>`获得基础功能
- 使用MyBatis-Plus的条件构造器
- 复杂查询使用XML配置

### 2. 实体类设计

#### 2.1 使用Lombok简化代码
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("task")
public class Task {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
}
```

#### 2.2 MyBatis-Plus注解使用
- `@TableName`: 指定表名
- `@TableId`: 主键配置
- `@TableField`: 字段配置
- `@TableLogic`: 逻辑删除

### 3. 枚举类设计

#### 3.1 JSON序列化处理
```java
public enum TaskStatus {
    TODO("待办"),
    IN_PROGRESS("进行中"), 
    COMPLETED("已完成");
    
    private final String displayName;
    
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
    
    @JsonCreator
    public static TaskStatus fromName(String name) {
        return TaskStatus.valueOf(name.toUpperCase());
    }
}
```

**关键点**:
- `@JsonValue`: 序列化时返回中文显示名
- `@JsonCreator`: 反序列化时接受英文枚举名

### 4. 配置类实现

#### 4.1 MyBatis-Plus配置
```java
@Configuration
@MapperScan("com.mgmtsystem.mapper")
public class MyBatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

### 5. 数据传输对象(DTO)设计

#### 5.1 请求DTO
```java
@Data
public class TaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}
```

#### 5.2 响应DTO
```java
@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
```

### 6. 关键业务逻辑实现

#### 6.1 权限验证
```java
private void validateProjectOwnership(Long projectId, Long userId) {
    Project project = projectMapper.selectById(projectId);
    if (project == null || !project.getOwnerId().equals(userId)) {
        throw new IllegalArgumentException("项目不存在或无权限访问");
    }
}
```

#### 6.2 任务筛选查询
```java
public List<Task> getTasksByProject(Long projectId, TaskStatus status, 
                                   TaskPriority priority, String sortBy, String sortDir) {
    QueryWrapper<Task> wrapper = new QueryWrapper<>();
    wrapper.eq("project_id", projectId);
    
    if (status != null) {
        wrapper.eq("status", status.name());
    }
    if (priority != null) {
        wrapper.eq("priority", priority.name());
    }
    
    // 排序处理
    if (StringUtils.hasText(sortBy)) {
        if ("desc".equalsIgnoreCase(sortDir)) {
            wrapper.orderByDesc(sortBy);
        } else {
            wrapper.orderByAsc(sortBy);
        }
    } else {
        wrapper.orderByDesc("created_at");
    }
    
    return taskMapper.selectList(wrapper);
}
```


## 开发流程总结

### 1. 开发顺序
1. **数据库设计** → 创建表结构和初始化脚本
2. **实体类** → 定义Entity和枚举类
3. **Mapper层** → 实现数据访问层
4. **Service层** → 实现业务逻辑
5. **Controller层** → 实现API接口
6. **测试验证** → 功能测试

### 2. 关键决策点

#### 2.1 技术选型
- **Spring Boot 3.2.5**: 稳定版本，兼容性好
- **MyBatis-Plus 3.5.7**: 提供丰富的基础功能

#### 2.2 设计模式
- **DTO模式**: 数据传输对象
- **Repository模式**: MyBatis-Plus的BaseMapper
- **统一响应格式**: ApiResponse包装

### 3. 遇到的问题及解决方案

#### 3.1 版本兼容性问题
**问题**: MyBatis-Plus与Spring Boot版本不兼容
**解决**: 降级Spring Boot到3.2.5版本

#### 3.2 枚举序列化问题
**问题**: 前端需要中文显示，后端需要英文枚举
**解决**: 使用`@JsonValue`和`@JsonCreator`注解


