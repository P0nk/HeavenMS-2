#!/bin/sh
scriptsysdstarted=0
if ! ps -eo comm | grep -q '^mariadbd$'; then
    if [ -d /run/systemd/system ]; then
        sudo systemctl start mariadb
        scriptsysdstarted=1
    else
        echo "please make sure the mariadb server is running"
        exit
    fi
fi
if sudo mysqlshow | grep -q '^| cosmic *|$'; then
    printf "\033[1;31mWARNING\033[0m: database cosmic already detected. Do you want to \033[1;31mDELETE\033[0m the database cosmic and all data? \033[1;31mTHIS CANNOT BE UNDONE\033[0m (y/n)\n"
    read deldata
    if [ "${deldata}" = "y" ]; then
        unset -v deldata
        printf "\033[1;31mTHIS IS THE FINAL WARNING\033[0m: database cosmic already detected. Do you want to \033[1;31mDELETE\033[0m the database cosmic and all data, including ALL accounts, character progress, and other user data? \033[1;31mTHIS CANNOT BE UNDONE\033[0m (y/n)\n"
        read deldata
        if [ "${deldata}" = "y" ]; then
            echo "DROP DATABASE [IF EXISTS] cosmic;" | sudo mysql
        fi
    else
        echo "You did not delete the database named cosmic. Proceeding with the rest of the script which interacts with the database named cosmic may fail. You may quit now or proceed."
    fi
fi
echo "Do you want to run the optional custom shop sql? (y/n)"
read -r optionalshop
echo "Do you want to run the optional admin sql? (y/n)"
read -r optionaladmin
if [ "${optionaladmin}" = "y" ]; then
    while [ ${#adminname} -eq 0 ]; do
        echo "What should the admin's username be?"
        read -r adminname
    done
    while [ ${#adminpass} -eq 0 ]; do
        echo "What should the admin's password be? (Warning: will be stored in unencrypted plaintext in database until login)"
        read -r adminpass
    done
fi
echo "running 1-db_database.sql"
sudo mysql < "../database/sql/1-db_database.sql"
echo "running 2-db_drops.sql"
sudo mysql < "../database/sql/2-db_drops.sql"
if [ "${optionalshop}" = "y" ]; then
    echo "running 3-db_shopupdate.sql"
    sudo mysql < "../database/sql/3-db_shopupdate.sql"
fi
if [ "${optionaladmin}" = "y" ]; then
    vfn=0
    while [ -f "4-temp-""${vfn}"".sql" ]; do
        vfn=$((vfn+1))
    done
    echo "running 4-db-admin.sql"
    sed -e "s/admin/""${adminname}""/" -e 's#$2y$12$aFD9BDeUocDMY1X4tDYDyeJw/HhkQwCQWs3KAY7gCaRG0cpqJcaL.#'"${adminpass}""#" "../database/sql/4-db-admin.sql" > "4-temp-""${vfn}"".sql"
    sudo mysql < "4-temp-""${vfn}"".sql"
    rm "4-temp-""${vfn}"".sql"
fi
if [ "${scriptsysdstarted}" -eq 1 ]; then
    sudo systemctl stop mariadb
fi
