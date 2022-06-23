#!/bin/bash

TARGET="third_party/java_src/xplat/kmpbench/gencode_snapshot"

mkdir $TARGET

blaze build third_party/java_src/xplat/kmpbench/java:benchmarks-j2kt-jvm

# TODO(b/230841155): Use go/vcstool to make this fig compatible

g4 edit $TARGET/*/*/*.kt $TARGET/*/*.kt 

cp -r blaze-bin/third_party/java_src/xplat/kmpbench/java/benchmarks-j2kt-jvm.kt/com/google/j2cl/benchmarks/* $TARGET

g4 add $TARGET/*/*/*.kt $TARGET/*/*.kt
