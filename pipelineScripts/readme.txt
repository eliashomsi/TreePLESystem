This read me would contain explanation of some extra tools used in the script

ngrok is an alternative to localtunnel. It enables to expose local ports on the machine to the outside world. In this case it was used to expose the backend.

catalina.sh is the script that runs the tomcat server. The script is a symbolic link to the /opt/tomcat/bin/catalina.sh


current ngrok for the backend:
http://9e68ed46.ngrok.io


ngrok run command
sudo nohup ./ngrok start backend -config=/home/ecse321/.ngrok2/ngrok.yml -log=stdout  &

