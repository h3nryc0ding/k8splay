FROM node:20-alpine AS keywind

RUN apk add --no-cache git \
    && npm install -g pnpm

RUN git clone https://github.com/lukin/keywind.git \
    && cd keywind \
    && pnpm install \
    && pnpm build \
    && pnpm build:jar

FROM quay.io/keycloak/keycloak:latest AS builder

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_DB=postgres

COPY --from=keywind keywind/out/keywind.jar /opt/keycloak/providers/keywind.jar

RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_PROXY="edge"
ENV KC_DB="postgres"
ENV KC_HOSTNAME_STRICT=false

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--optimized"]