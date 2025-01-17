npm run build
rm -rf ../papapi/src/main/resources/static
mkdir ../papapi/src/main/resources/static
cp -r build/* ../papapi/src/main/resources/static/
