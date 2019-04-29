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
package org.bdp4j.sample.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Implements a FeatureVector
 *
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
    private HashMap<String, Double> features = new HashMap<>();

    /**
     * Create a void FeatureVector
     */
    public FeatureVector() {

    }

    /**
     * Build a feature vector with the compiled features
     *
     * @param compiledFeatures The compiled features
     */
    protected FeatureVector(HashMap<String, Double> compiledFeatures) {
        this.features = compiledFeatures;
    }

    /**
     * Add a feature
     *
     * @param feature The feature to be added
     * @param value The value for the feature
     */
    public void add(String feature, Double value) {
        features.put(feature, value);
    }

    /**
     * Get the value for a feature
     *
     * @param feature Returns the value for a feature
     * @return the value for a feature
     */
    public double getValue(String feature) {
        Double retVal = features.get(feature);
        if (retVal == null) {
            retVal = 0d;
        }

        return retVal;
    }

    /**
     * Retrieve a iterator to iterate through the features
     *
     * @return an interator to iterate through features
     */
    public Iterator<String> iterator() {
        return features.keySet().iterator();
    }
}
