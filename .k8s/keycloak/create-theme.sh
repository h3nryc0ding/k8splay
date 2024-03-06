#!/bin/bash

# Run the submodule
cd keywind
pnpm install
pnpm build
pnpm build:jar

cp ./out/keywind.jar ../keywind.jar