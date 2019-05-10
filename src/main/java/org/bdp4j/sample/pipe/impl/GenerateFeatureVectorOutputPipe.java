/*
 * BDP4J-sample implements a list of BDP4J (https://github.com/sing-group/bdp4j) 
 * tasks (org.bdp4j.pipe.Pipe). These tasks implement common text preprocessing 
 * stages and can be easilly combined to create a BDP4J pipeline for preprocessig 
 * a set of ham/spam SMS messages downloaded from http://www.esp.uem.es/jmgomez/smsspamcorpus/
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.TeePipe;
import org.bdp4j.pipe.SharedDataConsumer;
import org.bdp4j.sample.types.Dictionary;
import org.bdp4j.sample.types.FeatureVector;
import org.bdp4j.types.Instance;
import org.bdp4j.util.CSVDataset;

import java.io.File;
import java.util.Base64;
import java.util.Iterator;

/**
 * Generate a CSV file from a Feature Vector
 *
 * @author José Ramón Méndez Reboredo
 */
@TeePipe
@AutoService(AbstractPipe.class)
public class GenerateFeatureVectorOutputPipe extends AbstractPipe implements SharedDataConsumer {

    /**
     * A logger for logging purposes
     */
    private static final Logger logger = LogManager.getLogger(GenerateFeatureVectorOutputPipe.class);

    /**
     * The default file to store CSV contents
     */
    public static final String DEFAULT_FILE = "output2.csv";

    /**
     * The output file
     */
    String outFile = null;

    /**
     * Csv DAtaset to store data
     */
    CSVDataset dataset=null;

    /**
     * Number of properties
     */
    int nprops=0;

    /**
     * Length of the dictionary
     */
    int dictLength=0;

    /**
     * Default consturctor
     */
    public GenerateFeatureVectorOutputPipe() {
        /**
         * Invoke the constructor with the default value
         */
        this(DEFAULT_FILE);
    }

    /**
     * Constructor customizing the property name
     *
     * @param outFile The file to store the information contained in instances
     */
    public GenerateFeatureVectorOutputPipe(String outFile) {
        /* Must declare here the dependencies */
 /* alwaysBefore         notAfter */
        super(new Class<?>[0], new Class<?>[0]);

        this.outFile = outFile;
        File f=new File(outFile); if (f.exists()) f.delete();
        this.dataset=new CSVDataset(outFile);
    }

    /**
     * Getter for outFile (the filepath to store the CSV representation of
     * Instances)
     *
     * @return Return the filepath to store the CSV representation of Instances
     */
    public String getOutFile() {
        return this.outFile;
    }

    /**
     * Setter for outFile (the filepath to store the CSV representation of
     * Instances)
     *
     * @param outFile the filepath to store the CSV representation of Instances
     */
    @PipeParameter(name = "outFile", description = "The file to store the CSV representation of instances", defaultValue = DEFAULT_FILE)
    public void setOutFile(String outFile) {
        this.dataset.flushAndClose();
        this.outFile = outFile;
        File f=new File(outFile); if (f.exists()) f.delete();
        
        this.dataset=new CSVDataset(outFile);
    }

    /**
     * Returns the input type
     *
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return FeatureVector.class;
    }

    /**
     * Returns the output type
     *
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return FeatureVector.class;
    }

    private static boolean contains(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

    private String encodeFeat(String feat){
        return Base64.getEncoder().encodeToString(feat.getBytes()); 
    }

    /**
     * AbstractPipe the instance
     *
     * @param carrier the instance to pipe
     * @return the piped instance
     */
    @Override
    public Instance pipe(Instance carrier) {
        FeatureVector fv=(FeatureVector)carrier.getData();

        //Ensure the columns of the dataset fits with the instance
        if (dataset.getColumnCount()==0){
            nprops=carrier.getPropertyList().size();
            dictLength=Dictionary.getDictionary().size();

            String columnsToAdd[]=new String[2+nprops+dictLength];
            Object defaultValues[]=new Object[2+nprops+dictLength];
            columnsToAdd[0]="id"; defaultValues[0]="0";

            int j=1;
            for (String i : carrier.getPropertyList()) {
                columnsToAdd[j]=i; defaultValues[j]="0";
                j++;
            }

            Iterator<String> it= Dictionary.getDictionary().iterator();
            while (it.hasNext()){
                String dictEntry=it.next();
                columnsToAdd[j]=encodeFeat(dictEntry); defaultValues[j]="0";
                j++;
            }

            columnsToAdd[j]="target"; defaultValues[j]="";
            dataset.addColumns(columnsToAdd, defaultValues);
            
        }else if (dataset.getColumnCount()!=Dictionary.getDictionary().size()+2+carrier.getPropertyList().size()){
            String currentProps[]=dataset.getColumnNames();
            
            String newProps[]=new String[carrier.getPropertyList().size()-nprops];
            Object newDefaultValues[]=new Object[carrier.getPropertyList().size()-nprops];
            int j=0;
            for (String prop:carrier.getPropertyList())
                if (!contains(currentProps, prop)) { newProps[j]=prop; newDefaultValues[j]="0"; j++;}
            dataset.insertColumnsAt(newProps, newDefaultValues,nprops+1);
            nprops+=newProps.length;

            newProps=new String[Dictionary.getDictionary().size()-dictLength];
            newDefaultValues=new Object[Dictionary.getDictionary().size()-dictLength];
            j=0;
            int currentEntryIdx=0;
            Iterator<String> it= Dictionary.getDictionary().iterator();
            while (it.hasNext()){
                String dictEntry=it.next();
                if (currentEntryIdx>=dictLength){ newProps[j]=encodeFeat(dictEntry); newDefaultValues[j]="0"; j++; }
                currentEntryIdx++;
            }

            dataset.insertColumnsAt(newProps, newDefaultValues, dataset.getColumnCount()-1);
            dictLength+=newProps.length;
        }

        //Create and add the new row
        Object newRow[]=new Object[nprops+dictLength+2];
        newRow[0]=carrier.getName();
        int i=1;
        for (Object current : carrier.getValueList()) {
            newRow[i]=current;
            i++;
        }
        Iterator<String> it = Dictionary.getDictionary().iterator();
        while (it.hasNext()) {
            newRow[i]=fv.getValue(it.next());
            i++;
        }
        newRow[i]=carrier.getTarget();
        dataset.addRow(newRow);

        //If islast on the current burst close the dataset
        if (isLast()) {
            dataset.flushAndClose();
        }

        return carrier;
    }

    @Override
    public void readFromDisk(String filename) {
        Dictionary.getDictionary().readFromDisk(filename);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
