#!/bin/sh
if command -v apt > /dev/null; then
    sudo apt install openjdk-17-jdk mariadb-server unzip wget
    #Debian/Ubuntu's maven 3.6.3 is too old. May be corrected in Debian 12.
    if [ ! -f ../apache-maven-3.8.6/bin/mvn ]; then
        if ps -eo comm | grep -q '^tor$' && command -v torsocks > /dev/null; then
            torsocks wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip
        else
            wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip
        fi
        unzip apache-maven-3.8.6-bin.zip -d ../../
        rm  apache-maven-3.8.6-bin.zip
    fi
elif command -v dnf > /dev/null; then
    sudo dnf install java-17-openjdk java-17-openjdk-devel mariadb maven
elif command -v zypper > /dev/null; then
    sudo zypper install java-17-openjdk java-17-openjdk-devel mariadb maven
#yum should be last as Fedora still has it even after switching to dnf.
elif command -v yum > /dev/null; then
    sudo yum install java-17-openjdk java-17-openjdk-devel mariadb maven
fi
if ! command -v mysql_secure_installation 2> /dev/null; then
    printf 'Installation has failed, you still need to run \e[1;32mmysql_secure_installation\e[0m\n'
    exit
else
    scriptsysdstarted=0
    if ! ps -eo comm | grep -q '^mariadbd$'; then
        if [ -d /run/systemd/system ]; then
            sudo systemctl start mariadb
            scriptsysdstarted=1
        else
            echo "Please make sure the mariadb server is running"
            exit
        fi
    fi
    sudo mysql_secure_installation
    if [ "${scriptsysdstarted}" -eq 1 ]; then
        sudo systemctl stop mariadb
    fi
fi
