# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install everything — dependencies + devDependencies
RUN npm install --include=dev

# Just to be safe: force install typescript manually
RUN npm install typescript --save-dev

# Add local bin to PATH for tsc and vite
ENV PATH="./node_modules/.bin:$PATH"

# Copy the source
COPY . .

# Build
RUN npm run build


# ---------- Stage 2: Serve with Nginx ----------
FROM nginx:stable-alpine AS production

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
