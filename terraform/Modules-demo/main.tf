provider "aws" {
  region = var.region
}

module "ec2_instance" {
  source        = "./modules/ec2"
}

module "s3_bucket" {
  source   = "./modules/s3"
}