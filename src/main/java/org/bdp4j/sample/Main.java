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
package org.bdp4j.sample;

import org.bdp4j.dataset.DatasetFromFile;
import org.bdp4j.pipe.AbstractPipe;
import org.bdp4j.pipe.SerialPipes;
import org.bdp4j.sample.pipe.impl.*;
import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Instance;
import org.bdp4j.types.Transformer;
import org.bdp4j.util.InstanceListUtils;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import java.awt.SystemTray;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Test pipe functioanlity
 */
public class Main {

    /**
     * List of instances to process
     */
    static List<Instance> carriers = new ArrayList<>();

    public static void main(String[] args) {
        /* Load instances */
        generateInstances("./samples/");

        List<Instance> burst1, burst2, burst3;
        burst1=new ArrayList<Instance>(carriers.subList(0, (int)Math.floor(carriers.size()/3)));
        burst2=new ArrayList<Instance>(carriers.subList((int)Math.floor(carriers.size()/3)+1,(int)Math.floor(carriers.size()/2)));
        burst3=new ArrayList<Instance>(carriers.subList((int)Math.floor(carriers.size()/2)+1, carriers.size()));

        /* Create the prorcessing pipe */
        AbstractPipe p = new SerialPipes(new AbstractPipe[]{
            new File2TargetAssignPipe(),
            new FileSizePipe(),
            new File2StringPipe(),
            new MeasureLengthPipe(),
            new GenerateStringOutputPipe(),
            new String2TokenArray(),
            new TokenArray2FeatureVector(),
            new GenerateFeatureVectorOutputPipe()
        }
        );

        System.out.println("The Pipeline used is" + p);

        /* Check dependencies */
        if (!p.checkDependencies()) {
            System.out.println(AbstractPipe.getErrorMessage());
            System.exit(-1);
        }

        /* Process instances */
        //p.pipeAll(carriers);
        p.pipeAll(burst1);
        p.pipeAll(burst2);
        p.pipeAll(burst3);

        /* Drop instances invalidated through piping process */
        carriers = InstanceListUtils.dropInvalid(carriers);
        System.out.println("Instances were processed.");

        //Then load the dataset to use it with Weka TM
        Map<String, Integer> targetValues = new HashMap<>();
        targetValues.put("ham", 0);
        targetValues.put("spam", 1);

        //Lets define transformers for the dataset
        Map<String, Transformer> transformersList = new HashMap<>();
        transformersList.put("target", new Enum2IntTransformer(targetValues));

        Instances data = (new DatasetFromFile(GenerateFeatureVectorOutputPipe.DEFAULT_FILE, transformersList)).loadFile().getWekaDataset();

        data.deleteStringAttributes();
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("Instance no: " + data.numInstances());
        System.out.println("Attritubes no: " + data.numAttributes());
        System.out.println("Target Attribute index: " + data.classIndex());

        try {
            System.out.println("------------------------------------------");
            System.out.println("--------- Naive Bayes Classifier ---------");
            System.out.println("------------------------------------------");
            Evaluation nvEvaluation = new Evaluation(data);
            nvEvaluation.crossValidateModel(new NaiveBayes(), data, 10, new Random(1));

            System.out.println("Summary: ");
            System.out.println("------------------------------------------");
            System.out.println(">> TN: " + nvEvaluation.confusionMatrix()[0][0]);
            System.out.println(">> FP: " + nvEvaluation.confusionMatrix()[0][1]);
            System.out.println(">> FN: " + nvEvaluation.confusionMatrix()[1][0]);
            System.out.println(">> TP: " + nvEvaluation.confusionMatrix()[1][1]);
        } catch (Exception ex) {
            System.out.println("Error executing Naïve Bayes: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     * Generate a instance List on instances attribute by recursivelly finding
     * all files included in testDir directory
     *
     * @param testDir The directory where the instances should be loaded
     */
    private static void generateInstances(String testDir) {
        try {
            Files.walk(Paths.get(testDir))
                    .filter(Files::isRegularFile)
                    .forEach(FileMng::visit);
        } catch (IOException e) {
            System.exit(0);
        }
    }

    /**
     * Used to add a new instance on instances attribute when a new file is
     * detected.
     */
    static class FileMng {

        /**
         * Include a filne in the instancelist
         *
         * @param path The path of the file
         */
        static void visit(Path path) {
            File data = path.toFile();
            String target = null;
            String name = data.getPath();
            File source = data;

            carriers.add(new Instance(data, target, name, source));
        }
    }
}
