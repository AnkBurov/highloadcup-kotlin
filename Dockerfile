FROM centos:7

WORKDIR /root

RUN yum install -y wget && \
wget --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u144-b01/090f390dda5b47b9b721c7dfaa008135/jdk-8u144-linux-x64.rpm

RUN yum -y localinstall jdk-8u144-linux-x64.rpm

RUN mkdir project

#RUN mkdir /tmp/data
#ADD data.zip /tmp/data
ADD highloadcup-1.0-SNAPSHOT.jar project

EXPOSE 80

CMD java -XX:+UseG1GC -Xms3500m -Xmx3500m -DdataFile=/tmp/data/data.zip -jar project/highloadcup-1.0-SNAPSHOT.jar