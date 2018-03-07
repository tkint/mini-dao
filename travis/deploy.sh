#!/usr/bin/env bash

if [[ $TRAVIS_TAG ]]; then
    if [ $TRAVIS_PULL_REQUEST = 'false' ]; then
        if [ $TRAVIS_BRANCH = 'master' ]; then
            echo "Deploy master"
            mvn deploy -P sign,build-extras -s travis/mvnsettings.xml
        elif [ $TRAVIS_BRANCH = 'develop' ]; then
            echo "Deploy snapshot"
            mvn deploy -s travis/mvnsettings.xml
        fi
    else
        echo "Pull request detected: deploy canceled"
    fi
else
    echo "No tag detected: deploy canceled"
fi
