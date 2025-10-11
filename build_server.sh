# change to project root directory
cd /programs/camino

# create bin directory
mkdir bin

# compile src/server/*.java files in src directory, put *.class files in bin directory
echo "Compiling server code..."
javac -d bin -cp lib/ojdbc8.jar src/server/*.java src/common/*.java

# set server keystore path and password
echo "Starting server..."
KEYSTORE="certs/serverkeystore.jks"
STOREPASS="password"

# start server
java \
-Djavax.net.ssl.keyStore="$KEYSTORE" \
-Djavax.net.ssl.keyStorePassword="$STOREPASS" \
-cp "bin:lib/ojdbc8.jar" Server