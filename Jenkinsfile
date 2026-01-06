pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK23'
    }

    environment {
        // Jenkins Windows üzerinde çalıştığı için localhost kullanabiliriz
        // Eğer Selenium kodun bu değişkeni okuyorsa harika, okumuyorsa kodda localhost:5173 olmalı.
        BASE_URL = 'http://localhost:5173'
    }

    stages {
        stage('Backend Derleme') {
            steps {
                dir('backend') {
                    // Derlerken testleri atlayalım, zaman kazanalım.
                    // Testleri bir sonraki aşamada zaten yapacağız.
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Tüm Testler (Unit + Integration + Selenium)') {
                    steps {
                        dir('backend') {
                            echo '🚀 Selenium Testleri Headless Modda Başlatılıyor...'
                            // DİKKAT: "-Dheadless=true" parametresini ekledik!
                            // Bu parametre Java kodundaki "if (headless)" bloğunu çalıştırır.
                            bat 'mvn test -Dheadless=true'
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