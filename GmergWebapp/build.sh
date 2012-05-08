#! /bin/csh
# build -- Build script for LocalServer deployment

# Show what deployment is being built.  Real purpose is to abort if 
# $CONFIGURATION is not defined
echo
echo "Building ==> $CONFIGURATION <=="
echo

# set java and tomcat environment variables
setenv JAVA_HOME '/opt/java'
setenv CATALINA_HOME '/export/data0/tomcat'

# Execute ANT to perform the requested build target
ant $*

	
