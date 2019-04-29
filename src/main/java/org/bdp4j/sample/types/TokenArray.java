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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * A tokenArray implementation
 *
 * @author José Ramón Méndez Reboredo
 */
public class TokenArray implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Tokens included in the TokenArray
     */
    private List<String> tokens = new ArrayList<>();

    /**
     * The separators for tokenising
     */
    public static final String DEFAULT_SEPARATORS = " \t\r\n\f!\"#$%&'()*+,\\-./:;<=>?@[]^_`{|}~";

    /**
     * Default consturctor
     */
    public TokenArray() {

    }

    /**
     * Constructor with a string that will be tokenized
     *
     * @param toTokenize The string to tokenize
     * @param separators the separators to be used
     */
    public TokenArray(String toTokenize, String separators) {
        tokens = Collections.list(new StringTokenizer(toTokenize, separators)).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }

    /**
     * Add a term to the tokenArray
     *
     * @param t the term (token) to add
     */
    public void add(String t) {
        tokens.add(t);
    }

    /**
     * Build a Feature Vector
     *
     * @return The feature Vector built
     */
    public FeatureVector buildFeatureVector() {
        HashMap<String, Double> retVal = new HashMap<>();

        for (String token : tokens) {

            /**
             * Add the token to dictionary
             */
            Dictionary.getDictionary().add(token);

            /**
             * Add the feature to the returnValue
             */
            Double val = retVal.get(token);
            retVal.put(token, (val != null) ? val + 1 : 1);
        }

        return new FeatureVector(retVal);
    }
}
