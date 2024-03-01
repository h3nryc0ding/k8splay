#!/bin/bash

# Run the submodule
cd keywind
pnpm install
pnpm build
pnpm build:jar

# Encode the file in base64
encoded_file=$(base64 -w 0 out/keywind.jar)

# Create the YAML file
cat <<-EOF > ../keycloak-theme.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: keycloak-theme
  namespace: shared
binaryData:
  keywind.jar: $encoded_file
EOF
