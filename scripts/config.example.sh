# Copy to scripts/config.sh (ignored by git) and fill in.
export AWS_REGION="us-east-1"
export EB_APPLICATION="name of your Elastic Beanstalk application"
export EB_ENVIRONMENT="name of your Elastic Beanstalk environment"
export EB_BUCKET="elasticbeanstalk-us-east-1-YOUR_ACCOUNT_ID"
export FRONTEND_BUCKET="name of the S3 bucket CloudFront serves"
export CLOUDFRONT_DISTRIBUTION_ID="your distribution id, starts with E"
export API_URL="https://the URL your frontend calls for the backend, no trailing slash"
