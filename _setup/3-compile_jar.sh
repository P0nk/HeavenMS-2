#!/bin/sh
if [ ! -f ../../apache-maven-3.8.6/bin/mvn ] && ! command -v mvn > /dev/null; then
    printf "please install \e[1;32mmvn\e[0m\n"
else
    cd ../
    if [ -f ../apache-maven-3.8.6/bin/mvn ]; then
        ../apache-maven-3.8.6/bin/mvn clean install
    else
        mvn clean install
    fi
fi
