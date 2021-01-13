FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ARG JVM_OPTIONS
ENV JVM_OPTIONS ${JVM_OPTIONS}
ENTRYPOINT ["java","-jar","/app.jar",'${JVM_OPTIONS}']