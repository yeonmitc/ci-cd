pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git (branch: 'master'
                    , url:'https://github.com/yu3papa/guestbook.git')
            }
        }
    }
}
