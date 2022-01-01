#!/usr/bin/env bash

# Hostname
echo "Updating Hostname ..."
sed -i 's/^ubuntu-focal$/localpbx/g' /etc/hostname
sed -i 's/ubuntu-focal$/localpbx/g' /etc/hosts
systemctl restart systemd-logind.service
hostnamectl set-hostname localpbx

# get the latest packages and upgrade them
echo "Updating System ..."
apt update
apt -y upgrade

# install some pre-requisites (mostly for Asterisk)
echo "Installing pre-requisites ..."
apt -y install \
    wget \
    unzip \
    subversion \
    build-essential \
    openssl \
    pkg-config \
    libssl-dev \
    libcurl4-openssl-dev \
    libgsm1-dev \
    libnewt-dev \
    libxml2-dev \
    libsqlite3-dev \
    uuid-dev \
    libjansson-dev \
    libncurses5-dev \
    libedit-dev \
    xmlstarlet \
    libsrtp2-dev \
    zlib1g-dev \
    mpg123 \
    subversion \
    tcl \
    sox \
    lame

# create user & folders for Asterisk
echo "Creating asterisk user and required folders ..."
adduser --system --group --no-create-home asterisk
mkdir -p /var/{lib,log,spool}/asterisk

# goto home folder, download and build Asterisk using the version specified in AST_VER
AST_VER=18.9.0
echo "Download, compile & setup Asterisk $AST_VER ..."
cd ~
wget http://downloads.asterisk.org/pub/telephony/asterisk/releases/asterisk-$AST_VER.tar.gz
tar xvfz asterisk-$AST_VER.tar.gz
cd asterisk-$AST_VER/
contrib/scripts/get_mp3_source.sh
./configure --with-pjproject-bundled --with-jansson-bundled
make menuselect.makeopts && menuselect/menuselect \
--enable codec_opus \
--enable codec_g729a \
--enable EXTRA-SOUNDS-EN-G722 \
--enable EXTRA-SOUNDS-EN-WAV \
--enable format_mp3 \
--disable chan_sip \
--disable BUILD_NATIVE \
menuselect.makeopts
make
make install
#make sure the asterisk user owns its folders
chown -R asterisk:asterisk /var/{lib,log,spool}/asterisk
# copy config & http content
cp -f /vagrant/asterisk/* /etc/asterisk/
cp -f /vagrant/static-http/* /var/lib/asterisk/static-http/

# create keys for TLS
echo "Creating TLS keys ..."
mkdir -p /etc/asterisk/keys
openssl genrsa -des3 -out /etc/asterisk/keys/ca.key -passout pass:asterisk 4096 > /dev/null
openssl req -batch -new -x509 -days 3650 -subj "/O=ARI4Java/CN=ARI4Java CA" -key /etc/asterisk/keys/ca.key -passin pass:asterisk -out /etc/asterisk/keys/ca.crt > /dev/null
openssl genrsa -out /etc/asterisk/keys/asterisk.key 2048 > /dev/null
openssl req -batch -new -subj "/O=ARI4Java/CN=192.168.56.44" -key /etc/asterisk/keys/asterisk.key -out /etc/asterisk/keys/asterisk.csr > /dev/null
openssl x509 -req -days 3650 -in /etc/asterisk/keys/asterisk.csr -CA /etc/asterisk/keys/ca.crt -CAkey /etc/asterisk/keys/ca.key -passin pass:asterisk -set_serial 01 -out /etc/asterisk/keys/asterisk.crt > /dev/null
chown -R asterisk:asterisk /etc/asterisk/keys

# add to systemd & start
echo "Setup & Start Asterisk Service"
cp /vagrant/scripts/asterisk.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable asterisk.service
systemctl start asterisk.service

echo "Provisioning Complete!"
