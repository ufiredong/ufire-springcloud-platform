# 基于java镜像创建新镜像
FROM openjdk:8-jre-alpine
# 作者
MAINTAINER ufiread@sina.com
WORKDIR /build
ARG JAR_FILE=target/ufire-nacos-1.0-SNAPSHOT.jar
# 将jar包添加到容器中并更名为app.jar
ADD  ${JAR_FILE} ufire-nacos.jar
# 暴露8848端口
EXPOSE 8848
# 运行jar包
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/.urandom","ufire-nacos.jar"]