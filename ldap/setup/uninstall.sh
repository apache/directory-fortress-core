#!/bin/sh

export SLAPD_REMOVE="$1"

if [ -z $2 ]
then
    echo 'sudo NOT enabled'	
else
    export SUDO=true
    export SUPW=$2
    fi

echo 'uninstall cds server ' $SLAPD_REMOVE
if [ $SUDO ]
  then
    echo $SUPW | $SLAPD_REMOVE
  else
    $SLAPD_REMOVE
fi