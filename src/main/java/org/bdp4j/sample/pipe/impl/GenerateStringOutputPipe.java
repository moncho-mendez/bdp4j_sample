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

import java.io.File;

import com.google.auto.service.AutoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.TeePipe;
import org.bdp4j.types.Instance;

import org.bdp4j.util.CSVDataset;

/**
 * A pipe able to measure the length of a string and create the corresponding
 * property
 */
@AutoService(AbstractPipe.class)
@TeePipe
public class GenerateStringOutputPipe extends AbstractPipe {

    /**
     * A logger for logging purposes
     */
    private static final Logger logger = LogManager.getLogger(GenerateStringOutputPipe.class);

    /**
     * The default file to store CSV contents
     */
    public static final String DEFAULT_FILE = "output.csv";

    /**
     * The output file
     */
    String outFile = null;

    /**
     * Csv DAtaset to store data
     */
    CSVDataset dataset=null;

    /**
     * Default consturctor
     */
    public GenerateStringOutputPipe() {
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
    public GenerateStringOutputPipe(String outFile) {
        /* Must declare here the dependencies */
 /* alwaysBefore notAfter */
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
     * The imput type of Instance.getData
     *
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return String.class;
    }

    /**
     * The output type of Instance.getData
     *
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return String.class;
    }

    private static boolean contains(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

    /**
     * AbstractPipe the instance
     *
     * @param carrier The instance to pipe
     */
    @Override
    public Instance pipe(Instance carrier) {
        //Ensure the columns of the dataset fits with the instance
        if (dataset.getColumnCount()==0){
            String columnsToAdd[]=new String[3+carrier.getPropertyList().size()];
            Object defaultValues[]=new Object[3+carrier.getPropertyList().size()];
            columnsToAdd[0]="id";defaultValues[0]="0";
            columnsToAdd[1]="content";defaultValues[0]="";
            int j=2;

            //dataset.addColumn("id", "0");
            //dataset.addColumn("content", "0");

            for (String i : carrier.getPropertyList()) {
                columnsToAdd[j]=i;defaultValues[j]="";
                //this.dataset.addColumn(i, "0");
                j++;
            }

            columnsToAdd[j]="target";defaultValues[j]="";
            dataset.addColumns(columnsToAdd,defaultValues);
        }else if (dataset.getColumnCount()!=(carrier.getPropertyList().size()+3)){
            String currentProps[]=dataset.getColumnNames();
            for (String prop:carrier.getPropertyList())
                if (!contains(currentProps, prop)) dataset.insertColumnAt(prop, "0", dataset.getColumnCount()-1);
        }
        
        //Create and add the new row
        Object newRow[]=new Object[carrier.getPropertyList().size()+3];
        newRow[0]=carrier.getName();
        newRow[1]=carrier.getData();
        int i=2;
        for (Object current : carrier.getValueList()) {
            newRow[i]=current;
            i++;
        }
        newRow[newRow.length-1]=carrier.getTarget();
        dataset.addRow(newRow);

        //If islast on the current burst close the dataset
        if (isLast()) {
            dataset.flushAndClose();
        }
        return carrier;
    }
}
