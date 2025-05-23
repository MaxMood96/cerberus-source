#!/bin/bash
#
# Cerberus Copyright (C) 2016 Cerberus Testing
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This file is part of Cerberus.
#
# Cerberus is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Cerberus is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Cerberus. If not, see <http://www.gnu.org/licenses/>.

# Working around permission errors locally by making sure that "mysql" uses the same uid and gid as the host volume
# See https://github.com/docker-library/mysql/issues/99 for more details

# Fail on any error
#!/bin/bash
set -e

echo "* Adjusting MySQL user/group IDs to match host volume"

TARGET_UID=$(stat -c "%u" /var/lib/mysql 2>/dev/null || echo 999)
echo "-- Setting mysql user to use uid ${TARGET_UID}"
usermod -o -u ${TARGET_UID} mysql || true

TARGET_GID=$(stat -c "%g" /var/lib/mysql 2>/dev/null || echo 999)
echo "-- Setting mysql group to use gid ${TARGET_GID}"
groupmod -o -g ${TARGET_GID} mysql || true

echo "* Done adjusting UID/GID."
