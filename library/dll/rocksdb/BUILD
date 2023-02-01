#
# Copyright (C) 2022 Vaticle
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#


package(default_visibility = ["//visibility:public"])

load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_binary", "kt_jvm_library")
load("@vaticle_dependencies//distribution:deployment.bzl", "deployment")
load("@vaticle_bazel_distribution//artifact:rules.bzl", "deploy_artifact")
load("@vaticle_dependencies//distribution:deployment.bzl", "deployment")

kt_jvm_binary(
    name = "builder-mac-arm64",
    main_class = "com.vaticle.dependencies.library.dll.rocksdb.BuilderMac_arm64Kt",
    srcs = ["BuilderMac_arm64.kt", "BuilderMacCommon.kt"],
    deps = [
        "//library/util:common",
        "@maven//:org_zeroturnaround_zt_exec"
    ],
)

genrule(
    name = "compile-mac-arm64",
    outs = [
        "librocksdb-arm64.zip"
    ],
    cmd = "$(execpath :builder-mac-arm64) $(version) $@",
    tools = ["@bazel_tools//tools/jdk:current_host_java_runtime", ":builder-mac-arm64"],
    tags = ["manual", "no-ide"],
)

deploy_artifact(
    name = "deploy-mac-arm64",
    target = ":compile-mac-arm64",
    artifact_group = "com.facebook",
    artifact_name = "librocksdb-mac-arm64-{version}.tar.gz",
    release = deployment['dll'],
    snapshot = deployment['dll'],
)

kt_jvm_binary(
    name = "builder-mac-x86_64",
    main_class = "com.vaticle.dependencies.library.dll.rocksdb.BuilderMac_x86_64Kt",
    srcs = ["BuilderMac_x86_64.kt", "BuilderMacCommon.kt"],
    deps = [
        "//library/util:common",
        "@maven//:org_zeroturnaround_zt_exec"
    ],
)

genrule(
    name = "compile-mac-x86_64",
    outs = [
        "librocksdb-x86_64.zip"
    ],
    cmd = "$(execpath :builder-mac-x86_64) $(version) $@",
    tools = ["@bazel_tools//tools/jdk:current_host_java_runtime", ":builder-mac-x86_64"],
    tags = ["manual", "no-ide"],
)

deploy_artifact(
    name = "deploy-mac-x86_64",
    target = ":compile-mac-x86_64",
    artifact_group = "com.facebook",
    artifact_name = "librocksdb-mac-x86_64-{version}.tar.gz",
    release = deployment['dll'],
    snapshot = deployment['dll'],
)