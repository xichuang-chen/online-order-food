
该项目为网上订餐Springboot项目，同时会启动 mysql 服务器，Zookeeper 以及 Kafka服务

## 启动项目

### docker 方式
- docker-compose up -d
- docker-compose up -d --build (重新构建镜像)

服务启动成功后，通过 http://localhost:8888/test 验证服务是否启动成功

## Run test
### 命令行方式
- 本地安装以及配置好maven
- 执行命令 mvn test

### 使用IDE
- IDE 中配置好maven
- 点击 test 运行所有测试（也可在测试文件中单独Run test）
