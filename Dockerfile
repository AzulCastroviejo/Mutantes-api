# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
# Imagen base ligera de Alpine Linux (~5MB) para compilar el código
# Se usa "as build" para nombrar esta etapa y referenciarla después
FROM eclipse-temurin:17-jdk-alpine as build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos solo los archivos necesarios para construir (Gradle)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./

# Copiamos el código fuente
COPY src ./src

# Damos permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# Ejecutamos Gradle para generar el JAR
# (Usamos ./gradlew para consistencia, y --no-daemon)
RUN ./gradlew bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Usamos la imagen de JRE (Runtime), que es más ligera
FROM eclipse-temurin:17-jre-alpine

# Establecemos el directorio de trabajo
WORKDIR /app

# (Opcional pero recomendado) Creamos un usuario para no correr como root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiamos el JAR generado en la ETAPA 1
# ¡¡IMPORTANTE: Verificá que este sea el nombre correcto!!
COPY --from=build /app/build/libs/Mutantes-api-1.0-SNAPSHOT.jar ./app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]