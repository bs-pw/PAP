cd /papapp
./move_build_to_java_server
cd ..
export JAVA_HOME=/usr/lib/jvm/java-23-openjdk/
mvn -Dmaven.compiler.proc=full clean package
sudo java -jar target/*.jar
