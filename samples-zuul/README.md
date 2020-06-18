# Samples Zuul

基于`Netflix Zuul`的网关服务

## docker脚本
```docker
docker run -d -p 9090:8080 --rm \
-e JAVA_OPTS='-server -Xmx1g' \
-e PROFILE='default' \
-e SERVER_PORT=8080 \
-e EUREKA_SERVER_HOST=127.0.0.1 \
registry.cn-qingdao.aliyuncs.com/upcwangying/samples-zuul:0.2.1-SNAPSHOT
```

