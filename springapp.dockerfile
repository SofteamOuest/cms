FROM maven:3.5-jdk-8-alpine as build

# set deployment directory
WORKDIR /tmp

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for package
RUN mvn clean package

# our final base image
FROM openjdk:8u171-jre-alpine

# copy over the built artifact from the maven image
COPY --from=build /tmp/target/cms-*.jar ./cms.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n","-Dspring.profiles.active=container","-jar","cms.jar","container-entrypoint"]
