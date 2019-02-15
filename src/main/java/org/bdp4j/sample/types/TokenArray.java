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
    private List<String> tokens=new ArrayList<>();

    public static final String DEFAULT_SEPARATORS=" \t\r\n\f!\"#$%&'()*+,\\-./:;<=>?@[]^_`{|}~";

    /**
     * Default consturctor
     */
    public TokenArray(){

    }

    /**
     * Constructor with a string that will be tokenized
     * @param toTokenize The string to tokenize
     * @param separators the separators to be used
     */
    public TokenArray(String toTokenize, String separators){
       Collections.list(new StringTokenizer(toTokenize, separators)).stream()
          .map(token -> (String) token)
          .collect(Collectors.toList());
    }

    /**
     * Add a term to the tokenArray
     * @param t the term (token) to add
     */
    public void add(String t){
        tokens.add(t);
    }

    /**
     * Build a Feature Vector
     * @return The feature Vector built
     */
    public FeatureVector buildFeatureVector () {
        HashMap<String,Double> retVal=new HashMap<>();

        for (String token:tokens){
            /**
             * Add the token to dictionary
             */
            Dictionary.getDictionary().add(token);

            /**
             * Add the feature to the returnValue
             */
            Double val=retVal.get(token);
            retVal.put(token,(val!=null)?val+1:1);
        }

        return new FeatureVector(retVal);
    }
}