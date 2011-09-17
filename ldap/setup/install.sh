#!/bin/sh

export SLAPD_INSTALL="$1"

if [ -z $2 ]
then
    echo 'sudo NOT enabled'	
else
    export SUDO=true
    export SUPW=$2
    fi

echo 'install cds server ' $SLAPD_INSTALL
if [ $SUDO ]
  then
      echo $SUPW | $SLAPD_INSTALL
  else
      $SLAPD_INSTALL
      fi