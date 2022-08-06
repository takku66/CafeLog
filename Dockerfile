FROM tomcat:9.0.64

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ENV WAR_FILE target/*.war
WORKDIR $CATALINA_HOME

COPY ${WAR_FILE} webapps/cafelog.war

