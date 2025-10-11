@echo off

REM change to project root directory
cd C:\programs\camino

REM create bin directory
mkdir bin

REM compile src\server\*.java files in src directory, put *.class files in bin directory
echo Compiling server code...
javac -d bin -cp lib\ojdbc8.jar src\server\*.java src\common\*.java

REM set server keystore path and password
echo Starting server...
set KEYSTORE=certs\serverkeystore.jks
set STOREPASS=password

REM start server
java ^
-Djavax.net.ssl.keyStore="%KEYSTORE%" ^
-Djavax.net.ssl.keyStorePassword="%STOREPASS%" ^
-cp bin;lib\ojdbc8.jar Server