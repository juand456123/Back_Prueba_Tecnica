#!/bin/bash
set -e

dnf install -y java-21-amazon-corretto

JAVA_BIN=$(find /usr/lib/jvm -type f -path "*java-21*bin/java" | head -n 1)

mkdir -p /opt/btg-funds
cd /opt/btg-funds

curl -L "${jar_url}" -o btg-funds.jar

cat > /opt/btg-funds/application.properties <<EOF
server.port=${app_port}

spring.application.name=btg-funds

spring.mongodb.uri=${mongo_uri}

spring.datasource.url=${datasource_url}
spring.datasource.username=${datasource_username}
spring.datasource.password=${datasource_password}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.default_schema=btg

spring.sendgrid.api-key=${sendgrid_api_key}
spring.sendgrid.from-email=${sendgrid_from_email}

jwt.secret=${jwt_secret}
jwt.expiration=${jwt_expiration}

twilio.account-sid=${twilio_account_sid}
twilio.auth-token=${twilio_auth_token}
twilio.from-number=${twilio_from_number}
EOF

cat > /etc/systemd/system/btg-funds.service <<EOF
[Unit]
Description=BTG Funds API
After=network.target

[Service]
User=root
WorkingDirectory=/opt/btg-funds
ExecStart=$${JAVA_BIN} -jar /opt/btg-funds/btg-funds.jar --spring.config.location=file:/opt/btg-funds/application.properties
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable btg-funds
systemctl start btg-funds