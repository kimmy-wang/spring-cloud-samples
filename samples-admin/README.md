# Samples Admin

```docker
docker run -d -p 9093:8080 --rm \
-e JAVA_OPTS='-server -Xmx1g' \
-e PROFILE='default' \
-e SERVER_PORT=8080 \
registry.cn-qingdao.aliyuncs.com/upcwangying/samples-admin:0.1.0-SNAPSHOT
```
