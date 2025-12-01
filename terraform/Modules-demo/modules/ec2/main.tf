resource "aws_instance" "web" {
  count         = var.size
  ami           = var.ami
  instance_type = var.instance_type
  tags = var.instance_tags
}

