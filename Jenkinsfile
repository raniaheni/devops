pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        // Nexus configuration
        def artifactPath = 'target/Foyer-0.0.1-SNAPSHOT.jar'
        NEXUS_URL = 'http://localhost:8081/repository/maven-releases/'
        NEXUS_CREDENTIALS_ID = 'nexus_credentials_id' // Define in Jenkins credentials
        NEXUS_GROUP_ID = 'tn.esprit'
        NEXUS_ARTIFACT_ID = 'DevOps_Project'
        NEXUS_VERSION = '1.0'
        NEXUS_REPO = 'maven-releases' // Adjust to your Nexus repository
    }

    stages {
        stage('Clone') {
            steps {
                git url: 'https://github.com/raniaheni/devops.git',
                    branch: 'kaissGH',
                    credentialsId: 'kais'
            }
        }
        
        


        stage('Start Services with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
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

