# Use an official Node.js runtime as the base image
FROM node:20-alpine

# Set the working directory in the container to /app
WORKDIR /app

# Copy package.json and pnpm-lock.yaml to the working directory
COPY package*.json pnpm-lock.yaml ./

# Install pnpm globally in the container
RUN npm install -g pnpm

# Install any needed packages specified in package.json
RUN pnpm install

# Bundle the app source inside the Docker image
COPY . .

# Make port 3000 available to the world outside the container
EXPOSE 3000

# Define the command to run the app
CMD [ "node", "index.js" ]
