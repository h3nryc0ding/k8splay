include ../env.mk

export MONGO_PORT 		= 27017
export KEYCLOAK_PORT 	= 9090

export MONGO_URI 		= mongodb://localhost:$(MONGO_PORT)
export OAUTH2_CLIENT_ID	= default
export OAUTH2_ISSUER_URI= http://localhost:$(KEYCLOAK_PORT)/realms/$(OAUTH2_CLIENT_ID)

help: ## Display this help message
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

install: build.gradle.kts ## Install dependencies
	./gradlew dependencies

dev: install ## Start the development environment
	./gradlew bootRun

clean: ## Clean the project
	./gradlew clean
	rm -rf .gradle
	rm -rf build
	rm -rf .kotlin
