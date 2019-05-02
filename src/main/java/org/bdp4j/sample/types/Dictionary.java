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

import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Base64.Encoder;

/**
 * A generic dictionary to store any kind of string entries
 *
 * @author José Ramón Méndez
 */
public class Dictionary implements Iterable<String> {

    /**
     * The information storage for the dictionary. Only a Hashset of entries is
     * required
     */
    private LinkedHashSet<String> entries;

    /**
     * A instance of the Dictionary to implement a singleton pattern
     */
    private static Dictionary dictionary = null;

    /**
     * The default constructor
     */
    private Dictionary() {
        entries = new LinkedHashSet<>();
    }

    /**
     * Retrieve the System Dictionary
     *
     * @return The default dictionary for the system
     */
    public static Dictionary getDictionary() {
        if (dictionary == null) {
            dictionary = new Dictionary();
        }
        return dictionary;
    }

    /**
     * Add a entry to dictionarly
     *
     * @param entry the entry to add to the dictionary
     */
    public void add(String entry) {
        entries.add(entry);
    }

    /**
     * Determines if a entry is included in the dictionary
     *
     * @param entry the entry to check
     * @return a boolean indicating whether the entry is included in the
     * dictionary or not
     */
    public boolean isIncluded(String entry) {
        return entry.contains(entry);
    }

    /**
     * Achieves an iterator to iterate through the stored entries
     *
     * @return an iterator
     */
    @Override
    public Iterator<String> iterator() {
        return this.entries.iterator();
    }

    /**
     * Return a list of CSV entries
     *
     * @return list of CSVEntries
     */
    public String toBase64CSV() {
        StringBuilder retVal = new StringBuilder();
        Encoder b64encoder = Base64.getEncoder();
        for (String entry : entries) {
            String encoded = new String(b64encoder.encode(entry.getBytes()));
            retVal.append(encoded + ";");
        }
        return retVal.toString();
    }

    /**
     * Returns the size of the dictionary
     * @return the size of the dictionary
     */
    public int size(){
        return entries.size();
    }
}
