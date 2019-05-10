/*
 * BDP4J-sample implements a list of BDP4J (https://github.com/sing-group/bdp4j) 
 * tasks (org.bdp4j.pipe.Pipe). These tasks implement common text preprocessing 
 * stages and can be easilly combined to create a BDP4J pipeline for preprocessig 
 * a set of ham/spam SMS messages downloaded from http://www.esp.uem.es/jmgomez/smsspamcorpus/
 *
 * Copyright (C) 2018  Sing Group (University of Vigo)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.bdp4j.sample.pipe.impl;

import com.google.auto.service.AutoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.TargetAssigningPipe;
import org.bdp4j.types.Instance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A pipe able to find the target for the SMS representation
 */
@AutoService(Pipe.class)
@TargetAssigningPipe
public class File2TargetAssignPipe extends AbstractPipe {

    /**
     * A logger for logging purposes
     */
    private static final Logger logger = LogManager.getLogger(File2TargetAssignPipe.class);

    /**
     * Default consturctor
     */
    public File2TargetAssignPipe() {
        /* Must declare here the dependencies */
 /*     alwaysBefore     notAfter */
        super(new Class<?>[0], new Class<?>[0]);
    }

    /**
     * The imput type of Instance.getData
     *
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return File.class;
    }

    /**
     * The output type of Instance.getData
     *
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return File.class;
    }

    /**
     * AbstractPipe the instance
     *
     * @param carrier the instance to pipe
     */
    @Override
    public Instance pipe(Instance carrier) {
        //Load the contents of the file
        String loadedStr = loadFile((File) carrier.getData());

        //Invalidate the instance if data couldn't be loaded
        if (loadedStr == null) {
            carrier.invalidate();
        } else {
            carrier.setTarget(loadedStr.substring(loadedStr.lastIndexOf(",") + 1).replaceAll("[\\p{Cntrl}\\p{Space}]", "")); //And set the data otherwise
        }
        return carrier;
    }

    /**
     * Read the full contents of a file into a string
     *
     * @param file The file (java.io.File)
     * @return The string representation of the contents for the file
     */
    private String loadFile(File file) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(file.getPath()));
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
        return new String(encoded, Charset.defaultCharset());
    }

}
