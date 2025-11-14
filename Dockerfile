# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
# Esta etapa se llama "build"
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Copiamos TODOS los archivos de build y el código
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./src

# Damos permisos
RUN chmod +x ./gradlew

# ¡¡AQUÍ ES DONDE SE COMPILA!!
# Esta línea debe estar en la Etapa 1
RUN ./gradlew bootJar --no-daemon
RUN ls -la /app/build/libs
# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Esta es la imagen final, ligera
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Creamos un usuario (buenas prácticas)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# ¡¡AQUÍ SOLO COPIAMOS EL RESULTADO!!
# Copiamos el JAR de la etapa "build"
#
# ⚠️ IMPORTANTE: ¡Asegurate de que este nombre sea el correcto!
# (En el log anterior te falló "Mutantes-api-1.0-SNAPSHOT.jar",
# tenés que poner el nombre que genera tu build de verdad).
COPY --from=build /app/build/libs/ExamenMercado-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]