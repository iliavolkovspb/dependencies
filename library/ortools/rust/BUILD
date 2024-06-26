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


load("@rules_rust//rust:defs.bzl", "rust_library")
load("@vaticle_dependencies//builder/rust:rules.bzl", "rust_cxx_bridge")

package(default_visibility = ["//visibility:public"])

rust_library(
    name = "ortools",
    srcs = ["ortools.rs"],
    deps = [
        "@crates//:cxx",
        ":ortools_bridge",
    ]
)

rust_cxx_bridge(
    name = "ortools_bridge",
    src = "ortools.rs",
    deps = select({
        "@vaticle_bazel_distribution//platform:is_mac": [
            "@or_tools_mac//:lib",
            "@or_tools_mac//:incl",
        ],
        "@vaticle_bazel_distribution//platform:is_linux": [
            "@or_tools_linux//:lib",
            "@or_tools_linux//:incl",
        ],
        "@vaticle_bazel_distribution//platform:is_windows": [
            "@or_tools_windows//:lib",
            "@or_tools_windows//:incl",
        ],
    }) + [":ortools_bridge_wrapper"],
)

cc_library(
    name = "ortools_bridge_wrapper",
    hdrs = ["OrToolsWrapper.h"],
    deps = select({
        "@vaticle_bazel_distribution//platform:is_mac": [
            "@or_tools_mac//:lib",
            "@or_tools_mac//:incl",
        ],
        "@vaticle_bazel_distribution//platform:is_linux": [
            "@or_tools_linux//:lib",
            "@or_tools_linux//:incl",
        ],
        "@vaticle_bazel_distribution//platform:is_windows": [
            "@or_tools_windows//:lib",
            "@or_tools_windows//:incl",
        ],
    })
)
