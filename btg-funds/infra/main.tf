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

data "aws_security_group" "existing_btg_sg" {
  count = var.existing_security_group_name != "" ? 1 : 0

  filter {
    name   = "group-name"
    values = [var.existing_security_group_name]
  }
}

resource "aws_security_group" "btg_sg" {
  count       = var.existing_security_group_name == "" ? 1 : 0
  name        = "btg-funds-sg"
  description = "Security group for BTG Funds API"

  ingress {
    description = "Allow SSH access"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.ssh_cidr]
  }

  ingress {
    description = "Allow application access on port 8080 from internet"
    from_port   = tonumber(var.app_port)
    to_port     = tonumber(var.app_port)
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound internet traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "btg-funds-sg"
  }
}

locals {
  security_group_id = var.existing_security_group_name != "" ? data.aws_security_group.existing_btg_sg[0].id : aws_security_group.btg_sg[0].id
}

resource "aws_instance" "btg_api" {
  ami                         = var.ami_id
  instance_type               = var.instance_type
  key_name                    = var.key_name
  vpc_security_group_ids      = [local.security_group_id]
  associate_public_ip_address = true

  user_data = templatefile("${path.module}/user-data.sh.tpl", {
    app_port = var.app_port
  })

  tags = {
    Name = "btg-funds-api"
  }
}