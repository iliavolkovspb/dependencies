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

deployment = {
    "apt.release": "https://repo.vaticle.com/repository/apt/",
    "apt.snapshot": "https://repo.vaticle.com/repository/apt-snapshot/",

    "artifact.release": "https://repo.vaticle.com/repository/artifact/",
    "artifact.snapshot": "https://repo.vaticle.com/repository/artifact-snapshot/",

    "brew.release": "https://github.com/vaticle/homebrew-tap/",
    "brew.snapshot": "https://github.com/vaticle/homebrew-tap-test/",

    "crate.release": "https://crates.io",
    "crate.snapshot": "https://repo.vaticle.com/repository/crates-snapshot/",

    "dll": "https://repo.vaticle.com/repository/dll",

    "helm.release": "https://repo.vaticle.com/repository/helm/",
    "helm.snapshot": "https://repo.vaticle.com/repository/helm-snapshot/",

    "maven.release": "https://repo.vaticle.com/repository/maven/",
    "maven.snapshot": "https://repo.vaticle.com/repository/maven-snapshot/",

    "npm.release": "https://registry.npmjs.org/",
    "npm.snapshot": "https://repo.vaticle.com/repository/npm-snapshot/",

    "pypi.release": "https://upload.pypi.org/legacy/",
    "pypi.snapshot": "https://repo.vaticle.com/repository/pypi-snapshot/",

    "rpm.release": "https://repo.vaticle.com/repository/rpm/",
    "rpm.snapshot": "https://repo.vaticle.com/repository/rpm-snapshot/",
}

deployment_private = {
    "artifact.release": "https://repo.vaticle.com/repository/private-artifact/",
    "artifact.snapshot": "https://repo.vaticle.com/repository/private-artifact-snapshot/",

    "npm.release": "https://repo.vaticle.com/repository/npm-private/",
}
