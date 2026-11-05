#!/usr/bin/env bash
# Deploys Backend/target/*.jar to Elastic Beanstalk. Usage: scripts/deploy-backend.sh v1.8.0
set -euo pipefail

VERSION="${1:?Usage: scripts/deploy-backend.sh <version, for example v1.8.0>}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
[ -f "$ROOT/scripts/config.sh" ] && source "$ROOT/scripts/config.sh"
: "${AWS_REGION:?}" "${EB_APPLICATION:?}" "${EB_ENVIRONMENT:?}" "${EB_BUCKET:?}" "${API_URL:?}"

shopt -s nullglob
JARS=("$ROOT"/Backend/target/*.jar)
if [ "${#JARS[@]}" -ne 1 ]; then
  echo "Expected exactly one jar in Backend/target, found ${#JARS[@]}. Run mvn clean verify first." >&2
  exit 1
fi
JAR="${JARS[0]}"

LABEL="${VERSION}-$(date +%Y%m%d-%H%M%S)"   # Elastic Beanstalk needs a unique label per upload
KEY="${EB_APPLICATION}/${LABEL}.jar"

PREVIOUS="$(aws elasticbeanstalk describe-environments --region "$AWS_REGION" \
  --environment-names "$EB_ENVIRONMENT" --query 'Environments[0].VersionLabel' --output text)"
echo "Currently running: $PREVIOUS (your rollback target)"

echo "Uploading $(basename "$JAR") to s3://$EB_BUCKET/$KEY"
aws s3 cp "$JAR" "s3://$EB_BUCKET/$KEY" --region "$AWS_REGION"

echo "Creating application version $LABEL"
aws elasticbeanstalk create-application-version --region "$AWS_REGION" \
  --application-name "$EB_APPLICATION" --version-label "$LABEL" \
  --source-bundle "S3Bucket=$EB_BUCKET,S3Key=$KEY"

echo "Updating environment $EB_ENVIRONMENT"
aws elasticbeanstalk update-environment --region "$AWS_REGION" \
  --application-name "$EB_APPLICATION" --environment-name "$EB_ENVIRONMENT" \
  --version-label "$LABEL" > /dev/null

echo "Waiting for the update to finish (a few minutes)"
aws elasticbeanstalk wait environment-updated --region "$AWS_REGION" \
  --application-name "$EB_APPLICATION" --environment-names "$EB_ENVIRONMENT" \
  || echo "The wait timed out; checking health anyway"

if "$ROOT/scripts/wait-for-health.sh" "$API_URL"; then
  echo "Deployed $LABEL"
else
  echo "The new version is not healthy. Roll back with: scripts/rollback-backend.sh $PREVIOUS" >&2
  exit 1
fi
