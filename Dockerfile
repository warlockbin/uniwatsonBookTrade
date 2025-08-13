# ---- build stage：用 Maven 編譯出 WAR ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

# ---- run stage：Tomcat 佈署 WAR ----
FROM tomcat:9.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
# 你的 <finalName> 是 uniwatsonBookTrade，這裡照該檔名
COPY --from=build /app/target/uniwatsonBookTrade.war /usr/local/tomcat/webapps/ROOT.war

# 避免中文亂碼
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8"
EXPOSE 8080
CMD ["catalina.sh","run"]
