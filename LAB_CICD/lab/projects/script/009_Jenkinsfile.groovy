pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url:'https://github.com/yu3papa/guestbook.git'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }
        stage('Unit Test') {
            steps {
                sh './mvnw test'
            }
            
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps{
                withSonarQubeEnv('SonarQube-Server'){
                    sh '''
                        ./mvnw sonar:sonar \
                        -Dsonar.projectKey=guestbook \
                        -Dsonar.host.url=http://172.31.0.120:9000 \
                        -Dsonar.login=eb91ad3f657c5abda75ce5f624a3ed39439c29d6
                    '''
                }
            }
        }
        stage('SonarQube Quality Gate'){
            steps{
                timeout(time: 1, unit: 'MINUTES') {
                    script{
                        def qg = waitForQualityGate()
                        if(qg.status != 'OK') {
                            echo "NOT OK Status: ${qg.status}"
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        } else{
                            echo "OK Status: ${qg.status}"
                        }
                    }
                }
            }
        }
    }
}

