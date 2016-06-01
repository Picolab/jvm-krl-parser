#!/bin/bash

echo "compiling..."
javac -cp parser/lib/antlrworks-1.4.jar:parser/lib/antlr-3.3-complete.jar:parser/output/classes:. Antlr_.java
javac -cp parser/lib/antlrworks-1.4.jar:parser/lib/antlr-3.3-complete.jar:parser/output/classes:. KRLParser.java

echo "running..."
java -cp parser/lib/antlrworks-1.4.jar:parser/lib/antlr-3.3-complete.jar:parser/output/classes:. KRLParser "$@"
