# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

# 1. Set safer directory permissions upfront
RUN mkdir -p /app && chown -R node:node /app

# 2. Install system dependencies and update npm
RUN apt-get update && apt-get install -y \
    build-essential \
    python3 \
    && rm -rf /var/lib/apt/lists/* \
    && npm install -g npm@10.8.2 --unsafe-perm

WORKDIR /app

# 3. Copy package files with node user ownership
COPY --chown=node:node package*.json ./

# 4. Install dependencies with clean environment
USER node
RUN npm config set update-notifier false \
    && npm cache clean --force \
    && npm ci --include=dev

# 5. Copy application files
COPY --chown=node:node . .

# 6. Build the application
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