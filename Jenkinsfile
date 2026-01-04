pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK23'
    }

    environment {
        // Docker içinden bilgisayarına erişim adresi
        BASE_URL = 'http://host.docker.internal:5173'
    }

    stages {
        stage('Backend Derleme') {
            steps {
                // 'backend' klasörüne girip derleme yapıyoruz
                dir('backend') {
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Selenium Testleri') {
            steps {
                // Testleri çalıştırmak için yine 'backend' klasörüne giriyoruz
                dir('backend') {
                    bat 'mvn test -Dheadless=true -DbaseUrl="http://host.docker.internal:5173"'
                }
            }
        }
    }

    post {
        always {
            // Raporları toplarken de klasör içine bakıyoruz
            dir('backend') {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}