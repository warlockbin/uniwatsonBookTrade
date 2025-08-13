# ==== Build stage: 用 Maven 編譯 war ====
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 先複製 pom.xml 並預先下載相依，讓快取生效
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# 再複製原始碼並真正編譯
COPY src ./src
RUN mvn -q -e -DskipTests package

# ==== Run stage: 用 Tomcat 9 跑 ====
FROM tomcat:9.0-jdk17-temurin

# 清掉預設 apps
RUN rm -rf /usr/local/tomcat/webapps/*

# 讓 context path = /uniwatsonBookTrade（與你本機一致）
COPY --from=build /app/target/uniwatsonBookTrade.war /usr/local/tomcat/webapps/uniwatsonBookTrade.war

# 避免亂碼
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8"

EXPOSE 8080
CMD ["catalina.sh", "run"]
