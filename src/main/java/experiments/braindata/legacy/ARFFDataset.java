package experiments.braindata.legacy;

import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.util.Iterator;

public class ARFFDataset {

    private String directory;
    private Instances instances;
    private Instances trainInstances;
    private Instances testInstances;

    public ARFFDataset(String directory, Instances instances, Instances testInstances, Instances trainInstances) {
        this.directory = directory;
        this.instances = instances;
        this.trainInstances = trainInstances;
        this.testInstances = testInstances;
    }



    public String getDirectory() {
        return this.directory;
    }

    public String getProblemName() {
        return this.directory.substring(this.directory.lastIndexOf(File.separator) + 1);
    }

    public double[] getClassDistribution() {
        double[] distribution = new double[instances.numClasses()];
        Iterator<Instance> iterator = instances.iterator();

        while (iterator.hasNext()) {
            distribution[(int)iterator.next().classValue()] += 1d / instances.numInstances();
        }

        return distribution;
    }

    public double[] getClassCounts() {
        double[] distribution = new double[instances.numClasses()];
        Iterator<Instance> iterator = instances.iterator();

        while (iterator.hasNext()) {
            distribution[(int)iterator.next().classValue()] += 1d;
        }

        return distribution;
    }

    public double[] getTrainClassDistribution() {
        double[] distribution = new double[trainInstances.numClasses()];
        Iterator<Instance> iterator = trainInstances.iterator();

        while (iterator.hasNext()) {
            distribution[(int)iterator.next().classValue()] += 1d / trainInstances.numInstances();
        }

        return distribution;
    }


    public Instances getInstances() {
        return this.instances;
    }

    public Instances getTrainInstances() {
        return this.trainInstances;
    }

    public Instances getTestInstances() {
        return this.getTestInstances();
    }



    public boolean hasInstances() {
        return instances != null;
    }

    public boolean hasTrainInstances() {
        return trainInstances != null;
    }

    public boolean hasTestInstances() {
        return testInstances != null;
    }
}
