# Demo

## Create the app
```
quarkus create app de.codepitbull.josdk:demo --extension=quarkus-operator-sdk --gradle-kotlin-dsl
```

### K3d
```
k3d cluster delete k3s-default
k3d cluster create 
kubectl cluster-info
export KUBECONFIG=$(k3d kubeconfig write k3s-default)
```

## Telepresence

Install telepresence
```
brew install datawire/blackbird/telepresence
```

Install telepresence into cluster
```
telepresence helm install
telepresence connect
```

### Check stuff

```
kubectl get crd
```