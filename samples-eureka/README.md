# Samples Eureka

```docker
docker run -d -p 8762:8080 --rm \
-e JAVA_OPTS='-server -Xmx1g' \
-e PROFILE='default' \
-e SERVER_PORT=8080 \
registry.cn-qingdao.aliyuncs.com/upcwangying/samples-eureka:0.2.1-SNAPSHOT
```
