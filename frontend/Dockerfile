# ---------- Stage 1: Build React App ----------
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first
COPY package*.json ./

# Install dev dependencies (tsc, vite etc.)
ENV NODE_ENV=development
RUN npm install

# Copy rest of the app
COPY . .

# Build the app
RUN npm run build


# ---------- Stage 2: Serve with Nginx ----------
FROM nginx:stable-alpine AS production

# Clean default nginx html
RUN rm -rf /usr/share/nginx/html/*

# Copy build output from builder
COPY --from=builder /app/dist /usr/share/nginx/html

# Optional: Add custom nginx.conf if needed
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
