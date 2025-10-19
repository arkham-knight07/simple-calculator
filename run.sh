#!/usr/bin/env bash
# Small helper to compile and run the CalculatorApp
set -e
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT_DIR/src"
OUT="$ROOT_DIR/out"

mkdir -p "$OUT"

echo "Compiling..."
javac -d "$OUT" "$SRC"/CalculatorApp.java

echo "Running..."
java -cp "$OUT" CalculatorApp
