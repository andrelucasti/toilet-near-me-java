# toilet-near-me-java

# infrastructure

### 1 - Create the cluster using kind 
```shell
kind create cluster --config kind-config.yaml --name programmingonmars
```

## In the cluster is needed to install the following tools:

### 1 - Install prometheus operator
```shell
kubectl create ns monitoring
helm install prometheus prometheus-community/kube-prometheus-stack -f infraascode/prometheus/prometheus.yaml --namespace monitoring
```

### 2 - Install argocd
```shell
kubectl create ns argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```
### 3 - Install Kong
```shell
kubectl create ns kong
helm install kong kong/kong -f infraascode/kong/kong-conf.yaml --set proxy.type=NodePort,proxy.http.nodePort=30000,proxy.tls.nodePort=30003 --set ingressController.installCRDs=false --set serviceMonitor.enabled=true --set serviceMonitor.labels.release=prometheus --namespace kong
```

### 4 - Install Postgres to application to use
```shell
kubectl create ns postgres
helm install postgresql bitnami/postgresql --version 15.5.28 --namespace postgres --set auth.database=toilet --set auth.password=toilet --set auth.username=toilet
```

### 5 - Add application to argocd
```shell
kubectl apply -n argocd -f infraascode/argocd/toilet-argo.yaml
```

### 6 - Create tests environment
```shell
kubectl create ns testkube
helm install testkube kubeshop/testkube --set testkube-api.prometheus.enabled=true -n testkube
```

### 7 - Run tests
```shell
kubectl testkube create test --file load-test/create-toilet.js --type k6/script --name create-toilet
kubectl testkube run test create-toilet -f
```
