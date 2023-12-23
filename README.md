# microk8s_plaground

## Steps

Apply services.
```bash
microk8s kubectl apply -f backend-service.yml
microk8s kubectl apply -f frontend-service.yml
```
Apply ingress.
```bash
microk8s kubectl apply -f ingress.yml
```

Apply new images:
```bash
kubectl rollout restart deployment/backend
kubectl rollout restart deployment/frontend
```

## Guides

https://benbrougher.tech/posts/microk8s-ingress/
https://medium.com/geekculture/run-and-deploy-containers-in-k8s-for-free-in-oracle-cloud-a362b9ec5b1f

## VM Config

https://github.com/kubernetes/minikube/issues/4350
https://github.com/canonical/microk8s/issues/854
