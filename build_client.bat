@echo off

REM change to project root directory
cd C:\programs\camino

REM create bin directory
mkdir bin

REM compile src\client\*.java files in src directory, put *.class files in bin directory
echo Compiling client code...
javac -d bin src\client\*.java src\common\*.java

REM set client truststore path and password
echo Starting client...
set TRUSTSTORE=certs\clienttruststore.jks
set STOREPASS=password

REM start client
java ^
-Djavax.net.ssl.trustStore="%TRUSTSTORE%" ^
-Djavax.net.ssl.trustStorePassword="%STOREPASS%" ^
-cp bin ChargeInterface