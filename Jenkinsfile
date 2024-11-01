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
        
       
        

         stage('Publish to Nexus') {
            steps {
                script {
                    echo "Publishing ${env.jarFileName} to Nexus"
                    withCredentials([usernamePassword(credentialsId: "${env.NEXUS_CREDENTIALS_ID}", passwordVariable: 'NEXUS_PASS', usernameVariable: 'NEXUS_USER')]) {
                        sh """
                        curl -v -u ${NEXUS_USER}:${NEXUS_PASS} \
                        --upload-file ${env.SPRING_BOOT_PROJECT_NAME}/${env.jarFileName} \
                        "${env.NEXUS_URL}/${env.NEXUS_GROUP_ID}/${env.NEXUS_ARTIFACT_ID}/${env.NEXUS_VERSION}/${env.NEXUS_ARTIFACT_ID}-${env.NEXUS_VERSION}.jar"
                        """
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

