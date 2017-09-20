This test coding exercise was designed and implemented according to 
specification given in "OMS_candidate.docx" document for UBS.
I have build client and server side  order-management-system 
To develop it I used java, maven, spring, cxf, jpa, hibernate, javascript, jquery and mokito for unit test. 

Client side html pages
http://localhost:8080/oms/page/login.htm
http://localhost:8080/oms/page/registration.htm
http://localhost:8080/oms/page/product.htm
http://localhost:8080/oms/page/placeorder.htm

Server side rest services 
POST http://localhost:8080/oms/services/user/register 
POST http://localhost:8080/oms/services/user/orders
POST http://localhost:8080/oms/services/user/placeorder 
POST http://localhost:8080/oms/services/product

Also, have provided simple soap ui tests for interacting directly with web services
/src/test/resources/soapui-project-01.xml

Maven build produces war file oms.war
To run it, place it on tomcat or runnit directly from maven jetty plugin, mvn clean -Pjetty jetty:run-war

Written by Edmond Gjermeni
