# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

WORKDIR /app

# 1. First update npm to latest version
RUN npm install -g npm@latest

# 2. Copy package files with proper ownership
COPY --chown=node:node package*.json ./

# 3. Install dependencies with clean cache
RUN npm ci --include=dev --no-audit --prefer-offline

# 4. Copy application files with proper ownership
COPY --chown=node:node . .

# 5. Build with node user
USER node
RUN npm run build

# ---------- Stage 2: Serve with Nginx ----------
FROM nginx:stable-alpine AS production

# Remove the default Nginx html content
RUN rm -rf /usr/share/nginx/html/*

# Copy the built files from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose the default HTTP port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]