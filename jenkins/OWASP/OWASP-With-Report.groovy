#OWASP require HTMLPublisher
#e375da58-e43c-4802-b713-134b9b9fc517

pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        // Tomcat details
        TOMCAT_HOST = '172.31.33.34'
        TOMCAT_PORT = '8080'
        APP_CONTEXT = '/Springdemo-0.0.1-SNAPSHOT'

        // SonarQube details
        SONAR_HOST_URL     = 'http://172.31.26.201:9000/'
        SONAR_PROJECT_KEY  = 'myprodcode'
        SONAR_PROJECT_NAME = 'prod-app-code'
    }

    stages {
        stage('checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/avizway1/awar05-jenkins.git'
            }
        }

        stage('build') {
            steps {
                sh '''
                    echo "Building my code using Maven..."
                    mvn clean install
                '''
            }
        }

        stage('test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('dependency-check') {
            steps {
                sh '''
                    echo "Running OWASP Dependency-Check..."
                    mvn org.owasp:dependency-check-maven:10.0.4:check
                '''
            }
        }

        stage('publish-owasp-report') {
            steps {
                publishHTML(target: [
                    reportDir: 'target',
                    reportFiles: 'dependency-check-report.html',
                    reportName: 'OWASP Dependency-Check Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }

        stage('sonar-analysis') {
            steps {
                withCredentials([
                    string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')
                ]) {
                    sh '''
                        echo "Running SonarQube analysis..."

                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                          -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                          -Dsonar.projectName=$SONAR_PROJECT_NAME \
                          -Dsonar.host.url=$SONAR_HOST_URL \
                          -Dsonar.token=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('deploy-to-tomcat') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'tomcat',
                        usernameVariable: 'TOMCAT_USER',
                        passwordVariable: 'TOMCAT_PASS'
                    )
                ]) {
                    sh '''
                        echo "Finding WAR file..."
                        WAR_FILE=$(ls target/*.war | head -n 1)
                        echo "Deploying $WAR_FILE to Tomcat..."

                        curl --fail -u "$TOMCAT_USER:$TOMCAT_PASS" \
                          -T "$WAR_FILE" \
                          "http://$TOMCAT_HOST:$TOMCAT_PORT/manager/text/deploy?path=$APP_CONTEXT&update=true"

                        echo "Deployment triggered at: http://$TOMCAT_HOST:$TOMCAT_PORT$APP_CONTEXT"
                    '''
                }
            }
        }
    }

    post {
        success {
            withCredentials([
                string(credentialsId: 'webhook', variable: 'SLACK_WEBHOOK_URL')
            ]) {
                sh '''
                    SONAR_DASHBOARD_URL="${SONAR_HOST_URL}/dashboard?id=${SONAR_PROJECT_KEY}"

                    PAYLOAD=$(cat <<EOF
{
  "text": "✅ *Deployment SUCCESS*\\n• Job: ${JOB_NAME}\\n• Build: #${BUILD_NUMBER}\\n• App: http://${TOMCAT_HOST}:${TOMCAT_PORT}${APP_CONTEXT}\\n• SonarQube: ${SONAR_DASHBOARD_URL}\\n• Channel: #webhook-test"
}
EOF
                    )

                    curl -X POST -H 'Content-type: application/json' \
                         --data "$PAYLOAD" \
                         "$SLACK_WEBHOOK_URL"
                '''
            }
        }

        failure {
            withCredentials([
                string(credentialsId: 'webhook', variable: 'SLACK_WEBHOOK_URL')
            ]) {
                sh '''
                    PAYLOAD=$(cat <<EOF
{
  "text": "❌ *Deployment FAILED*\\n• Job: ${JOB_NAME}\\n• Build: #${BUILD_NUMBER}\\n• Console: ${BUILD_URL}\\n• Channel: #webhook-test"
}
EOF
                    )

                    curl -X POST -H 'Content-type: application/json' \
                         --data "$PAYLOAD" \
                         "$SLACK_WEBHOOK_URL"
                '''
            }
        }
    }
}