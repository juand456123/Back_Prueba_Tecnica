output "public_ip" {
  description = "IP pública de la instancia EC2"
  value       = aws_instance.btg_api.public_ip
}

output "public_dns" {
  description = "DNS público de la instancia EC2"
  value       = aws_instance.btg_api.public_dns
}

output "ssh_host" {
  description = "Host para conexión SSH"
  value       = aws_instance.btg_api.public_dns
}

output "ssh_user" {
  description = "Usuario SSH por defecto"
  value       = "ec2-user"
}

output "ssh_private_key_file" {
  description = "Archivo PEM utilizado para la conexión"
  value       = "bgt-key2.pem"
}

output "ssh_key_permissions_command" {
  description = "Comando para asignar permisos correctos a la clave PEM"
  value       = "chmod 400 \"bgt-key2.pem\""
}

output "ssh_connect_command" {
  description = "Comando SSH para conectarse a la instancia"
  value       = "ssh -i bgt-key2.pem ec2-user@${aws_instance.btg_api.public_dns}"
}

output "app_port" {
  description = "Puerto configurado para la aplicación"
  value       = var.app_port
}

output "app_url" {
  description = "URL base de la aplicación"
  value       = "http://${aws_instance.btg_api.public_ip}:${var.app_port}"
}