# change to project root directory
cd /programs/camino

# create certs directory
mkdir -p certs

# set CN, keystore/truststore filenames, keystore/truststore password
CN="localhost"
KEYSTORE="certs/serverkeystore.jks"
CERT="certs/server.cer"
TRUSTSTORE="certs/clienttruststore.jks"
STOREPASS="password"

# generate server keystore file
echo "Generating server keystore file..."
keytool -genkeypair \
-keyalg RSA \
-keypass "$STOREPASS" \
-storepass "$STOREPASS" \
-keystore "$KEYSTORE" \
-dname "CN=$CN, OU=ou, O=o, L=l, S=s, C=c"

# export public certificate
echo "Exporting public certificate..."
keytool -export \
-storepass "$STOREPASS" \
-file "$CERT" \
-keystore "$KEYSTORE"

# generate client truststore file
echo "Generating client truststore file..."
keytool -import \
-noprompt \
-file "$CERT" \
-keypass "$STOREPASS" \
-storepass "$STOREPASS" \
-keystore "$TRUSTSTORE"