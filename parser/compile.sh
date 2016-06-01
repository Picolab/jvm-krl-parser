#!/bin/bash

export CLASSPATH=`pwd`/lib/antlr-3.3-complete.jar:`pwd`/out/classes:.
java org.antlr.Tool RuleSet.g -o out/src
mkdir -p out/classes
javac -d out/classes out/src/*.java src/org/json/*.java src/*.java
cd out/classes
jar -xf ../../lib/antlr-3.3-complete.jar
jar -cf ../krl_parser.jar *
cd ../..
