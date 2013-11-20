#! /bin/sh
# build -- Build script for LocalServer deployment

# set java and tomcat environment variables
JAVA_HOME='/opt/java'
export CATALINA_HOME='/opt/tomcat'

if [ -d '/opt/apache-ant' ]
then
ANT_HOME='/opt/apache-ant'
else
ANT_HOME='/net/mahost/export/system1/LinuxX86/apache-ant/apache-ant-1.7.0'
fi

CP="$ANT_HOME"/etc/ant-bootstrap.jar
CP="$JAVA_HOME"/lib/tools.jar
CP="$CP":"$ANT_HOME"/lib/ant.jar
CP="$CP":"$ANT_HOME"/lib/ant-launcher.jar
echo $CP

# Execute ANT to perform the requested build target
 $JAVA_HOME/bin/java -cp $CP org.apache.tools.ant.Main $*

	
