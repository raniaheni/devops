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
        
       
        

         stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'SonarQube Scanner'
            }
            steps {
                withSonarQubeEnv('SonarQube Server') { // Ensure this matches the name in Jenkins configuration
                    sh "${scannerHome}/bin/sonar-scanner " +
                       "-Dsonar.projectKey=ddf " +
                       "-Dsonar.sources=. " +
                       "-Dsonar.java.binaries=target/classes " + // Specify the compiled classes directory
                       "-Dsonar.host.url=http://localhost:9000 " +
                       "-Dsonar.login=${SONAR_AUTH_TOKEN}"
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

