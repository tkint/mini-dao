#!/usr/bin/env bash

if [ -n "$TRAVIS_TAG" ]; then
    if [ $TRAVIS_PULL_REQUEST = 'false' ]; then
        echo "Deploying $TRAVIS_TAG"
        if [ $TRAVIS_BRANCH = 'master' ]; then
            echo "Deploying production"
            mvn deploy -P sign,build-extras -s travis/mvnsettings.xml
        elif [ $TRAVIS_BRANCH = 'develop' ]; then
            echo "Deploying snapshot"
            mvn deploy -s travis/mvnsettings.xml
        fi
    else
        echo "Pull request detected: deploy canceled"
    fi
else
    echo "No tag detected: deploy canceled"
fi
