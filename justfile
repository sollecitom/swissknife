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
    ./scripts/update-gradle.sh

@update-container-versions:
    ./scripts/update-container-versions.sh

update-all:
    just update-dependencies && just update-gradle && just update-container-versions
