variable "mybucket" {
  description = "Name of the s3 bucket you want to create"
  type = string
  default = "aviz.tftest.bucket"
}

variable "s3tags" {
  type    = map(string)
  default = {
    Name        = "my-s3-example"
    environment = "Dev"
    owner = "avinash"
  }
}