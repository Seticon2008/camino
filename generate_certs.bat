@echo off

REM change to project root directory
cd C:\programs\camino

REM create certs directory
mkdir certs

REM set CN, keystore/truststore filenames, keystore/truststore password
set CN=localhost
set KEYSTORE=certs\serverkeystore.jks
set CERT=certs\server.cer
set TRUSTSTORE=certs\clienttruststore.jks
set STOREPASS=password

REM generate server keystore file
echo Generating server keystore file...
keytool -genkeypair ^
-keyalg RSA ^
-keypass %STOREPASS% ^
-storepass %STOREPASS% ^
-keystore %KEYSTORE% ^
-dname "CN=%CN%, OU=ou, O=o, L=l, S=s, C=c"

REM export public certificate
echo Exporting public certificate...
keytool -export ^
-storepass %STOREPASS% ^
-file %CERT% ^
-keystore %KEYSTORE%

REM generate client truststore file
echo Generating client truststore file...
keytool -import ^
-noprompt ^
-file %CERT% ^
-keypass %STOREPASS% ^
-storepass %STOREPASS% ^
-keystore %TRUSTSTORE%