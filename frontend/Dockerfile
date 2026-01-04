# --- AŞAMA 1: Build (İnşa Et) ---
FROM node:20-alpine as buildStage
WORKDIR /app
# Önce paket listesini kopyala ve yükle (Cache optimizasyonu için)
COPY package*.json ./
RUN npm install
# Sonra tüm kaynak kodunu kopyala
COPY . .
# Projeyi derle (dist klasörü oluşur)
RUN npm run build

# --- AŞAMA 2: Production (Sunum) ---
FROM nginx:alpine
# Build aşamasında oluşan 'dist' klasörünü Nginx'in sunacağı yere kopyala
COPY --from=buildStage /app/dist /usr/share/nginx/html
# Nginx ayar dosyamızı kopyala
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]