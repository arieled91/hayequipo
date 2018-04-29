#!/usr/bin/env bash
cordova-hcp build
cordova build android
cordova-icon
cordova build --release
#upload apk
aws s3 cp platforms/android/app/build/outputs/apk/release s3://futboldesa/mobile --recursive --acl public-read

