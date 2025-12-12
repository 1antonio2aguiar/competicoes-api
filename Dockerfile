# === Etapa 1: Build com Maven Wrapper Competicoes-api ===
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia o wrapper do Maven e a pasta .mvn
COPY mvnw .
COPY .mvn .mvn

# Dá permissão de execução para o mvnw
RUN chmod +x mvnw

# Instala utilitário dos2unix para conversão de scripts
RUN apk add --no-cache dos2unix bash

# Converte scripts e arquivos do wrapper para formato Unix
RUN dos2unix mvnw
RUN dos2unix .mvn/wrapper/maven-wrapper.properties

# Copia pom.xml primeiro (para aproveitar cache)
COPY pom.xml .

# Copia código-fonte
COPY src src

# Usa o wrapper do Maven para compilar sem rodar testes
RUN ./mvnw package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# === Etapa 2: Runtime ===
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o jar do estágio de build (agora com o nome correto)
COPY --from=build /app/target/api-1.0.0.jar app.jar

EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]