# Samples Admin

```docker
docker run -d -p 9093:8080 --rm \
-e JAVA_OPTS='-server -Xmx1g' \
-e PROFILE='default' \
-e SERVER_PORT=8080 \
docker.pkg.github.com/upcwangying/spring-cloud-samples/samples-admin:0.1.0-SNAPSHOT
```
