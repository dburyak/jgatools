package dburyak.jgatools;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.Duration;
import java.time.Instant;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.junit.Test;

import dburyak.jgatools.impl.BitSetChromosome;
import dburyak.jgatools.impl.BitSetChromosome.BitSetChromosomeBuilder;
import dburyak.jgatools.impl.Population;
import dburyak.jgatools.impl.Population.PopulationBuilder;
import dburyak.jgatools.impl.ReactiveGA.ReactiveGABuilder;
import dburyak.jtools.Validators;
import rx.Observable;


/**
 * Project : jgatools.<br>
 * Equal split set problem. Given set of integers, need to split it into two subsets in such way, that elements sum of
 * the first subset is equal to one in the second subset.
 * <br><b>Created on:</b> <i>5:13:31 AM Sep 12, 2016</i>
 * 
 * @author <i>Dmytro Buryak &lt;dmytro.buryak@gmail.com&gt;</i>
 * @version 0.1
 */
public class TestSplitSet {

    private static final Logger LOG = LogManager.getFormatterLogger(TestSplitSet.class);

    private static final Logger REPORT = LogManager.getFormatterLogger("REPORT");

    private static final boolean DO_TEST = true;

    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(240);


    private static final BitSet randomBitset(final int size) {
        final Random rnd = new Random(System.currentTimeMillis());
        final BitSet bitset = new BitSet(size);
        for (int i = 0 ; i < size / 2 ; i++) {
            bitset.set(i, rnd.nextBoolean());
        }
        return bitset;
    }


    private final int size = 1000;

    private final int populationSize = 150;

    private final int bufferSize = (int) (populationSize * 1.7D);

    private final double mutationRate = 0.3D;

    private final double crossoverRate = 0.2D;

    private final List<Integer> weights;

    private final long totalWeight;

    private final Instant startTime;

    private final IFitnessFunction<BitSet> fitnessFunc = new IFitnessFunction<BitSet>() {

        @Override
        public final Fitness calcFitness(final BitSet data) {
            final long weight1 = data.stream().parallel() // indices
                                                          // with
                                                          // "true"
                .map(weights::get) // index -> weight
                .mapToLong(i -> (long) i) // just convert int to long to avoid overflow
                .sum();
            final long weight2 = totalWeight - weight1;
            LOG.debug("weights : w1 = [%d] ; w2 = [%d]", weight1, weight2);
            final double diffRatio = Math.abs((double) (weight1 - weight2) / (double) totalWeight);
            final double fitValue = 1.0D - diffRatio;
            return new Fitness(fitValue);
        }
    };

    private final ITerminationCondition<BitSetChromosome, Population<BitSetChromosome>> termCondition =
        (final Population<BitSetChromosome> population, final int iteration, final Duration runtime) -> {
            if (runtime.compareTo(MAX_TIMEOUT) > 0) {
                LOG.debug("termination by timeout : runtime = [%s]", runtime);
                return true;
            }
            final Fitness fittestFit = population.fittest().fitness();
            if (fittestFit.equals(Fitness.max())) {
                LOG.debug("termination by result : fitness = [%f]", fittestFit.value());
                return true;
            }
            return false;
        };

    /**
     * Bit-flip mutation.
     * <br><b>Created on:</b> <i>5:05:25 PM Sep 12, 2016</i>
     */
    private final IMutationStrategy<BitSetChromosome> mutationFunc = new IMutationStrategy<BitSetChromosome>() {

        private final Random rnd = new Random(System.currentTimeMillis());


        @Override
        public final BitSetChromosome mutate(final BitSetChromosome chromosome) {
            final BitSet mutated = chromosome.bitset();
            mutated.flip(rnd.nextInt(size));
            return (new BitSetChromosomeBuilder()).fitnessFunc(fitnessFunc).age(0).generation(chromosome.generation()
                + 1).data(mutated).build();
        }
    };

    private final ICrossoverStrategy<BitSetChromosome> crossoverFunc = new ICrossoverStrategy<BitSetChromosome>() {

        private final Random rndPoint = new Random(System.currentTimeMillis());
        private final Random rndParent = new Random(System.nanoTime());
        private final double mean = 0.5D;
        private final double factor = 0.18D;
        private final double min = 0.0D;
        private final double max = 1.0D;


        private final int generateCrossoverPoint() {
            return DoubleStream.generate(rndPoint::nextGaussian).map(d -> {
                return (d * factor) + mean;
            }).filter(d -> Double.compare(min, d) <= 0 && Double.compare(d, max) <= 0).mapToInt(posFct -> (int) (posFct
                * size)).findFirst().getAsInt();
        }

        private final BitSet copySubset(final BitSet source, final BitSet target, final int start, final int end) {
            Validators.isTrue(start <= end);
            target.clear(start, end);
            source.stream().filter(i -> (i >= start) && (i < end)).forEach(target::set);
            return target;
        }

        @Override
        public final Observable<BitSetChromosome> crossover(final Observable<BitSetChromosome> parents) {
            return parents.toList().doOnNext((list) -> Validators.isTrue(list.size() == 2)) // check
                                                                                            // number
                                                                                            // of
                                                                                            // parents
                .map((list) -> {
                    BitSetChromosome parent1 = list.get(0);
                    BitSetChromosome parent2 = list.get(1);
                    // randomly choose which parent should give first part of chromosome, and which the second one
                    if (rndParent.nextBoolean()) {
                        BitSetChromosome tmp = parent1;
                        parent1 = parent2;
                        parent2 = tmp;
                    }

                    final BitSet offspringData = new BitSet(size);
                    final int splitPoint = generateCrossoverPoint();
                    copySubset(parent1.bitset(), offspringData, 0, splitPoint);
                    copySubset(parent2.bitset(), offspringData, splitPoint, size);

                    return (new BitSetChromosomeBuilder()).data(offspringData).age(0).fitnessFunc(fitnessFunc)
                        .generation(Math.max(parent1.generation(), parent2.generation())).build(); // do
                                                                                                   // not
                                                                                                   // keep
                                                                                                   // track
                                                                                                   // of
                                                                                                   // parents
                });
        }
    };

    private final ISelectionStrategy<BitSetChromosome> selectionFunc = (buffer) -> {
        return buffer.sorted((c1, c2) -> -c1.fitness().compareTo(c2.fitness()))
            .take(populationSize);
    };

    private final IMatesSelector<BitSetChromosome> matesSelectFunc = new IMatesSelector<BitSetChromosome>() {

        private final double similarityFactor = 0.75D;


        private final double similarity(final BitSetChromosome c1, final BitSetChromosome c2) {
            final BitSet xored = c1.bitset();
            xored.xor(c2.bitset());
            return (size - xored.cardinality()) / (double) size;
        }

        @Override
        public final Observable<BitSetChromosome> select(
            final BitSetChromosome parent1,
            final Observable<BitSetChromosome> candidates) {

            return candidates.sorted((c1, c2) -> -Double.compare(Math.abs(similarity(parent1, c1) - similarityFactor),
                Math.abs(similarity(parent1, c2) - similarityFactor))).take(1);
        }
    };

    private final TimeSeries seriesMinAge = new TimeSeries("minAge");
    private final TimeSeries seriesAvgAge = new TimeSeries("avgAge");
    private final TimeSeries seriesMaxAge = new TimeSeries("maxAge");

    private final TimeSeries seriesMinGen = new TimeSeries("minGen");
    private final TimeSeries seriesAvgGen = new TimeSeries("avgGen");
    private final TimeSeries seriesMaxGen = new TimeSeries("maxGen");

    private final TimeSeries seriesMinFit = new TimeSeries("minFit");
    private final TimeSeries seriesAvgFit = new TimeSeries("avgFit");
    private final TimeSeries seriesMaxFit = new TimeSeries("maxFit");

    private final TimeSeries seriesMem = new TimeSeries("MEM");


    @SuppressWarnings("boxing")
    private final List<Integer> generateWeights() {
        final Random rnd = new Random(System.currentTimeMillis());
        return Stream.generate(() -> rnd.nextInt(29) + 1)
            .limit(size)
            .collect(Collectors.toList());
    }

    public TestSplitSet() {
        weights = generateWeights();
        totalWeight = weights.stream().parallel()
            .collect(Collectors.summarizingInt(Integer::intValue))
            .getSum();

        startTime = Instant.now();
    }

    private final Observable<BitSetChromosome> appearSource() {
        return Observable.<BitSetChromosome> create(subscriber -> {
            while (!subscriber.isUnsubscribed()) {
                final BitSetChromosome randomChromosome = (new BitSetChromosomeBuilder())
                    .age(0)
                    .generation(0)
                    .data(randomBitset(size))
                    .fitnessFunc(fitnessFunc)
                    .build();
                subscriber.onNext(randomChromosome);
            }
        });
    }

    private final JFreeChart buildMemChart() {
        final TimeSeriesCollection memSeriesColl = new TimeSeriesCollection(seriesMem);
        return ChartFactory.createTimeSeriesChart("MEM", "time", "mem MB", memSeriesColl, true, true, false);
    }

    private final JFreeChart buildAgeChart() {
        final TimeSeriesCollection ageSeriesColl = new TimeSeriesCollection();
        ageSeriesColl.addSeries(seriesMinAge);
        ageSeriesColl.addSeries(seriesAvgAge);
        ageSeriesColl.addSeries(seriesMaxAge);
        return ChartFactory.createTimeSeriesChart("AGE", "time", "age", ageSeriesColl, true, true, false);
    }

    private final JFreeChart buildGenChart() {
        final TimeSeriesCollection genSeriesColl = new TimeSeriesCollection();
        genSeriesColl.addSeries(seriesMinGen);
        genSeriesColl.addSeries(seriesAvgGen);
        genSeriesColl.addSeries(seriesMaxGen);
        return ChartFactory.createTimeSeriesChart("GEN", "time", "generation", genSeriesColl, true, true, false);
    }

    private final JFreeChart buildFitChart() {
        final TimeSeriesCollection fitSeriesColl = new TimeSeriesCollection();
        fitSeriesColl.addSeries(seriesMinFit);
        fitSeriesColl.addSeries(seriesAvgFit);
        fitSeriesColl.addSeries(seriesMaxFit);
        return ChartFactory.createTimeSeriesChart("FITNESS", "time", "fitness", fitSeriesColl, true, true, false);
    }

    private final void buildUI() {
        final ChartPanel panelFit = new ChartPanel(buildFitChart());
        final ChartPanel panelAge = new ChartPanel(buildAgeChart());
        final ChartPanel panelGen = new ChartPanel(buildGenChart());
        final ChartPanel panelMem = new ChartPanel(buildMemChart());
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame("stats");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final JPanel mainPanel = new JPanel(new GridBagLayout());

            final GridBagConstraints cFit = new GridBagConstraints();
            cFit.gridx = 0;
            cFit.gridy = 0;
            mainPanel.add(panelFit, cFit);

            final GridBagConstraints cAge = new GridBagConstraints();
            cAge.gridx = 1;
            cAge.gridy = 0;
            mainPanel.add(panelAge, cAge);

            final GridBagConstraints cGen = new GridBagConstraints();
            cGen.gridx = 0;
            cGen.gridy = 1;
            mainPanel.add(panelGen, cGen);

            final GridBagConstraints cMem = new GridBagConstraints();
            cMem.gridx = 1;
            cMem.gridy = 1;
            mainPanel.add(panelMem, cMem);

            frame.getContentPane().add(mainPanel);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.pack();
            frame.setVisible(true);
        });
    }

    @Test
    public void test() {
        if (!DO_TEST) {
            return;
        }

        buildUI();

        final Random mutSelectionRnd = new Random();
        final Random p1SelectionRnd = new Random();
        final Observable<BitSetChromosome> appearSource = appearSource();

        final IGeneticAlgorithm<BitSetChromosome, Population<BitSetChromosome>> ga =
            (new ReactiveGABuilder<BitSetChromosome, Population<BitSetChromosome>>())
                .termCondition(termCondition)
                .appearSource(appearSource)
                .populationBuilder(() -> {
                    return (new PopulationBuilder<BitSetChromosome>())
                        .size(populationSize)
                        .eliteCount(0)
                        .appearSource(appearSource);
                })
                .chromosomeBuilder(() -> (new BitSetChromosomeBuilder()).fitnessFunc(fitnessFunc))
                .mutationSelector((c) -> Double.compare(mutSelectionRnd.nextDouble(), mutationRate) < 0)
                .mutationFunc(mutationFunc)
                .crossoverFunc(crossoverFunc)
                .selectionFunc(selectionFunc)
                .parent1Selector((c) -> Double.compare(p1SelectionRnd.nextDouble(), crossoverRate) < 0)
                .matesSelector(matesSelectFunc)
                .bufferSize(100)
                .build();

        ga.stats().subscribe((stats) -> {
            REPORT.info("stats : fitMax = [%s] ; fitAvg = [%s]", stats.maxFitness(), stats.avgFitness());
            final Millisecond timeMS = new Millisecond();
            seriesMinAge.add(timeMS, stats.minAge());
            seriesAvgAge.add(timeMS, stats.avgAge());
            seriesMaxAge.add(timeMS, stats.maxAge());

            seriesMinGen.add(timeMS, stats.minGeneration());
            seriesAvgGen.add(timeMS, stats.avgGeneration());
            seriesMaxGen.add(timeMS, stats.maxGeneration());

            seriesMinFit.add(timeMS, stats.minFitness().value());
            seriesAvgFit.add(timeMS, stats.avgFitness().value());
            seriesMaxFit.add(timeMS, stats.maxFitness().value());

            final Runtime rt = Runtime.getRuntime();
            final double usedMemMB = (double) (rt.totalMemory() - rt.freeMemory()) / (double) (1024 * 1024);
            seriesMem.add(timeMS, usedMemMB);
        });

        ga.start();
        ga.result().subscribe((c) -> {
            LOG.info("result : c = [%s]", c);
            ga.stop();
        });

        try {
            Thread.sleep((long) (MAX_TIMEOUT.toMillis() * 1.1D));
        } catch (InterruptedException e) {
            // TODO [8:08:47 AM Sep 14, 2016] Implement catch block [InterruptedException].
            LOG.error("exception caught:", e);
        }
    }

}
