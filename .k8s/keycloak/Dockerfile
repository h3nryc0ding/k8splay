FROM node:20-alpine AS keywind-builder
RUN apk add --no-cache git \
    && npm install -g pnpm
WORKDIR /build
RUN git clone --depth 1 https://github.com/lukin/keywind.git . \
    && pnpm install \
    && pnpm build \
    && pnpm build:jar

FROM quay.io/keycloak/keycloak:24.0 AS keycloak-builder
ENV KC_HEALTH_ENABLED=true \
    KC_METRICS_ENABLED=true \
    KC_DB=postgres
COPY --from=keywind-builder /build/out/keywind.jar /opt/keycloak/providers/keywind.jar
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:24.0
COPY --from=keycloak-builder /opt/keycloak/ /opt/keycloak/
ENV KC_PROXY=edge \
    KC_HOSTNAME_STRICT=false
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--optimized"]
