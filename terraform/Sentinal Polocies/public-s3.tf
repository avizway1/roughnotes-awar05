# Configure the AWS Provider
provider "aws" {
  region = "ap-south-1"

# 1. Create the S3 bucket
resource "aws_s3_bucket" "public_bucket" {
  bucket = "aviz-hcp-test"
  tags = {
    Environment = "PublicAccessDemo"
  }
}

# 2. Disable public access blocks at the bucket level
resource "aws_s3_bucket_public_access_block" "public_access_block" {
  bucket = aws_s3_bucket.public_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# 3. Apply a bucket policy to allow public read (s3:GetObject) access
resource "aws_s3_bucket_policy" "public_read_policy" {
  bucket = aws_s3_bucket.public_bucket.id
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = "*",
        Action = "s3:GetObject",
        Resource = "${aws_s3_bucket.public_bucket.arn}/*" # Grants access to all objects within the bucket
      }
    ]
  })
  # Ensure the public access block is applied first
  depends_on = [aws_s3_bucket_public_access_block.public_access_block]
}
