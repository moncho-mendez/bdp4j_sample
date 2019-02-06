# bdp4j_sample
BPP4J (Big Data Preprocessing for Java) example

This repository contains an example of use can be found to process SMS messages from http://www.esp.uem.es/jmgomez/smsspamcorpus/ 
a make a simple Weka 10-fold crosvalidation experiment. It is very simple but you can find in the example several pipes of different pipes working 
together. 

In order to run this example, you should run the following steps:

* Clone bdp4j repository into the folder you desire
* Install bdp4j in maven local respository (execute "mvn clean install" inside the folder where you cloned bdp4j)
* clone bdp4j_sample repository into the folder you desire (please do not use the same folder as for bdp4j)
* Compile bdp4j_sample: execute "mvn clean install" inside the folder where you cloned bdp4j_sample)
* Execute bdp4j_sample: execute java -jar target/bdp4j-sample-1.0.0-SNAPSHOT.jar

You will see the results in standard output.
