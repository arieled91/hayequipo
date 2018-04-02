#!/usr/bin/env bash
#upload files
npm run build
cordova-hcp build
cordova build android
#cordova-icon
cordova build --release
if [ -e  ./www/index.html ] # check index exists
then
  aws s3 rm s3://futboldesa --recursive
  aws s3 cp ./www s3://futboldesa --recursive --acl public-read
  aws s3 cp platforms/android/app/build/outputs/apk/release s3://futboldesa/mobile --recursive --acl public-read
fi
