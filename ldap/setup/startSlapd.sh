#!/bin/sh
#Echo on

export SLAPD="$1"

if [ -z $2 ]
then
    echo 'sudo NOT enabled'	
else
    export SUDO=true
    export SUPW=$2
    fi

if [ $SUDO ]
then
    echo $SUPW | sudo -S $SLAPD
else
    $SLAPD
    fi
