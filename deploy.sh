#! /bin/bash

cd /usr/src/CafeLog

git pull origin main

# 初回だと実行権限ないためつける
# Javaの実行にroot権限必要なので、sudo実行
sudo chmod 777 ./mvnw
sudo ./mvnw clean package

sudo docker build -t takku66/cafelog .

echo $GPG_PASSWORD | sudo gpg --batch --yes --passphrase-fd 0 --output ./src/main/resources/application-security.yml --decrypt ./src/main/resources/application-security.yml.gpg

sudo docker-compose -f docker-compose.yml up -d

exit