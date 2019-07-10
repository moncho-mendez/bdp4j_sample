[![Build Status](https://travis-ci.com/sing-group/bdp4j_sample.svg?branch=master)](https://travis-ci.com/sing-group/bdp4j_sample)
[![lifecycle](https://img.shields.io/badge/lifecycle-stable-brightgreen.svg)](https://www.tidyverse.org/lifecycle/#stable)

# BPP4J (Big Data Preprocessing for Java, https://github.com/sing-group/bdp4j) example

This repository contains an example of use can be found to process SMS messages from http://www.esp.uem.es/jmgomez/smsspamcorpus/ 
a make a simple Weka 10-fold crosvalidation experiment. It is very simple but you can find in the example several pipes of different pipes working together. 

## Running the example

In order to run this example, you should run the following steps:

* Clone bdp4j repository into the folder you desire
* Install bdp4j in maven local respository (execute "mvn clean install" inside the folder where you cloned bdp4j)
* clone bdp4j_sample repository into the folder you desire (please do not use the same folder as for bdp4j)
* Compile bdp4j_sample: execute "mvn clean install" inside the folder where you cloned bdp4j_sample)
* Execute bdp4j_sample: execute java -jar target/bdp4j-sample-1.0.0-SNAPSHOT.jar

You will see the results in standard output and several csv files are generated in the root folder. 

## License

Copyright (C) 2018  Sing Group (University of Vigo)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
