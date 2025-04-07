# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

WORKDIR /app

# Copy only package files for better caching
COPY package*.json ./

# Install all dependencies including devDependencies
RUN npm install --include=dev

# Manually ensure typescript is installed
RUN npm install typescript --save-dev

# Add tsc and vite to PATH
ENV PATH="./node_modules/.bin:$PATH"

# Copy the rest of the app
COPY . .

# Build
RUN npm run build


# ---------- Stage 2: Serve with Nginx ----------
FROM nginx:stable-alpine AS production

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
