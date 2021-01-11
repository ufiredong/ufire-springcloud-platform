# 基于java镜像创建新镜像
FROM java:8
# 作者
MAINTAINER ufiread@sina.com
WORKDIR /build
ARG JAR_FILE=target/ufire-getway-1.0-SNAPSHOT.jar
# 将jar包添加到容器中并更名为ufire-getway.jar
ADD  ${JAR_FILE} ufire-getway.jar
# 运行jar包
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/.urandom","ufire-getway.jar"]