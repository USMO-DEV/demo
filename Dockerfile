# ---------- 构建阶段：编译 Java 源码 ----------
FROM eclipse-temurin:11-jdk AS build
WORKDIR /app
COPY src/Main.java .
RUN javac -encoding UTF-8 Main.java

# ---------- 运行阶段：只保留 JRE 和产物 ----------
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/*.class ./
COPY web ./web

EXPOSE 8080
CMD ["java", "Main"]
