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
  git checkout master --force
  git pull origin
fi

git log --reverse --pretty=oneline --all -- rest-api/resources.json | while read log
do
  IFS=' ' read -r -a array <<< "$log"
  git checkout ${array[0]} --force > /dev/null
  VER=`cat rest-api/resources.json | jq -r '.apiVersion'`
  FOLDER="${VER//./_}"
  echo $FOLDER
  rm -rf ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  mkdir -p ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  cp rest-api/api-docs/*.json ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
done

cd ${START_DIR}

curl -s http://downloads.asterisk.org/pub/telephony/asterisk/ | \
    awk -v RS='(<[^>]+>|[:space:]]+)' -v ORS='\n' '$1~/current.tar.gz$/{print$1}' | while read file; do
  mkdir -p tmp/${file%-current*}
  curl -s http://downloads.asterisk.org/pub/telephony/asterisk/$file | \
  tar -C tmp/${file%-current*} -zx --xform='s,asterisk-[0-9\.]*/,,' --wildcards */rest-api
  (
    cd tmp/${file%-current*}
    VER=`cat rest-api/resources.json | jq -r '.apiVersion'`
    FOLDER="${VER//./_}"
    echo $FOLDER
    rm -rf ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
    mkdir -p ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
    cp rest-api/api-docs/*.json ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  )
done
