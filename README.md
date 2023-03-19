
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


## 一些说明
### 通过docker-compose 启动，有时候服务启动失败
原因是，mysql还未启动完成，导致服务连不上mysql。  
即使docker-compose中配置了depends_on，但是depends_on 并不保证被依赖服务容器运行成功后才启动依赖服务  
解决方法：  
使用自定义 health check。详情见 docker-compose

### Springboot测试
尽量不要启动spring 容器，即不要添加 @springbootTest注解，这样会启动spring容器，太慢了。
大部分测试用new实例就完了，不需要使用容器

### 使用H2 fake mysql object 来测试，这样做fake了一个数据库，避免连接真正的数据库，提高测试效率
注意点：  
- mybatis 如果使用的注解的方式，就在你的 @Configuration 标注的配置类上加 @MapperScan 你的Mapper包
- 在搞一个application-test.yml的配置文件，配置连接h2
- 然后在测试类上加@SpringbootTest @ExtendWith(SpringExtension.class)注解。以前是用 @Runwith。 但是貌似现在不用了。直接用 @ExtendWith(SpringExtension.class)

### 使用 @SpringbootTest时，需要启动spring容器，这样依赖的Kafka就要连接，如果Kafka没启动就报错
测试时，我不想启动kafka之类的，可以使用 Mockito的 @MockBean 把这些需要连接资源的玩意给mock掉，
通过这样来避免他们干扰我们的测试

