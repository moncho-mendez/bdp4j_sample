/*
 * BDP4J-sample implements a list of BDP4J (https://github.com/sing-group/bdp4j) 
 * tasks (org.bdp4j.pipe.Pipe). These tasks implement common text preprocessing 
 * stages and can be easilly combined to create a BDP4J pipeline for preprocessig 
 * a set of ham/spam SMS messages downloaded from http://www.esp.uem.es/jmgomez/smsspamcorpus/
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
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.TeePipe;
import org.bdp4j.pipe.SharedDataConsumer;
import org.bdp4j.sample.types.Dictionary;
import org.bdp4j.sample.types.FeatureVector;
import org.bdp4j.types.Instance;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * Generate a CSV file from a Feature Vector
 *
 * @author José Ramón Méndez Reboredo
 */
@TeePipe
@AutoService(AbstractPipe.class)
public class GenerateFeatureVectorOutputPipe extends AbstractPipe implements SharedDataConsumer {

    /**
     * A logger for logging purposes
     */
    private static final Logger logger = LogManager.getLogger(GenerateFeatureVectorOutputPipe.class);

    /**
     * The default file to store CSV contents
     */
    public static final String DEFAULT_FILE = "output2.csv";

    /**
     * The output file
     */
    String outFile = null;

    /**
     * Marks if the file has been opened
     */
    boolean fileOpened = false;

    /**
     * The output printWriter
     */
    PrintWriter out;

    /**
     * Default consturctor
     */
    public GenerateFeatureVectorOutputPipe() {
        /**
         * Invoke the constructor with the default value
         */
        this(DEFAULT_FILE);
    }

    /**
     * Constructor customizing the property name
     *
     * @param outFile The file to store the information contained in instances
     */
    public GenerateFeatureVectorOutputPipe(String outFile) {
        /* Must declare here the dependencies */
 /* alwaysBefore         notAfter */
        super(new Class<?>[0], new Class<?>[0]);

        this.outFile = outFile;
    }

    /**
     * Getter for outFile (the filepath to store the CSV representation of
     * Instances)
     *
     * @return Return the filepath to store the CSV representation of Instances
     */
    public String getOutFile() {
        return this.outFile;
    }

    /**
     * Setter for outFile (the filepath to store the CSV representation of
     * Instances)
     *
     * @param outFile the filepath to store the CSV representation of Instances
     */
    @PipeParameter(name = "outFile", description = "The file to store the CSV representation of instances", defaultValue = DEFAULT_FILE)
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * Returns the input type
     *
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return FeatureVector.class;
    }

    /**
     * Returns the output type
     *
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return FeatureVector.class;
    }

    /**
     * AbstractPipe the instance
     *
     * @param carrier the instance to pipe
     * @return the piped instance
     */
    @Override
    public Instance pipe(Instance carrier) {
        FeatureVector fv = (FeatureVector) (carrier.getData());
        if (!fileOpened) {
            try {
                out = new PrintWriter(outFile);
                out.append("id;");
                out.append(Dictionary.getDictionary().toBase64CSV());
                for (String i : carrier.getPropertyList()) {
                    out.append(i + ";");
                }
                out.append("target\n");
            } catch (FileNotFoundException e) {
                logger.fatal(e);
                e.printStackTrace();
                System.exit(0);
            }
            fileOpened = true;
        }

        out.append(carrier.getName() + ";");
        Iterator<String> it = Dictionary.getDictionary().iterator();
        while (it.hasNext()) {
            String current = it.next();
            out.append(fv.getValue(current) + ";");
        }

        for (Object i : carrier.getValueList()) {
            out.append(i.toString() + ";");
        }
        out.append(carrier.getTarget().toString() + "\n");

        if (isLast() && fileOpened) {
            out.close();
        }

        return carrier;
    }

    @Override
    public void readFromDisk(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
