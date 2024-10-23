pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        DOCKER_CREDENTIALS = credentials('dockerhub_credentials_id') // Store Docker credentials in Jenkins
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
                sh 'docker build -t kaissgh_docker/foyer:latest .'
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    // Login to DockerHub
                    sh 'echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin'
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
            echo 'Build successful!'
        }
        failure {
            mail to: 'kun.elghoul@gmail.com',
                 subject: "Jenkins Build Failed: ${env.JOB_NAME}",
                 body: "Build ${env.BUILD_NUMBER} of job ${env.JOB_NAME} failed. Check Jenkins for details."
            echo 'Build failed'
        }
    }
}

