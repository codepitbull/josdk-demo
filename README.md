# Demo

## Create the app
```
quarkus create app de.codepitbull.josdk:demo --extension=quarkus-operator-sdk --gradle-kotlin-dsl
```

### K3d
```
k3d cluster delete k3s-default -p "8081:80@loadbalancer" --agents 2
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


### Manual work

``` 
k3d cluster create --api-port 6550 -p "8081:80@loadbalancer" --agents 2
export KUBECONFIG="$(k3d kubeconfig write k3s-default)"
kubectl create deployment nginx --image=nginx
kubectl create service clusterip nginx --tcp=80:80
kubectl apply -f src/main/resources/ingress.yaml
```