#!/bin/bash
set -e

dnf update -y
dnf install -y java-21-amazon-corretto

mkdir -p /opt/btg-funds
mkdir -p /opt/btg-funds/logs

chown -R ec2-user:ec2-user /opt/btg-funds
chmod -R 755 /opt/btg-funds