#!/bin/sh
if [ ! -d "../.git" ]; then
    printf "You must have cloned Cosmic using git for this to work (\e[1;32mgit clone https://github.com/p0nk/Cosmic.git\e[0m).\n"
elif ! command -v git > /dev/null; then
    printf "You need \e[1;32mgit\e[0m for this to work (\e[1;32mpackagemanager install git\e[0m).\n"
else
    cd ../
    if ps -eo comm | grep -q '^tor$' && command -v torsocks > /dev/null; then
        torsocks git pull "https://github.com/p0nk/Cosmic.git"
    else
        git pull "https://github.com/p0nk/Cosmic.git"
    fi
fi
printf "You still need to run \e[1;32m./3-compile_jar.sh\e[0m to update the server before launching it.\nIf the database was changed you may also need to run \e[1;32m./2-exec_sql.sh\e[0m.\n"
