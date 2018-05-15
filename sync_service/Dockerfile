FROM openjdk:8u92-jre-alpine
ADD sync.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-server","-Xms256m","-Xmx1536m","-Djava.io.tmpdir=/tmp","-jar", "app.jar", "configuration.MicroServiceMain", "-dcv"]