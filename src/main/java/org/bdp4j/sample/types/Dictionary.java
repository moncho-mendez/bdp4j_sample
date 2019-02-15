package org.bdp4j.sample.types;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A generic dictionary to store any kind of string entries
 *
 * @author José Ramón Méndez
 */
public class Dictionary implements Iterable<String> {

    /**
     * The information storage for the dictionary. Only a Hashset of entries
     * is required
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
     * @return list of CSVEntries
     */
    public String toCSV(){
        StringBuilder retVal=new StringBuilder();
        boolean isFirst=true;
        for (String entry:entries){
            retVal.append(isFirst ? entry : (entry + ";"));
            isFirst=false;
        }
        return retVal.toString();
    }
}
