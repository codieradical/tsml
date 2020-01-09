package machine_learning.classifiers.tuned.incremental.configs;

import evaluation.storage.ClassifierResults;
import experiments.data.DatasetLoading;
import machine_learning.classifiers.tuned.incremental.*;
import tsml.classifiers.distance_based.distances.DistanceMeasureConfigs;
import tsml.classifiers.distance_based.knn.KnnLoocv;
import tsml.classifiers.distance_based.knn.KnnConfigs;
import utilities.Utilities;
import utilities.params.ParamSpace;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class Configs {

    public static void main(String[] args) throws Exception {
        int seed = 0;
        Instances[] instances = DatasetLoading.sampleGunPoint(seed);
        Instances train = instances[0];
        Instances test = instances[1];
        IncTuner incTunedClassifier = buildTunedDtw1nnV1();
        incTunedClassifier.setTrainTimeLimit(10, TimeUnit.SECONDS);//TimeUnit.MINUTES);
        incTunedClassifier.buildClassifier(train);
        ClassifierResults trainResults = incTunedClassifier.getTrainResults();
        ClassifierResults results = new ClassifierResults();
        results.setDetails(incTunedClassifier, train);
        for(Instance testCase : test) {
            long startTime = System.nanoTime();
            double[] distribution = incTunedClassifier.distributionForInstance(testCase);
            long timeTaken = System.nanoTime() - startTime;
            int prediction = Utilities.argMax(distribution, new Random(seed));
            results.addPrediction(testCase.classValue(), distribution, prediction, timeTaken, "");
        }
        System.out.println(results.getAcc());
        System.out.println(trainResults.getBuildTime());
    }

    public static IncTuner buildTunedDtw1nnV1() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildDtwSpaceV1);
    }

    public static IncTuner buildTunedDdtw1nnV1() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildDdtwSpaceV1);
    }

    public static IncTuner buildTunedWdtw1nnV1() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildWdtwSpaceV1());
    }

    public static IncTuner buildTunedWddtw1nnV1() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildWddtwSpaceV1());
    }

    public static IncTuner buildTunedDtw1nnV2() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildDtwSpaceV2);
    }

    public static IncTuner buildTunedDdtw1nnV2() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildDdtwSpaceV2);
    }

    public static IncTuner buildTunedWdtw1nnV2() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildWdtwSpaceV2());
    }

    public static IncTuner buildTunedWddtw1nnV2() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildWddtwSpaceV2());
    }

    public static IncTuner buildTunedMsm1nnV1() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildMsmSpace());
    }

    public static IncTuner buildTunedTwed1nnV1() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildTwedSpace());
    }

    public static IncTuner buildTunedErp1nnV1() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildErpSpace);
    }

    public static IncTuner buildTunedLcss1nnV1() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildLcssSpace);
    }

    public static IncTuner buildTunedMsm1nnV2() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildMsmSpace());
    }

    public static IncTuner buildTunedTwed1nnV2() {
        return buildTuned1nnV1(i -> DistanceMeasureConfigs.buildTwedSpace());
    }

    public static IncTuner buildTunedErp1nnV2() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildErpSpace);
    }

    public static IncTuner buildTunedLcss1nnV2() {
        return buildTuned1nnV1(DistanceMeasureConfigs::buildLcssSpace);
    }

    public static IncTuner buildTunedKnn(Function<Instances, ParamSpace> paramSpaceFunction,
                                         Supplier<KnnLoocv> supplier) {
        IncTuner incTunedClassifier = new IncTuner();
        incTunedClassifier.setOnTrainDataAvailable(new IncKnnTunerBuilder().setIncTunedClassifier(incTunedClassifier).setParamSpace(paramSpaceFunction).setKnnSupplier(supplier));
        return incTunedClassifier;
    }

    public static IncTuner buildTuned1nnV1(Function<Instances, ParamSpace> paramSpaceFunction) {
        return buildTunedKnn(paramSpaceFunction, KnnConfigs::build1nnV1);
    }
    
    public static IncTuner buildTuned1nnV1(ParamSpace paramSpace) {
        return buildTunedKnn(i -> paramSpace, KnnConfigs::build1nnV1);
    }
    
    public static IncTuner buildTuned1nnV2(Function<Instances, ParamSpace> paramSpaceFunction) {
        return buildTunedKnn(paramSpaceFunction, KnnConfigs::build1nnV2);
    }

    public static IncTuner buildTuned1nnV2(ParamSpace paramSpace) {
        return buildTunedKnn(i -> paramSpace, KnnConfigs::build1nnV2);
    }

}
