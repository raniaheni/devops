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
        
        stage('Build') {
            steps {
                sh 'mvn clean package'
                sh 'ls -l target/'  // Check if the jar file exists
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

	stage('Publish to Nexus') {
	    steps {
		script {
		    echo "Publishing target/Foyer-0.0.1-SNAPSHOT.jar to Nexus"
		    withCredentials([usernamePassword(credentialsId: env.NEXUS_CREDENTIALS_ID, passwordVariable: 'NEXUS_PASS', usernameVariable: 'NEXUS_USER')]) {
		        sh """
		            curl -v -u ${NEXUS_USER}:${NEXUS_PASS} \
		            --upload-file target/Foyer-0.0.1-SNAPSHOT.jar \
		            "${env.NEXUS_URL}/repository/maven-releases/${env.NEXUS_GROUP_ID.replaceAll('\\.', '/')}/${env.NEXUS_ARTIFACT_ID}/${env.NEXUS_VERSION}/${env.NEXUS_ARTIFACT_ID}-${env.NEXUS_VERSION}.jar"
		        """
		    }
		}
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

