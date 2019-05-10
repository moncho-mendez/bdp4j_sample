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
import org.bdp4j.pipe.TransformationPipe;
import org.bdp4j.pipe.SharedDataProducer;
import org.bdp4j.sample.types.Dictionary;
import org.bdp4j.sample.types.FeatureVector;
import org.bdp4j.sample.types.TokenArray;
import org.bdp4j.types.Instance;

/**
 * Build a FeatureVector from a TokenArray
 *
 * @author José Ramón Méndez Reboredo
 */
@AutoService(Pipe.class)
@TransformationPipe
public class TokenArray2FeatureVector extends AbstractPipe implements SharedDataProducer {

    /**
     * The generic constructor
     */
    public TokenArray2FeatureVector() {
        /* Must declare here the dependencies */
 /* alwaysBefore          notAfter */
        super(new Class<?>[]{}, new Class<?>[]{});
    }

    /**
     * Indicates the input type for the pipe
     */
    @Override
    public Class<?> getInputType() {
        return TokenArray.class;
    }

    /**
     * Indicates the output type for the pipe
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
        TokenArray t = (TokenArray) (carrier.getData());
        carrier.setData(t.buildFeatureVector());
        return carrier;
    }

    @Override
    public void writeToDisk(String filename) {
        Dictionary.getDictionary().writeToDisk(filename);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
