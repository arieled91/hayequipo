#!/usr/bin/env bash
keytool -genkey -v -keystore Futboldesa.keystore -alias futboldesa -validity 20000
cp ./Futboldesa.keystore ./platforms/android
rm ./Futboldesa.keystore
echo 'storeFile=Futboldesa.keystore
storeType=jks
keyAlias=futboldesa
keyPassword=java1942
storePassword=java1942' > ./platforms/android/release-signing.properties
cordova build android --release

