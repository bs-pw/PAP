if [ "$1" = "build" ]; then
    npm run build
    rm -rf papapi/src/main/resources/static
    mkdir papapi/src/main/resources/static
    cp -r papapp/build/* papapi/src/main/resources/static/
    export JAVA_HOME=/usr/lib/jvm/java-23-openjdk/
    ./papapi/mvnw -Dmaven.compiler.proc=full clean package
fi
sudo java -jar papapi/target/*.jar