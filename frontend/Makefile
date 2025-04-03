include ../env.mk

help: ## Display this help message
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

install: package.json pnpm-lock.yaml ## Install dependencies
	pnpm install

dev: install ## Start the development environment
	pnpm dev

clean: ## Clean the project
	rm -rf node_modules
	rm -rf build
	rm -rf .svelte-kit
	rm -rf \$$houdini
