package experiments.braindata;

import experiments.Experiments;

import java.io.File;

    public class MultivariateEEGAndMEGEvaluation {

    private static String datasetsPath;
    private static String classifier = null;
    private static String dataset = null;


    // Experiment 1 is fairly small and could be run on a decent PC, perhaps up to the larger datasets.
    // Experiment 2 is larger and was run on the UEA HPC.
    // Experiment 2 is best run alone with a classifier launch arg for each classifier. (PF_I, TSF_I, TS-CHIEF_I, CBOSS_I)
    // Experiment 3 is seemingly the most resource intensive experiment and failed to run properly on the HPC.

    // This TSML fork has been modified for these experiments.
    // For instance, a new Classifier Results File Format has been added (3) which outputs both metrics and predictions.
    // There are other tweaks and logs throughout the code, mostly to solve issues I experienced.
    // These are labelled with "// ALEX DEBUG" if you wish to search for them.

    // Launch arguments:
    //  <Datasets Path> [classifier (experiment 2)] [dataset (experiment 2)]
    public static void main(String[] args) {
        datasetsPath = args[0];
        if(args.length > 1) {
            classifier = args[1];
        } if(args.length > 2) {
            dataset = args[2];
        }

        // Uncomment the experiment you'd like to run.

        //experiment1();
        experiment2();
        //experiment3();
    }

    public static void experiment1() {
        System.out.println("Running Experiment 1");
        try {
            String[] classifiers = {"ED_I", "ED_D", "DTW_I", "DTW_D", "DTW_A"};

            Experiments.ExperimentalArguments expThreaded = new Experiments.ExperimentalArguments();
            expThreaded.dataReadLocation = datasetsPath;
            expThreaded.resultsWriteLocation = "results" + File.separator + "Experiment1";
            expThreaded.trainEstimateMethod = "StratifiedResamplesEvaluator";
            expThreaded.generateErrorEstimateOnTrainSet = true;
            expThreaded.foldId = 0;
            expThreaded.classifierResultsFileFormat = 3;

            if(classifier != null) {
                expThreaded.classifierName = classifier;

                // If a specific dataset was provided, run on it.
                if(dataset != null) {
                    expThreaded.datasetName = dataset;
                    Experiments.setupAndRunExperiment(expThreaded);
                } else {
                    for (String dataset : Datasets.ALL_DATASETS) {
                        expThreaded.datasetName = dataset;
                        Experiments.setupAndRunExperiment(expThreaded);
                        System.gc();
                    }
                }
            } else {
                Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.ALL_DATASETS, 0, 1);
            }
        } catch (Exception ex) {
            System.out.println("Experiment 1 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /*
    This was an early take on Experiment 2, it doesn't work.

    public static void experiment2() {
        System.out.println("Running Experiment 2");
        try {
            // HIVE-COTE + Shapelets have been removed temporarily due to issues.
            // Add them in a separate test if they can be fixed.
            String[] classifiers={"ED_I","ED_D","DTW_I","DTW_D","DTW_A", "CBOSS_I", "RISE_I", "STC_I", "TSF_I","PF_I","TS-CHIEF_I"};

            Experiments.ExperimentalArguments expThreaded = new Experiments.ExperimentalArguments();
            // Expected datasets are defined in the Datasets class.
            expThreaded.dataReadLocation = datasetsPath;
            expThreaded.resultsWriteLocation = "results" + File.separator + "Experiment2";
            expThreaded.trainEstimateMethod = "StratifiedResamplesEvaluator";
            expThreaded.generateErrorEstimateOnTrainSet = true;
            expThreaded.foldId = 0;
            expThreaded.classifierResultsFileFormat = 3;

            Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.SMALL_DATASETS, 0, 1);

            // Failed to run, so fix and run separately.
            // Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.BIG_DATASETS, 0, 1);

        } catch (Exception ex) {
            System.out.println("Experiment 2 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    */

    public static void experiment2() {
        System.out.println("Running Experiment 2");
        try {
            String[] classifiers={"PF_I", "TSF_I", "CBOSS_I", "TS-CHIEF_I"};

            // A classifier can be specified with a launch arg to run it separately.
            // This was used on the HPC due to threading issues I encountered but probably isn't necessary anymore.
            if(classifier != null)
                classifiers = new String[] { classifier };

            Experiments.ExperimentalArguments expThreaded = new Experiments.ExperimentalArguments();
            // Expected datasets are defined in the Datasets class.
            expThreaded.dataReadLocation = datasetsPath;
            expThreaded.resultsWriteLocation = "results" + File.separator + "Experiment2";
            expThreaded.trainEstimateMethod = "StratifiedResamplesEvaluator";
            expThreaded.generateErrorEstimateOnTrainSet = true;
            expThreaded.foldId = 0;
            expThreaded.classifierResultsFileFormat = 3;
            expThreaded.debug = true;

            // Often runs out of memory when running in parallel.
            if(classifier != null) {
                expThreaded.classifierName = classifier;

                // If a specific dataset was provided, run on it.
                if(dataset != null) {
                    expThreaded.datasetName = dataset;
                    Experiments.setupAndRunExperiment(expThreaded);
                } else {
                    for (String dataset : Datasets.ALL_DATASETS) {
                        expThreaded.datasetName = dataset;
                        Experiments.setupAndRunExperiment(expThreaded);
                        System.gc();
                    }
                }
            } else {
                Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.SMALL_DATASETS, 0, 1);
            }
        } catch (Exception ex) {
            System.out.println("Experiment 2 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }


    // For some reason this didn't run on the HPC.
    // So this experiment was run on my desktop.
    public static void experiment3() {
        System.out.println("Running Experiment 3");

        try {
            Experiments.ExperimentalArguments shapeletExperiment = new Experiments.ExperimentalArguments();
            // Expected datasets are defined in the Datasets class.
            shapeletExperiment.dataReadLocation = datasetsPath;
            shapeletExperiment.resultsWriteLocation = "results" + File.separator + "Experiment3";
            shapeletExperiment.trainEstimateMethod = "StratifiedResamplesEvaluator";
            shapeletExperiment.generateErrorEstimateOnTrainSet = true;
            shapeletExperiment.foldId = 0;
            shapeletExperiment.classifierResultsFileFormat = 3;
            shapeletExperiment.debug = true;

//            shapeletExperiment.datasetName = "TINY_TEST";
//            shapeletExperiment.classifierName = "Shapelet_D";
//            Experiments.setupAndRunExperiment(shapeletExperiment);

            for(String dataset : Datasets.ALL_DATASETS) {
                shapeletExperiment.datasetName = dataset;

                if(classifier != null) {
                    shapeletExperiment.classifierName = classifier;
                    Experiments.setupAndRunExperiment(shapeletExperiment);
                } else {
                    shapeletExperiment.classifierName = "Shapelet_D";
                    Experiments.setupAndRunExperiment(shapeletExperiment);
                    shapeletExperiment.classifierName = "Shapelet_I";
                    Experiments.setupAndRunExperiment(shapeletExperiment);
                    shapeletExperiment.classifierName = "Shapelet_Indep";
                    Experiments.setupAndRunExperiment(shapeletExperiment);
                }
            }

        } catch (Exception ex) {
            System.out.println("Experiment 3 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
