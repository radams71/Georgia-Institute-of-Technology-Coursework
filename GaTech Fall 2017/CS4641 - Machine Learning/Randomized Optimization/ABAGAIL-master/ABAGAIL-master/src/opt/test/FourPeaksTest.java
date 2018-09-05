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
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    private static final int N = 75;
    /** The t value */
    private static final int T = N / 5;
    
    public static void main(String[] args) {
        int iterations = 1000;
        for (int j = 100; j <=iterations; j +=100) {
            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);
            EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
            FixedIterationTrainer fit;
            int[] data = new int[200];
            double average = 0.0;
            double stdev = 0.0;
            System.out.println(j);
            for (int i = 0; i < 200; i++) {
                RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
                fit = new FixedIterationTrainer(rhc, j);
                fit.train();
                //System.out.println(ef.value(rhc.getOptimal()));
                average += ef.value(rhc.getOptimal());
                data[i] = (int) ef.value(rhc.getOptimal());
            }
            average = average / 200.0;
            for (int i = 0; i < 200; i++) {
                stdev += (average - data[i]) * (average - data[i]) / 199.0;
            }
            stdev = Math.sqrt(stdev);
            System.out.println(average);
            System.out.println(stdev * 2);
            data = new int[200];
            average = 0.0;
            stdev = 0.0;
            for (int i = 0; i < 200; i++) {
                SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
                fit = new FixedIterationTrainer(sa, j);
                fit.train();
                //System.out.println(ef.value(sa.getOptimal()));
                average += ef.value(sa.getOptimal());
                data[i] = (int) ef.value(sa.getOptimal());
            }
            average = average / 200.0;
            for (int i = 0; i < 200; i++) {
                stdev += (average - data[i]) * (average - data[i]) / 199.0;
            }
            stdev = Math.sqrt(stdev);
            System.out.println(average);
            System.out.println(stdev * 2);
            data = new int[200];
            average = 0.0;
            stdev = 0.0;

            for (int i = 0; i < 200; i++) {
                StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(600, 480, 90, gap);
                fit = new FixedIterationTrainer(ga, j);
                fit.train();
                //System.out.println(ef.value(ga.getOptimal()));
                average += ef.value(ga.getOptimal());
                data[i] = (int) ef.value(ga.getOptimal());
            }
            average = average / 200.0;
            for (int i = 0; i < 200; i++) {
                stdev += (average - data[i]) * (average - data[i]) / 199.0;
            }
            stdev = Math.sqrt(stdev);
            System.out.println(average);
            System.out.println(stdev * 2);

        }
    }
}
