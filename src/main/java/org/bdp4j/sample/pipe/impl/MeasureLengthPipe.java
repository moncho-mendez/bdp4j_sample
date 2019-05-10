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
import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.PropertyComputingPipe;
import org.bdp4j.types.Instance;

/**
 * A pipe able to measure the length of a string and create the corresponding
 * property
 */
@AutoService(Pipe.class)
@PropertyComputingPipe
public class MeasureLengthPipe extends AbstractPipe {

    /**
     * The name of the property to store the length of a string
     */
    public static final String DEFAULT_LENGTH_PROP_NAME = "length";

    /**
     * The property name
     */
    String propName = null;

    /**
     * Default consturctor
     */
    public MeasureLengthPipe() {
        /**
         * Invoke the constructor with the default value
         */
        this(DEFAULT_LENGTH_PROP_NAME);
    }

    /**
     * Constructor customizing the property name
     *
     * @param propName The property name to store the length
     */
    public MeasureLengthPipe(String propName) {
        /* Must declare here the dependencies */
 /*     alwaysBefore     notAfter */
        super(new Class<?>[0], new Class<?>[0]);

        this.propName = propName;
    }

    /**
     * Getter for propName (the name of the property to store the length)
     *
     * @return Return the name of the property to store the length
     */
    public String getPropName() {
        return this.getPropName();
    }

    /**
     * Setter for propName (the name of the property to store the length)
     *
     * @param propName The name of the property to store the length
     */
    @PipeParameter(name = "propName", description = "The name of the property to store the length of the text", defaultValue = DEFAULT_LENGTH_PROP_NAME)
    public void setPropName(String propName) {
        this.propName = propName;
    }

    /**
     * The imput type of Instance.getData
     *
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return String.class;
    }

    /**
     * The output type of Instance.getData
     *
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return String.class;
    }

    /**
     * AbstractPipe the instance
     *
     * @param carrier The instance to pipe
     */
    @Override
    public Instance pipe(Instance carrier) {
        carrier.setProperty(propName, ((String) (carrier.getData())).length());
        return carrier;
    }
}
