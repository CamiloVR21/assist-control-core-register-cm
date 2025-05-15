
# Etapa única para desarrollo con Quarkus en modo dev
FROM maven:3.9.9-eclipse-temurin-21

# Crear directorio de trabajo
WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Exponer el puerto en que corre el backend en modo dev
EXPOSE 8081

# Comando por defecto: levantar en modo dev con perfil local
CMD ["mvn", "quarkus:dev", "-Dquarkus.profile=local"]
