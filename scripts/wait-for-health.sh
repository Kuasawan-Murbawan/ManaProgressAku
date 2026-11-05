#!/usr/bin/env bash
# Usage: scripts/wait-for-health.sh https://api.example.com [attempts]
set -euo pipefail
URL="${1:?Usage: wait-for-health.sh <base url> [attempts]}"
ATTEMPTS="${2:-30}"

for attempt in $(seq 1 "$ATTEMPTS"); do
  code="$(curl -s -o /dev/null -w '%{http_code}' "$URL/actuator/health" || true)"
  if [ "$code" = "200" ]; then
    echo "Healthy after $attempt attempt(s)"
    exit 0
  fi
  echo "  attempt $attempt of $ATTEMPTS: HTTP $code"
  sleep 10
done

echo "No healthy answer from $URL/actuator/health" >&2
exit 1
