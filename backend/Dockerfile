# HATA ÇÖZÜMÜ: openjdk yerine eclipse-temurin kullanıyoruz
FROM eclipse-temurin:23-jdk

WORKDIR /app

# JAR dosyasını kopyala
COPY target/*.jar app.jar

# Portu aç
EXPOSE 8181

# Başlat
ENTRYPOINT ["java", "-jar", "app.jar"]