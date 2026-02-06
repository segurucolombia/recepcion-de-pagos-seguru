# 🐳 Guía de Docker para el Gateway

## 📋 Comparación con Node.js

| Node.js | Java Spring Boot |
|---------|------------------|
| `npm install` | `mvn dependency:go-offline` |
| `npm run build` | `mvn clean package` |
| `npm start` | `java -jar app.jar` |
| `node:18-alpine` | `eclipse-temurin:17-jre-alpine` |

---

## 🚀 Comandos básicos

### 1️⃣ Construir la imagen Docker

```bash
# Construir la imagen (similar a 'docker build' en Node.js)
docker build -t gateway-transacciones-wompi:latest .

# O con docker-compose (recomendado)
docker-compose build
```

**Explicación:**
- `-t gateway-transacciones-wompi:latest` → Nombre y tag de la imagen
- `.` → Contexto de construcción (directorio actual)

---

### 2️⃣ Ejecutar el contenedor

**Opción A: Con Docker directamente**
```bash
docker run -d \
  --name gateway-wompi \
  -p 3001:3001 \
  -e EXTERNAL_API_RESERVAS_SEGURU=http://host.docker.internal:5019/nuevo-back/transacciones/respuesta-transaccion \
  -e EXTERNAL_API_ALIADOS_SEGURU=http://host.docker.internal:3000/api-aliados/transacciones-wompi/respuesta-transaccion \
  gateway-transacciones-wompi:latest
```

**Opción B: Con docker-compose (recomendado)**
```bash
# Iniciar todos los servicios
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f gateway

# Detener servicios
docker-compose down
```

---

### 3️⃣ Comandos útiles

```bash
# Ver contenedores corriendo
docker ps

# Ver logs del contenedor
docker logs gateway-transacciones-wompi -f

# Entrar al contenedor (similar a 'docker exec -it' en Node.js)
docker exec -it gateway-transacciones-wompi sh

# Reiniciar el contenedor
docker restart gateway-transacciones-wompi

# Detener el contenedor
docker stop gateway-transacciones-wompi

# Eliminar el contenedor
docker rm gateway-transacciones-wompi

# Ver uso de recursos
docker stats gateway-transacciones-wompi
```

---

## 🔧 Desarrollo vs Producción

### **Desarrollo local (sin Docker)**
```bash
mvn spring-boot:run
```

### **Producción (con Docker)**
```bash
docker-compose up -d
```

---

## 🌐 Acceder a APIs en el host

**⚠️ IMPORTANTE:** Cuando tu aplicación corre en Docker y necesita acceder a servicios en tu máquina local (host):

❌ **NO usar:** `http://localhost:5019`
✅ **SÍ usar:** `http://host.docker.internal:5019`

**¿Por qué?** Dentro del contenedor, `localhost` se refiere al contenedor mismo, no a tu máquina.

---

## 📝 Variables de entorno

Hay 3 formas de configurar variables de entorno:

### 1. Archivo `.env` (local)
```bash
EXTERNAL_API_RESERVAS_SEGURU=http://localhost:5019/nuevo-back/transacciones/respuesta-transaccion
```

### 2. En `docker-compose.yml`
```yaml
environment:
  - EXTERNAL_API_RESERVAS_SEGURU=http://host.docker.internal:5019/nuevo-back/transacciones/respuesta-transaccion
```

### 3. Al ejecutar Docker
```bash
docker run -e EXTERNAL_API_RESERVAS_SEGURU=http://host.docker.internal:5019/...
```

**Prioridad:** 3 > 2 > 1

---

## 🐛 Troubleshooting

### Problema 1: "Cannot connect to localhost"
**Solución:** Usa `host.docker.internal` en lugar de `localhost`

### Problema 2: "Port already in use"
```bash
# Ver qué proceso usa el puerto 3001
lsof -i :3001

# O detener el contenedor anterior
docker stop gateway-transacciones-wompi
```

### Problema 3: Cambios no se reflejan
```bash
# Reconstruir sin cache
docker-compose build --no-cache
docker-compose up -d
```

### Problema 4: Ver errores de compilación
```bash
# Ver logs completos del build
docker-compose build --progress=plain
```

---

## 📦 Despliegue en servidor

### 1. Copiar archivos al servidor
```bash
scp -r gateway/ usuario@servidor:/ruta/destino/
```

### 2. En el servidor, construir y ejecutar
```bash
cd /ruta/destino/gateway
docker-compose up -d
```

### 3. Verificar que está corriendo
```bash
docker ps
curl http://localhost:3001/actuator/health
```

---

## 🔄 Actualizar la aplicación

```bash
# 1. Detener el contenedor
docker-compose down

# 2. Obtener los cambios (si usas Git)
git pull

# 3. Reconstruir la imagen
docker-compose build

# 4. Iniciar de nuevo
docker-compose up -d

# 5. Ver logs
docker-compose logs -f
```

---

## 💾 Persistencia de datos

Si necesitas persistir datos (por ejemplo, una base de datos), agrega volúmenes:

```yaml
volumes:
  - ./data:/app/data  # HOST:CONTAINER
```

---

## 🎯 Health Check

Para verificar que tu aplicación está saludable:

```bash
# Desde el host
curl http://localhost:3001/actuator/health

# Desde Docker
docker exec gateway-transacciones-wompi wget -qO- http://localhost:3001/actuator/health
```

---

## 📊 Monitoreo

```bash
# Ver uso de CPU y memoria
docker stats gateway-transacciones-wompi

# Ver logs con timestamp
docker logs gateway-transacciones-wompi --timestamps

# Ver últimas 100 líneas de logs
docker logs gateway-transacciones-wompi --tail 100
```

---

## 🔐 Buenas prácticas

1. **No incluir secretos en el Dockerfile** → Usar variables de entorno
2. **Usar multi-stage builds** → Reduce el tamaño de la imagen final
3. **Usar .dockerignore** → Evita copiar archivos innecesarios
4. **Configurar health checks** → Para detectar fallos automáticamente
5. **Limitar recursos** → Evita que un contenedor consuma todos los recursos

```yaml
deploy:
  resources:
    limits:
      cpus: '0.5'
      memory: 512M
```

---

## 🆚 Diferencias clave con Node.js

| Aspecto | Node.js | Java Spring Boot |
|---------|---------|------------------|
| **Imagen base** | `node:18-alpine` (40MB) | `eclipse-temurin:17-jre-alpine` (170MB) |
| **Build** | No requiere compilación | Requiere compilación (Maven) |
| **Hot reload** | `nodemon` | Spring DevTools (solo desarrollo) |
| **Tamaño final** | ~100-200MB | ~250-350MB |
| **Inicio** | ~1-2 segundos | ~10-15 segundos |
| **Multi-stage** | Opcional | Recomendado (reduce tamaño) |

---

## 📚 Recursos adicionales

- [Docker Docs](https://docs.docker.com/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
