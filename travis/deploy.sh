#!/usr/bin/env bash
echo $PWD
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -P sign,build-extras --settings ~/travis/mvnsettings.xml
fi