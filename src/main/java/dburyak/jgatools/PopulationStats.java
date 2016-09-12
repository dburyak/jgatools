package dburyak.jgatools;


import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.InstanceBuilder;
import dburyak.jtools.Validators;


/**
 * Project : jgatools.<br/>
 * Population statistics.
 * <br/><b>Created on:</b> <i>12:12:36 AM Sep 4, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
public final class PopulationStats {

    /**
     * Population size.
     * <br/><b>Created on:</b> <i>2:09:39 AM Sep 7, 2016</i>
     */
    private final int size;

    /**
     * Number of elite chromosomes in population.
     * <br/><b>Created on:</b> <i>2:09:51 AM Sep 7, 2016</i>
     */
    private final int eliteCount;

    /**
     * Chromosomes minimal age in population.
     * <br/><b>Created on:</b> <i>2:10:04 AM Sep 7, 2016</i>
     */
    private final int minAge;

    /**
     * Chromosomes maximum age in population.
     * <br/><b>Created on:</b> <i>2:10:28 AM Sep 7, 2016</i>
     */
    private final int maxAge;

    /**
     * Average chromosomes age in population.
     * <br/><b>Created on:</b> <i>2:10:41 AM Sep 7, 2016</i>
     */
    private final double avgAge;

    /**
     * Chromosomes minimal generation number in population.
     * <br/><b>Created on:</b> <i>2:10:56 AM Sep 7, 2016</i>
     */
    private final int minGeneration;

    /**
     * Chromosomes maximum generation number in population.
     * <br/><b>Created on:</b> <i>2:11:14 AM Sep 7, 2016</i>
     */
    private final int maxGeneration;

    /**
     * Average chromosomes generation number in population.
     * <br/><b>Created on:</b> <i>2:11:40 AM Sep 7, 2016</i>
     */
    private final double avgGeneration;

    /**
     * Minimal chromosomes fitness in population.
     * <br/><b>Created on:</b> <i>2:11:56 AM Sep 7, 2016</i>
     */
    private final Fitness minFitness;

    /**
     * Maximum chromosomes fitness in population.
     * <br/><b>Created on:</b> <i>2:12:24 AM Sep 7, 2016</i>
     */
    private final Fitness maxFitness;

    /**
     * Average chromosomes fitness in population.
     * <br/><b>Created on:</b> <i>2:12:37 AM Sep 7, 2016</i>
     */
    private final Fitness avgFitness;


    /**
     * Constructor for class : [jgatools] dburyak.jgatools.PopulationStats.<br/>
     * <br/><b>PRE-conditions:</b> complex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:12:55 AM Sep 7, 2016</i>
     * 
     * @param size
     *            size of population
     * @param eliteCount
     *            number of elite chromosomes in population
     * @param minAge
     *            minimal chromosome age in population
     * @param maxAge
     *            maximum chromosome age in population
     * @param avgAge
     *            average chromosomes age in population
     * @param minGeneration
     *            minimal chromosome generation number in population
     * @param maxGeneration
     *            maximum chromosome generation number in population
     * @param avgGeneration
     *            average chromosomes generation number in population
     * @param minFitness
     *            minimum chromosome fitness in population
     * @param maxFitness
     *            maximum chromosome fitness in population
     * @param avgFitness
     *            average chromosomes fitness in population
     */
    private PopulationStats(
        final int size,
        final int eliteCount,
        final int minAge,
        final int maxAge,
        final double avgAge,
        final int minGeneration,
        final int maxGeneration,
        final double avgGeneration,
        final Fitness minFitness,
        final Fitness maxFitness,
        final Fitness avgFitness) {

        this.size = size;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.avgAge = avgAge;
        this.minGeneration = minGeneration;
        this.maxGeneration = maxGeneration;
        this.avgGeneration = avgGeneration;
        this.minFitness = minFitness;
        this.maxFitness = maxFitness;
        this.avgFitness = avgFitness;
        this.eliteCount = eliteCount;
    }


    /**
     * Get size of the population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:15:24 AM Sep 7, 2016</i>
     * 
     * @return size of the population
     */
    public final int size() {
        return size;
    }

    /**
     * Get number of elite chromosomes in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:15:49 AM Sep 7, 2016</i>
     * 
     * @return elite chromosomes number in population
     */
    public final int eliteCount() {
        return eliteCount;
    }

    /**
     * Get age of the chromosome that is the youngest in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:53:19 PM Sep 3, 2016</i>
     * 
     * @return age of the youngest chromosome
     */
    public final int minAge() {
        return minAge;
    }

    /**
     * Get age of the oldest chromosome in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:55:04 PM Sep 3, 2016</i>
     * 
     * @return age of the oldest chromosome
     */
    public final int maxAge() {
        return maxAge;
    }

    /**
     * Get average age of chromosomes in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:56:02 PM Sep 3, 2016</i>
     * 
     * @return average age of chromosomes in population
     */
    public final double avgAge() {
        return avgAge;
    }

    /**
     * Get generation number of chromosome with minimal generation number in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:56:53 PM Sep 3, 2016</i>
     * 
     * @return generation number of chromosome with minimal generation number
     */
    public final int minGeneration() {
        return minGeneration;
    }

    /**
     * Get generation number of chromosome with maximum generation number in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:58:16 PM Sep 3, 2016</i>
     * 
     * @return generation number of chromosome with maximum generation number
     */
    public final int maxGeneration() {
        return maxGeneration;
    }

    /**
     * Get average generation number of chromosomes in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:59:18 PM Sep 3, 2016</i>
     * 
     * @return average generation number of population
     */
    public final double avgGeneration() {
        return avgGeneration;
    }

    /**
     * Get minimal chromosome fitness in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:16:29 AM Sep 7, 2016</i>
     * 
     * @return minimal chromosome fitness in population
     */
    public final Fitness minFitness() {
        return minFitness;
    }

    /**
     * Get maximum chromosome fitness in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:18:46 AM Sep 7, 2016</i>
     * 
     * @return maximum chromosome fitness in population
     */
    public final Fitness maxFitness() {
        return maxFitness;
    }

    /**
     * Get average chromosomes fitness in population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:19:18 AM Sep 7, 2016</i>
     * 
     * @return average chromosomes fitness in population
     */
    public final Fitness avgFitness() {
        return avgFitness;
    }


    /**
     * Project : jgatools.<br/>
     * Builder of {@link PopulationStats} instances.
     * <br/><b>Created on:</b> <i>2:20:08 AM Sep 7, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    @NotThreadSafe
    public static final class PopulationStatsBuilder implements InstanceBuilder<PopulationStats> {

        /**
         * System logger.
         * <br/><b>Created on:</b> <i>12:20:24 AM Sep 4, 2016</i>
         */
        private static final Logger LOG = LogManager.getFormatterLogger(PopulationStatsBuilder.class);

        /**
         * Indicator for "not set" values.
         * <br/><b>Created on:</b> <i>3:02:55 AM Sep 7, 2016</i>
         */
        private static final int NOT_SET_INT = Integer.MIN_VALUE;

        /**
         * Idicator for "not set" values.
         * <br/><b>Created on:</b> <i>3:03:19 AM Sep 7, 2016</i>
         */
        private static final double NOT_SET_DBL = Double.NaN;

        /**
         * Population size for target stats.
         * <br/><b>Created on:</b> <i>12:20:29 AM Sep 4, 2016</i>
         */
        private int size = NOT_SET_INT;

        /**
         * Number of elite individuals for target stats.
         * <br/><b>Created on:</b> <i>1:37:44 AM Sep 7, 2016</i>
         */
        private int eliteCount = NOT_SET_INT;

        /**
         * Minimal chromosome age in population.
         * <br/><b>Created on:</b> <i>12:26:21 AM Sep 4, 2016</i>
         */
        private int minAge = NOT_SET_INT;

        /**
         * Maximum chromosome age in population.
         * <br/><b>Created on:</b> <i>12:26:52 AM Sep 4, 2016</i>
         */
        private int maxAge = NOT_SET_INT;

        /**
         * Average chromosome age in population.
         * <br/><b>Created on:</b> <i>12:27:13 AM Sep 4, 2016</i>
         */
        private double avgAge = NOT_SET_DBL;

        /**
         * Minimal chromosome generation number in population.
         * <br/><b>Created on:</b> <i>12:28:12 AM Sep 4, 2016</i>
         */
        private int minGeneration = NOT_SET_INT;

        /**
         * Maximum chromosome generation number in population.
         * <br/><b>Created on:</b> <i>12:28:43 AM Sep 4, 2016</i>
         */
        private int maxGeneration = NOT_SET_INT;

        /**
         * Average generation number in population.
         * <br/><b>Created on:</b> <i>12:29:04 AM Sep 4, 2016</i>
         */
        private double avgGeneration = NOT_SET_DBL;

        /**
         * Minimal chromosome fitness in population.
         * <br/><b>Created on:</b> <i>12:31:05 AM Sep 4, 2016</i>
         */
        private Fitness minFitness = null;

        /**
         * Maximum chromosome fitness in population.
         * <br/><b>Created on:</b> <i>12:31:37 AM Sep 4, 2016</i>
         */
        private Fitness maxFitness = null;

        /**
         * Average chromosome fitness in population.
         * <br/><b>Created on:</b> <i>12:32:01 AM Sep 4, 2016</i>
         */
        private Fitness avgFitness = null;


        /**
         * Evaluate all population stats from given chromosomes. Only eliteCount is not evaluated by this method.
         * <br><b>PRE-conditions:</b> non-null chromosomes
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>4:42:23 AM Sep 12, 2016</i>
         * 
         * @param <C>
         *            concrete chromosome implementation type
         * @param chromosomes
         *            chromosomes of population
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("resource")
        public final <C extends IChromosome> PopulationStatsBuilder eval(final Stream<C> chromosomes) {
            final Stream<C> parallel = chromosomes.parallel();

            final long sizeL = parallel.count();
            Validators.isTrue(Long.compare(sizeL, Integer.MAX_VALUE) <= 0); // sizeL <= MAX_INT
            size((int) sizeL);

            final IntStream ages = parallel.mapToInt(IChromosome::age);
            minAge(ages.min().orElse(0));
            maxAge(ages.max().orElse(0));
            avgAge(ages.average().orElse(0.0D));

            final IntStream generations = parallel.mapToInt(IChromosome::generation);
            minGeneration(generations.min().orElse(0));
            maxGeneration(generations.max().orElse(0));
            avgGeneration(generations.average().orElse(0.0D));

            final Stream<Fitness> fits = parallel.map(IChromosome::fitness);
            minFitness(fits.min(Fitness::compareTo).orElse(Fitness.min()));
            maxFitness(fits.max(Fitness::compareTo).orElse(Fitness.min()));
            avgFitness(new Fitness(fits.mapToDouble(Fitness::value).average().orElse(Fitness.minValue())));

            return this;
        }

        /**
         * Set size for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative size, eliteCount &lt= size
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:20:51 AM Sep 4, 2016</i>
         * 
         * @param size
         *            population size
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder size(final int size) {
            Validators.nonNegative(size);
            if (eliteCount != NOT_SET_INT) { // elite is set
                Validators.isTrue(eliteCount <= size);
            }
            this.size = size;
            return this;
        }

        /**
         * Set elite number for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative eliteCount, eliteCount &lt= size
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>2:22:42 AM Sep 7, 2016</i>
         * 
         * @param eliteCount
         *            number of elite chromosomes
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder eliteCount(final int eliteCount) {
            Validators.nonNegative(eliteCount);
            if (size != NOT_SET_INT) { // size is set
                Validators.isTrue(eliteCount <= size);
            }
            this.eliteCount = eliteCount;
            return this;
        }

        /**
         * Set minimal chromosome age for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative minAge, minAge &lt= avgAge &lt = maxAge
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>2:23:58 AM Sep 7, 2016</i>
         * 
         * @param minAge
         *            minimal chromosome age
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder minAge(final int minAge) {
            Validators.nonNegative(minAge);
            final boolean isAvgAgeSet = Double.compare(avgAge, NOT_SET_DBL) != 0;
            final boolean isMaxAgeSet = maxAge != NOT_SET_INT;
            if (isAvgAgeSet) {
                Validators.isTrue(Double.compare(minAge, avgAge) <= 0); // minAge <= avgAge
            }
            if (isMaxAgeSet) {
                Validators.isTrue(minAge <= maxAge);
            }
            this.minAge = minAge;
            return this;
        }

        /**
         * Set maximum chromosome age for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative maxAge, minAge &lt= avgAge &lt = maxAge
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>2:25:30 AM Sep 7, 2016</i>
         * 
         * @param maxAge
         *            maximum chromosome age
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder maxAge(final int maxAge) {
            Validators.nonNegative(maxAge);
            final boolean isAvgAgeSet = Double.compare(avgAge, NOT_SET_DBL) != 0;
            final boolean isMinAgeSet = minAge != NOT_SET_INT;
            if (isAvgAgeSet) {
                Validators.isTrue(Double.compare(avgAge, maxAge) <= 0);
            }
            if (isMinAgeSet) {
                Validators.isTrue(minAge <= maxAge);
            }
            this.maxAge = maxAge;
            return this;
        }

        /**
         * Set average chromosomes age for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative avgAge, minAge &lt= avgAge &lt = maxAge
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>2:26:31 AM Sep 7, 2016</i>
         * 
         * @param avgAge
         *            average chromosomes age
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder avgAge(final double avgAge) {
            Validators.nonNegative(avgAge);
            final boolean isMinAgeSet = minAge != NOT_SET_INT;
            final boolean isMaxAgeSet = maxAge != NOT_SET_INT;
            if (isMinAgeSet) {
                Validators.isTrue(Double.compare(minAge, avgAge) <= 0);
            }
            if (isMaxAgeSet) {
                Validators.isTrue(Double.compare(avgAge, maxAge) <= 0);
            }
            this.avgAge = avgAge;
            return this;
        }

        /**
         * Set minimum chromosome generation number for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative minGeneration, minGeneration &lt= avgGeneration &lt= maxGeneration
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>2:55:59 AM Sep 7, 2016</i>
         * 
         * @param minGeneration
         *            minimum chromosome generation number
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder minGeneration(final int minGeneration) {
            Validators.nonNegative(minGeneration);
            final boolean isAvgGenSet = Double.compare(avgGeneration, NOT_SET_DBL) != 0;
            final boolean isMaxGenSet = maxGeneration != NOT_SET_INT;
            if (isAvgGenSet) {
                Validators.isTrue(Double.compare(minGeneration, avgGeneration) <= 0);
            }
            if (isMaxGenSet) {
                Validators.isTrue(minGeneration <= maxGeneration);
            }
            this.minGeneration = minGeneration;
            return this;
        }

        /**
         * Set maximum chromosome generation number for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative maxGeneration, minGeneration &lt= avgGeneration &lt= maxGeneration
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>2:57:50 AM Sep 7, 2016</i>
         * 
         * @param maxGeneration
         *            maximum chromosome generation number
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder maxGeneration(final int maxGeneration) {
            Validators.nonNegative(maxGeneration);
            final boolean isMinGenSet = minGeneration != NOT_SET_INT;
            final boolean isAvgGenSet = Double.compare(avgGeneration, NOT_SET_DBL) != 0;
            if (isMinGenSet) {
                Validators.isTrue(minGeneration <= maxGeneration);
            }
            if (isAvgGenSet) {
                Validators.isTrue(Double.compare(avgGeneration, maxGeneration) <= 0);
            }
            this.maxGeneration = maxGeneration;
            return this;
        }

        /**
         * Set average chromosome generation number for target population stats.
         * <br/><b>PRE-conditions:</b> non-negative avgGeneration, minGeneration &lt= avgGeneration &lt= maxGeneration
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>6:47:22 AM Sep 7, 2016</i>
         * 
         * @param avgGeneration
         *            average chromosome generation number
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder avgGeneration(final double avgGeneration) {
            Validators.nonNegative(avgGeneration);
            final boolean isMinGenSet = minGeneration != NOT_SET_INT;
            final boolean isMaxGenSet = maxGeneration != NOT_SET_INT;
            if (isMinGenSet) {
                Validators.isTrue(Double.compare(minGeneration, avgGeneration) <= 0);
            }
            if (isMaxGenSet) {
                Validators.isTrue(Double.compare(avgGeneration, maxGeneration) <= 0);
            }
            this.avgGeneration = avgGeneration;
            return this;
        }

        /**
         * Set minimum fitness for target population stats.
         * <br/><b>PRE-conditions:</b> non-null minFitness, minFitness <= avgFitness <= maxFitness
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>6:51:02 AM Sep 7, 2016</i>
         * 
         * @param minFitness
         *            minimal fitness
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder minFitness(final Fitness minFitness) {
            Validators.nonNull(minFitness);
            final boolean isAvgFitSet = avgFitness != null;
            final boolean isMaxFitSet = maxFitness != null;
            if (isAvgFitSet) {
                Validators.isTrue(minFitness.compareTo(avgFitness) <= 0);
            }
            if (isMaxFitSet) {
                Validators.isTrue(minFitness.compareTo(maxFitness) <= 0);
            }
            this.minFitness = minFitness;
            return this;
        }

        /**
         * Set maximum fitness for target population stats.
         * <br/><b>PRE-conditions:</b> non-null maxFitness, minFitness <= avgFitness <= maxFitness
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>7:11:04 AM Sep 7, 2016</i>
         * 
         * @param maxFitness
         *            maximum fitness
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder maxFitness(final Fitness maxFitness) {
            Validators.nonNull(maxFitness);
            final boolean isMinFitSet = minFitness != null;
            final boolean isAvgFitSet = avgFitness != null;
            if (isMinFitSet) {
                Validators.isTrue(minFitness.compareTo(maxFitness) <= 0);
            }
            if (isAvgFitSet) {
                Validators.isTrue(avgFitness.compareTo(maxFitness) <= 0);
            }
            this.maxFitness = maxFitness;
            return this;
        }

        /**
         * Set average fitness for target population stats.
         * <br/><b>PRE-conditions:</b> non-null avgFitness, minFitness <= avgFitness <= maxFitness
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>7:13:36 AM Sep 7, 2016</i>
         * 
         * @param avgFitness
         *            average fitness for target population stats
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        public final PopulationStatsBuilder avgFitness(final Fitness avgFitness) {
            Validators.nonNull(avgFitness);
            final boolean isMinFitSet = minFitness != null;
            final boolean isMaxFitSet = maxFitness != null;
            if (isMinFitSet) {
                Validators.isTrue(minFitness.compareTo(avgFitness) <= 0);
            }
            if (isMaxFitSet) {
                Validators.isTrue(avgFitness.compareTo(maxFitness) <= 0);
            }
            this.avgFitness = avgFitness;
            return this;
        }

        /**
         * Build target {@link PopulationStats} instance.
         * <br/><b>PRE-conditions:</b> all parameters were set, all parameters are valid
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>7:21:02 AM Sep 7, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#build()
         * @return new {@link PopulationStats} instance
         * @throws IllegalStateException
         *             if builder was configured incorrectly
         */
        @Override
        public PopulationStats build() throws IllegalStateException {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            return new PopulationStats(size,
                eliteCount,
                minAge,
                maxAge,
                avgAge,
                minGeneration,
                maxGeneration,
                avgGeneration,
                minFitness,
                maxFitness,
                avgFitness);
        }

        /**
         * Check if builder is in valid state.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>7:23:02 AM Sep 7, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#isValid()
         * @return true if builder is in valid state, false otherwise
         */
        @SuppressWarnings({ "nls", "boxing" })
        @Override
        public boolean isValid() {
            if (!(minAge <= avgAge && avgAge <= maxAge)) {
                LOG.error("wrong age stats : minAge = [%d] ; avgAge = [%f] ; maxAge = [%d]", minAge, avgAge, maxAge);
                return false;
            }
            if (!(minGeneration <= avgGeneration && avgGeneration <= maxGeneration)) {
                LOG.error("wrong generation stats : minGeneration = [%d] ; avgGeneration = [%f] ; "
                    + "maxGeneration = [%d]", minGeneration, avgGeneration, maxGeneration);
                return false;
            }
            if (!(minFitness.compareTo(avgFitness) <= 0 && avgFitness.compareTo(maxFitness) <= 0)) {
                LOG.error("wrong fitness stats : minFitness = [%s] ; avgFitness = [%s] ; maxFitness = [%s]",
                    minFitness, avgFitness, maxFitness);
                return false;
            }
            if ((size == 0)
                && (minAge != 0
                    || maxAge != 0
                    || avgAge != 0
                    || minGeneration != 0
                    || maxGeneration != 0
                    || avgGeneration != 0
                    || minFitness.value() != 0
                    || maxFitness.value() != 0
                    || avgFitness.value() != 0)) {
                LOG.error("wrong stats for empty population");
            }
            if ((size == 1)
                && (minAge != maxAge
                    || minAge != avgAge
                    || minGeneration != maxGeneration
                    || minGeneration != avgGeneration
                    || !minFitness.equals(avgFitness)
                    || !minFitness.equals(maxFitness))) {
                LOG.error("wrong stats for single individual population");
            }
            return true;
        }

    }

}
