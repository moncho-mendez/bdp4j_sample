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
import org.bdp4j.pipe.TransformationPipe;
import org.bdp4j.sample.types.TokenArray;
import org.bdp4j.types.Instance;

/**
 * AbstractPipe to tokenize a string included in the data of the instance
 *
 * @author José Ramón Méndez
 */
@TransformationPipe
@AutoService(AbstractPipe.class)
public class String2TokenArray extends AbstractPipe {

    /**
     * The generic constructor
     */
    public String2TokenArray() {

        /* Must declare here the dependencies */
 /* alwaysBefore          notAfter */
        super(new Class<?>[]{}, new Class<?>[]{});
    }

    /**
     * Return the input type
     *
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return String.class;
    }

    /**
     * Return the outputtype
     *
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return TokenArray.class;
    }

    /**
     * AbstractPipe the instance
     *
     * @param carrier the instance to pipe
     * @return the piped instance
     */
    @Override
    public Instance pipe(Instance carrier) {
        String data = (String) (carrier.getData());
        carrier.setData(new TokenArray(data, TokenArray.DEFAULT_SEPARATORS));
        return carrier;
    }

}
