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
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.PropertyComputingPipe;
import org.bdp4j.types.Instance;

import java.io.File;

/**
 * A pipe able to compute the size of a file
 */
@AutoService(AbstractPipe.class)
@PropertyComputingPipe
public class FileSizePipe extends AbstractPipe {

    /**
     * The name of the deafult propety to store the filesize
     */
    public static final String DEFAULT_FILESIZE_PROP = "filesize";

    /**
     * The property Name to store the filesize
     */
    String propName = null;

    /**
     * Default consturctor
     */
    public FileSizePipe() {
        this(DEFAULT_FILESIZE_PROP);
    }

    public FileSizePipe(String propName) {
        /* Must declare here the dependencies */
 /*     alwaysBefore     notAfter */
        super(new Class<?>[0], new Class<?>[0]);
        this.propName = propName;
    }

    /**
     * Setter for propName (the name of the property to store the filesize)
     *
     * @param propName The name of the property to store the filesize
     */
    @PipeParameter(name = "propName", description = "The name of the property to store the filesize", defaultValue = DEFAULT_FILESIZE_PROP)
    public void setPropName(String propName) {
        this.propName = propName;
    }

    /**
     * Getter for propName (the name of the property to store the filesize)
     *
     * @return Return the name of the property to store the filesize
     */
    public String getPropName() {
        return this.getPropName();
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
     * @param carrier The instance to pipe
     */
    @Override
    public Instance pipe(Instance carrier) {
        carrier.setProperty(propName, ((File) (carrier.getData())).length());

        return carrier;
    }

}
