#!/bin/sh
project=$1


# Update and build.
echo 'Update and build it...'
cd /home/source/yike
git pull
mvn clean install -Dmaven.test.skip

cd /home/source/yike/$project
mvn clean package -Dmaven.test.skip -Ponline


# Deploy and restart the server.
echo 'Kill the existing application...'
pid=`ps -ef | grep /home/server/$project/ | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
  kill -9 $pid
fi
sleep 3

echo 'Deploy and start the application...'
rm -rf /home/server/$project/webapps/*
cp /home/source/yike/$project/target/ROOT.war /home/server/$project/webapps/
setsid sh /home/server/$project/bin/startup.sh

echo 'Deploy completed!'
sleep 5

tailf /home/server/$project/logs/catalina.out