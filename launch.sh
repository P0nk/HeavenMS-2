#!/bin/sh
scriptsysdstarted=0
if ! ps -eo comm | grep -q '^mariadbd$'; then
    if [ -d /run/systemd/system ]; then
        sudo systemctl start mariadb
        scriptsysdstarted=1
    fi
fi
if command -v dpkg > /dev/null; then
    java="$(dpkg -L openjdk-17-jre-headless | grep '^/bin/java$' | head -n1)"
elif command -v rpm > /dev/null; then
    java="$(rpm -ql java-17-openjdk-headless | grep '^/usr/lib/jvm.*/bin/java$' | head -n1)"
fi
if [ "${#java}" -lt 4 ]; then
    java="java"
fi
"${java}" -Xmx2048m -Dwz-path=wz -jar target/Cosmic.jar
if [ "${scriptsysdstarted}" -eq 1 ]; then
    sudo systemctl stop mariadb
fi
