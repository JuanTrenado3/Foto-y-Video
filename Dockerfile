FROM tomcat:9.0-jdk11
COPY dist/SistemaFotoVideo.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
