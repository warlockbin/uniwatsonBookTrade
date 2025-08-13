# ====== Stage 1: 用 Maven 編譯 WAR ======
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 先複製 pom.xml 讓相依先快取
COPY pom.xml ./
# 若你有 settings.xml（私有套件庫）就加：COPY settings.xml /root/.m2/settings.xml

# 再複製原始碼
COPY src ./src
COPY src/main/webapp ./src/main/webapp

# 打包（跳過測試，加速）
RUN mvn -DskipTests package

# ====== Stage 2: 用 Tomcat 執行 ======
FROM tomcat:9.0-jdk17-temurin

# 清空預設 webapps，避免干擾
RUN rm -rf /usr/local/tomcat/webapps/*

# 把剛打好的 WAR 放成 ROOT.war（網址就不需帶專案名）
COPY --from=build /app/target/uniwatsonBookTrade.war /usr/local/tomcat/webapps/ROOT.war

# 避免中文亂碼
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8"

# Tomcat 監聽 8080
EXPOSE 8080
CMD ["catalina.sh", "run"]
