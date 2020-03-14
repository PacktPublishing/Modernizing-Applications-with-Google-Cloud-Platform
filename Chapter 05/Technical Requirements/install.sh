#!/bin/bash
sudo apt-get -y update
sudo apt-get -y install apt-transport-https software-properties-common dirmngr default-jre default-jdk
echo "deb https://jfrog.bintray.com/artifactory-debs xenial main" | sudo tee -a /etc/apt/sources.list
curl https://bintray.com/user/downloadSubjectPublicKey?username=jfrog | sudo apt-key add -
sudo apt-get -y update
sudo apt-get -y install nginx python-certbot-nginx jfrog-artifactory-oss
