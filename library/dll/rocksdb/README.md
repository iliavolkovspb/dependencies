#  Building RocksDB Dynamically Linked Libraries

The rules in this package build, package, and deploy RocksDB dynamic libraries as binary artifacts.


## Building RocksDB 

The general usage to compile RocksDB is:
```bazel
bazel build --define version=v7.4.4 :compile-<os>-<arch>
```

For example, to build a Mac arm64 shared library, we would run:
```bazel
bazel build --define version=v7.4.4 :compile-mac-arm64
```

The version number provided is a git within the RocksDB repository.

These rules will output a .zip including the main shared library plus possibly some symlinks to canonicalise the version names.

## Deploying RocksDB

To deploy instead of just producing the compiled files, we provide the repository username/password and run the deploy rule:
```bazel
bazel run --define version=v7.4.4 :deploy-<os>-<arch>
```

## Operating systems and architectures

Mac - arm64, x86_64
Win - x86_64
Linux - x86_64
