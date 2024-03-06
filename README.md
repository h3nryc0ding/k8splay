# MicroK8s Playground

## Motivation

This repository is a playground for me to experiment with Kubernetes and its ecosystem. It is a full stack application
with a Spring Boot backend, a SvelteKit frontend, and a Keycloak server for authentication.

## Features

- [x] GraphQL API
- [x] SSO Authentication
- [x] SSL Encryption using Let's Encrypt and cert-manager
- [x] WebSocket based live chat

### Backend

Deployed at [api.k8splay.xyz](https://api.k8splay.xyz/graphiql)

- [x] Reactive Spring Boot with WebFlux and Kotlin
- [x] Persistence with R2DBC and MongoDB
- [x] Authentication and Authorization using Keycloak and oAuth2
- [x] GraphiQL available at `/graphiql`

### Frontend

Deployed at [k8splay.xyz](https://k8splay.xyz)

- [x] SvelteKit
- [x] ShadCN and TailwindCSS for styling
- [x] Houdini GraphQL client for SSR
- [x] Authenticated routes
- [x] All pages support SSR with in-cluster backend requests

### Keycloak

Deployed at [accounts.k8splay.xyz](https://accounts.k8splay.xyz)

- [x] Optimized Docker image
- [x] Persistence with PostgreSQL
- [x] Custom theme

### DevOps

Deployed at `<PR>.test.k8splay.xyz`. Please replace `<PR>` with the PR number, e.g. `pr-1.test.k8splay.xyz`. The API is
deployed at `api.<PR>.test.k8splay.xyz`. The deployment is automatically deleted when the PR is closed.

Implemented using GitHub Actions:

- [x] Automated **prod deployment**
- [x] Automated **test deployments** on pull requests
- [x] Automated **linting** and **testing**
