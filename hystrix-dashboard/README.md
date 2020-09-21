## Hystrix Dashboard

## docker脚本

```docker
docker run -d -p 9094:8080 --rm \
-e JAVA_OPTS='-server -Xmx1g' \
-e PROFILE='default' \
-e SERVER_PORT=8080 \
-e EUREKA_SERVER_HOST=127.0.0.1 \
registry.cn-qingdao.aliyuncs.com/upcwangying/hystrix-dashboard:0.2.1.RELEASE
```
