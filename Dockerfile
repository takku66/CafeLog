FROM tomcat:9.0.65

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ENV WAR_FILE target/*.war
ENV SPRING_PROFILES_ACTIVE prd
WORKDIR $CATALINA_HOME

COPY ${WAR_FILE} webapps/cafelog.war
RUN sudo apt install less
