#!/bin/sh
export JAVA_HOME=/opt/jdk1.6.0_27
export ANT_HOME=./apache-ant
$ANT_HOME/bin/ant $1 $2 $3 $4
