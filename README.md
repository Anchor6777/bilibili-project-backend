# 仿写b站网页端—后端部分

简介：这是仿B站网页端项目的后端服务，涵盖用户、视频、评论、弹幕、关注、公共聊天室和私信等功能。

前端部分参见：[GitHub - Anchor6777/bilibili-project: 仿写b站web端的个人前端项目 · GitHub](https://github.com/Anchor6777/bilibili-project)

## 技术栈

- 语言：Java
- 框架（IOC容器）：Spring、SpringBoot
- 数据库：MySQL
- ORM框架：mybatius-plus
- lombok：Java注解库，作用是减少样板代码
- 接口文档：Swagger UI



## 功能

- 用户注册、登录、Token 刷新与管理员用户管理
- 视频推荐列表、视频详情、按用户查询视频
- 视频一级评论与二级回复
- 视频弹幕查询与发送
- 用户关注、取关、粉丝数和关注数查询
- 公共聊天室历史消息
- 用户间私信与最近聊天联系人查询
- WebSocket 实时消息能力



## 项目结构

```text
src/main/java/com/example/usercenterbackend
├── controller     # HTTP 接口层
├── service        # 业务逻辑层
├── mapper         # 数据库访问层
├── model
│   ├── domain     # 数据库实体
│   └── body       # 请求与响应对象
├── websocket      # WebSocket 消息处理
└── commons        # 统一响应、异常、JWT 等公共组件
```



## 库表设计

| 表              | 用途           | 关键字段                                             |
| --------------- | -------------- | ---------------------------------------------------- |
| `user`          | 用户账户与资料 | `id`、`username`、`password`、头像、邮箱、角色、标签 |
| `user_relation` | 用户关注关系   | `follower_id`、`followee_id`、`status`               |
| `video`         | 视频元数据     | `uid`、标题、视频/封面地址、播放量、标签             |
| `video_comment` | 视频评论及回复 | `uid`、`video_id`、`parent_id`、`reply_to_uid`       |
| `danmu`         | 视频弹幕       | `vid`、作者昵称、视频时间点、内容、颜色、类型        |
| `msg`           | 公共聊天室消息 | `userID`、用户名、头像、内容、时间                   |
| `new_msg`       | 用户私信       | `sender_id`、`receiver_id`、内容、时间               |
| `tag`           | 标签层级       | `tagName`、`parentId`、`isParent`                    |



## API 概览

| 模块 | 示例接口                    | 说明           |
| ---- | --------------------------- | -------------- |
| 用户 | `POST /user/register`       | 注册           |
| 用户 | `POST /user/login`          | 登录           |
| 视频 | `GET /video/video_list`     | 推荐视频列表   |
| 视频 | `GET /video/one`            | 视频详情       |
| 评论 | `POST /video/send_comment`  | 发表评论或回复 |
| 弹幕 | `GET/POST /video/danmu/v3/` | 查询/发送弹幕  |
| 关注 | `POST /relation/focus`      | 关注用户       |
| 私信 | `POST /new_msg/send`        | 发送私信       |



## 后续计划

- 完善登录鉴权和权限校验
- 增加单元测试与集成测试
- 增加视频上传、文件存储和内容审核能力
- 添加视频推荐算法