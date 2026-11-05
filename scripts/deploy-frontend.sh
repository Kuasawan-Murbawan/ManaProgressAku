#!/usr/bin/env bash
# Builds the React app and publishes it to S3 and CloudFront. Usage: scripts/deploy-frontend.sh
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
[ -f "$ROOT/scripts/config.sh" ] && source "$ROOT/scripts/config.sh"
: "${AWS_REGION:?}" "${FRONTEND_BUCKET:?}" "${CLOUDFRONT_DISTRIBUTION_ID:?}"
FRONTEND_DIR="${FRONTEND_DIR:-$ROOT/Frontend}"

cd "$FRONTEND_DIR"
npm ci
npm run lint
npm run build

# 1. Files that keep their name between builds: cache for an hour.
aws s3 sync dist "s3://$FRONTEND_BUCKET" --region "$AWS_REGION" --delete \
  --exclude "index.html" --exclude "assets/*" --cache-control "public,max-age=3600"

# 2. Vite puts a content hash in every file name under assets/, so they can be cached for a year.
aws s3 sync dist/assets "s3://$FRONTEND_BUCKET/assets" --region "$AWS_REGION" --delete \
  --cache-control "public,max-age=31536000,immutable"

# 3. index.html names those hashed files, so it must never be cached.
aws s3 cp dist/index.html "s3://$FRONTEND_BUCKET/index.html" --region "$AWS_REGION" \
  --cache-control "no-cache" --content-type "text/html"

aws cloudfront create-invalidation --distribution-id "$CLOUDFRONT_DISTRIBUTION_ID" --paths "/*" > /dev/null
echo "Frontend published and CloudFront invalidated"
