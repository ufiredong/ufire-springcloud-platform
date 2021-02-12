# 基于java镜像创建新镜像
FROM openjdk:8-jre-alpine
# 作者
MAINTAINER ufiread@sina.com
WORKDIR /build
ARG JAR_FILE=target/ufire-websocket-1.0-SNAPSHOT.jar
# 将jar包添加到容器中并更名为app.jar
ADD  ${JAR_FILE} ufire-websocket.jar
# 运行jar包
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/.urandom","-Dspring.profiles.active=prd","ufire-websocket.jar"]