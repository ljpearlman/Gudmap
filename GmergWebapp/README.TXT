******* GUDMAP/EUREGENE (Gmerg) GENE EXPRESSION DATABASE WEB APPLICATION *******

The following application conatins source code required to build web-based user interface required to access Gmerg gene expression databases.

Compliation of the code will produce a web archive (war) file which can be deployed to a servlet container (apache tomcat).

*COMPILATION INSTRUCTIONS

1. To complie the application, build.xml and build.sh files are provided. The build.sh shell script uses ant to run build.xml and compile the application.
2. To compile successfully, a soft link called "tomcat" must be created under the directory /export/data0/ and must point to the tomcat's home directory ($CATALINA_HOME)
3. Before compilation an environment variable $CONFIGURATION must be created or the build script will not run. Currently, the default for this is "gmerg" although this will be configured to be either "gudmap or "euregenedb".