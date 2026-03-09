output "public_ip" {
  value = aws_instance.btg_api.public_ip
}

output "app_url" {
  value = "http://${aws_instance.btg_api.public_ip}:${var.app_port}"
}