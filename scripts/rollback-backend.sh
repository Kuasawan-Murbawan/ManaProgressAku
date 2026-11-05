#!/usr/bin/env bash
# Usage: scripts/rollback-backend.sh <version label>
# List the labels you can go back to:
#   aws elasticbeanstalk describe-application-versions --application-name NAME \
#     --query 'ApplicationVersions[].[VersionLabel,DateCreated]' --output table
set -euo pipefail

LABEL="${1:?Usage: scripts/rollback-backend.sh <version label>}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
[ -f "$ROOT/scripts/config.sh" ] && source "$ROOT/scripts/config.sh"
: "${AWS_REGION:?}" "${EB_APPLICATION:?}" "${EB_ENVIRONMENT:?}" "${API_URL:?}"

echo "Rolling $EB_ENVIRONMENT back to $LABEL"
aws elasticbeanstalk update-environment --region "$AWS_REGION" \
  --application-name "$EB_APPLICATION" --environment-name "$EB_ENVIRONMENT" \
  --version-label "$LABEL" > /dev/null
aws elasticbeanstalk wait environment-updated --region "$AWS_REGION" \
  --application-name "$EB_APPLICATION" --environment-names "$EB_ENVIRONMENT" \
  || echo "The wait timed out; checking health anyway"
"$ROOT/scripts/wait-for-health.sh" "$API_URL"
