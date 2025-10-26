#!/usr/bin/env bash
# Wrapper to expand environment variables and exec wait-for-it with the expanded timeout.

# Default timeout if not set
WAIT_FOR_DB_TIMEOUT=${WAIT_FOR_DB_TIMEOUT:-120}

# Log what we're doing
echo "docker-entrypoint: waiting for db:3306 with timeout=${WAIT_FOR_DB_TIMEOUT}s"

# Exec wait-for-it with expanded timeout; the -- ensures the following command runs after wait
exec /wait-for-it.sh db:3306 -t "$WAIT_FOR_DB_TIMEOUT" -s -- java -jar /app.jar
