#!/bin/bash
rm -rf ./temp
rm release.zip
mkdir temp
cp ../config.properties ./temp/config.properties
cp -R ../resources ./temp/resources
cp -R ../languages ./temp/languages
cp ../dist/Supaplex_Level_Editor.jar ./temp/editor.jar
cat > ./temp/run.sh << EOF
#!/bin/bash
java -jar ./editor.jar

EOF
chmod +x ./temp/run.sh
zip -r ./release.zip temp
rm -rf temp
