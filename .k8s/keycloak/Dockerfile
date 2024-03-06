FROM quay.io/keycloak/keycloak:latest as builder

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

ENV KC_DB=postgres

WORKDIR /opt/keycloak

ADD --chown=keycloak:keycloak keywind.jar /opt/keycloak/providers/keywind.jar

RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_PROXY="edge"
ENV KC_DB="postgres"
ENV KC_DB_URL="jdbc:postgresql://keycloak-db:5432/keycloak"
ENV KC_DB_USERNAME="keycloak"
ENV KC_DB_PASSWORD="xJV&xQgZZyVpQ8ntoLxW3sg8"
ENV KC_HOSTNAME_STRICT=false

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--optimized"]