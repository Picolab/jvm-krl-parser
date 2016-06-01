#!/bin/bash

echo "compiling..."
javac -cp lib/antlr-3.3-complete.jar:output/classes:. Antlr_.java
javac -cp lib/antlr-3.3-complete.jar:output/classes:. KRLParser.java

echo "running..."
java -cp lib/antlr-3.3-complete.jar:output/classes:. KRLParser "$@"
