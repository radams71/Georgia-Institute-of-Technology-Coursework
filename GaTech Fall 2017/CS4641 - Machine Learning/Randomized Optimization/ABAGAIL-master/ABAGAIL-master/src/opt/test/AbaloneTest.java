package opt.test;

import dist.*;
import func.nn.NeuralNetwork;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;
import shared.filt.LabelSplitFilter;
import shared.reader.ArffDataSetReader;
import shared.reader.DataSetLabelBinarySeperator;
import shared.reader.DataSetReader;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying abalone as having either fewer 
 * or more than 15 rings. 
 *
 * @author Hannah Lau
 * @version 1.0
 */
public class AbaloneTest {
    private static Instance[] instances = initializeInstances();
    private static Instance[] testinst;
    private static int inputLayer = 104, hiddenLayer = 54, outputLayer = 1, trainingIterations = 500;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure;

    private static DataSet set = new DataSet(instances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[1];
    private static String[] oaNames = {"GA"};//{"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) throws Exception{
        DataSetReader dsr = new ArffDataSetReader(new File("").getAbsolutePath() + "/src/adult.test1.arff");
        DataSet ds = dsr.read();
        LabelSplitFilter lsf = new LabelSplitFilter();
        lsf.filter(ds);
        testinst = ds.getInstances();
        double[] data = new double[5];
        double average = 0.0;
        double stdev = 0.0;
        for(int k = 0; k < 5; k++) {
            results = "";
            measure = new SumOfSquaresError();
            instances = initializeInstances();
            for (int i = 0; i < oa.length; i++) {
                networks[i] = factory.createClassificationNetwork(
                        new int[]{inputLayer, hiddenLayer, outputLayer});
                nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
            }

            //oa[0] = new RandomizedHillClimbing(nnop[0]);
            //oa[0] = new SimulatedAnnealing(1E11, .90, nnop[0]);
            oa[0] = new StandardGeneticAlgorithm(100, 50, 10, nnop[0]);

            for (int i = 0; i < oa.length; i++) {
                double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
                train(oa[i], networks[i], oaNames[i], trainingIterations); //trainer.train();
                end = System.nanoTime();
                trainingTime = end - start;
                trainingTime /= Math.pow(10, 9);
                correct = 0;
                incorrect = 0;
                Instance optimalInstance = oa[i].getOptimal();
                networks[i].setWeights(optimalInstance.getData());

                double predicted, actual;
                start = System.nanoTime();
                for (int j = 0; j < testinst.length; j++) {
                    networks[i].setInputValues(testinst[j].getData());
                    networks[i].run();

                    predicted = Double.parseDouble(testinst[j].getLabel().toString());
                    actual = Double.parseDouble(networks[i].getOutputValues().toString());

                    double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;

                }
                end = System.nanoTime();
                testingTime = end - start;
                testingTime /= Math.pow(10, 9);

                results += "\nResults for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                        "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                        + df.format(correct / (correct + incorrect) * 100) + "%\nTraining time: " + df.format(trainingTime)
                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
                data[k] = (correct / (correct + incorrect) * 100);
                average += (correct / (correct + incorrect) * 100);
            }

            System.out.println(results);
        }
        average = average / 5.0;
        for (int i = 0; i < 5; i++) {
            stdev += (average - data[i]) * (average - data[i]) / 4.0;
        }
        stdev = Math.sqrt(stdev);
        System.out.println(average);
        System.out.println(stdev * 2);
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName, int trainingIterationsk) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");

        for(int i = 0; i < trainingIterationsk; i++) {
            oa.train();

            double error = 0;
            for(int j = 0; j < instances.length; j++) {
                network.setInputValues(instances[j].getData());
                network.run();

                Instance output = instances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += measure.value(output, example);
            }
            Instance optimalInstance = oa.getOptimal();
            NeuralNetwork network1 = factory.createClassificationNetwork(new int[] {inputLayer, hiddenLayer, outputLayer});
            network1.setWeights(optimalInstance.getData());
            /*
            double predicted, actual;
            double correct = 0, incorrect = 0;
            for (int j = 0; j < testinst.length; j++) {
                network1.setInputValues(testinst[j].getData());
                network1.run();

                predicted = Double.parseDouble(testinst[j].getLabel().toString());
                actual = Double.parseDouble(network1.getOutputValues().toString());

                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;

            }
            System.out.println(df.format(correct / (correct + incorrect) * 100));
            */
        }
    }

    private static Instance[] initializeInstances() {
        /*
        double[][][] attributes = new double[4177][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/abalone.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[7]; // 7 attributes
                attributes[i][1] = new double[1];

                for(int j = 0; j < 7; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
            instances[i].setLabel(new Instance(attributes[i][1][0] < 15 ? 0 : 1));
        }

        return instances;
        */
        DataSetReader dsr = new ArffDataSetReader(new File("").getAbsolutePath() + "/src/adult.6%data.arff");
        DataSet ds;
        try {
            ds = dsr.read();
            LabelSplitFilter lsf = new LabelSplitFilter();
            lsf.filter(ds);
            //DataSetLabelBinarySeperator.seperateLabels(ds);
        } catch (Exception e){
            return new Instance[0];
        }
        return ds.getInstances();
    }
}
