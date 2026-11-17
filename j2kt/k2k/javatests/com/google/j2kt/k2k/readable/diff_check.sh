#!/bin/bash
set -e
echo "Checking goldens..."
diff -r $1 $2 ||
  (echo Golden files changed. Please run \'third_party/java_src/xplat/j2kt/k2k/javatests/com/google/j2kt/k2k/gen.py\' &&
    exit 1)

echo "PASS"
