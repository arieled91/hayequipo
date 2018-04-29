#!/usr/bin/env bash
#upload files
npm run build
if [ -e  ./www/index.html ] # check index exists
then
  aws s3 rm s3://futboldesa --recursive
  aws s3 cp ./www s3://futboldesa --recursive --acl public-read
fi
