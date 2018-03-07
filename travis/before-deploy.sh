#!/usr/bin/env bash

if [ -n "$TRAVIS_TAG" ]; then
    gpg --import travis/private-key.gpg
fi
