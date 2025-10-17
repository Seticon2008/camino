# change to project root directory
cd /programs/camino

# create bin directory
mkdir bin

# compile src/client/*.java files in src directory, put *.class files in bin directory
echo "Compiling client code..."
javac -d bin src/client/*.java src/common/*.java

# set client truststore path and password
echo "Starting client..."
TRUSTSTORE="certs/clienttruststore.jks"
STOREPASS="password"

# start client
java \
-Djavax.net.ssl.trustStore="$TRUSTSTORE" \
-Djavax.net.ssl.trustStorePassword="$STOREPASS" \
-cp "bin" ChargeInterface