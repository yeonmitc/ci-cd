pipeline {
    agent any
    stages { 
        stage('SSH Staging Server') {
            steps {
                sshagent(credentials: ['Staging-PrivateKey']) {
                    sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.0.110 whoami"
                    sh "ssh -o StrictHostKeyChecking=no ec2-user@172.31.0.110 env"
                }
            }
        }
    }
}
