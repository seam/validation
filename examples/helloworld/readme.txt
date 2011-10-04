Seam Validation Hello World Example
=========================================

This is an example application which demonstrates the usage of the Seam Validation module. 

It comes in form of a web application, which contains a simple Servlet that takes the name of the user as input and invokes a simple business service with that name as parameter value. 

This service invocation is intercepted by the Seam Validation interceptor and depending on whether the input was valid (at least three characters long) or not, either a simple output will be shown or an exception describing the occurred constraint violation will be thrown.

Running the example on Jetty
=========================================
mvn clean jetty:run -Pservlet

Running the example on JBoss AS 7
=========================================
mvn clean package -Pjbossas
$JBOSS_HOME/bin/standalone.sh
deploy the example

Running the example on JBoss AS 6
=========================================
mvn clean package -Pjbossas jboss:hard-deploy
$JBOSS_HOME/bin/run.sh

Running the example on GlassFish V3
=========================================
mvn clean package -Pglassfish
$GF_HOME/bin/asadmin start-domain
$GF_HOME/bin/asadmin deploy target/validation-helloworld.war

Running the example from within your IDE
=========================================
Open the project within your IDE, e.g. Eclipse
Execute java.org.jboss.seam.validation.JettyRunner.java (in src/test/java)

** Important Note - If your IDE has Maven support, you will need to configure it to use the "servlet" profile to execute the example successfully. 

After launching the example open your browser and access the example page at http://localhost:8080/validation-helloworld/HelloWorld
