pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK23'
    }

    environment {
        BASE_URL = 'http://localhost:5173'
    }

    stages {
        stage('Build ') {
            steps {
                dir('backend') {
                    echo ' Proje build ediliyor (Testler atlanıyor)...'
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Birim (Unit) Testleri') {
            steps {
                dir('backend') {
                    echo ' Sadece Birim Testler çalışıyor...'
                    bat 'mvn test -Dtest=!YurtSystemE2ETest,!*IntegrationTest -DfailIfNoTests=false'
                }
            }
        }

        stage('Entegrasyon Testleri') {
            steps {
                dir('backend') {
                    echo ' Entegrasyon Testleri çalışıyor...'
                    bat 'mvn test -Dtest=*IntegrationTest -DfailIfNoTests=false'
                }
            }
        }

        stage('Selenium (E2E) Testleri') {
            steps {
                dir('backend') {
                    echo ' Robot (Selenium) Testleri Headless Modda Başlatılıyor...'
                    bat 'mvn test -Dtest=YurtSystemE2ETest -Dheadless=true -DfailIfNoTests=false'
                }
            }
        }
    }

    post {
        always {
            dir('backend') {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}