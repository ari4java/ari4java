#!/usr/bin/env bash

START_DIR=`pwd`

if [ ! -d "tmp/asterisk" ]
then
  mkdir tmp
  cd tmp
  git clone "https://gerrit.asterisk.org/asterisk"
  cd asterisk
else
  cd tmp/asterisk
  git pull
fi

git log --reverse --pretty=oneline --all -- rest-api/resources.json | while read log
do
  IFS=' ' read -r -a array <<< "$log"
  git checkout ${array[0]} > /dev/null
  VER=`cat rest-api/resources.json | jq -r '.apiVersion'`
  FOLDER="${VER//./_}"
  echo $FOLDER
  rm -rf ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  mkdir -p ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  cp rest-api/api-docs/*.json ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
done

cd ${START_DIR}
