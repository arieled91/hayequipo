#!/usr/bin/env bash
#upload files
npm run build
if [ -e  ./dist/index.html ] # check index exists
then
  aws s3 rm s3://futboldesa --recursive
  aws s3 cp ./dist s3://futboldesa --recursive --acl public-read
fi
