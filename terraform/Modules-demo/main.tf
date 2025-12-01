provider "aws" {
  region = var.region
}

module "ec2_instance" {
  source        = "./modules/ec2"
  instance_type = var.instance_type
  ami           = var.ami
  size          = var.size
}

module "s3_bucket" {
  source   = "./modules/s3"
  mybucket = var.mybucket
}