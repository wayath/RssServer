FROM ubuntu:14.04

RUN apt-get update && apt-get upgrade -y

RUN apt-get install software-properties-common wget curl python supervisor mysql-client maven -y

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
ENV CATALINA_HOME /opt/tomcat

RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer


RUN wget http://apache.rediris.es/tomcat/tomcat-8/v8.0.33/bin/apache-tomcat-8.0.33.tar.gz -O /tmp/tomcat.tgz && \
tar xzvf /tmp/tomcat.tgz -C /opt && \
mv /opt/apache-tomcat-8.0.33 /opt/tomcat && \
rm /tmp/tomcat.tgz && \
rm -rf /opt/tomcat/webapps/examples && \
rm -rf /opt/tomcat/webapps/docs && \
rm -rf /opt/tomcat/webapps/ROOT
ADD ./config/tomcat-users.xml /opt/tomcat/conf/

COPY ./src /root/server/src
COPY ./nb-configuration.xml /root/server/nb-configuration.xml
COPY ./pom.xml /root/server/pom.xml

RUN sed 's/"jdbc:mysql:\/\/localhost:3306\/rss"/"jdbc:mysql:\/\/mysql:3306\/rss"/' -i /root/server/src/main/resources/spring.xml
WORKDIR /root/server/
RUN mvn clean install -Dmaven.test.skip=true
RUN cp ./target/rss-restapi-1.0-SNAPSHOT.war /opt/tomcat/webapps/api.war

COPY ./config/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

COPY ./config/init_db.sh /opt/init_db.sh
RUN chmod +x /opt/init_db.sh

EXPOSE 8080

CMD ["/usr/bin/supervisord"]
