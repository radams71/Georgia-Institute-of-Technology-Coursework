package opt.test;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * A test using the flip flop evaluation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FlipFlopTest {
    /** The n value */
    private static final int N = 300;
    
    public static void main(String[] args) {
        int iterations = 5000;
        for (int j = 5000; j <= iterations; j +=500) {
            System.out.println(j);
            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);
            EvaluationFunction ef = new FlipFlopEvaluationFunction();
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
            FixedIterationTrainer fit;

            int[] data = new int[30];
            double average = 0.0;
            double stdev = 0.0;
            for (int i = 0; i < 30; i++) {
                RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
                fit = new FixedIterationTrainer(rhc, j);
                fit.train();
                //System.out.println(ef.value(rhc.getOptimal()));
                average += ef.value(rhc.getOptimal());
                data[i] = (int) ef.value(rhc.getOptimal());
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
                SimulatedAnnealing sa = new SimulatedAnnealing(1E10, .90, hcp);
                fit = new FixedIterationTrainer(sa, j);
                fit.train();
                //System.out.println(ef.value(sa.getOptimal()));
                average += ef.value(sa.getOptimal());
                data[i] = (int) ef.value(sa.getOptimal());
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
                StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 20, gap);
                fit = new FixedIterationTrainer(ga, j);
                fit.train();
                //System.out.println(ef.value(ga.getOptimal()));
                average += ef.value(ga.getOptimal());
                data[i] = (int) ef.value(ga.getOptimal());
            }
            average = average / 30.0;
            for (int i = 0; i < 30; i++) {
                stdev += (average - data[i]) * (average - data[i]) / 29.0;
            }
            stdev = Math.sqrt(stdev);
            System.out.println(average);
            System.out.println(stdev * 2);
        /*
        MIMIC mimic = new MIMIC(200, 5, pop);
        fit = new FixedIterationTrainer(mimic, 1000);
        fit.train();
        System.out.println(ef.value(mimic.getOptimal()));
        */
        }
    }
}
