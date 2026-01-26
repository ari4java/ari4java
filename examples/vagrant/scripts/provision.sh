#!/usr/bin/env bash

DOCKER=false
if [ "$1" == "docker" ]; then
  DOCKER=true
fi

if [ "$DOCKER" == "false" ]; then
  # Hostname
  echo "Updating Hostname ..."
  sed -i 's/^ubuntu-focal$/localpbx/g' /etc/hostname
  sed -i 's/ubuntu-focal$/localpbx/g' /etc/hosts
  systemctl restart systemd-logind.service
  hostnamectl set-hostname localpbx
else
  sed -i 's/localhost$/localhost ari4java.localhost/g' /etc/hosts
fi

# get the latest packages and upgrade them
echo "Updating System ..."
export DEBIAN_FRONTEND=noninteractive
apt update
apt -y upgrade

# install some pre-requisites
echo "Installing some pre-requisites ..."
apt -y install \
    sox \
    lame \
    mpg123 \
    libopusfile-dev \
    autoconf \
    ca-certificates

# update CAs
update-ca-certificates

# set the timezone
ln -snf /usr/share/zoneinfo/$(curl https://ipapi.co/timezone) /etc/localtime

# create user & folders for Asterisk
echo "Creating asterisk user and required folders ..."
adduser --system --group --no-create-home asterisk
mkdir -p /var/{lib,log,spool}/asterisk

# goto home folder, download and build Asterisk using the version specified in AST_VER
AST_VER=16.27.0 # works but old Jun-2022
#AST_VER=18.26.4 # Aug-2025
#AST_VER=20.0.3 # what different use... May-2023
AST_VER=20.18.0 # LTS Jan-2026
#AST_VER=22.8.0 # latest LTS Jan-2026, but JsSIP doesn't work on this version
echo "Download, compile & setup Asterisk $AST_VER ..."
cd ~
wget http://downloads.asterisk.org/pub/telephony/asterisk/releases/asterisk-$AST_VER.tar.gz
tar xvfz asterisk-$AST_VER.tar.gz
rm asterisk-$AST_VER.tar.gz
cd asterisk-$AST_VER/
echo "Installing Asterisk pre-requisites ..."
contrib/scripts/install_prereq install
contrib/scripts/get_mp3_source.sh
if [ "$DOCKER" == "true" ]; then
  echo "Getting Open Source OPUS Codec"
  # although we're using a later Asterisk this is the latest opus but seems to work (from my limited testing)
  wget github.com/traud/asterisk-opus/archive/asterisk-13.7.tar.gz
  tar zvxf asterisk-13.7.tar.gz
  rm asterisk-13.7.tar.gz
  cp --verbose ./asterisk-opus*/include/asterisk/* ./include/asterisk
  cp --verbose ./asterisk-opus*/codecs/* ./codecs
  cp --verbose ./asterisk-opus*/res/* ./res
  cp --verbose ./asterisk-opus*/formats/* ./formats
  patch -p1 <./asterisk-opus*/asterisk.patch
  ./bootstrap.sh
fi
./configure --with-pjproject-bundled --with-jansson-bundled
make menuselect.makeopts
if [ "$DOCKER" == "true" ]; then
  menuselect/menuselect \
  --enable CORE-SOUNDS-EN-WAV \
  --enable EXTRA-SOUNDS-EN-WAV \
  --enable format_mp3 \
  --disable chan_sip \
  --disable BUILD_NATIVE \
  menuselect.makeopts
else
  menuselect/menuselect \
  --enable CORE-SOUNDS-EN-WAV \
  --enable EXTRA-SOUNDS-EN-WAV \
  --enable codec_opus \
  --enable format_mp3 \
  --disable chan_sip \
  --disable BUILD_NATIVE \
  menuselect.makeopts
fi
make
make install
#make sure the asterisk user owns its folders
chown -R asterisk:asterisk /var/{lib,log,spool}/asterisk
# copy config & http content
cp -f /vagrant/asterisk/* /etc/asterisk/
cp -f /vagrant/static-http/* /var/lib/asterisk/static-http/
if [ "$DOCKER" == "true" ]; then
  sed -i 's/codec_opus/codec_opus_open_source/g' /etc/asterisk/modules.conf
  sed -i 's/format_ogg_opus/format_ogg_opus_open_source/g' /etc/asterisk/modules.conf
fi
# create keys for TLS
echo "TLS keys ..."
if [ ! -f /vagrant/asterisk/keys/asterisk.crt ]; then
  openssl genrsa -des3 -out /vagrant/asterisk/keys/ca.key -passout pass:asterisk 4096 > /dev/null
  openssl req -batch -new -x509 -days 3650 -config /vagrant/scripts/ca-req.cnf extensions v3_ca -key /vagrant/asterisk/keys/ca.key -passin pass:asterisk -out /vagrant/asterisk/keys/ca.crt > /dev/null
  openssl genrsa -noenc -out /vagrant/asterisk/keys/asterisk.key 2048 > /dev/null
  openssl req -batch -new -config /vagrant/scripts/cert-req.cnf -key /vagrant/asterisk/keys/asterisk.key -out /vagrant/asterisk/keys/asterisk.csr > /dev/null
  openssl x509 -req -days 3650 -in /vagrant/asterisk/keys/asterisk.csr -CA /vagrant/asterisk/keys/ca.crt -CAkey /vagrant/asterisk/keys/ca.key -passin pass:asterisk -set_serial 01 -out /vagrant/asterisk/keys/asterisk.crt -extensions v3_req -extfile /vagrant/scripts/cert-req.cnf > /dev/null
fi
mkdir -p /etc/asterisk/keys
cp /vagrant/asterisk/keys/ca.crt /etc/asterisk/keys/
cp /vagrant/asterisk/keys/asterisk.{key,crt} /etc/asterisk/keys/
chown -R asterisk:asterisk /etc/asterisk/keys
chmod 400 /etc/asterisk/keys/*.key
chmod 444 /etc/asterisk/keys/*.crt

# add to systemd & start
echo "Setup & Start Asterisk Service"
cp /vagrant/scripts/asterisk.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable asterisk.service
systemctl start asterisk.service

echo "Provisioning Complete!"
