#!/bin/bash

PWD=`dirname $0`
cd $PWD
cd ..

SCHEDULER_DIR=$PWD/../../scheduler

getPId() {
    if [ -f $1 ]; then
        PID=`cat $1`
        if [ -d /proc/$PID ]; then
            echo $PID
        fi
    fi
}

stopPidProc() {
    PID=`getPId $1`
    if [ -z "$PID" ]; then
        echo "$2 process doesn't exist" >&2
    else
        kill -9 $PID
        rm $1
    fi
}

stopProduceApplication() {
	stopPidProc produce.pid "ProduceApplication"
}

#stopRedis() {
#    redis-cli shutdown
#}

stopAll() {
    stopProduceApplication
#    stopRedis
}

obj=all

if [ "$1" != "" ]; then
    obj=$1
fi

case $obj in
    produce)
        stopProduceApplication
        ;;
    all)
        stopAll;
        ;;
    *)
        echo "Usage: start.sh ( all | produce )"
        exit
        ;;
esac

