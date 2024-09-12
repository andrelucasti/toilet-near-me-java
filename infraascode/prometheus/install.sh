#!/bin/bash
kubectl create ns monitoring
helm install prometheus prometheus-community/kube-prometheus-stack -f prometheus.yaml --namespace monitoring