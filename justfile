#!/usr/bin/env just --justfile

set quiet

push:
    git add -A && (git diff --quiet HEAD || git commit -am "WIP") && git push origin main

pull:
    git pull

build:
    bash ../scripts/update-internal-catalog-versions.sh . && ./gradlew build

license-audit:
    bash ../scripts/run-license-audit.sh swissknife

license-audit-compact:
    bash ../scripts/run-license-audit.sh swissknife --compact

generate-sbom:
    bash ../scripts/run-generate-sbom.sh swissknife

cleanup:
    bash ../scripts/cleanup-maven-local.sh --repo-root . --keep 5 --max-age-days 30

update-internal-dependencies:
    bash ../scripts/update-internal-catalog-versions.sh .

rebuild:
    ./gradlew clean build --refresh-dependencies --rerun-tasks

publish:
    ./scripts/publish-if-changed.sh

update-dependencies:
    ./gradlew versionCatalogUpdate

@update-gradle:
    ./scripts/update-gradle.sh

@update-container-versions:
    ./scripts/update-container-versions.sh

update-all:
    just update-internal-dependencies && just update-dependencies && just update-gradle && just update-container-versions

workflow +steps:
    bash ../scripts/run-just-workflow.sh {{steps}}
