# 첫 번째 빌드 스테이지
FROM eclipse/ubuntu_jdk8:latest as build

USER root

WORKDIR /app

COPY . .

# create keystore for ssl
#ENV SSLPATH=/app/src/main/resources/ssl
#RUN apt-get update && \
#    apt-get install -y libssl-dev openssl
#
#RUN openssl pkcs12 -export -in $SSLPATH/fullchain.pem -inkey $SSLPATH/privkey.pem -out $SSLPATH/keystore.p12 -name ttp -CAfile $SSLPATH/chain.pem -caname root -passout pass:0000
#RUN openssl pkcs12 -export -in $SSLPATH/tls.crt -inkey $SSLPATH/tls.key -out $SSLPATH/keystore.p12 -name ttp -CAfile $SSLPATH/ca.crt -caname spring-ca -passout pass:0000

RUN chmod +x ./gradlew

RUN ./gradlew build -x test

# 두 번째 빌드 스테이지
FROM openjdk:8-jdk-alpine as app

WORKDIR /app

# jar 파일만 복사
COPY --from=build /app/build/libs/trans-back-0.0.1-SNAPSHOT.jar /app

EXPOSE 9091

#CMD java -jar -Dspring.profiles.active=${ENV} /app/trans-back-0.0.1-SNAPSHOT.jar
CMD java -jar /app/trans-back-0.0.1-SNAPSHOT.jar
