package opt.test;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TravelingSalesmanTest {
    /** The n value */
    private static final int N = 50;
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Random random = new Random();
        // create the random points
        double[][] points = new double[N][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();   
        }
        // for rhc, sa, and ga we use a permutation based encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        FixedIterationTrainer fit;
        int iterations = 10000;
        int[] data = new int[30];
        double average = 0.0;
        double stdev = 0.0;
        for (int i = 0; i < 30; i++){
            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            fit = new FixedIterationTrainer(rhc, iterations);
            fit.train();
            //System.out.println(ef.value(rhc.getOptimal()));
            average += ef.value(rhc.getOptimal());
            data[i] = (int)ef.value(rhc.getOptimal());
        }
        average = average / 30.0;
        for (int i = 0; i < 30; i++) {
            stdev += (average - data[i]) * (average - data[i]) / 29.0;
        }
        stdev = Math.sqrt(stdev);
        System.out.println(average);
        System.out.println(stdev * 2);
        data = new int[30];
        average = 0.0;
        stdev = 0.0;
        for(int i = 0 ; i < 30; i++) {
            SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
            fit = new FixedIterationTrainer(sa, iterations);
            fit.train();
            //System.out.println(ef.value(sa.getOptimal()));
            average += ef.value(sa.getOptimal());
            data[i] = (int)ef.value(sa.getOptimal());
        }
        average = average / 30.0;
        for (int i = 0; i < 30; i++) {
            stdev += (average - data[i]) * (average - data[i]) / 29.0;
        }
        stdev = Math.sqrt(stdev);
        System.out.println(average);
        System.out.println(stdev * 2);
        data = new int[30];
        average = 0.0;
        stdev = 0.0;
        for (int i = 0; i < 30; i++) {
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
            fit = new FixedIterationTrainer(ga, iterations);
            fit.train();
            //System.out.println(ef.value(ga.getOptimal()));
            average += ef.value(ga.getOptimal());
            data[i] = (int)ef.value(ga.getOptimal());
        }
        average = average / 30.0;
        for (int i = 0; i < 30; i++) {
            stdev += (average - data[i]) * (average - data[i]) / 29.0;
        }
        stdev = Math.sqrt(stdev);
        System.out.println(average);
        System.out.println(stdev * 2);
    }
}
