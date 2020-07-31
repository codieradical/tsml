package experiments.braindata.legacy;

import weka.core.Instances;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;

public class ARFFDatasetRepository {

    private String path;
    private File directory;

    public String getPath() {
        return path;
    }

    public ARFFDatasetRepository(String path) {
        this.path = path;
        directory = new File(path);

        if(!directory.exists() || !directory.isDirectory())
            throw new IllegalArgumentException("Path is not a directory.");

        if(!directory.canRead())
            throw new IllegalArgumentException("Directory is not readable.");
    }

    public ARFFDataset getDataset(String name) {
        File datasetDirectory = new File(this.directory.getPath() + File.separator + name);

        if(!directory.exists() || !directory.isDirectory())
            throw new IllegalArgumentException("Dataset not found.");

        if(!directory.canRead())
            throw new IllegalArgumentException("Dataset is not readable.");

        Instances instances = null;
        Instances trainInstances = null;
        Instances testInstances = null;

        for(File instancesFile : datasetDirectory.listFiles((file) -> file.getName().toLowerCase().contains(".arff"))) {
            if(instancesFile.getName().toLowerCase().contains("test")) {
                testInstances = loadClassificationData(instancesFile.getPath());
            } else if (instancesFile.getName().toLowerCase().contains("train")) {
                trainInstances = loadClassificationData(instancesFile.getPath());
            } else {
                instances = loadClassificationData(instancesFile.getPath());
            }
        }

        return new ARFFDataset(datasetDirectory.getPath(), instances, trainInstances, testInstances);
    }

    public ARFFDatasetRepositoryIterator iterator() {
        return new ARFFDatasetRepositoryIterator(directory);
    }

    private class ARFFDatasetRepositoryIterator implements Iterator<ARFFDataset> {

        int cursor = -1;
        int lastReturnedIndex = -1;

        ArrayList<File> datasetDirectories;

        @Override
        public boolean hasNext() {
            return cursor < datasetDirectories.size() - 1;
        }

        private File directory;

        public ARFFDatasetRepositoryIterator(File directory) {
            this.directory = directory;
            this.datasetDirectories = new ArrayList(Arrays.asList(directory.listFiles(File::isDirectory)));
        }

        @Override
        public ARFFDataset next() {
            if (!hasNext())
                throw new NoSuchElementException();

            cursor++;

            File datasetDirectory = datasetDirectories.get(cursor);

            Instances instances = null;
            Instances trainInstances = null;
            Instances testInstances = null;

            for(File instancesFile : datasetDirectory.listFiles((file) -> file.getName().toLowerCase().contains(".arff"))) {
                if(instancesFile.getName().toLowerCase().contains("test")) {
                    testInstances = loadClassificationData(instancesFile.getPath());
                } else if (instancesFile.getName().toLowerCase().contains("train")) {
                    trainInstances = loadClassificationData(instancesFile.getPath());
                } else {
                    instances = loadClassificationData(instancesFile.getPath());
                }
            }

            lastReturnedIndex = cursor;

            return new ARFFDataset(datasetDirectory.getPath(), instances, trainInstances, testInstances);
        }

        @Override
        public void remove() {
            if (lastReturnedIndex < 0)
                throw new IllegalStateException();

            datasetDirectories.remove(lastReturnedIndex);
            cursor = lastReturnedIndex;
            lastReturnedIndex = -1;
        }

    }

    private static Instances loadClassificationData(String dataLocation) {
        try{
            FileReader reader = new FileReader(dataLocation);
            Instances instances = new Instances(reader);
            instances.setClassIndex(instances.numAttributes()-1);
            return instances;
        } catch(Exception e){
            System.out.println("Exception caught: "+e);
        }
        return null;
    }

}
