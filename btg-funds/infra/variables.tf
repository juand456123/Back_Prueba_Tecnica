variable "aws_region" {
  type = string
}

variable "instance_type" {
  type = string
}

variable "ami_id" {
  type = string
}

variable "key_name" {
  type = string
}

variable "jar_url" {
  type = string
}

variable "mongo_uri" {
  type = string
}

variable "jwt_secret" {
  type      = string
  sensitive = true
}

variable "jwt_expiration" {
  type = string
}

variable "app_port" {
  type = string
}

variable "datasource_url" {
  type = string
}

variable "datasource_username" {
  type = string
}

variable "datasource_password" {
  type      = string
  sensitive = true
}

variable "sendgrid_api_key" {
  type      = string
  sensitive = true
}

variable "sendgrid_from_email" {
  type = string
}

variable "twilio_account_sid" {
  type      = string
  sensitive = true
}

variable "twilio_auth_token" {
  type      = string
  sensitive = true
}

variable "twilio_from_number" {
  type = string
}

variable "ssh_cidr" {
  description = "CIDR allowed to SSH into the instance"
  type        = string
  default     = "0.0.0.0/0"
}