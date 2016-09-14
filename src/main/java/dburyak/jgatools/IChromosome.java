package dburyak.jgatools;


import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import dburyak.jtools.InstanceBuilder;


/**
 * Project : jgatools.<br/>
 * Chromosome to be used by genetic algorithm {@code IGeneticAlgorithm}. Represents concrete individual and its
 * corresponding builder.
 * <br/><b>Created on:</b> <i>12:26:23 AM Sep 1, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
public interface IChromosome {

    /**
     * Get unique identifier object of this chromosome internal data. Metadata such as fitness, age, generation and etc.
     * is <b>NOT</b> taken in account, <b>ONLY</b> internal data that encodes information is relevant. This means that
     * twins with different age <b>MUST</b> have the same fingerprint regardless of who were their parents or any other
     * metadata. <br/>
     * It should be used anywhere where there's a need of unique identifier of the chromosome. Particularly, it can be
     * used as a key in the hash map of chromosomes cache.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:00:36 AM Sep 3, 2016</i>
     * 
     * @return unique identifier
     */
    public Object fingerprint();

    /**
     * Get fitness of this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:22:31 AM Sep 3, 2016</i>
     * 
     * @return fitness of this chromosome
     */
    public Fitness fitness();

    /**
     * Get age of this chromosome. Age is a number of iterations that passed in pool since this chromosome was created.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result >= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:23:19 AM Sep 3, 2016</i>
     * 
     * @return age of this chromosome
     */
    public int age();

    /**
     * Get generation number of this chromosome. Generation is a number of modification steps (mutation and crossover
     * operations) of the original appeared from nowhere (by appearence operation) chromosome that led to creation of
     * this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result >= 0
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:26:25 AM Sep 3, 2016</i>
     * 
     * @return generation number of this chromosome
     */
    public int generation();

    /**
     * Get parents of this chromosome. There could be several situations:
     * <ul>
     * <li> chromosome appeared from nothing and has no parents
     * <li> chromosome was produced by mutation and has one parent
     * <li> chromosome was produced by classic crossover strategy and has two parents
     * <li> chromosome was produced by exotic crossover strategy and has more than two parents
     * </ul>
     * Result of this method is dependent on {@link ICrossoverStrategy} and {@link IMutationStrategy} implementations
     * used - they are free to not keep track of parents.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:39:22 AM Sep 3, 2016</i>
     * 
     * @return stream of parent chromosomes
     */
    public Stream<? extends IChromosome> parents();


    /**
     * Project : jgatools.<br/>
     * Builder of {@link IChromosome} instances.
     * <br/><b>Created on:</b> <i>11:49:39 PM Sep 2, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     * @param <D>
     *            type of underlying data structure that encodes genes
     * @param <C>
     *            type of {@link IChromosome} implementation this builder is dedicated to
     */
    @NotThreadSafe
    public static interface IChromosomeBuilder<C extends IChromosome, D extends Cloneable>
        extends
            InstanceBuilder<C> {

        /**
         * Generate fingerprint of chromosome gene data.
         * <br/><b>PRE-conditions:</b> non-null data
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>1:55:48 AM Sep 3, 2016</i>
         * 
         * @param data
         *            chromosome data that encodes individual
         * @return fingerprint object that uniquely identifies provided data
         */
        public Object fingerprint(final D data);

        /**
         * Set chromosome gene data for the target chromosome.
         * <br/><b>PRE-conditions:</b> non-null data
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>2:00:05 AM Sep 3, 2016</i>
         * 
         * @param data
         *            chromosome gene data
         * @return this builder (for call chaining)
         */
        public IChromosomeBuilder<C, D> data(final D data);

        /**
         * Configures builder to produce an exact copy of given chromosome. Copies <b>ALL</b> properties of given
         * chromosome including all metadata.<br/>
         * Common use-cases:
         * <ul>
         * <li> increment age of a chromosome that should be added to the pool for next iteration
         * <li> remove parents of a chromosome if ancestry limit is reached
         * </ul>
         * Implementation should decide whether there's a need of deep copy of the underlying gene data. Usually this
         * should be dictated by thread safety of gene data.<br/>
         * After data was copied from prototype chromosome, target chromosome can be configured by overriding age,
         * generation, fitness or even data.
         * <br/><b>PRE-conditions:</b> non-null chromosome
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>2:01:31 AM Sep 3, 2016</i>
         * 
         * @param chromosome
         *            original chromosome to copy all data from
         * @return this builder (for call chaining)
         */
        public IChromosomeBuilder<C, D> from(final C chromosome);

        /**
         * Set age for target chromosome. If age is not specified, default age (0 makes sense) is used.
         * <br/><b>PRE-conditions:</b> age >= 0
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>2:05:23 AM Sep 3, 2016</i>
         * 
         * @param age
         *            age of target chromosome
         * @return this builder (for call chaining)
         */
        public IChromosomeBuilder<C, D> age(final int age);

        /**
         * Set generation number for target chromosome. If generation is not specified, default generation is used (0
         * makes sense).
         * <br/><b>PRE-conditions:</b> generation >= 0
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>4:37:13 PM Sep 3, 2016</i>
         * 
         * @param generation
         *            generation of target chromosome
         * @return this builder (fo call chaining)
         */
        public IChromosomeBuilder<C, D> generation(final int generation);

        /**
         * Set fitness for target chromosome.<br/>
         * If fitness is not specified, then fitness function must be provided in
         * {@link #fitnessFunc(IFitnessFunction)}. {@link #fitness(Fitness)} and {@link #fitnessFunc(IFitnessFunction)}
         * are mutually exclusive and are two different ways to set fitness of target chromosome.
         * <br/><b>PRE-conditions:</b> non-null fitness
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>4:39:14 PM Sep 3, 2016</i>
         * 
         * @param fitness
         *            fitness for target chromosome
         * @return this builder (for call chaining)
         */
        public IChromosomeBuilder<C, D> fitness(final Fitness fitness);

        /**
         * Set fitness calculation function for target chromosome. This function is called by {@link #build()}
         * method.<br/>
         * If fitness function is not specified, then fitness should be specified manually by {@link #fitness(Fitness)}.
         * {@link #fitnessFunc(IFitnessFunction)} and {@link #fitness(Fitness)} are mutually exclusive and are two
         * different ways to set fitness of target chromosome.
         * <br/><b>PRE-conditions:</b> non-null func
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>6:24:35 PM Sep 3, 2016</i>
         * 
         * @param fitnessFunc
         *            fitness calculation function
         * @return this builder (for call chaining)
         */
        public IChromosomeBuilder<C, D> fitnessFunc(final IFitnessFunction<D> fitnessFunc);

        /**
         * Add parent to target chromosome.
         * <br/><b>PRE-conditions:</b> non-null parent
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> UNKNOWN
         * <br/><b>Created on:</b> <i>1:49:30 AM Sep 4, 2016</i>
         * 
         * @param parent
         *            one of the parents of the target chromosome
         * @return this builder (for call chaining)
         */
        public IChromosomeBuilder<C, D> parent(final C parent);

    }

}
