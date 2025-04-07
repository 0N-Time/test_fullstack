# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

WORKDIR /app

# Copy package files first
COPY package*.json ./

# Install all deps including dev tools like tsc, vite
ENV NODE_ENV=development
RUN npm install

# Copy the rest
COPY . .

# Build
RUN npm run build


# ---------- Stage 2: Serve via Nginx ----------
FROM nginx:stable-alpine AS production

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
