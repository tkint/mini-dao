#!/usr/bin/env bash
echo $PWD
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_25cf0c002c9c_key -iv $encrypted_25cf0c002c9c_iv -in ~/travis/codesigning.asc.enc -out ~/travis/codesigning.asc -d
    gpg --fast-import ~/travis/codesigning.asc
fi