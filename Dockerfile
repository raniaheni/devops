# Utiliser une image de base Java
FROM openjdk:17

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le livrable du dossier cible vers le conteneur
COPY target/Foyer-0.0.1-SNAPSHOT.jar /app/foyerIbra.jar

# Commande pour lancer le livrable
CMD ["java", "-jar", "foyerIbra.jar"]