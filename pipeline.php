pipeline {
    agent any
    stages {
        stage ('hello') {
            steps {
                echo "Hello from jenkins pipeline"
            }
        }
    }
}

---

pipeline {
    agent any
    stages {
        stage ('code') {
            steps {
                echo "Getting code from git.."
            }
        }
        stage ('build') {
            steps {
                echo "Building using maven.."
            }
        }
        stage ('deploy') {
            steps {
                echo "Deploying to tomcat.."
            }
        }
    }
}

---

pipeline {
    agent any
    stages {
        stage ('command-1') {
            steps {
                sh 'date'
            }
        }
        stage ('command-2') {
            steps {
                sh 'df -Th'
            }
        }
    }
}

---

pipeline {
    agent any
    stages {
        stage ('hello') {
            steps {
                sh'''
                ls
                df -Th
                hostname
                id
                ''' 
            }
        }
    }
}

---

pipeline {
    agent any
    stages {
        stage ('hello') {
            steps {
                script {
                    def name = "Jenkins"
                    echo "Hello, ${name}!"
                }
            }
        }
    }
}

---

pipeline {
    agent any
    tools {
        maven "maven"
    }
    stages {
        stage ('checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/avizway1/awar05-jenkins.git'
            }
        }
        stage ('build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
    }
}

---

