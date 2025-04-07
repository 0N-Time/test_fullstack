# Stage 1: Build React app
FROM node:20-alpine AS builder

WORKDIR /app

# Copy everything (or first package.json + lock)
COPY package*.json ./

# Ensure dev dependencies get installed
ENV NODE_ENV=development
RUN npm install

# Copy rest of the code
COPY . .

# Build the app
RUN npm run build
