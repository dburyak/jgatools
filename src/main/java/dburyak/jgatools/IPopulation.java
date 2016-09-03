package dburyak.jgatools;


import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import dburyak.jtools.IConfigurable;
import dburyak.jtools.IConfigured;
import dburyak.jtools.InstanceBuilder;


/**
 * Project : jgatools.<br/>
 * Population that holds chromosomes and tracks their statistics.
 * <br/><b>Created on:</b> <i>4:42:07 PM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
public interface IPopulation extends IConfigured {

    /**
     * Get all chromosomes of this population. Sorting is not specified (implementation dependent).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:47:16 PM Sep 3, 2016</i>
     * 
     * @return all chromosomes of this population
     */
    public Stream<IChromosome> chromosomes();

    /**
     * Get size of this population. It is always equal to number of chromosomes in stream returned by
     * {@link #chromosomes()} method.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:52:12 PM Sep 3, 2016</i>
     * 
     * @return size of this population
     */
    public int size();

    /**
     * Get number of elite individuals preserved in this population. If result is zero, then no elite individuals are
     * preserved.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>12:00:14 AM Sep 4, 2016</i>
     * 
     * @return number of elite individuals in this population
     */
    public int eliteCount();

    /**
     * Get fittest chromosome of this population. May be null if this population is empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>12:13:57 AM Sep 4, 2016</i>
     * 
     * @return fittest chromosome of this population
     */
    public IChromosome getFittest();

    /**
     * Get statistic information of this population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:53:19 AM Sep 4, 2016</i>
     * 
     * @return stats of this population
     */
    public PopulationStats getStats();


    /**
     * Project : jgatools.<br/>
     * Builder for {@link IPopulation} instances. Implementations can support more specific configuration options
     * through {@link IConfigurable} interface methods.
     * <br/><b>Created on:</b> <i>5:13:28 PM Sep 3, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     * @param <P>
     *            type of {@link IPopulation} implementation this builder is dedicated to
     */
    @NotThreadSafe
    public interface IPopulationBuilder<P extends IPopulation> extends InstanceBuilder<IPopulation>, IConfigurable {

        /**
         * Copy given population into target population. Doesn't copy derivative attributes, they are calculated when
         * target {@link IPopulation} instance is built in {@link #build()} method.
         * <br/><b>PRE-conditions:</b> non-null population
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>5:16:32 PM Sep 3, 2016</i>
         * 
         * @param population
         *            source population to copy chromosomes from
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> from(final P population);

        /**
         * Add chromosome to target population.
         * <br/><b>PRE-conditions:</b> non-null chromosome
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:31:24 PM Sep 3, 2016</i>
         * 
         * @param chromosome
         *            chromosome to be added to the target population
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> addChromosome(final IChromosome chromosome);

        /**
         * Remove chromosome from target population.
         * <br/><b>PRE-conditions:</b> non-null chromosome
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:32:29 PM Sep 3, 2016</i>
         * 
         * @param chromosome
         *            chromosome to be removed from target population
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> removeChromosome(final IChromosome chromosome);

        /**
         * Configure this builder to remove duplicates when building target population. This method only configures this
         * builder, but doesn't invoke duplicates removal right away. Frankly, implementations are even allowed to
         * totally ignore this call if it is compliant with specific GA strategy of the implementation.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:33:23 PM Sep 3, 2016</i>
         * 
         * @param removeDuplicates
         *            indicates if duplicate chromosomes should be removed from target population
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> removeDuplicates(final boolean removeDuplicates);

        /**
         * Set appearance function that is responsible for generating new individuals when population is less than its
         * size.
         * <br/><b>PRE-conditions:</b> non-null appearanceFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:44:47 PM Sep 3, 2016</i>
         * 
         * @param appearanceFunc
         *            function for generating new individuals
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> appearanceFunc(final IAppearanceStrategy appearanceFunc);

        /**
         * Set selection function that is responsible for cutting down population when number of chromosomes exceeds
         * population size.
         * <br/><b>PRE-conditions:</b> non-null selectionFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>11:30:17 PM Sep 3, 2016</i>
         * 
         * @param selectionFunc
         *            function for selection operation
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> selectionFunc(final ISelectionStrategy selectionFunc);

        /**
         * Set size of target population.
         * <br/><b>PRE-conditions:</b> size &gt= 0
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>11:04:44 PM Sep 3, 2016</i>
         * 
         * @param size
         *            size of target population
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> size(final int size);

        /**
         * Set number of elite individuals that should be preserved in target population. Elite chromosomes are not
         * affected by selection operation.<br/>
         * If not specified, 0 value is used, that is equal to turning off elite chromosomes preservation.<br/>
         * Implementations are free to not support elite preservation mechanism at all and can indicate it by throwing
         * exception unchecked here.
         * <br/><b>PRE-conditions:</b> 0 &lt= eliteCount &lt= size
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>11:33:43 PM Sep 3, 2016</i>
         * 
         * @param eliteCount
         *            number of elite chromosomes to be preserved
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<P> eliteCount(final int eliteCount);

    }

}
