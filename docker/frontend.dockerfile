FROM node:18-alpine AS build

WORKDIR /app

COPY frontend/ZeroWaste/package*.json ./
RUN npm install

COPY ./frontend/ZeroWaste .

RUN npm install -g @angular/cli

RUN ng build --configuration=production

FROM nginx:alpine

COPY ./docker/nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist/zero-waste/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]