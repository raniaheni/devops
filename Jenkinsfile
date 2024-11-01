pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
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

        stage('Test') {
            steps {
                // Execute tests and generate jacoco.exec
                sh 'mvn clean test'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'SonarQube Scanner'
            }
            steps {
                withSonarQubeEnv('SonarQube Server') { // Ensure this matches the name in Jenkins configuration
                    withCredentials([string(credentialsId: 'sonarqube12', variable: 'SONAR_TOKEN')]) {
                        sh """
                        ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=foyer \
                        -Dsonar.sources=. \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=$SONAR_TOKEN \
                        -Dsonar.javascript.node.maxBridgeTimeout=600
                        """
                    }
                }
            }
        }

        stage('Code Coverage Report') {
            steps {
                jacoco execPattern: '**/target/jacoco.exec', 
                       classPattern: '**/target/classes', 
                       sourcePattern: '**/src/main/java', 
                       exclusionPattern: '**/target/test-classes/**',
                       inclusionPattern: '**/*.class'
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
                        sh '''
                            echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                            docker push kaissgh11/foyer:latest
                            docker logout
                        '''
                    }
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
                replyTo: 'kun.elghoul@gmail.com',
                attachLog: true
            )
            echo 'Build successful!'
        }
        failure {
            emailext (
                to: 'kun.elghoul@gmail.com',
                subject: "Jenkins Build Failed: ${env.JOB_NAME}",
                body: "Build ${env.BUILD_NUMBER} of job ${env.JOB_NAME} failed. Check Jenkins for details.",
                mimeType: 'text/plain',
                from: 'kun.elghoul@gmail.com',
                replyTo: 'kun.elghoul@gmail.com',
                attachLog: true
            )
            echo 'Build failed'
        }
    }
}

