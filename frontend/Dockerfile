FROM node:20-slim AS builder

WORKDIR /app

COPY package*.json ./

ENV NODE_ENV=development
RUN npm install

# Add local bin to PATH
ENV PATH="./node_modules/.bin:$PATH"

COPY . .

RUN npm run build

FROM nginx:stable-alpine AS production

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
