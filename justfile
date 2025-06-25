#!/usr/bin/env just --justfile

initSubmodule submodule:
    git submodule update --init --recursive {{submodule}}

push:
    git add . && git commit -m "WIP" && git push --recurse-submodules=on-demand origin main

pull:
    git submodule update --recursive --remote

build:
    ./gradlew build

rebuild:
    ./gradlew build --refresh-dependencies --rerun-tasks

publish:
    ./gradlew publishToMavenLocal

updateDependencies:
    ./gradlew versionCatalogUpdate

updateGradle:
    ./gradlew wrapper --gradle-version latest --distribution-type all

updateAll:
    just updateDependencies && just updateGradle

publishLibraries:
    ./gradlew build publishToMavenLocal