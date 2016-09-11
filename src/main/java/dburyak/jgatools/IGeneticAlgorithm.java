package dburyak.jgatools;


import java.util.function.Supplier;

import javax.annotation.concurrent.NotThreadSafe;

import dburyak.jgatools.IPopulation.IPopulationBuilder;
import dburyak.jtools.IConfigurable;
import dburyak.jtools.IConfigured;
import dburyak.jtools.INameable;
import dburyak.jtools.INamed;
import dburyak.jtools.InstanceBuilder;
import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Core entity of genetic algorithm operation.<br/>
 * Is NOT thread safe since it can be in different states.
 * <br/><b>Created on:</b> <i>12:14:01 AM Sep 1, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 * @param <C>
 *            concrete chromosome implementation type that this GA works with
 * @param <P>
 *            population implementation type
 */
@SuppressWarnings("unused") // P is used by IGeneticAlgorithmBuilder as type parameter
@NotThreadSafe
public interface IGeneticAlgorithm<C extends IChromosome, P extends IPopulation<C>> extends INamed, IConfigured {

    /**
     * Start this GA. It is advised that this is done asynchronously, but not restricted to.
     * <br/><b>PRE-conditions:</b> this GA is stopped or never been run
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> this GA makes transition to running state
     * <br/><b>Created on:</b> <i>2:38:01 AM Sep 11, 2016</i>
     */
    public void start();

    /**
     * Stop this GA. It is advised that this is done asynchronously, but not restricted to.
     * <br/><b>PRE-conditions:</b> this GA is running
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> this GA makes transition to stopped state
     * <br/><b>Created on:</b> <i>2:50:40 AM Sep 11, 2016</i>
     */
    public void stop();


    /**
     * Project : jgatools.<br/>
     * Builder for {@link IGeneticAlgorithm} instances.
     * <br/><b>Created on:</b> <i>3:12:28 AM Sep 11, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     * @param <C>
     *            concrete chromosome implementation type to be used by target GA
     * @param <P>
     *            concrete population implementation type to be used by target GA
     */
    public static interface IGeneticAlgorithmBuilder<C extends IChromosome, P extends IPopulation<C>>
        extends
            InstanceBuilder<IGeneticAlgorithm<C, P>>,
            INameable,
            IConfigurable {

        /**
         * Set termination condition evaluation function for detecting end of the GA evaluation.
         * <br/><b>PRE-conditions:</b> non-null termCondition
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:14:37 AM Sep 11, 2016</i>
         * 
         * @param termCondition
         *            terminal condition evaluation function
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> termCondition(final ITerminationCondition<C, P> termCondition);

        /**
         * Set supplier of population builders for producing new populations on each GA iteration.
         * <br/><b>PRE-conditions:</b> non-null populationBuilder
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:18:05 AM Sep 11, 2016</i>
         * 
         * @param populationBuilder
         *            supplier of population builders
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> populationBuilder(
            final Supplier<IPopulationBuilder<C, P>> populationBuilder);

        /**
         * Set strategy of selecting chromosomes for mutation operation.
         * <br/><b>PRE-conditions:</b> non-null mutationSelector
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:20:30 AM Sep 11, 2016</i>
         * 
         * @param mutationSelector
         *            strategy for selecting mutating chromosomes
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> mutationSelector(final ISelectionPredicate<C> mutationSelector);

        /**
         * Set strategy for performing the mutation operation to be used by target GA.
         * <br/><b>PRE-conditions:</b> non-null mutationFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:22:11 AM Sep 11, 2016</i>
         * 
         * @param mutationFunc
         *            strategy for performing mutation operation
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> mutationFunc(final IMutationStrategy<C> mutationFunc);

        /**
         * Set strategy for performing the crossover operation to be used by target GA.
         * <br/><b>PRE-conditions:</b> crossoverFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:41:33 AM Sep 11, 2016</i>
         * 
         * @param crossoverFunc
         *            strategy for performing crossover operation
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> crossoverFunc(final ICrossoverStrategy<C> crossoverFunc);

        /**
         * Set strategy for performing natural selection operation over chromosomes buffer.
         * <br/><b>PRE-conditions:</b> non-null selectionFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:42:46 AM Sep 11, 2016</i>
         * 
         * @param selectionFunc
         *            strategy for performing natural selection operation
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> selectionFunc(final ISelectionStrategy<C> selectionFunc);

        /**
         * Set source of generated "out of nowhere" chromosomes.
         * <br/><b>PRE-conditions:</b> non-null appearSource
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:44:13 AM Sep 11, 2016</i>
         * 
         * @param appearSource
         *            observable that emits newly created chromosomes
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> appearSource(final Observable<C> appearSource);

        /**
         * Set strategy for choosing first parent chromosome for crossover operation.
         * <br/><b>PRE-conditions:</b> non-null parent1Selector
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:45:30 AM Sep 11, 2016</i>
         * 
         * @param parent1Selector
         *            selection function for choosing first parent for crossover operation
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> parent1Selector(final ISelectionPredicate<C> parent1Selector);

        /**
         * Set strategy for choosing other mates (besides first parent) for crossover operation.
         * <br/><b>PRE-conditions:</b> non-null matesSelector
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:46:56 AM Sep 11, 2016</i>
         * 
         * @param matesSelector
         *            strategy for choosing mates for crossover operation
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> matesSelector(final IMatesSelector<C> matesSelector);

        /**
         * Set size of the chromosomes buffer. "Buffer" is a set of chromosomes that contains:
         * <p>
         * <ul>
         * <li> mutants
         * <li> offsprings
         * <li> custom chromosomes
         * <li> newly generated "out of nowhere" chromosomes
         * </ul>
         * "Buffer" exists internally, its details are left for implementations, and it is not exposed to user code. It
         * is advised to have buffer size such that:
         * <code>
         * bufferSize &gt= popSize + mutantsSize + offspringsSize + addedSize
         * </code>
         * <br/><b>PRE-conditions:</b> bufferSize &gt= 0
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>3:48:05 AM Sep 11, 2016</i>
         * 
         * @param bufferSize
         *            size of the "buffer"
         * @return this builder (for call chaining)
         */
        public IGeneticAlgorithmBuilder<C, P> bufferSize(final int bufferSize);

    }

}
