package org.bdp4j.sample.pipe.impl;

import com.google.auto.service.AutoService;

import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.TransformationPipe;
import org.bdp4j.sample.types.FeatureVector;
import org.bdp4j.sample.types.TokenArray;
import org.bdp4j.types.Instance;

/**
 * Build a FeatureVector from a TokenArray
 * @author José Ramón Méndez Reboredo
 */
@AutoService(Pipe.class)
@TransformationPipe
public class TokenArray2FeatureVector extends Pipe {

    /**
     * The generic constructor
     */
    public TokenArray2FeatureVector(){
        /* Must declare here the dependencies */
        /* alwaysBefore          notAfter */
        super(new Class<?>[]{},new Class<?>[]{});
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
     * Pipe the instance
     * @param carrier the instance to pipe
     * @return the piped instance
     */
    @Override
    public Instance pipe(Instance carrier) {
        TokenArray t=(TokenArray)(carrier.getData());
        carrier.setData(t.buildFeatureVector());
        return carrier;
    }

}