FROM openjdk:17-jdk-slim
#RUN apt-get update && apt-get install -y maven
WORKDIR /app
#COPY . .
#RUN ./mvnw clean package
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/bookstore-0.0.1-SNAPSHOT.jar bookstore.jar
EXPOSE 8080
#ENTRYPOINT exec java $JAVA_OPTS -jar bookstore.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar bookstore.jar
