# Build stage
FROM node:20-alpine AS builder
WORKDIR /app

# Install pnpm
RUN npm install -g pnpm

# Copy package files and install dependencies
COPY package.json ./
RUN pnpm install

# Copy source files
COPY . .

# Build the application
RUN pnpm run build

# Remove development dependencies
RUN pnpm prune --prod

# Production stage
FROM node:18-alpine
WORKDIR /app

# Copy built files and production dependencies from the build stage
COPY --from=builder /app/build ./build/
COPY --from=builder /app/node_modules ./node_modules/

# Copy package.json
COPY package.json .

# Expose the port the app runs in
EXPOSE 3000

# Set NODE_ENV environment variable to "production"
ENV NODE_ENV=production

# Serve the app
CMD [ "node", "build" ]