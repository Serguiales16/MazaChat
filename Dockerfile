# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Empaquetar saltando los tests para ir más rápido
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiar el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
