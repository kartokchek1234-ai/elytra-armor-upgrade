#!/usr/bin/env sh
set -eu
GRADLE_VERSION="9.7.1"
GRADLE_DIR="${GRADLE_USER_HOME:-$HOME/.gradle}/manual-wrapper/gradle-${GRADLE_VERSION}"
GRADLE_BIN="$GRADLE_DIR/gradle-${GRADLE_VERSION}/bin/gradle"
JAVA_MAJOR="$(java -version 2>&1 | sed -n '1s/.*version "\([0-9]*\).*/\1/p')"
if [ -z "$JAVA_MAJOR" ] || [ "$JAVA_MAJOR" -lt 25 ]; then
  echo "Java 25 or newer is required." >&2
  exit 1
fi
if [ ! -x "$GRADLE_BIN" ]; then
  mkdir -p "$(dirname "$GRADLE_DIR")"
  TMP_ZIP="${TMPDIR:-/tmp}/gradle-${GRADLE_VERSION}-$$.zip"
  curl -fL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o "$TMP_ZIP"
  mkdir -p "$GRADLE_DIR"
  unzip -q "$TMP_ZIP" -d "$GRADLE_DIR"
  rm -f "$TMP_ZIP"
fi
exec "$GRADLE_BIN" "$@"
