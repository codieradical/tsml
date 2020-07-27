/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tsml.classifiers.multivariate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import evaluation.storage.ClassifierResults;
import machine_learning.classifiers.ensembles.AbstractEnsemble.EnsembleModule;
import machine_learning.classifiers.ensembles.voting.MajorityVote;
import machine_learning.classifiers.ensembles.voting.ModuleVotingScheme;
import machine_learning.classifiers.ensembles.weightings.EqualWeighting;
import machine_learning.classifiers.ensembles.weightings.ModuleWeightingScheme;
import static utilities.GenericTools.indexOfMax;
import static utilities.multivariate_tools.MultivariateInstanceTools.splitMultivariateInstanceWithClassVal;
import static utilities.multivariate_tools.MultivariateInstanceTools.splitMultivariateInstances;

import org.apache.commons.lang3.time.StopWatch;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.RandomizableIteratedSingleClassifierEnhancer;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author raj09hxu
 */
public class DimensionIndependentEnsemble extends AbstractClassifier{
    
    protected ModuleWeightingScheme weightingScheme = new EqualWeighting();
    protected ModuleVotingScheme votingScheme = new MajorityVote();
    protected EnsembleModule[] modules;
    
    long seed;
    
    int numClasses, numChannels;
    Instances train;
    Instances[] channels;
    Classifier[] classifiers;
    String[] classifierNames;
    
    Classifier original_model;
    
    double[] priorWeights;
    
    public DimensionIndependentEnsemble(Classifier cla){
        original_model = cla;
    }
   
    public void setSeed(long sd){
        seed = sd;
        
        if(original_model instanceof RandomizableIteratedSingleClassifierEnhancer){
            RandomizableIteratedSingleClassifierEnhancer r = (RandomizableIteratedSingleClassifierEnhancer) original_model;
            r.setSeed((int) seed);
        }
        else{ 
            //check through reflection if the classifier has a method with seed in the name, that takes an int or a long.
            Method[] methods = original_model.getClass().getMethods();
            for (Method method : methods) {
                Class[] paras = method.getParameterTypes();
                //if the method contains the name seed, and takes in 1 parameter, thats a primitive. probably setRandomSeed.
                String name = method.getName().toLowerCase();
                if((name.contains("random") || name.contains("seed"))
                    && paras.length == 1 && (paras[0] == int.class || paras[0] == long.class )){
                    try {
                        if(paras[0] == int.class)
                            method.invoke(original_model, (int) seed);
                        else
                            method.invoke(original_model, seed);
                        
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        System.out.println(ex);
                        System.out.println("Tried to set the seed method name: " + method.getName());
                    }
                }
            }
        }
        
    }
    
    public void setPriorWeights(double[] weights){
        priorWeights = weights;
    }
    
    protected void initialiseModules() throws Exception{
        classifiers = AbstractClassifier.makeCopies(original_model, numChannels);
        classifierNames = new String[numChannels];
        
        //one module for each channel.
        this.modules = new EnsembleModule[numChannels];
        for (int m = 0; m < numChannels; m++){
            classifierNames[m] = classifiers[m].getClass().getSimpleName() +"_"+m;
            modules[m] = new EnsembleModule(classifierNames[m], classifiers[m], "");
            if(priorWeights != null)
                modules[m].priorWeight = priorWeights[m];
        }
        
        weightingScheme.defineWeightings(modules, numClasses);
        votingScheme.trainVotingScheme(modules, numClasses);
    }
    
    @Override
    public void buildClassifier(Instances data) throws Exception {
        train = data;
        numClasses = data.numClasses();
        channels = splitMultivariateInstances(data);
        numChannels = channels.length;
        initialiseModules();
               
        //build the classifier.
        for(int i=0; i<numChannels; i++){
            Instances channel = channels[i];
            // ALEX DEBUG
            // classifiers aren't always evaluated on build time.
            // I think this has something to do with contract implementations.
            // Here the build time is recorded and set.
            StopWatch trainTime = new StopWatch();
            trainTime.start();
            modules[i].getClassifier().buildClassifier(channel);
            trainTime.stop();
            if(modules[i].trainResults == null) {
                modules[i].trainResults = new ClassifierResults();
                modules[i].trainResults.setBuildTime(trainTime.getNanoTime());
            }
        }
    }
    
    Instances[] convertedTest = null;
    
    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {   
        double[] dist = distributionForInstance(votingScheme, modules, splitMultivariateInstanceWithClassVal(instance));
        return dist;
    }

    /*@Override
    public String toString(){
        String output = "";
        for(Classifier cl : classifiers)
            output += cl.toString() + ",";
        return output;
    }*/
    
    private double[] distributionForInstance(ModuleVotingScheme vs, EnsembleModule[] modules, Instance[] testInstance) throws Exception{
        double[] preds = new double[numClasses];
        
        int pred;
        double[] dist;
        for(int m = 0; m < numChannels; m++){
            long startTime = System.currentTimeMillis();
            dist = modules[m].getClassifier().distributionForInstance(testInstance[m]);
            long predTime = System.currentTimeMillis() - startTime;
            
            vs.storeModuleTestResult(modules[m], dist, predTime);
            
            pred = (int)indexOfMax(dist);
            preds[pred] += modules[m].priorWeight * 
                           modules[m].posteriorWeights[pred];
        }
        
        return vs.normalise(preds);
    }
}
