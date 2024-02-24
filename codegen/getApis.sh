#!/usr/bin/env bash

START_DIR=`pwd`

if [ ! -d "tmp/asterisk" ]
then
  mkdir tmp
  cd tmp
  git clone "https://github.com/asterisk/asterisk"
  cd asterisk
else
  cd tmp/asterisk
  git checkout master --force
  git pull origin
fi

git log --tags --no-walk --reverse --pretty='%H %D' | while read log
do
  IFS=' ' read -r -a array <<< "$log"
  # skip 0.x 1.x, 10.x and 11.x
  if [ "${array[2]:0:2}" = "0." ] || [ "${array[2]:0:2}" = "1." ] || \
     [ "${array[2]:0:3}" = "10." ] || [ "${array[2]:0:3}" = "11." ] || \
     [ "${array[2]:0:12}" = "certified/1." ] || [ "${array[2]:0:13}" = "certified/11." ]; then
    continue
  fi
  # skip any -alpha -beta & -rc
  if [[ "${array[2]}" == "-alpha" ]] || [[ "${array[2]}" == "-beta" ]] || [[ "${array[2]}" == "-rc" ]]; then
    continue
  fi
  git checkout ${array[0]} --force > /dev/null
  VER=`cat rest-api/resources.json | jq -r '.apiVersion'`
  FOLDER="${VER//./_}"
  echo $FOLDER
  rm -rf ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  mkdir -p ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
  cp rest-api/api-docs/*.json ${START_DIR}/src/main/resources/codegen-data/ari_${FOLDER}
done

cd ${START_DIR}
