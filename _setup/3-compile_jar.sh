#!/bin/sh
if ! command -v mvn > /dev/null; then
    printf "please install \e[1;32mmvn\e[0m\n"
else
    cd ../
    mvn clean install
fi
