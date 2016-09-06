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
final class BitSetChromosome implements IChromosome {

    private static final String fingerprint(final BitSet bitset) {
        Validators.nonNull(bitset);
        return bitset.toString();
    }


    private final BitSet bitset;
    private final int age;
    private final int generation;
    private final Fitness fitness;
    private final IFitnessFunction<BitSetChromosome> fitnessFunc;
    private final List<BitSetChromosome> parents;


    private BitSetChromosome(
        final BitSet bitset,
        final int age,
        final int generation,
        final Fitness fitness,
        final IFitnessFunction<BitSetChromosome> fitnessFunc,
        final Stream<BitSetChromosome> parents) {

        this.bitset = bitset;
        this.age = age;
        this.generation = generation;
        this.fitness = fitness;
        this.fitnessFunc = fitnessFunc;
        this.parents = parents.collect(Collectors.toList());
    }

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#fingerprint()
     * @return
     */
    @Override
    public final Object fingerprint() {
        return fingerprint(bitset);
    }

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#fitness()
     * @return
     */
    @Override
    public final Fitness fitness() {
        return fitness;
    }

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#age()
     * @return
     */
    @Override
    public final int age() {
        return age;
    }

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#generation()
     * @return
     */
    @Override
    public final int generation() {
        return generation;
    }

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>3:45:44 AM Sep 4, 2016</i>
     * 
     * @see dburyak.jgatools.IChromosome#parents()
     * @return
     */
    @Override
    public final Stream<BitSetChromosome> parents() {
        return parents.stream();
    }


    static final class BitSetChromosomeBuilder implements IChromosomeBuilder<BitSetChromosome, BitSet> {

        private BitSet bitset = null;
        private int age = -1;
        private int generation = -1;
        private Fitness fitness = null;
        private IFitnessFunction<BitSetChromosome> fitnessFunc = null;
        private final List<BitSetChromosome> parents = new ArrayList<>();


        @Override
        public IChromosome build() throws IllegalStateException {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            return new BitSetChromosome(bitset, age, generation, fitness, fitnessFunc, parents.stream());
        }

        @Override
        public boolean isValid() {
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

        @Override
        public final Object fingerprint(final BitSet data) {
            Validators.nonNull(data);
            return BitSetChromosome.fingerprint(data);
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> data(final BitSet data) {
            Validators.nonNull(data);
            bitset = (BitSet) data.clone();
            return this;
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> from(final BitSetChromosome chromosome) {
            Validators.nonNull(chromosome);
            this.bitset = (BitSet) chromosome.bitset.clone();
            this.age = chromosome.age;
            this.generation = chromosome.generation;
            return this;
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> age(final int age) {
            Validators.nonNegative(age);
            this.age = age;
            return this;
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> generation(final int generation) {
            Validators.nonNegative(generation);
            this.generation = generation;
            return this;
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> fitness(final Fitness fitness) {
            Validators.nonNull(fitness);
            this.fitness = fitness;
            return this;
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> fitnessFunc(
            final IFitnessFunction<BitSetChromosome> fitnessFunc) {

            Validators.nonNull(fitnessFunc);
            this.fitnessFunc = fitnessFunc;
            return this;
        }

        @Override
        public final IChromosomeBuilder<BitSetChromosome, BitSet> parent(final BitSetChromosome parent) {
            Validators.nonNull(parent);
            parents.add(parent);
            return this;
        }

    }

}
