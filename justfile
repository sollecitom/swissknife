#!/usr/bin/env just --justfile

set quiet

push:
    git add -A && (git diff --quiet HEAD || git commit -am "WIP") && git push origin main

pull:
    git pull

build:
    ./scripts/publish-if-changed.sh

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
    just update-dependencies && just update-gradle && just update-container-versions
