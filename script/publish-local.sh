#!/usr/bin/env bash
set -e

ROOT_PROJECT_DIR="$( cd .. && pwd )"
GIT_HASH="$( git rev-parse HEAD )"
GIT_HASH="${GIT_HASH:0:7}"

echo -e "Publishing with version: ${GIT_HASH}"

./gradlew clean publishToMavenLocal -Pversion=${GIT_HASH} --stacktrace

echo -e "Published with version: ${GIT_HASH}"
