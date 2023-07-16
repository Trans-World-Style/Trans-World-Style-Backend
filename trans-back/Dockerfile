# Build stage
FROM eclipse/ubuntu_jdk8 as build
USER root
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build

# Package stage
FROM eclipse/ubuntu_jdk8
WORKDIR /app
COPY --from=build /app/build/libs/trans-back-0.0.1-SNAPSHOT.jar ./trans-back-0.0.1-SNAPSHOT.jar
EXPOSE 9090
CMD java -jar trans-back-0.0.1-SNAPSHOT.jar
