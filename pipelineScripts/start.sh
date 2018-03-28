#!/bin/bash
# building the backend and deploying it
sudo cp /home/ecse321/workspace/TreePLE/TreePLE/TreePLE-Spring/build/libs/eventregistration-0.0.1-SNAPSHOT.war /opt/tomcat/webapps/ROOT.war
sudo systemctl stop tomcat
sudo bash /home/ecse321/catalina.sh stop
sudo bash /home/ecse321/catalina.sh start

# building android app and deploying it
cd /home/ecse321/workspace/TreePLE/TreePLE/TreePLE-Android
sudo ./gradlew assembleRelease
sudo cp -rf /home/ecse321/workspace/TreePLE/TreePLE/TreePLE-Android/app/release/app-release.apk /var/www/html/

# building front-end and deploying it
sudo npm run build --prefix /home/ecse321/workspace/TreePLE/TreePLE/TreePLE-Web/
sudo cp -rf /home/ecse321/workspace/TreePLE/TreePLE/TreePLE-Web/dist/* /var/www/html/
sudo chown apache:apache -R /var/www/
sudo systemctl restart httpd


