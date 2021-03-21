#!/usr/bin/env bash
kubectl create namespace hw02
helm install --replace --namespace=hw02 hw02-library-app ./hw02-library-app