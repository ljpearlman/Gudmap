#! /bin/csh
# build -- Build script for LocalServer deployment

# set java and tomcat environment variables
setenv JAVA_HOME '/opt/java'
setenv CATALINA_HOME '/export/data0/tomcat'

# Execute ANT to perform the requested build target
ant $*

	
