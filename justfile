#!/usr/bin/env just --justfile

set quiet

push:
    git add -A && (git diff --quiet HEAD || git commit -am "WIP") && git push origin main

pull:
    git pull

build:
    ./gradlew build

rebuild:
    ./gradlew clean build --refresh-dependencies --rerun-tasks

publish:
    ./gradlew publishToMavenLocal

update-dependencies:
    ./gradlew versionCatalogUpdate

@update-gradle:
    #!/usr/bin/env bash
    set -euo pipefail
    ./gradlew wrapper --gradle-version latest --distribution-type all
    DIST_URL=$(grep distributionUrl gradle/wrapper/gradle-wrapper.properties | cut -d= -f2 | sed 's/\\//g')
    CHECKSUM=$(curl -sL "${DIST_URL}.sha256")
    sed -i '' "s/distributionSha256Sum=.*/distributionSha256Sum=${CHECKSUM}/" gradle/wrapper/gradle-wrapper.properties
    echo "Updated wrapper checksum: ${CHECKSUM}"

@update-container-versions:
    #!/usr/bin/env bash
    set -euo pipefail
    props="container-versions.properties"

    echo "Checking for container image version updates..."

    # Fetch latest versions from Docker Hub / GitHub
    fetch_latest_tag() {
        local image="$1"
        local filter="${2:-}"
        if [ -n "$filter" ]; then
            curl -s "https://hub.docker.com/v2/repositories/library/$image/tags?page_size=100&ordering=last_updated" 2>/dev/null \
                | grep -o '"name":"[^"]*"' | sed 's/"name":"//;s/"//' | grep "$filter" | head -1
        else
            curl -s "https://hub.docker.com/v2/repositories/library/$image/tags?page_size=10&ordering=last_updated" 2>/dev/null \
                | grep -o '"name":"[^"]*"' | sed 's/"name":"//;s/"//' | grep -v latest | head -1
        fi
    }

    fetch_latest_org_tag() {
        local org="$1"
        local image="$2"
        curl -s "https://hub.docker.com/v2/repositories/$org/$image/tags?page_size=10&ordering=last_updated" 2>/dev/null \
            | grep -o '"name":"[^"]*"' | sed 's/"name":"//;s/"//' | grep -E '^[0-9]+\.[0-9]+\.[0-9]+$' | head -1
    }

    update_version() {
        local key="$1" current="$2" latest="$3"
        if [ -n "$latest" ] && [ "$current" != "$latest" ]; then
            echo "  $key: $current → $latest"
            sed -i '' "s/^$key=.*/$key=$latest/" "$props"
        else
            echo "  $key: $current (up to date)"
        fi
    }

    # Read current versions
    source "$props"

    # Check each container
    latest_trivy=$(fetch_latest_org_tag "aquasec" "trivy")
    latest_pulsar=$(fetch_latest_org_tag "apachepulsar" "pulsar")
    latest_keycloak=$(fetch_latest_org_tag "keycloak" "keycloak")
    latest_postgres=$(fetch_latest_tag "postgres" "^[0-9]*$")
    latest_nats=$(fetch_latest_tag "nats" "^alpine")

    update_version "trivy" "$trivy" "$latest_trivy"
    update_version "pulsar" "$pulsar" "$latest_pulsar"
    update_version "keycloak" "$keycloak" "$latest_keycloak"
    update_version "postgres" "$postgres" "$latest_postgres"
    update_version "nats" "$nats" "$latest_nats"

    # Sync versions into source code
    sed -i '' "s/DEFAULT_TRIVY_VERSION = \"[^\"]*\"/DEFAULT_TRIVY_VERSION = \"$(grep '^trivy=' "$props" | cut -d= -f2)\"/" \
        modules/security/scan/src/main/kotlin/sollecitom/libs/swissknife/security/scan/TrivyImageScanner.kt
    sed -i '' "s/DEFAULT_PULSAR_DOCKER_IMAGE_VERSION = \"[^\"]*\"/DEFAULT_PULSAR_DOCKER_IMAGE_VERSION = \"$(grep '^pulsar=' "$props" | cut -d= -f2)\"/" \
        modules/pulsar/test/utils/src/main/kotlin/sollecitom/libs/swissknife/pulsar/test/utils/PulsarContainerExtensions.kt
    sed -i '' "s/defaultImageVersion = \"[0-9][0-9]*\.[0-9]*\.[0-9]*\"/defaultImageVersion = \"$(grep '^keycloak=' "$props" | cut -d= -f2)\"/" \
        modules/keycloak/container/src/main/kotlin/sollecitom/libs/swissknife/keycloak/container/Keycloak.kt
    sed -i '' "s/const val defaultImageVersion = \"[0-9]*\"/const val defaultImageVersion = \"$(grep '^postgres=' "$props" | cut -d= -f2)\"/" \
        modules/sql/postgres/container/src/main/kotlin/sollecitom/libs/swissknife/sql/postgres/container/PostgresContainer.kt
    sed -i '' "s/DEFAULT_IMAGE_VERSION = \"[^\"]*\"/DEFAULT_IMAGE_VERSION = \"$(grep '^nats=' "$props" | cut -d= -f2)\"/" \
        modules/nats/test/utils/src/main/kotlin/sollecitom/libs/swissknife/nats/test/utils/NatsContainerAdapter.kt

    echo "Done."

update-all:
    just update-dependencies && just update-gradle && just update-container-versions
