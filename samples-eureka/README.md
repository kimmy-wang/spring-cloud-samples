# Samples Eureka

```docker
docker run -d -p 8762:8080 --rm \
-e JAVA_OPTS='-server -Xmx1g' \
-e PROFILE='default' \
-e SERVER_PORT=8080 \
docker.pkg.github.com/upcwangying/spring-cloud-samples/samples-eureka:0.1.0-SNAPSHOT
```
