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
        stage('Backend Derleme') {
            steps {
                dir('backend') {
                    echo '📦 Proje derleniyor (Testler atlanıyor)...'
                    // -DfailIfNoTests=false: Test yoksa bile build patlamasın
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Birim (Unit) Testleri') {
            steps {
                dir('backend') {
                    echo '🧪 Sadece Birim Testler çalışıyor...'
                    // Mantık: E2E ve Integration testleri HARİÇ (!) her şeyi çalıştır.
                    // ! işareti "Hariç" demektir.
                    bat 'mvn test -Dtest=!YurtSystemE2ETest,!*IntegrationTest -DfailIfNoTests=false'
                }
            }
        }

        stage('Entegrasyon Testleri') {
            steps {
                dir('backend') {
                    echo '🔗 Entegrasyon Testleri çalışıyor...'
                    // Mantık: Sadece ismi "IntegrationTest" ile bitenleri çalıştır.
                    bat 'mvn test -Dtest=*IntegrationTest -DfailIfNoTests=false'
                }
            }
        }

        stage('Selenium (E2E) Testleri') {
            steps {
                dir('backend') {
                    echo '🤖 Robot (Selenium) Testleri Headless Modda Başlatılıyor...'
                    // Mantık: Sadece "YurtSystemE2ETest" dosyasını çalıştır.
                    // Headless mod parametresini buraya ekledik.
                    bat 'mvn test -Dtest=YurtSystemE2ETest -Dheadless=true -DfailIfNoTests=false'
                }
            }
        }
    }

    post {
        always {
            dir('backend') {
                // Hangi aşamada olursa olsun raporları topla
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}