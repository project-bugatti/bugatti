FROM maven:3.6-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn /home/app/pom.xml clean package -Dversion=${version} -DskipTests 

FROM openjdk:8-jre-slim
ARG version
COPY --from=build /home/app/target/bugatti-${version}.jar /usr/local/lib/bugatti-${version}.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/bugatti-${version}.jar"]