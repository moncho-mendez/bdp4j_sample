package org.bdp4j.sample.pipe.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Iterator;

import com.google.auto.service.AutoService;

import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.TeePipe;
import org.bdp4j.sample.types.Dictionary;
import org.bdp4j.sample.types.FeatureVector;
import org.bdp4j.types.Instance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generate a CSV file from a Feature Vector
 * @author José Ramón Méndez Reboredo
 */
@TeePipe
@AutoService(Pipe.class)
public class GenerateFeatureVectorOutputPipe extends Pipe {
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
     * Marks if the file has been opened
     */
    boolean fileOpened = false;

    /**
     * The output printWriter
     */
    PrintWriter out;

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
    }

    /**
     * Getter for outFile (the filepath to store the CSV representation of Instances)
     *
     * @return Return the filepath to store the CSV representation of Instances
     */
    public String getOutFile() {
        return this.outFile;
    }

    /**
     * Setter for outFile (the filepath to store the CSV representation of Instances)
     *
     * @param outFile the filepath to store the CSV representation of Instances
     */
    @PipeParameter(name = "outFile", description = "The file to store the CSV representation of instances", defaultValue = DEFAULT_FILE)
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * Returns the input type
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return FeatureVector.class;
    }

    /**
     * Returns the output type
     * @return the output type
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
        FeatureVector fv=(FeatureVector)(carrier.getData());
        if (!fileOpened) {
            try {
                out = new PrintWriter(outFile);
                out.append("id;");
                out.append(Dictionary.getDictionary().toCSV());
                for (String i : carrier.getPropertyList()) {
                    out.append(i + ";");
                }
                out.append("target\n");
            } catch (FileNotFoundException e) {
                logger.fatal(e);
                e.printStackTrace();
                System.exit(0);
            }
            fileOpened = true;
        }

        out.append(carrier.getName() + ";" );
        Iterator<String> it=Dictionary.getDictionary().iterator();
        while (it.hasNext()){
            String current=it.next();
            out.append(fv.getValue(current)+";");
        }

        for (Object i : carrier.getValueList()) {
            out.append(i.toString() + ";");
        }
        out.append(carrier.getTarget().toString() + "\n");

        if (isLast() && fileOpened) {
            out.close();
        }
                
        return carrier;
    }

}