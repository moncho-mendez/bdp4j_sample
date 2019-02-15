package org.bdp4j.sample.pipe.impl;

import com.google.auto.service.AutoService;

import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.TransformationPipe;
import org.bdp4j.sample.types.TokenArray;
import org.bdp4j.types.Instance;

/**
 * Pipe to tokenize a string included in the data of the instance
 * @author José Ramón Méndez
 */
@TransformationPipe
@AutoService(Pipe.class)
public class String2TokenArray extends Pipe {
    /**
     * The generic constructor
     */
    public String2TokenArray(){

        /* Must declare here the dependencies */
        /* alwaysBefore          notAfter */        
        super(new Class<?>[]{},new Class<?>[]{});
    }

    /**
     * Return the input type
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return String.class;
    }

    
    /**
     * Return the outputtype
     * @return the output type 
     */
    @Override
    public Class<?> getOutputType() {
        return TokenArray.class;
    }

    /**
     * Pipe the instance
     * @param carrier the instance to pipe
     * @return the piped instance
     */    
    @Override
    public Instance pipe(Instance carrier) {
        String data=(String)(carrier.getData());
        carrier.setData(new TokenArray(data,TokenArray.DEFAULT_SEPARATORS));
        return carrier;
    }
    
}