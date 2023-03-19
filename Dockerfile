FROM openjdk:8-jdk-alpine
MAINTAINER xichuang.chen
COPY target/online-order-food.jar online-order-food.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/online-order-food.jar"]
