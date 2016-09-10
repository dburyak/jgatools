package dburyak.jgatools;


import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import dburyak.jtools.IConfigurable;
import dburyak.jtools.IConfigured;
import dburyak.jtools.InstanceBuilder;
import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Population that holds chromosomes and tracks their statistics.
 * <br/><b>Created on:</b> <i>4:42:07 PM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete implementation type of chromosome that this population works with
 */
@Immutable
public interface IPopulation<C extends IChromosome> extends IConfigured {

    /**
     * Get all chromosomes of this population. Sorting is not specified (implementation dependent).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:47:16 PM Sep 3, 2016</i>
     * 
     * @return observable that emits all chromosomes of this population
     */
    public Observable<C> chromosomes();

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
    public C fittest();

    /**
     * Get statistic information of this population.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:53:19 AM Sep 4, 2016</i>
     * 
     * @return stats of this population
     */
    public PopulationStats stats();


    /**
     * Project : jgatools.<br/>
     * Builder for {@link IPopulation} instances. Implementations can support more specific configuration options
     * through {@link IConfigurable} interface methods.
     * <br/><b>Created on:</b> <i>5:13:28 PM Sep 3, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     * @param <C>
     *            concrete chromosome implementation type that is used by this builder
     * @param <P>
     *            type of {@link IPopulation} implementation this builder is dedicated to
     */
    @NotThreadSafe
    public static interface IPopulationBuilder<C extends IChromosome, P extends IPopulation<C>>
        extends
            InstanceBuilder<P>,
            IConfigurable {

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
        public IPopulationBuilder<C, P> from(final P population);

        /**
         * Supply this builder with stream of chromosomes.
         * <br/><b>PRE-conditions:</b> non-null chromosomes
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:59:35 PM Sep 5, 2016</i>
         * 
         * @param chromosomes
         *            observable that emits chromosomes to be added to the target population
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<C, P> chromosomes(final Observable<C> chromosomes);

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
        public IPopulationBuilder<C, P> addChromosome(final C chromosome);

        /**
         * Deny chromosome in target population.
         * <br/><b>PRE-conditions:</b> non-null chromosome
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:32:29 PM Sep 3, 2016</i>
         * 
         * @param chromosome
         *            chromosome to be denied in target population
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<C, P> denyChromosome(final C chromosome);

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
        public IPopulationBuilder<C, P> removeDuplicates(final boolean removeDuplicates);

        /**
         * Set appearance source that emits new "out of nowhere" individuals when population is less than its size.
         * <br/><b>PRE-conditions:</b> non-null appearanceSource
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>7:44:47 PM Sep 3, 2016</i>
         * 
         * @param appearSource
         *            observable for generating new individuals
         * @return this builder (for call chaining)
         */
        public IPopulationBuilder<C, P> appearSource(final Observable<C> appearSource);

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
        public IPopulationBuilder<C, P> size(final int size);

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
        public IPopulationBuilder<C, P> eliteCount(final int eliteCount);

    }

}
