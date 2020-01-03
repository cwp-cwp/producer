#!/bin/bash

PWD=`dirname $0`
cd $PWD
cd ..

getPId() {
    if [ -f $1 ]; then
        PID=`cat $1`
        if [ -d /proc/$PID ]; then
            echo $PID
        fi
    fi
}

checkPidProcess() {
    PID=`getPId $1`
    if [ -z "$PID" ]; then
        echo $2 off
    else
        echo $2 on
    fi
}

#checkRedisStatus() {
#    PID=`redis-cli ping`
#    if [ "$PID" == "PONG" ]; then
#        echo Sku on
#    else
#        echo Sku off
#    fi
#}

getAllProcStatus() {
    checkPidProcess produce.pid ProduceApplication
}

obj=all
if [ "$1" != "" ]; then
    obj=$1
fi
case $obj in
    produce)
		checkPidProcess produce.pid ProduceApplication
        ;;
    all)
        getAllProcStatus;
        ;;
    *)
        echo "Usage: start.sh ( all | produce )"
        exit
        ;;
esac
