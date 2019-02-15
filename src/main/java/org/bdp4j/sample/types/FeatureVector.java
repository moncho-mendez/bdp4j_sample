package org.bdp4j.sample.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Implements a FeatureVector
 * @author José Ramón Méndez Reboredo
 */
public class FeatureVector implements Serializable {
    /**
     * The serial Version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The features
     */
    private HashMap<String,Double> features=new HashMap<>();

    /**
     * Create a void FeatureVector 
     */
    public FeatureVector(){

    }

    /**
     * Build a feature vector with the compiled features
     * @param compiledFeatures The compiled features
     */
    protected FeatureVector(HashMap<String,Double> compiledFeatures){
        this.features=compiledFeatures;
    }

    /**
     * Add a feature 
     * @param feature The feature to be added
     * @param value The value for the feature
     */
    public void add(String feature, Double value){
        features.put(feature, value);
    }

    /**
     * Get the value for a feature
     * @param feature Returns the value for a feature
     * @return the value for a feature
     */
    public double getValue(String feature){
        Double retVal=features.get(feature);
        if (retVal==null) retVal=0d;
        
        return retVal;
    }
    
    /**
     * Retrieve a iterator to iterate through the features
     * @return an interator to iterate through features
     */
    public Iterator<String> iterator(){
        return features.keySet().iterator();
    }
}