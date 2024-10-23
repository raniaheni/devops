pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        // Allocate a maximum of 1024 MB (1 GB) of heap memory.
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Clone') {
            steps {
                git url: 'https://github.com/raniaheni/devops.git',
                    branch: 'kaissGH',
                    credentialsId: 'kais'
            }
        }

        stage('Build') {
            steps {
                dir('') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x mvnw'
                // Exécuter les tests en utilisant H2
                sh './mvnw test -Dspring.profiles.active=test'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Remplacez votre_nom_utilisateur_docker par votre nom d'utilisateur Docker Hub
                    sh 'docker build -t kaissgh_docker/foyer:latest .'
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    // Remplacez votre_nom_utilisateur_docker par votre nom d'utilisateur Docker Hub
                    sh 'docker push kaissgh_docker/foyer:latest'
                }
            }
        }
    }

    post {
        success {
            mail to: 'kun.elghoul@gmail.com',
                 subject: "Jenkins Build Successful: ${env.JOB_NAME}",
                 body: "Build ${env.BUILD_NUMBER} of job ${env.JOB_NAME} was successful. Check Jenkins for details."
            echo 'Build successful !'
        }
        failure {
            mail to: 'kun.elghoul@gmail.com',
                 subject: "Jenkins Build Failed: ${env.JOB_NAME}",
                 body: "Build ${env.BUILD_NUMBER} of job ${env.JOB_NAME} failed. Check Jenkins for details."
            echo 'Build failed'
        }
    }
}

