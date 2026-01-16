# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar la caché de capas de Docker
COPY pom.xml .
# Descargamos dependencias (esto se cacheará si el pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Construimos la aplicación limitando la memoria para evitar errores en Railway
# -Xmx384m limita el heap a 384MB, suficiente para el build pero seguro para contenedores pequeños
ENV MAVEN_OPTS="-Xmx384m"
RUN mvn clean package -DskipTests -B

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
