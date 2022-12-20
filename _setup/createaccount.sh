#!/bin/sh
if ! command -v mysql > /dev/null; then
    printf "Command \e[1;32mmysql\e[0m not found. You may still need to install mariadb/mysql before using this.\n"
    exit
fi
while [ ${#name} -le 1 ]; do
    echo "Enter account name"
    read -r name
done
while [ ${#pass} -le 1 ]; do
    echo "Enter account password (Warning: will be stored in unencrypted plain text until login)"
    read -r pass
done
sudo mysql cosmic -e "INSERT INTO cosmic.accounts (id,name,password,nxCredit,maplePoint,nxPrepaid,characterslots) VALUES (2,""${name}"",""${pass}"",1000000,1000000,1000000,8);"
