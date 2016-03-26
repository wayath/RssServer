#!/bin/bash

while ! nc -vz 'mysql' $MYSQL_PORT_3306_TCP_PORT; do sleep 1; done

mysql -u root -p$RSSSERVER_RSS_MYSQL_1_ENV_MYSQL_ROOT_PASSWORD -h 'mysql' < /root/server/src/main/resources/rss.sql
