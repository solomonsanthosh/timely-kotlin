FROM openjdk:17-jdk-alpine

COPY target/timelyserver-0.0.1-SNAPSHOT.jar timelyserver.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" ,"timelyserver.jar"]
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar timelyserver.jar
