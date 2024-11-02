pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        // MySQL configuration
        MYSQL_CONTAINER_NAME = 'my_mysql_container'
        MYSQL_IMAGE = 'mysql:8'
        MYSQL_ROOT_PASSWORD = '123'
        MYSQL_DATABASE = 'foyer1'
        MYSQL_PORT = '3306' // Port exposé pour MySQL
        DOCKER_NETWORK_NAME = 'app-network' // Nom de votre réseau Docker
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


        stage('Setup MySQL') {
            when {
                expression { !env.SKIP_TESTS.toBoolean() }
            }
            steps {
                script {
                    def startTime = System.currentTimeMillis()

                    // Create Docker network if it doesn't exist
                    sh "docker network create ${DOCKER_NETWORK_NAME} || true"

                    // Check if MySQL container is already running
                    def mysqlContainerExists = sh(script: "docker ps -a --filter name=${MYSQL_CONTAINER_NAME} --format '{{.Names}}'", returnStdout: true).trim()

                    if (mysqlContainerExists) {
                        echo "MySQL container '${MYSQL_CONTAINER_NAME}' already exists."
                        def mysqlContainerRunning = sh(script: "docker ps --filter name=${MYSQL_CONTAINER_NAME} --format '{{.Names}}'", returnStdout: true).trim()
                        if (!mysqlContainerRunning) {
                            sh "docker start ${MYSQL_CONTAINER_NAME}"
                            echo "Started the existing MySQL container '${MYSQL_CONTAINER_NAME}'."
                        } else {
                            echo "MySQL container '${MYSQL_CONTAINER_NAME}' is already running."
                        }
                    } else {
                        sh """
                        docker run --name ${MYSQL_CONTAINER_NAME} --network ${DOCKER_NETWORK_NAME} -d \
                        -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                        -p ${MYSQL_PORT}:3306 \
                        ${MYSQL_IMAGE}
                        """
                    }

                    def maxAttempts = 30
                    def attempt = 0
                    def mysqlReady = false

                    while (attempt < maxAttempts) {
                        sleep(5)
                        attempt++
                        def pingResult = sh(script: "docker exec ${MYSQL_CONTAINER_NAME} mysqladmin -uroot -p${MYSQL_ROOT_PASSWORD} ping", returnStatus: true)
                        if (pingResult == 0) {
                            mysqlReady = true
                            break
                        } else {
                            echo "Attempt ${attempt}: Waiting for MySQL to become ready..."
                        }
                    }

                    if (!mysqlReady) {
                        error "MySQL did not become ready in time."
                    }

                    def endTime = System.currentTimeMillis()
                    env.MYSQL_SETUP_DURATION = (endTime - startTime) / 1000
                    env.MYSQL_SETUP_STATUS = 'SUCCESS'
                    echo "MySQL is ready! Setup took ${env.MYSQL_SETUP_DURATION} seconds."
                }
            }
            post {
                failure {
                    script {
                        env.MYSQL_SETUP_STATUS = 'FAILURE'
                    }
                }
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

        stage('Run Spring Boot Application') {
            steps {
                script {
                    sh """
                    docker run --name springboot --network ${DOCKER_NETWORK_NAME} -d \
                    -e SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQL_CONTAINER_NAME}:${MYSQL_PORT}/${MYSQL_DATABASE}?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC \
                    -e SPRING_DATASOURCE_USERNAME=${MYSQL_USERNAME} \
                    -e SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD} \
                    -p 8082:8082 \
                    kaissgh11/foyer:latest
                    """
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

