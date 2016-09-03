package dburyak.jgatools;


import javax.annotation.concurrent.Immutable;

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

    private final int size;
    private final int minAge;
    private final int maxAge;
    private final double avgAge;
    private final int minGeneration;
    private final int maxGeneration;
    private final double avgGeneration;
    private final Fitness minFitness;
    private final Fitness maxFitness;
    private final Fitness avgFitness;


    private PopulationStats(
        final int size,
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
    }


    public final int size() {
        return size;
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
    public int minAge() {
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
    public int maxAge() {
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
    public double avgAge() {
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
    public int minGeneration() {
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
    public int maxGeneration() {
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
    public double avgGeneration() {
        return avgGeneration;
    }

    public Fitness minFitness() {
        return minFitness;
    }

    public Fitness maxFitness() {
        return maxFitness;
    }

    public Fitness avgFitness() {
        return avgFitness;
    }


    public static final class PopulationStatsBuilder implements InstanceBuilder<PopulationStats> {

        /**
         * System logger.
         * <br/><b>Created on:</b> <i>12:20:24 AM Sep 4, 2016</i>
         */
        private static final Logger LOG = LogManager.getFormatterLogger(PopulationStatsBuilder.class);

        /**
         * Population size for target stats.
         * <br/><b>Created on:</b> <i>12:20:29 AM Sep 4, 2016</i>
         */
        private int size;

        /**
         * Minimal chromosome age in population.
         * <br/><b>Created on:</b> <i>12:26:21 AM Sep 4, 2016</i>
         */
        private int minAge;

        /**
         * Maximum chromosome age in population.
         * <br/><b>Created on:</b> <i>12:26:52 AM Sep 4, 2016</i>
         */
        private int maxAge;

        /**
         * Average chromosome age in population.
         * <br/><b>Created on:</b> <i>12:27:13 AM Sep 4, 2016</i>
         */
        private double avgAge;

        /**
         * Minimal chromosome generation number in population.
         * <br/><b>Created on:</b> <i>12:28:12 AM Sep 4, 2016</i>
         */
        private int minGeneration;

        /**
         * Maximum chromosome generation number in population.
         * <br/><b>Created on:</b> <i>12:28:43 AM Sep 4, 2016</i>
         */
        private int maxGeneration;

        /**
         * Average generation number in population.
         * <br/><b>Created on:</b> <i>12:29:04 AM Sep 4, 2016</i>
         */
        private double avgGeneration;

        /**
         * Minimal chromosome fitness in population.
         * <br/><b>Created on:</b> <i>12:31:05 AM Sep 4, 2016</i>
         */
        private Fitness minFitness;

        /**
         * Maximum chromosome fitness in population.
         * <br/><b>Created on:</b> <i>12:31:37 AM Sep 4, 2016</i>
         */
        private Fitness maxFitness;

        /**
         * Average chromosome fitness in population.
         * <br/><b>Created on:</b> <i>12:32:01 AM Sep 4, 2016</i>
         */
        private Fitness avgFitness;


        /**
         * Set size for target population stats.
         * <br/><b>PRE-conditions:</b> size &gt= 0
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:20:51 AM Sep 4, 2016</i>
         * 
         * @param size
         *            population size
         * @return this builder (for call chaining)
         */
        public final PopulationStatsBuilder size(@SuppressWarnings("hiding") final int size) {
            Validators.nonNegative(size);
            this.size = size;
            return this;
        }

        public final PopulationStatsBuilder minAge(final int minAge) {
            Validators.nonNegative(minAge);
            this.minAge = minAge;
            return this;
        }

        public final PopulationStatsBuilder maxAge(final int maxAge) {
            Validators.nonNegative(maxAge);
            this.maxAge = maxAge;
            return this;
        }

        public final PopulationStatsBuilder avgAge(final double avgAge) {
            Validators.nonNegative(avgAge);
            this.avgAge = avgAge;
            return this;
        }

        public final PopulationStatsBuilder minGeneration(final int minGeneration) {
            Validators.nonNegative(minGeneration);
            this.minGeneration = minGeneration;
            return this;
        }

        public final PopulationStatsBuilder maxGeneration(final int maxGeneration) {
            Validators.nonNegative(maxGeneration);
            this.maxGeneration = maxGeneration;
            return this;
        }

        public final PopulationStatsBuilder avgGeneration(final double avgGeneration) {
            Validators.nonNegative(avgGeneration);
            this.avgGeneration = avgGeneration;
            return this;
        }

        public final PopulationStatsBuilder minFitness(final Fitness minFitness) {
            Validators.nonNull(minFitness);
            this.minFitness = minFitness;
            return this;
        }

        public final PopulationStatsBuilder maxFitness(final Fitness maxFitness) {
            Validators.nonNull(maxFitness);
            this.maxFitness = maxFitness;
            return this;
        }

        public final PopulationStatsBuilder avgFitness(final Fitness avgFitness) {
            Validators.nonNull(avgFitness);
            this.avgFitness = avgFitness;
            return this;
        }

        @Override
        public PopulationStats build() throws IllegalStateException {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            return new PopulationStats(size,
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
