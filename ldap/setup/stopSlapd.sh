#!/bin/sh
if [ -z $1 ]
then
    echo 'sudo NOT enabled'
else
    echo 'sudo enabled='$1
    export SUDO=true
    fi

SERVICE='slapd'
x=1
while [ $x -le 10 ]
do
  echo "Attempt $x to stop $SERVICE"
  if ps ax | grep -v grep | grep $SERVICE > /dev/null
  then
	echo 'stop the slapd server ' $a
	if [ $SUDO ]
	then
			echo $SUPW | sudo -S kill -HUP $(pidof $SERVICE)
	else
			kill -HUP $(/sbin/pidof $SERVICE)
    fi

  else
	echo "$SERVICE has been stopped"
	break;
  fi
  x=$(( $x + 1 ))
done