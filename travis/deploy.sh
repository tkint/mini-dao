#!/usr/bin/env bash

if [ $TRAVIS_PULL_REQUEST = 'false' ]; then
    if [ $TRAVIS_BRANCH = 'master' ]; then
        mvn deploy -P sign,build-extras -s travis/mvnsettings.xml
    elif [ $TRAVIS_BRANCH = 'develop' ]; then
        mvn deploy -s travis/mvnsettings.xml
    fi
fi
