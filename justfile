#!/usr/bin/env just --justfile

set quiet

push:
    git add -A && (git diff --quiet HEAD || git commit -am "WIP") && git push origin main

pull:
    git pull

build:
    ./gradlew updateInternalCatalogVersions && ./gradlew build

cleanup:
    bash ../scripts/cleanup-maven-local.sh --repo-root . --keep 5 --max-age-days 30

update-internal-dependencies:
    ./gradlew updateInternalCatalogVersions

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
