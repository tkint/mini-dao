#!/usr/bin/env bash

if [ $TRAVIS_BRANCH = 'master' ] && [ $TRAVIS_PULL_REQUEST = 'false' ]; then
    gpg --import travis/private-key.gpg
fi
