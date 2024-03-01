# Project Structure

The Kubernetes directory is divided into two main directories: `/app` and `/keycloak`.

## LetsEncrypt

The `lets-encrypt.yaml` file contains the configuration for the Let's Encrypt certificate issuer. It is used to
automatically generate SSL certificates for the application.

To deploy it, run the following command:

```bash
kubectl apply -f lets-encrypt.yaml
```

## Application

The `/app` directory contains templates for both the backend and frontend applications. It also sets up an ingress and a
MongoDB instance for the backend.

### Deploying the Latest Version

The Helm chart for this project is not generic and is specifically tailored for this project. It uses specific image
tags and other parameters that may not work with other projects.

To deploy the latest version of the application, follow these steps:

1. Navigate to the `app` directory:

```bash
cd app
```

1. Install the Helm chart:

```bash
helm install <release_name> app \
--namespace <namespace> \
--set ingress.domain=<your_domain> \
--set app.image.tag=<image_tag> \
--set keycloak.realm=<realm_name> \
--wait \
--atomic \
--timeout 600s
```

Replace `<release_name>`, `<namespace>`, `<your_domain>`, `<image_tag>`, and `<realm_name>` with your specific values.

## Keycloak

The `/keycloak` directory contains the configuration for the Keycloak instance. It
uses [Keywind](https://github.com/lukin/keywind) as a theme.

### Deployment

To inject the theme into the Keycloak instance, a ConfigMap is created. Follow these steps to deploy it:

1. Navigate to the `keycloak` directory:

```bash
cd keycloak
```

1. Run the `create-theme.sh` script:

```bash
./create-theme.sh
```

1. Apply the Keycloak theme configuration:

```bash
kubectl apply -f keycloak-theme.yaml
```

1. Finally, apply the Keycloak configuration:

```bash
kubectl apply -f keycloak.yaml
```
