package dburyak.jgatools.impl;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import dburyak.jgatools.IChromosome;
import dburyak.jgatools.IPopulation;
import dburyak.jgatools.PopulationStats;
import dburyak.jtools.Validators;
import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Default implementation.<br/>
 * TODO : if no parts with possible different approaches would be found here, interface can be replaced by this class
 * <br/><b>Created on:</b> <i>12:18:51 AM Sep 7, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete chromosome implementation type this population works with
 */
public final class Population<C extends IChromosome> implements IPopulation<C> {

    /**
     * Properties of this population. Can contain some special configuration that is not part of the API.
     * <br/><b>Created on:</b> <i>12:24:38 AM Sep 7, 2016</i>
     */
    private final Properties props;

    /**
     * All chromosomes in this population in sorted order. The fittest ones come first.
     * <br/><b>Created on:</b> <i>12:30:31 AM Sep 7, 2016</i>
     */
    private final List<C> chromosomes;

    /**
     * Number of elite chromosomes in this population.
     * <br/><b>Created on:</b> <i>1:26:51 AM Sep 7, 2016</i>
     */
    private final int eliteCount;


    /**
     * Constructor for class : [jgatools] dburyak.jgatools.impl.Population.<br/>
     * <br/><b>PRE-conditions:</b> non-empty key
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:25:09 AM Sep 7, 2016</i>
     * 
     * @param props
     *            properties for this population
     * @param chromosomes
     *            all chromosomes for this population (sorted)
     * @param eliteCount
     *            number of elite chromosomes in this population
     */
    private Population(final Properties props, final List<C> chromosomes, final int eliteCount) {
        this.props = props;
        this.chromosomes = Collections.unmodifiableList(chromosomes);
        this.eliteCount = eliteCount;
        // TODO : add stats calculation
    }

    /**
     * Get property of this population for given key.
     * <br/><b>PRE-conditions:</b> non-empty key
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:25:37 AM Sep 7, 2016</i>
     * 
     * @see dburyak.jtools.IConfigured#property(java.lang.String)
     * @param key
     *            property key
     * @return value associated with given key, or null if there's no such key registered
     */
    @Override
    public final String property(final String key) {
        Validators.nonEmpty(key);
        return props.getProperty(key);
    }

    /**
     * Get all chromosomes from this population. This implementation preserves sorting, so all the chromosomes are
     * sorted by fitness. Thus, first come the most fittest chromosomes, and the least fittest ones come last.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:26:42 AM Sep 7, 2016</i>
     * 
     * @see dburyak.jgatools.IPopulation#chromosomes()
     * @return observable that emits all chromosomes of this population sorted by fitness
     */
    @Override
    public final Observable<C> chromosomes() {
        return Observable.from(chromosomes);
    }

    /**
     * Get size of this population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>1:24:11 AM Sep 7, 2016</i>
     * 
     * @see dburyak.jgatools.IPopulation#size()
     * @return size of this population
     */
    @Override
    public final int size() {
        return chromosomes.size();
    }

    /**
     * Number of elite individuals in this population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:26:11 AM Sep 7, 2016</i>
     * 
     * @see dburyak.jgatools.IPopulation#eliteCount()
     * @return number of elite chromosomes in this population
     */
    @Override
    public final int eliteCount() {
        return eliteCount;
    }

    /**
     * Get fittest chromosome from this population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:31:40 AM Sep 7, 2016</i>
     * 
     * @see dburyak.jgatools.IPopulation#fittest()
     * @return the fittest chromosome from this population or null if this is empty population ({@code size} == 0)
     */
    @Override
    public final C fittest() {
        return chromosomes.get(0);
    }

    /**
     * Get statistics of this population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:02 AM Sep 7, 2016</i>
     * 
     * @see dburyak.jgatools.IPopulation#stats()
     * @return statistics of this population
     */
    @Override
    public final PopulationStats stats() {
        // TODO [12:19:18 AM Sep 7, 2016] Implement method.
        return null;
    }


    /**
     * Project : jgatools.<br/>
     * Builder for {@link Population} instances.
     * <br/><b>Created on:</b> <i>6:13:38 PM Sep 7, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     * @param <C>
     *            concrete chromosome implementation type this builder works with
     */
    @NotThreadSafe
    public static final class PopulationBuilder<C extends IChromosome> implements IPopulationBuilder<C, Population<C>> {

        /**
         * Magic number that indicates that value was not set yet.
         * <br/><b>Created on:</b> <i>12:38:12 AM Sep 10, 2016</i>
         */
        private static final int NOT_SET_INT = -1;

        /**
         * Properties of target population.
         * <br/><b>Created on:</b> <i>6:15:51 PM Sep 7, 2016</i>
         */
        private Properties props = new Properties();

        /**
         * Chromosomes source for target population.
         * <br/><b>Created on:</b> <i>11:45:12 PM Sep 9, 2016</i>
         */
        private Observable<C> chromosomes = null;

        /**
         * Source of new generated "out of nowhere" chromosomes.
         * <br/><b>Created on:</b> <i>12:35:58 AM Sep 10, 2016</i>
         */
        private Observable<C> appearSource = null;

        /**
         * Extra chromosomes to be added to target population besides those from "chromosomes" observable.
         * <br/><b>Created on:</b> <i>11:50:03 PM Sep 9, 2016</i>
         */
        private final Set<C> added = new HashSet<>();

        /**
         * Chromosomes to be denied (filtered out) in target population.
         * <br/><b>Created on:</b> <i>11:50:50 PM Sep 9, 2016</i>
         */
        private final Set<C> denied = new HashSet<>();

        /**
         * Whether duplicate chromosomes should be removed from target population.
         * <br/><b>Created on:</b> <i>11:52:15 PM Sep 9, 2016</i>
         */
        private boolean removeDuplicates = true;

        /**
         * Size of the target population.
         * <br/><b>Created on:</b> <i>12:38:49 AM Sep 10, 2016</i>
         */
        private int size = NOT_SET_INT;

        /**
         * Number of elite chromosomes to be preserved in target population.
         * <br/><b>Created on:</b> <i>12:42:10 AM Sep 10, 2016</i>
         */
        private int eliteCount = NOT_SET_INT;


        /**
         * Build target population.
         * <br/><b>PRE-conditions:</b> this builder is in valid state
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>6:16:03 PM Sep 7, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#build()
         * @return new population instance
         * @throws IllegalStateException
         *             if builder was not configured properly
         */
        @Override
        public final Population<C> build() throws IllegalStateException {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            @SuppressWarnings("boxing") Observable<C> allChromosomes = Observable.from(added)
                .concatWith(chromosomes)
                .concatWith(appearSource)
                .filter(c -> !denied.contains(c));
            if (removeDuplicates) {
                allChromosomes = allChromosomes.distinct();
            }
            allChromosomes = allChromosomes.take(size);
            // subscription happens here, target population is the subscriber for all chromosomes (evaluate stats)
            final List<C> chromosomesList = allChromosomes.toSortedList().toBlocking().single();
            Validators.isTrue(chromosomesList.size() == size);
            return new Population<>(props, chromosomesList, eliteCount);
        }

        /**
         * Check if this builder is in valid state and target {@link Population} instance can be built.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NON
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>6:18:09 PM Sep 7, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#isValid()
         * @return true if this builder is in valid state, false otherwise
         */
        @Override
        public final boolean isValid() {
            if (chromosomes == null) {
                return false;
            }
            if (appearSource == null) {
                return false;
            }
            if (size == NOT_SET_INT) {
                return false;
            }
            if (eliteCount == NOT_SET_INT) {
                return false;
            }
            return true;
        }

        /**
         * Sep property for target population.
         * <br/><b>PRE-conditions:</b> non-empty key, non-null value
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>6:29:54 PM Sep 7, 2016</i>
         * 
         * @see dburyak.jtools.IConfigurable#property(java.lang.String, java.lang.String)
         * @param key
         *            property key
         * @param value
         *            property value
         * @return previous property for given key, or null if there was no value previously assigned for given key
         */
        @Override
        public final String property(final String key, final String value) {
            Validators.nonEmpty(key);
            Validators.nonNull(value);
            // bad API design
            return (String) props.setProperty(key, value);
        }

        /**
         * Remove property of target population.
         * <br/><b>PRE-conditions:</b> non-empty key
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>1:57:47 AM Sep 9, 2016</i>
         * 
         * @see dburyak.jtools.IConfigurable#removeProperty(java.lang.String)
         * @param key
         *            property key
         * @return previous value, or null if there was no property registered for given key
         */
        @Override
        public final String removeProperty(final String key) {
            Validators.nonEmpty(key);
            // bad API design
            return (String) props.remove(key);
        }

        /**
         * Get property for given key.
         * <br/><b>PRE-conditions:</b> non-empty key
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:00:38 AM Sep 9, 2016</i>
         * 
         * @see dburyak.jtools.IConfigured#property(java.lang.String)
         * @param key
         *            property key
         * @return property value for given key, or null if there's no value registered for given key
         */
        @Override
        public final String property(final String key) {
            Validators.nonEmpty(key);
            return props.getProperty(key);
        }

        /**
         * Copy all data from given population to this builder to produce exact copy. Shallow copy is performed.
         * <br/><b>PRE-conditions:</b> non-null population
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>3:50:48 AM Sep 9, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#from(dburyak.jgatools.IPopulation)
         * @param population
         *            original population to make copy from
         * @return this builder (for call chaining)
         */
        @Override
        public final IPopulationBuilder<C, Population<C>> from(final Population<C> population) {
            Validators.nonNull(population);
            props = (Properties) population.props.clone(); // hashtable shallow copy
            chromosomes(population.chromosomes());
            added.clear();
            denied.clear();
            size = population.size();
            eliteCount = population.eliteCount();
            return this;
        }

        /**
         * Set chromosomes source for target population.
         * <br/><b>PRE-conditions:</b> non-null chromosomes
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>4:00:10 AM Sep 9, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#chromosomes(rx.Observable)
         * @param chromosomes
         *            source of chromosomes for target population
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IPopulationBuilder<C, Population<C>> chromosomes(final Observable<C> chromosomes) {
            Validators.nonNull(chromosomes);
            this.chromosomes = chromosomes;
            return this;
        }

        /**
         * Add extra chromosome to the target population. Chromosomes passed through this method will be added first to
         * the target population.
         * <br/><b>PRE-conditions:</b> non-null chromosome
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:08:51 AM Sep 10, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#addChromosome(dburyak.jgatools.IChromosome)
         * @param chromosome
         *            chromosome to be added to target population
         * @return this builder (for call chaining)
         */
        @Override
        public final IPopulationBuilder<C, Population<C>> addChromosome(final C chromosome) {
            Validators.nonNull(chromosome);
            added.add(chromosome);
            return this;
        }

        /**
         * Deny specific chromosome in target population. Specified chromosome will be unable to be passed to target
         * population.
         * <br/><b>PRE-conditions:</b> non-null chromosome
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:31:24 AM Sep 10, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#denyChromosome(dburyak.jgatools.IChromosome)
         * @param chromosome
         *            chromosome to be denied in target population
         * @return this builder (for call chaining)
         */
        @Override
        public final IPopulationBuilder<C, Population<C>> denyChromosome(final C chromosome) {
            Validators.nonNull(chromosome);
            denied.add(chromosome);
            return this;
        }

        /**
         * Configure whether duplicates should be removed from target population.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>11:58:01 PM Sep 9, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#removeDuplicates(boolean)
         * @param removeDuplicates
         *            indicates whether duplicates should be removed from target population
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IPopulationBuilder<C, Population<C>> removeDuplicates(final boolean removeDuplicates) {
            this.removeDuplicates = removeDuplicates;
            return null;
        }

        /**
         * Set appear source observable to be used by target population.
         * <br/><b>PRE-conditions:</b> non-null appearSource
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:04:51 AM Sep 10, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#appearSource(rx.Observable)
         * @param appearSource
         *            observable that emits new individuals
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IPopulationBuilder<C, Population<C>> appearSource(final Observable<C> appearSource) {
            Validators.nonNull(appearSource);
            this.appearSource = appearSource;
            return this;
        }

        /**
         * Set size of the target population.
         * <br/><b>PRE-conditions:</b> non-negative size, eliteCount &lt;= size
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:39:26 AM Sep 10, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#size(int)
         * @param size
         *            size of the target population
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IPopulationBuilder<C, Population<C>> size(final int size) {
            Validators.nonNegative(size);
            if (eliteCount != NOT_SET_INT) {
                Validators.isTrue(eliteCount <= size);
            }
            this.size = size;
            return this;
        }

        /**
         * Set number of elite individuals to be preserved by the target population.
         * <br/><b>PRE-conditions:</b> non-negative eliteCount, eliteCount &lt;= size
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> this builder state is changed
         * <br/><b>Created on:</b> <i>12:44:07 AM Sep 10, 2016</i>
         * 
         * @see dburyak.jgatools.IPopulation.IPopulationBuilder#eliteCount(int)
         * @param eliteCount
         *            number of elite individuals to be preserved by the target population
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IPopulationBuilder<C, Population<C>> eliteCount(final int eliteCount) {
            Validators.nonNegative(eliteCount);
            if (size != NOT_SET_INT) {
                Validators.isTrue(eliteCount <= size);
            }
            this.eliteCount = eliteCount;
            return this;
        }

    }

}
