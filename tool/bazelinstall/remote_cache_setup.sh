#!/bin/bash

set -e

if [[ -n "$BAZEL_CACHE_CREDENTIAL" ]] && [[ -n "$BAZEL_CACHE_URL" ]]; then
    echo "Installing remote cache credential..."
    BAZEL_CACHE_CREDENTIAL_LOCATION=./.bazel-cache-credential.json
    echo "A remote cache credential is found and will be saved to $BAZEL_CACHE_CREDENTIAL_LOCATION. Artifact and test results will be cached remotely."
    BAZEL_CACHE_CREDENTIAL_JSON=$(echo "$BAZEL_CACHE_CREDENTIAL" | base64 -d)
    if [[ -n "$BAZEL_CACHE_CREDENTIAL_JSON" ]]; then
      echo "$BAZEL_CACHE_CREDENTIAL_JSON" > $BAZEL_CACHE_CREDENTIAL_LOCATION
      ( echo "build --remote_cache=$BAZEL_CACHE_URL --google_credentials=$BAZEL_CACHE_CREDENTIAL_LOCATION"; echo "test --cache_test_results=no" ) > ./.bazel-remote-cache.rc
      echo "The remote cache has been installed!"
    else
      echo "The remote cache was not installed."
      echo "\$BAZEL_CACHE_CREDENTIAL was non-empty, but failed to be decoded as base64. Confirm your value is valid."
      exit 1
    fi

else
    echo "The remote cache was not installed."
    echo "The environment variables \$BAZEL_CACHE_CREDENTIAL and \$BAZEL_CACHE_URL must both be set for remote cache installation."
fi
