echo "Downloading main JAR..." &&
wget -q "https://github.com/theapache64/tpb-cli/releases/latest/download/tpb-cli.main.jar" -O "tpb-cli.main.jar" --show-progress &&

echo "Moving files to ~/.tpb-cli" &&

mkdir -p ~/.tpb-cli &&
mv tpb-cli.main.jar ~/.tpb-cli/tpb-cli.main.jar

echo "Installing..." &&
echo "\nalias tpb-cli='java -jar ~/.tpb-cli/tpb-cli.main.jar'" >> ~/.bash_aliases &&

echo "Done"