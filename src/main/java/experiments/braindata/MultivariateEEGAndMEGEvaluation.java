package experiments.braindata;

import experiments.Experiments;

import java.io.File;

public class MultivariateEEGAndMEGEvaluation {

    private static String datasetsPath;

    public static void main(String[] args) {
        datasetsPath = args[0];

        //testClassifier();
        experiment1();
    }

    public static void experiment1() {
        System.out.println("Running Experiment 1");
        try {
            // HIVE-COTE + Shapelets have been removed temporarily due to issues.
            // Add them in a separate test if they can be fixed.
            String[] classifiers={"ED_I","ED_D","DTW_I","DTW_D","DTW_A", "CBOSS_I", "RISE_I", "STC_I", "TSF_I","PF_I","TS-CHIEF_I"};

            Experiments.ExperimentalArguments expThreaded = new Experiments.ExperimentalArguments();
            // Expected datasets are defined in the Datasets class.
            expThreaded.dataReadLocation = datasetsPath;
            expThreaded.resultsWriteLocation = "results" + File.separator + "Experiment1";
            expThreaded.trainEstimateMethod = "StratifiedResamplesEvaluator";
            expThreaded.generateErrorEstimateOnTrainSet = true;
            expThreaded.foldId = 0;
            expThreaded.classifierResultsFileFormat = 3;

            Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.SMALL_DATASETS, 0, 1);

            Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.BIG_DATASETS, 0, 1);

        } catch (Exception ex) {
            System.out.println("Experiment 1 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

//    public static void testClassifier() {
//        System.out.println("Running Experiment 1");
//        try {
//            String[] classifiers={"TS-CHIEF_I"};
//
//            Experiments.ExperimentalArguments expThreaded = new Experiments.ExperimentalArguments();
//            // Expected datasets are defined in the Datasets class.
//            expThreaded.dataReadLocation = datasetsPath;
//            expThreaded.resultsWriteLocation = "results" + File.separator + "Experiment1";
//            expThreaded.trainEstimateMethod = "StratifiedResamplesEvaluator";
//            expThreaded.generateErrorEstimateOnTrainSet = true;
//            expThreaded.foldId = 0;
//            expThreaded.classifierResultsFileFormat = 3;
//
//            Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.SMALL_DATASETS, 0, 1);
//
//            Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.BIG_DATASETS, 0, 1);
//
//        } catch (Exception ex) {
//            System.out.println("Experiment 1 Failed to run.\n" + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }
}
