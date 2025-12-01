output "instance_id_1" {
  description = "This is my first instance id.."
  value = aws_instance.web[0].id
}

output "public_ip_1" {
  description = "This is my first instance public ip"
  value = aws_instance.web[0].public_ip
}

output "private_ip_1" {
  description = "This is my first instance public ip"
  value = aws_instance.web[0].private_ip
}

output "instance_id_2" {
  description = "This is my second instance id.."
  value = aws_instance.web[1].id
}

output "public_ip_2" {
  description = "This is my second  instance public ip"
  value = aws_instance.web[1].public_ip
}

output "private_ip_2" {
  description = "This is my second  instance public ip"
  value = aws_instance.web[1].private_ip
}