package experiments.braindata;

import experiments.Experiments;
import weka.core.Instances;

import java.io.File;

    public class MultivariateEEGAndMEGEvaluation {

    private static String datasetsPath;
    private static String classifier = null;

    public static void main(String[] args) {
        datasetsPath = args[0];
        if(args.length > 1) {
            classifier = args[1];
        }


        //testClassifier();
        //experiment1();
        experiment2();
    }
    /*
    public static void experiment0() {
        System.out.println("Running Experiment 0");
        try {
            String[] classifiers = {"ED_I", "ED_D", "DTW_I", "DTW_D", "DTW_A"};
            String[] datasets = {""};

            Experiments.ExperimentalArguments expThreaded = new Experiments.ExperimentalArguments();
            expThreaded.dataReadLocation = datasetsPath;
            expThreaded.resultsWriteLocation = "results" + File.separator + "Experiment1";
            expThreaded.trainEstimateMethod = "StratifiedResamplesEvaluator";
            expThreaded.generateErrorEstimateOnTrainSet = true;
            expThreaded.foldId = 0;
            expThreaded.classifierResultsFileFormat = 3;

            Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, datasets, 0, 1);
        } catch (Exception ex) {
            System.out.println("Experiment 0 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    */

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

            // Failed to run, so fix and run separately.
            // Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.BIG_DATASETS, 0, 1);

        } catch (Exception ex) {
            System.out.println("Experiment 1 Failed to run.\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

        public static void experiment2() {
            System.out.println("Running Experiment 2");
            try {
                //String[] classifiers={"TS-CHIEF_I"}; // test individual
                String[] classifiers={"PF_I", "TSF_I", "Shapelet_D", "CBOSS_I", "TS-CHIEF_I"};
                //String[] classifiers={"PF", "TSF", "Shapelet_D", "CBOSS"};
                //String[] classifiers={"PF_I", "TSF_I", "CBOSS_I", "TS-CHIEF_I"};

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

                //Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, new String[] {Datasets.SMALL_DATASETS[0]}, 0, 1);
                Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.SMALL_DATASETS, 0, 1);

                //Experiments.setupAndRunMultipleExperimentsThreaded(expThreaded, classifiers, Datasets.BIG_DATASETS, 0, 1);

            } catch (Exception ex) {
                System.out.println("Experiment 2 Failed to run.\n" + ex.getMessage());
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
