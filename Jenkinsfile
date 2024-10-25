pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
        sonarQubeScanner 'SonarQube Scanner' // Add SonarQube scanner
    }

    environment {
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
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t kaissgh11/foyer:latest .'
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub12', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin'
                        sh 'docker push kaissgh11/foyer:latest'
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'SonarQube Scanner'
            }
            steps {
                withSonarQubeEnv('SonarQube Server') {
                    sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=your_project_key -Dsonar.sources=. -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_AUTH_TOKEN}"
                }
            }
        }
    }

    post {
        success {
            emailext (
                to: 'kun.elghoul@gmail.com',
                subject: "Jenkins Build Successful: ${env.JOB_NAME}",
                body: "Build ${env.BUILD_NUMBER} of job ${env.JOB_NAME} was successful. Check Jenkins for details.",
                mimeType: 'text/plain',
                from: 'kun.elghoul@gmail.com',
                replyTo: 'kun.elghoul@gmail.com

