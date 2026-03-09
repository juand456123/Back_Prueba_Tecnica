terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

resource "aws_security_group" "btg_sg" {
  name        = "btg-funds-sg"
  description = "Security group for BTG Funds API"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.ssh_cidr]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "btg-funds-sg"
  }
}

resource "aws_instance" "btg_api" {
  ami                         = var.ami_id
  instance_type               = var.instance_type
  key_name                    = var.key_name
  vpc_security_group_ids      = [aws_security_group.btg_sg.id]
  associate_public_ip_address = true

  user_data = templatefile("${path.module}/user-data.sh.tpl", {
    jar_url            = var.jar_url
    mongo_uri          = var.mongo_uri
    jwt_secret         = var.jwt_secret
    jwt_expiration     = var.jwt_expiration
    app_port           = var.app_port

    datasource_url      = var.datasource_url
    datasource_username = var.datasource_username
    datasource_password = var.datasource_password

    sendgrid_api_key    = var.sendgrid_api_key
    sendgrid_from_email = var.sendgrid_from_email

    twilio_account_sid  = var.twilio_account_sid
    twilio_auth_token   = var.twilio_auth_token
    twilio_from_number  = var.twilio_from_number
  })

  tags = {
    Name = "btg-funds-api"
  }
}