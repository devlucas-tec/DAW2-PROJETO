#Estágio 1: build
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia primeiro só o que define as dependências, para aproveitar o cache
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Agora copia o código e gera o jar
COPY src src
RUN ./mvnw package -DskipTests -B

# Estágio 2: execução
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
