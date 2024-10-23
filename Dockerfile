# Utiliser une image de base Java
FROM openjdk:17-jdk

# Spécifier le répertoire de travail
WORKDIR /app

# Copier le fichier jar dans le conteneur
COPY target/Foyer-0.0.1-SNAPSHOT.jar /app/Foyer.jar

# Exposer le port sur lequel l'application sera disponible
EXPOSE 8081

# Commande pour exécuter l'application
CMD ["java", "-jar", "Foyer.jar"]

