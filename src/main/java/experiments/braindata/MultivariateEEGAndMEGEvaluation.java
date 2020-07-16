package experiments.braindata;

import experiments.Experiments;

import java.io.File;

public class MultivariateEEGAndMEGEvaluation {

    private static String datasetsPath;

    public static void main(String[] args) {
        datasetsPath = args[0];

        evalAllMultivariate();
    }

    public static void evalAllMultivariate() {
        System.out.println("Running Experiment 1");
        try {
            String[] classifiers={"ED_I","ED_D","DTW_I","DTW_D","DTW_A","Shapelet_I","Shapelet_D","Shapelet_Indep", "HIVE-COTE_I", "HC_I", "CBOSS_I", "RISE_I", "STC_I", "TSF_I","PF_I","TS-CHIEF_I","HC-PF_I","HIVE-COTEn_I"};

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
}
