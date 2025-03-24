FROM hub.rat.dev/eclipse-temurin:17-jdk

VOLUME /tmp

ADD *.jar app.jar
ENTRYPOINT ["java","-jar","F:\Projects\Java Project\BalloonWords\docker\ans-0.0.1-SNAPSHOT.jar"]

# Ubuntu 时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime