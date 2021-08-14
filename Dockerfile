FROM openjdk:11-jre-slim

EXPOSE 8080

ADD /target/authentication-app-0.0.1-SNAPSHOT.jar authentication-app-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","authentication-app-0.0.1-SNAPSHOT.jar"]