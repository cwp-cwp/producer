#!/bin/bash

PWD=`dirname $0`
NOW=`date +%Y%m%d%H%M%S`
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

backupFile() {
	ls logs/$1 >/dev/null 2> /dev/null
    if [ $? = 0 ]; then
        if [ ! -d logs/$2 ]; then
            mkdir logs/$2
        fi
        mv logs/$1 logs/$2/
    fi
}

execCmd() {
    cmd=`bin/run.sh -debug $6 -Dspring.config.location=classpath:application.yml -Dsystem.config=configs/$3 $1 $@`
#    echo $cmd 
    nohup $cmd $@ > logs/$5_$NOW.out 2>&1 &
	echo "$!" > $2
}

startProduceApplication() {
    PID=`getPId transfer.pid`
    if [ -n "$PID" ]; then
        echo "ProduceApplication process already started" >&2
    else
        echo "Starting ProduceApplication..."
        backupFile nohup_produce_*.out produce_backup
        execCmd com.cwp.produce.ProduceApplication produce.pid log4j.properties nohup_produce 66666
	fi
}

#startRedis() {
#    redis-server conf/config/redis.conf
#}

startAll() {
#    startRedis
    startProduceApplication $@
}

obj=all

if [ "$1" != "" ]; then
    obj=$1
    shift
fi

case $obj in
    produce)
        startProduceApplication $@
        ;;
    all)
        startAll $@
        ;;
    *)
        echo "Usage: start.sh ( all | produce )"
        exit
        ;;
esac
