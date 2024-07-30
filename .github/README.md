# GitHub Actions Configuration

This directory contains the GitHub Actions workflows and custom actions for the project. Below is an overview of the
structure and the purpose of each file and directory.

## Custom Actions

The `actions` directory contains custom GitHub Actions used in workflows.

- [setup-java-gradle](actions/setup-java-gradle/action.yml): Custom action to set up Java and Gradle environment.
- [setup-playwright](actions/setup-playwright/action.yml): Custom action to set up Playwright for end-to-end testing.
- [setup-pnpm-node](actions/setup-pnpm-node/action.yml): Custom action to set up PNPM and Node.js environment.

## Dependabot Configuration

- [dependabot.yml](dependabot.yml): Configuration file for Dependabot, which automates dependency updates.

## Workflows

The `workflows` directory contains GitHub Actions workflow files. These are organized into Continuous Integration (CI)
and Continuous Deployment (CD) workflows.

### Continuous Integration (CI)

- [ci-backend.yml](workflows/ci-backend.yml): CI workflow for backend code.
    - Running unit and integration tests
    - Linting and static code analysis

- [ci-frontend.yml](workflows/ci-frontend.yml): CI workflow for frontend code.
    - Running unit and end-to-end tests
    - Linting and static code analysis

### Continuous Deployment (CD)

#### Production

- [cd-prod-deploy.yml](workflows/cd-prod-deploy.yml): Deployment workflow for the production environment.
    - Running final pre-deployment checks
    - Creating a release or tagging the code
    - Deploying to the production environment

#### Test

- [cd-test-deploy.yml](workflows/cd-test-deploy.yml): Deployment workflow for the test environment.
    - Spinning up a new environment
    - Deploying the latest code from the PR
    - Providing a URL for reviewers to test the changes

- [cd-test-cleanup.yml](workflows/cd-test-cleanup.yml): Cleanup workflow for the test environment after a PR is closed.
    - Identifying resources associated with the closed PR
    - Destroying the temporary environment
    - Removing related cloud resources

#### Keycloak

- [cd-keycloak-deploy.yml](workflows/cd-keycloak-deploy.yml): Deployment workflow for the Keycloak environment.
    - Building and deploying the Keycloak configuration
