resource "aws_s3_bucket" "mybucket" {
  bucket = var.mybucket
  tags = var.s3tags
}