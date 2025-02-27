#!/usr/bin/bash

gradle saturn-draft:shadowJar
cp saturn-draft/build/libs/saturn-draft-1.0-SNAPSHOT-all.jar ./saturn.jar
