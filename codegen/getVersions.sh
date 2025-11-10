#!/usr/bin/env bash

START_DIR=`pwd`

if [ ! -d "tmp/wiki" ]
then
  mkdir -p ${START_DIR}/tmp/wiki
fi

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

cat ${START_DIR}/versions-template.md > ${START_DIR}/tmp/wiki/Asterisk-Version-to-ARI-Version.md

git show-ref --tags | while read tags
do
  IFS=' ' read -r -a array <<< "$tags"
  # skip 0.x 1.x, 10.x and 11.x
  if [ "${array[1]:0:12}" = "refs/tags/0." ] || [ "${array[1]:0:12}" = "refs/tags/1." ] || \
     [ "${array[1]:0:13}" = "refs/tags/10." ] || [ "${array[1]:0:13}" = "refs/tags/11." ] || \
     [ "${array[1]:0:22}" = "refs/tags/certified/1." ] || [ "${array[1]:0:23}" = "refs/tags/certified/11." ]; then
    continue
  fi
  git checkout ${array[0]} --force > /dev/null 2>&1
  echo ${array[1]}
  if [ -f "rest-api/resources.json" ]; then
    VER=`cat rest-api/resources.json | jq -r '.apiVersion'`
    echo ${VER}
    echo "| ${array[1]:10} | ${VER} |" >> ${START_DIR}/tmp/wiki/Asterisk-Version-to-ARI-Version.md
  fi
done

cd ${START_DIR}
