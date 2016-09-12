package dburyak.jgatools.impl;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import dburyak.jgatools.Fitness;
import dburyak.jgatools.IChromosome;
import dburyak.jgatools.IFitnessFunction;
import dburyak.jtools.Validators;


/**
 * Project : jgatools.<br/>
 * Binary string chromosome that uses {@link BitSet} as underlying data implementation.
 * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
public final class BitSetChromosome implements IChromosome {

    /**
     * Calculate fingerprint for given chromosome bitset internal data.
     * <br><b>PRE-conditions:</b> non-null bitset
     * <br><b>POST-conditions:</b> non-empty result
     * <br><b>Side-effects:</b> NONE
     * <br><b>Created on:</b> <i>1:54:43 AM Sep 12, 2016</i>
     * 
     * @param bitset
     *            bitset to evaluate fingerprint for
     * @return fingerprint of provided bitset
     */
    @SuppressWarnings("boxing")
    private static final Integer fingerprint(final BitSet bitset) {
        Validators.nonNull(bitset);
        return bitset.hashCode();
    }


    /**
     * Internal data representation - {@link BitSet} object.
     * <br><b>Created on:</b> <i>1:58:25 AM Sep 12, 2016</i>
     */
    private final BitSet bitset;

    /**
     * Age of chromosome.
     * <br><b>Created on:</b> <i>1:58:57 AM Sep 12, 2016</i>
     */
    private final int age;

    /**
     * Generation number of chromosome.
     * <br><b>Created on:</b> <i>1:59:08 AM Sep 12, 2016</i>
     */
    private final int generation;

    /**
     * Fitness value of chromosome.
     * <br><b>Created on:</b> <i>1:59:19 AM Sep 12, 2016</i>
     */
    private final Fitness fitness;

    /**
     * Parents of this chromosome.
     * <br><b>Created on:</b> <i>2:00:03 AM Sep 12, 2016</i>
     */
    private final List<BitSetChromosome> parents;


    /**
     * Constructor for class : [jgatools] dburyak.jgatools.impl.BitSetChromosome.<br>
     * <br><b>PRE-conditions:</b> valid parameters
     * <br><b>POST-conditions:</b> NONE
     * <br><b>Side-effects:</b> NONE
     * <br><b>Created on:</b> <i>2:04:04 AM Sep 12, 2016</i>
     * 
     * @param bitset
     *            bitset for chromosome internal data
     * @param age
     *            age of chromosome
     * @param generation
     *            generation number
     * @param fitness
     *            fitness value of chromosome
     * @param parents
     *            parents of chromosome
     */
    private BitSetChromosome(
        final BitSet bitset,
        final int age,
        final int generation,
        final Fitness fitness,
        final Stream<BitSetChromosome> parents) {

        this.bitset = bitset;
        this.age = age;
        this.generation = generation;
        this.fitness = fitness;
        this.parents = parents.collect(Collectors.toList());
    }

    /**
     * Evaluate fingerprint of this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#fingerprint()
     * @return fingerprint of this chromosome
     */
    @Override
    public final Object fingerprint() {
        return fingerprint(bitset);
    }

    /**
     * Get fitness of this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#fitness()
     * @return fitness of this chromosome
     */
    @Override
    public final Fitness fitness() {
        return fitness;
    }

    /**
     * Get age of this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt;= 0
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#age()
     * @return age of this chromosome
     */
    @Override
    public final int age() {
        return age;
    }

    /**
     * Get generation number of this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result &gt;= 0
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#generation()
     * @return generation number of this chromosome
     */
    @Override
    public final int generation() {
        return generation;
    }

    /**
     * Get parents of this chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#parents()
     * @return stream of parents of this chromosome
     */
    @Override
    public final Stream<BitSetChromosome> parents() {
        return parents.stream();
    }

    /**
     * Get internal bitset. Returns copy of underlying data thus ensuring immutability.<br>
     * Intended to be used by crossover and mutation implementations.
     * <br><b>PRE-conditions:</b> NONE
     * <br><b>POST-conditions:</b> non-null result
     * <br><b>Side-effects:</b> NONE
     * <br><b>Created on:</b> <i>3:25:12 AM Sep 12, 2016</i>
     * 
     * @return clone of underlying bitset data
     */
    public final BitSet getBitset() {
        return (BitSet) bitset.clone();
    }


    /**
     * Project : jgatools.<br>
     * Builder of {@link BitSetChromosome} instances.
     * <br><b>Created on:</b> <i>2:13:51 AM Sep 12, 2016</i>
     * 
     * @author <i>Dmytro Buryak &lt;dmytro.buryak@gmail.com&gt;</i>
     * @version 0.1
     */
    public static final class BitSetChromosomeBuilder implements IChromosomeBuilder<BitSetChromosome, BitSet> {

        /**
         * Internal bitset data for target chromosome.
         * <br><b>Created on:</b> <i>2:14:53 AM Sep 12, 2016</i>
         */
        private BitSet bitset = null;

        /**
         * Age of target chromosome.
         * <br><b>Created on:</b> <i>2:15:09 AM Sep 12, 2016</i>
         */
        private int age = -1;

        /**
         * Generation number of target chromosome.
         * <br><b>Created on:</b> <i>2:15:17 AM Sep 12, 2016</i>
         */
        private int generation = -1;

        /**
         * Fitness of target chromosome.
         * <br><b>Created on:</b> <i>2:15:30 AM Sep 12, 2016</i>
         */
        private Fitness fitness = null;

        /**
         * Fitness evaluation function for calculating fitness of target chromosome.
         * <br><b>Created on:</b> <i>2:15:42 AM Sep 12, 2016</i>
         */
        private IFitnessFunction<BitSetChromosome> fitnessFunc = null;

        /**
         * Parents of target chromosome.
         * <br><b>Created on:</b> <i>2:16:05 AM Sep 12, 2016</i>
         */
        private final List<BitSetChromosome> parents = new ArrayList<>();


        /**
         * Build target bitset chromosome.
         * <br><b>PRE-conditions:</b> this builder is in valid state
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> NONE
         * <br><b>Created on:</b> <i>2:16:52 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#build()
         * @return new bitset chromosome instance
         * @throws IllegalStateException
         *             if this builder is in invalid state
         */
        @Override
        public final IChromosome build() throws IllegalStateException {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            return new BitSetChromosome(bitset, age, generation, fitness, parents.stream());
        }

        /**
         * Check if this builder is in valid state, though target chromosome can be built.
         * <br><b>PRE-conditions:</b> NONE
         * <br><b>POST-conditions:</b> NONE
         * <br><b>Side-effects:</b> NONE
         * <br><b>Created on:</b> <i>2:18:25 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#isValid()
         * @return true if this builder is in valid state, false otherwise
         */
        @Override
        public final boolean isValid() {
            if (bitset == null) {
                return false;
            }
            if (age < 0) {
                return false;
            }
            if (generation < 0) {
                return false;
            }
            if (fitness == null) {
                return false;
            }
            if (fitnessFunc == null) {
                return false;
            }
            return true;
        }

        /**
         * Evaluate fingerprint of given bitset chromosome internal data.
         * <br><b>PRE-conditions:</b> non-null data
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> NONE
         * <br><b>Created on:</b> <i>2:19:38 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#fingerprint(java.lang.Cloneable)
         * @param data
         *            internal chromosome bitset to calculate fingerprint from
         * @return fingerprint of provided chromosome data
         */
        @Override
        public final Object fingerprint(final BitSet data) {
            Validators.nonNull(data);
            return BitSetChromosome.fingerprint(data);
        }

        /**
         * Set internal bitset data for target chromosome.
         * <br><b>PRE-conditions:</b> non-null data
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>2:22:35 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#data(java.lang.Cloneable)
         * @param data
         *            bitset internal data for target chromosome
         * @return this builder (for call chaining)
         */
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> data(final BitSet data) {
            Validators.nonNull(data);
            bitset = (BitSet) data.clone();
            return this;
        }

        /**
         * Copy all the data and parameters from given chromosome.
         * <br><b>PRE-conditions:</b> non-null chromosome
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is chnaged
         * <br><b>Created on:</b> <i>3:17:44 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#from(dburyak.jgatools.IChromosome)
         * @param chromosome
         *            chromosome to copy data from
         * @return this builder (for call chaining)
         */
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> from(final BitSetChromosome chromosome) {
            Validators.nonNull(chromosome);
            this.bitset = (BitSet) chromosome.bitset.clone();
            this.age = chromosome.age;
            this.generation = chromosome.generation;
            return this;
        }

        /**
         * Set age of target chromosome.
         * <br><b>PRE-conditions:</b> age &gt;= 0
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>3:19:51 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#age(int)
         * @param age
         *            age of target chromosome
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> age(final int age) {
            Validators.nonNegative(age);
            this.age = age;
            return this;
        }

        /**
         * Set generation number of target chromosome.
         * <br><b>PRE-conditions:</b> generation &gt;= 0
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>3:21:15 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#generation(int)
         * @param generation
         *            generation number of target chromosome
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> generation(final int generation) {
            Validators.nonNegative(generation);
            this.generation = generation;
            return this;
        }

        /**
         * Set fitness of target chromosome.
         * <br><b>PRE-conditions:</b> non-null fitness
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>3:22:07 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#fitness(dburyak.jgatools.Fitness)
         * @param fitness
         *            fitness of target chromosome
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> fitness(final Fitness fitness) {
            Validators.nonNull(fitness);
            this.fitness = fitness;
            return this;
        }

        /**
         * Set fitness evaluation strategy for calculating fitness of the target chromosome.
         * <br><b>PRE-conditions:</b> non-null fitnessFunc
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>3:22:53 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#fitnessFunc(dburyak.jgatools.IFitnessFunction)
         * @param fitnessFunc
         *            fitness evaluation strategy
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> fitnessFunc(
            final IFitnessFunction<BitSetChromosome> fitnessFunc) {

            Validators.nonNull(fitnessFunc);
            this.fitnessFunc = fitnessFunc;
            return this;
        }

        /**
         * Add parent to parent list of target chromosome.
         * <br><b>PRE-conditions:</b> non-null parent
         * <br><b>POST-conditions:</b> non-null result
         * <br><b>Side-effects:</b> this builder state is changed
         * <br><b>Created on:</b> <i>3:30:44 AM Sep 12, 2016</i>
         * 
         * @see dburyak.jgatools.IChromosome.IChromosomeBuilder#parent(dburyak.jgatools.IChromosome)
         * @param parent
         *            parent of the chromosome
         * @return this builder for (call chaining)
         */
        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> parent(final BitSetChromosome parent) {
            Validators.nonNull(parent);
            parents.add(parent);
            return this;
        }

    }

}
