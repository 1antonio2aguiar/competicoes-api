# ==========================
# Estágio de build (compila a aplicação)
# ==========================
FROM openjdk:17-alpine AS build

WORKDIR /app

# Copia o wrapper do Maven e a pasta .mvn (incluindo subpastas!)
COPY mvnw .
COPY .mvn .mvn

# Dá permissão de execução para o mvnw
RUN chmod +x mvnw

# Instala utilitário dos2unix para conversão de scripts
RUN apk add --no-cache dos2unix

# Converte scripts e arquivos do wrapper para formato Unix
RUN dos2unix mvnw
RUN dos2unix .mvn/wrapper/maven-wrapper.properties

# Copia pom.xml primeiro (para aproveitar cache)
COPY pom.xml .

# Copia o código-fonte
COPY src src

# Usa o wrapper do Maven para compilar (sem rodar testes)
RUN ./mvnw package -DskipTests

# ==========================
# Estágio de runtime (roda a aplicação)
# ==========================
FROM openjdk:17-alpine

WORKDIR /app

# Define onde está o .jar gerado
ARG JAR_FILE=target/competicoes-api-1.0.0.jar

# Copia o jar do estágio de build
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
