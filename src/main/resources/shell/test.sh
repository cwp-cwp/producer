#!/bin/bash
#src=/opt/test/
#des1=web
#user1=webuser
#host1=192.168.154.129
#/opt/roadside/inotify/bin/inotifywait -mrq --timefmt '%d/%m/%y %H:%M' --format '%T %w%f%e' -e modify,delete,create,attrib $src
#rsync -avz --delete --progress --password-file=/opt/rsync/rsync.pwd $src $user1@$host1::$des1
#echo "${files} was rsynced" >>/tmp/rsync.log 2>&1

s=0
i=1
while [ $i -le 100 ]
do
     s=$[$s+$i]
     i=$[$i+1]
#    /opt/rsync/bin/rsync -avz --delete --progress --password-file=/opt/rsync/rsync.pwd webuser@192.168.154.129::web /opt/test/
    echo $s
done
echo $s




rsync -azP /opt/test/ root@192.168.154.129:/opt/test