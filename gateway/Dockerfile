# ==========================================
# STAGE 1: BUILD (compilar la aplicación)
# ==========================================
# Usamos Maven para compilar el proyecto
FROM maven:3.9-eclipse-temurin-17 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar solo pom.xml primero (para aprovechar cache de Docker)
# Similar a copiar package.json en Node.js
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
# Similar a 'npm install' en Node.js
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación (crear el JAR)
# Similar a 'npm run build' en Node.js
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: RUNTIME (ejecutar la aplicación)
# ==========================================
# Usamos una imagen más liviana solo con Java
FROM eclipse-temurin:17-jre-alpine

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
# El JAR contiene toda la aplicación empaquetada
COPY --from=builder /app/target/gateway-1.0-SNAPSHOT.jar app.jar

# Copiar el archivo .env (opcional, mejor usar variables de entorno)
COPY .env .env

# Exponer el puerto (debe coincidir con SERVER_PORT en .env)
EXPOSE 3001

# Configurar variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para ejecutar la aplicación
# Similar a 'npm start' en Node.js
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
