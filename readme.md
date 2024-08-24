


<p align="center">
  <a href="http://blog.icatw.top">
    <img src="http://www.static.icatw.top/config/logo.png" alt="icatw的个人博客" style="border-radius: 50%; max-width: 150px;">
  </a>
</p>


<p align="center">
  <a target="_blank" href="https://github.com/ladidol/hexo-blog-satoken">
    <img src="https://img.shields.io/hexpm/l/plug.svg" alt="License"/>
    <img src="https://img.shields.io/badge/JDK-17+-green.svg" alt="JDK"/>
    <img src="https://img.shields.io/badge/springboot-3.2.3.RELEASE-green" alt="Spring Boot"/>
    <img src="https://img.shields.io/badge/vue-2.5.17-green" alt="Vue"/>
    <img src="https://img.shields.io/badge/mysql-8.0.20-green" alt="MySQL"/>
    <img src="https://img.shields.io/badge/mybatis--plus-3.4.0-green" alt="MyBatis Plus"/>
    <img src="https://img.shields.io/badge/redis-6.0.5-green" alt="Redis"/>
    <img src="https://img.shields.io/badge/rabbitmq-3.8.5-green" alt="RabbitMQ"/>
    <img src="https://img.shields.io/badge/jsoup-3.8.5-green" alt="Jsoup"/>
  </a>
</p>


## 在线地址

- **个人主页：** [icatw.top](http://icatw.top)
- **项目链接：** [blog.icatw.top](http://blog.icatw.top)
- **后台链接：** [admin.icatw.top](http://admin.icatw.top)
- **Github地址：** [icatw/Sa-Blog](https://github.com/icatw/Sa-Blog/issues)

## 技术介绍

- **前端：** vue, vuex, vue-router, axios, vuetify, element, echarts, SSE
- **后端：** SpringBoot3, nginx, docker, Sa-token, Just Auth, Swagger2, MyBatisPlus, Mysql, Redis, RabbitMQ, Websocket, 通义千问

**ps：请先运行后端项目，再启动前端项目，前端项目配置由后端动态加载。**

## 项目结构

```plaintext
blog-springboot
├── annotation    -- 自定义注解
├── aspect        -- AOP模块
├── config        -- 配置模块
├── constant      -- 常量模块
├── consumer      -- MQ消费者模块
├── controller    -- 控制器模块
├── mapper        -- 框架核心模块
├── dto           -- DTO模块
├── enums         -- 枚举模块
├── exception     -- 自定义异常模块
├── handler       -- 处理器模块
├── service       -- 服务模块
├── strategy      -- 策略模块
├── util          -- 工具类模块
└── vo            -- VO模块
```

## 项目特点

- 前台参考"Hexo"的"Butterfly"设计，美观简洁，响应式体验好。
- 后台参考"element-admin"设计，侧边栏，历史标签，面包屑自动生成。
- 采用Markdown编辑器，写法简单。
- 项目采用Sa-token进行权限认证和登录，安全且实现简单
- 评论支持表情输入回复等，样式参考Valine。
- 前后端分离部署，适应当前潮流。
- 接入Just Auth第三方登录，减少注册成本。
- 支持文章导入导出，爬虫文章。
- 支持发布说说，随时分享趣事。
- 留言采用弹幕墙，更加炫酷。
- 支持代码高亮和复制，图片预览，深色模式等功能。
- 搜索文章支持高亮分词，响应速度快。
- 新增文章目录、推荐文章等功能，优化用户体验。
- 新增在线聊天室，支持撤回、语音输入、统计未读数量等功能。
- 新增AOP注解实现日志管理。
- 支持动态权限修改，采用RBAC模型，前端菜单和后台权限实时更新。
- 后台管理支持修改背景图片，博客配置等信息，操作简单，支持上传相册。
- 代码遵循阿里巴巴开发规范，利于开发者学习。
- 接入通义千问，支持AI流式对话。

## 项目截图

<table align="center"> <tr> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824161845988.png" alt="项目截图1" style="max-width: 100%; height: auto;"/></td> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824161929300.png" alt="项目截图2" style="max-width: 100%; height: auto;"/></td> </tr> <tr> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824161943272.png" alt="项目截图3" style="max-width: 100%; height: auto;"/></td> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162042865.png" alt="项目截图4" style="max-width: 100%; height: auto;"/></td> </tr> <tr> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162109205.png" alt="项目截图5" style="max-width: 100%; height: auto;"/></td> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162205169.png" alt="项目截图6" style="max-width: 100%; height: auto;"/></td> </tr> <tr> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162228513.png" alt="项目截图7" style="max-width: 100%; height: auto;"/></td> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162244487.png" alt="项目截图8" style="max-width: 100%; height: auto;"/></td> </tr> <tr> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162308716.png" alt="项目截图9" style="max-width: 100%; height: auto;"/></td> <td><img src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824162329195.png" alt="项目截图10" style="max-width: 100%; height: auto;"/></td> </tr> </table> 


## 启动说明

在启动项目之前，请按照以下步骤操作：

1. **克隆项目**

   ```bash
   git clone https://github.com/icatw/Sa-Blog
   cd your-project
   ```

2. **配置环境**

    - 确保已安装 [Maven](https://maven.apache.org/)。
    - 使用 [JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) 进行编译和运行。
    - 配置 [RabbitMQ](https://www.rabbitmq.com/download.html) 作为消息队列。

3. **修改配置文件**

    - 根据项目要求，修改 `application.yml` 或 `application.properties` 文件中的配置。确保正确配置数据库连接、RabbitMQ 设置等。

4. **运行项目**

    - 在 IDE 中，打开 `BlogApplication` 类。
    - 右键点击 `BlogApplication` 类并选择 `Run`，或直接点击运行按钮。

   ![image-20240824163556664](https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824163556664.png)

   > **提示**：第一次启动时，项目会自动创建并导入数据库文件，无需手动导入 SQL 文件。只需要确保配置文件设置正确即可。

## 其他说明

- **数据库**：第一次启动时，系统会自动创建必要的数据库表格和初始数据。
- **日志**：启动后可以查看日志以确认项目是否成功启动并运行正常。
- **问题**：如遇到问题，请参考 [常见问题解答](#) 或在 [Issues](https://github.com/icatw/Sa-Blog/issues) 中提交问题。

如果需要进一步的帮助或有任何问题，请随时在 [GitHub Issues](https://github.com/icatw/Sa-Blog/issues) 中提问或联系项目维护者。

与我联系：

<img align=left src="https://picgo-iamges.oss-cn-hangzhou.aliyuncs.com/img/image-20240824165658509.png" alt="image-20240824165658509" style="zoom:25%;" />

