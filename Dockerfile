# ============================================================================
# Spring Boot 的映像檔（Railway 部署用）
#
# 分成兩個階段（multi-stage build）：
#   1. build：用完整的 JDK + Maven 把程式編譯、打包成 jar
#   2. run  ：只放 JRE 和那個 jar，映像檔比較小，也不會帶著原始碼上線
#
# 前端不在這裡 build：src/main/resources/static/ 已經是 build 好的檔案（見 README-FRONTEND.md）
#
# 本機試跑：
#   docker build -t kinu-app .
#   docker run --rm -p 8080:8080 -e DB_URL="jdbc:postgresql://host.docker.internal:15432/unicloth" kinu-app
# ============================================================================

# ---------- 第 1 階段：編譯打包 ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# 先只複製 Maven 相關檔案、下載套件：之後只改 Java 程式的話，這一步會沿用快取，重建比較快
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

# 再複製原始碼並打包；測試需要資料庫，部署時先跳過
COPY src src
RUN ./mvnw -B -q package -DskipTests

# ---------- 第 2 階段：執行 ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# TZ：容器預設是 UTC，設成台灣時間，訂單時間、逾時取消才不會差 8 小時
ENV TZ=Asia/Taipei

# 從第 1 階段只拿打包好的 jar 過來
COPY --from=build /app/target/*.jar app.jar

# 只是標示用途；實際 port 由 Railway 的 PORT 環境變數決定（application.yml 的 server.port）
EXPOSE 8080

# MaxRAMPercentage：讓 Java 最多用容器記憶體的 75%，避免超過 Railway 的記憶體上限被砍掉
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
