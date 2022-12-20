#!/bin/sh
if ! command -v mysql > /dev/null; then
    printf 'Command \e[1;32mmysql\e[0m not found. You may still need to install mariadb/mysql\n'
    exit
fi
if [ ${#1} -lt 1 ]; then
    echo "Usage: makegm.sh CHARACTERNAME <1-6> (DEFAULT 6)"
else
    if [ ${#2} -ge 1 ] && [ ${#2} -le 6 ]; then
        sudo mysql cosmic -e 'UPDATE cosmic.`characters` SET gm='"${2}"' WHERE name='"'""${1}""';"
    else
        sudo mysql cosmic -e 'UPDATE cosmic.`characters` SET gm=6 WHERE name='"'""${1}""';"
    fi
fi
