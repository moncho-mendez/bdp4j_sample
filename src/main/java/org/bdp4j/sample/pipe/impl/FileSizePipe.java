package org.bdp4j.sample.pipe.impl;

import com.google.auto.service.AutoService;
import org.bdp4j.pipe.Pipe;
import org.bdp4j.pipe.PipeParameter;
import org.bdp4j.pipe.PropertyComputingPipe;
import org.bdp4j.types.Instance;

import java.io.File;

/**
 * A pipe able to compute the size of a file
 */
@AutoService(Pipe.class)
@PropertyComputingPipe
public class FileSizePipe extends Pipe {
    /**
     * The name of the deafult propety to store the filesize
     */
    public static final String DEFAULT_FILESIZE_PROP = "filesize";

    /**
     * The property Name to store the filesize
     */
    String propName=null;

    /**
     * Default consturctor
     */
    public FileSizePipe() {
        this(DEFAULT_FILESIZE_PROP);
    }

    public FileSizePipe(String propName) {
        /* Must declare here the dependencies */
        /*     alwaysBefore     notAfter */
        super(new Class<?>[0], new Class<?>[0]);
        this.propName=propName;
    }

    /**
     * Setter for propName (the name of the property to store the filesize)
     * @param propName The name of the property to store the filesize
     */
    @PipeParameter(name="propName", description="The name of the property to store the filesize", defaultValue=DEFAULT_FILESIZE_PROP)
    public void setPropName(String propName){
        this.propName=propName;
    }

    /**
     * Getter for propName (the name of the property to store the filesize)
     * @return Return the name of the property to store the filesize
     */
    public String getPropName(){
        return this.getPropName();
    }

    /**
     * The imput type of Instance.getData
     * @return the input type
     */
    @Override
    public Class<?> getInputType() {
        return File.class;
    }

    /**
     * The output type of Instance.getData
     * @return the output type
     */
    @Override
    public Class<?> getOutputType() {
        return File.class;
    }

    /**
     * Pipe the instance
     * @param carrier The instance to pipe
     */
    @Override
    public Instance pipe(Instance carrier) {
        carrier.setProperty(propName, ((File) (carrier.getData())).length());

        return carrier;
    }

}