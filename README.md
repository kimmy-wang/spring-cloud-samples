## Spring Cloud Samples

这是一个Spring Cloud 样例项目

## 项目依赖

- Spring Boot (2.0.6.RELEASE)
- Spring Cloud (Finchley.SR2)
- Spring Cloud Alibaba (0.2.2.RELEASE)
- Nacos (1.0.0)

## Seata

- AT: 注解只在事务最开始发起方添加即可, 框架可以通过链路将事务上下文传递下去并加入事务.
- MT: 就是TCC, 需要用户自己实现3个接口.
- 将所有TCC服务都发布成了dubbo服务, 不需要 @LocalTCC 注解, @LocalTCC是在调用本机的TCC bean时才使用的.
- 需要根据一阶段的结果自己实现，抛异常分布式事务回滚，不抛异常分布式事务提交.
- TCC模式现在不支持与feign集成.
- AT模式二阶段是依赖数据资源的, 这个数据资源是指AT一阶段提交业务数据更新和相应回滚日志记录.
- AT模式必须基于支持本地 ACID 事务的关系型数据库、MT模式不依赖于底层数据资源的事务支持, 是从Seata框架的角度看的.
- Seata框架只针对资源, AT模式的资源是数据库, MT模式的资源是业务的接口.

根据两阶段行为模式的不同，我们将分支事务划分为 **Automatic (Branch) Transaction Mode** 和 **Manual (Branch) Transaction Mode**.

AT 模式基于支持**本地 ACID 事务**的**关系型数据库**:

- 一阶段 prepare 行为: 在本地事务中, 一并提交业务数据更新和相应回滚日志记录.
- 二阶段 commit 行为: 马上成功结束, **自动** 异步批量清理回滚日志.
- 二阶段 rollback 行为: 通过回滚日志, **自动** 生成补偿操作，完成数据回滚.

相应的, MT 模式不依赖于底层数据资源的事务支持:

- 一阶段 prepare 行为: 调用 **自定义** 的 prepare 逻辑.
- 二阶段 commit 行为: 调用 **自定义** 的 commit 逻辑.
- 二阶段 rollback 行为: 调用 **自定义** 的 rollback 逻辑.

所谓 MT 模式，是指支持把 **自定义** 的分支事务纳入到全局事务的管理中.

### 流程说明

![](process.png)

1. business通知server开启事务
2. business调用storage, storage开启本地事务, 本地事务提交时, 通知server创建分支事务
3. order 和 account 同 storage
4. rpc调用完毕后, business通知server提交事务
5. 每个分支本地事务提交后, 会通知server创建分支事务, 当rpc调用完毕后, business通知server提交全局事务(这个business是不是根据全局事务发起者方法里有没有抛出异常来决定提交或者回滚全局事务)

## samples-zuul 项目

> 相关filter执行顺序

```
RateLimitPreFilter(限流) -> GrayFilter(灰度发布) -> AuthFilter(认证) -> PermissionFilter(鉴权)
```

### 路由通配符

映射路径 `/user/**` 之后的 `/**` 大有讲究, 还可以配置为 `/*` 或者 `/?`

| 规则 | 释义 | 示例 |
| :------: | :------: | :------: |
| /** | 匹配任意数量的路径与字符 | /user/add, /user/add/a, /user/add/a/b |
| /* | 匹配任意数量的字符 | /user/add, /user/mul, /user/b |
| /? | 匹配单个字符 | /user/a, /user/b |

## Maven dockerfile plugin

```text
docker login  -u username -p userpasswd 192.168.88.87

mvn package -DskipTests dockerfile:build dockerfile:push

mvn package -DskipTests dockerfile:build dockerfile:push -Dusername=wangy -Dpassword=Wangy123 
```

## Docker compose

- 单机模式下运行

在后台运行

```docker
docker-compose up -d
```

or 指定文件名, 默认是docker-compose.yml

```docker
docker-compose up -d -f filename
```

- swarm 集群模式运行

TODO...

## 测试用例

测试用例在spring-cloud-samples/samples-product/product-server/src/test目录下

注意

1. 执行 `mvn test` 启动测试用例
2. 执行 `mvn package` `mvn install` `mvn deploy`时跳过 `mvn test`
3. 建议使用git子模块管理项目

### Test
```text
DskipTests 不执行测试用例, 但编译测试用例类生成相应的class文件至target/test-classes下

Dmaven.test.skip=true, 不执行测试用例，也不编译测试用例类
```

1. 使用`mvn package -Dmaven.test.skip=true`, 不但跳过单元测试的运行, 也跳过测试代码的编译; 也可以在pom.xml文件中修改.

```mvn
<plugin>  
    <groupId>org.apache.maven.plugin</groupId>  
    <artifactId>maven-compiler-plugin</artifactId>  
    <version>2.1</version>  
    <configuration>  
        <skip>true</skip>  
    </configuration>  
</plugin>

<plugin>  
    <groupId>org.apache.maven.plugins</groupId>  
    <artifactId>maven-surefire-plugin</artifactId>  
    <version>2.5</version>  
    <configuration>  
        <skip>true</skip>  
    </configuration>  
</plugin> 
```

2. 使用`mvn package -DskipTests`跳过单元测试, 但是会继续编译; 也可以在pom.xml文件中修改.

```mvn
<plugin>  
    <groupId>org.apache.maven.plugins</groupId>  
    <artifactId>maven-surefire-plugin</artifactId>  
    <version>2.5</version>  
    <configuration>  
        <skipTests>true</skipTests>  
    </configuration>  
</plugin> 
```

## TODO

- [X] Netflix Eureka切换成Nacos Discovery
- [X] Spring Cloud Config切换成Nacos Config
- [X] 原samples-gateway模块更改为samples-zuul模块, 基于Netflix Zuul
- [ ] 新增samples-gateway模块, 基于Spring Cloud Gateway
- [ ] 集成分布式事务中间件Alibaba Seata
- [ ] Feign切换成gRPC
- [ ] Netflix Hystrix切换成Alibaba Sentinel
- [ ] Samples bpm集成Activiti
