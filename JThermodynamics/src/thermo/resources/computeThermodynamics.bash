#!/bin/bash
#
# FILE     Thermodynamics
# PACKAGE  JThermodynamics
# AUTHORS  Edward S. Blurock
#
# CONTENT
#   Calculate Thermodynamics
#
#c COPYRIGHT (C) 2009 Edward S. Blurock
#set verbose on

TYPE=$1
MOLECULE=$2
NAME=$3

PROJECT=JThermodynamicData
NETBEANROOT=/Users/edwardblurock/NetBeansProjects
PROJECTROOT=$NETBEANROOT/$PROJECT
JARROOT=$PROJECTROOT/dist/lib
MAINJAR=$PROJECTROOT/dist/$PROJECT.jar
echo $MAINJAR
JARS="$JARROOT/*.jar"
FILES="$MAINJAR"
for f in $JARS
do
  FILES="$FILES:$f"
done
echo $FILES

java -classpath $FILES thermo.compute.ComputeThermodynamicsFromNancyString $TYPE $MOLECULE $NAME

