# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install ALL dependencies (including tsc)
RUN npm install --include=dev

# Add node_modules/.bin to PATH for tsc to work
ENV PATH="./node_modules/.bin:$PATH"

# Copy source code
COPY . .

# Run the build
RUN npm run build


# ---------- Stage 2: Serve with Nginx ----------
FROM nginx:stable-alpine AS production

RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
