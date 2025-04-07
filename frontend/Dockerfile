# ---------- Stage 1: Build React App ----------
FROM node:20-slim AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install ALL dependencies including devDependencies
RUN npm ci --production=false  # 👈 This flag forces devDeps installation

# Copy the rest of the app
COPY . .

# Build the application
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