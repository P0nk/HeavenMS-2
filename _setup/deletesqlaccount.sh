#!/bin/sh

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

echo "Enter name of SQL Account to be deleted:"
read -r sname
sudo mysql -e "DROP USER IF EXISTS ""${sname}""@localhost;"
echo "The following accounts still exist:"
sudo mysql -NBe 'SELECT User FROM mysql.user;'
echo "Be sure to update config.yaml if you deleted it's account (DB_USER and DB_PASS)"


if [ "${scriptsysdstarted}" -eq 1 ]; then
    sudo systemctl stop mariadb
fi

