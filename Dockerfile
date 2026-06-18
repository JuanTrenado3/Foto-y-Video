FROM tomcat:10.1-jdk21
COPY dist/BD2.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
