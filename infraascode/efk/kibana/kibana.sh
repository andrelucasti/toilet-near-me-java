#!/bin/bash
helm install kibana elastic/kibana \
  --version=8.5.1 \
  --namespace=logs \
  --set service.type=NodePort \
  --set service.nodePort=31000
