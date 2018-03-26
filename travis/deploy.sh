#!/usr/bin/env bash

if [ -n "$TRAVIS_TAG" ]; then
    echo "Deploying $TRAVIS_TAG"
    mvn deploy -P sign,build-extras -s travis/mvnsettings.xml
elif [ $TRAVIS_BRANCH = 'develop' ]; then
    echo "Deploying snapshot"
    mvn deploy -P sign -s travis/mvnsettings.xml
fi
