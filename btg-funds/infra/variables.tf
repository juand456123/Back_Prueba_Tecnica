variable "aws_region" {
  description = "AWS region where the infrastructure will be created"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
}

variable "ami_id" {
  description = "AMI ID for the EC2 instance"
  type        = string
}

variable "key_name" {
  description = "AWS key pair name used to connect to the instance"
  type        = string
}

variable "app_port" {
  description = "Application port exposed by the backend"
  type        = string
  default     = "8080"
}

variable "ssh_cidr" {
  description = "CIDR allowed to SSH into the instance"
  type        = string
  default     = "0.0.0.0/0"
}

variable "existing_security_group_name" {
  description = "Existing Security Group name to reuse instead of creating a new one"
  type        = string
  default     = "btg-funds-sg"
}