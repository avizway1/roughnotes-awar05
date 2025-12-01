variable "region" {
  type    = string
  default = "ap-south-1"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "ami" {
  type    = string
  default = "ami-0d176f79571d18a8f"
}

variable "size" {
  type    = number
  default = 2
}

variable "instance_tags" {
  type = map(string)
  default = {
    Name        = "var-example"
    environment = "Dev"
    owner       = "avinash"
  }
}

variable "mybucket" {
  description = "Name of the s3 bucket you want to create"
  type        = string
  default     = "aviz.tftest.bucket"
}
