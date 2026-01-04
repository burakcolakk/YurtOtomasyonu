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
                        dir('backend') {
                            // !YurtSystemE2ETest diyerek Selenium testini şimdilik atlıyoruz
                            // Sadece veritabanı testleri çalışsın
                            bat 'mvn test -Dtest=!YurtSystemE2ETest'
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