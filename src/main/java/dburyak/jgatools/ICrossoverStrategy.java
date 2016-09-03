package dburyak.jgatools;


import java.util.stream.Stream;


/**
 * Project : jgatools.<br/>
 * Binary or N-ary operator to be applied to chromosomes when performing crossover.<br/>
 * General and most simple implementation takes two parents and produces one offspring. But implementations are not
 * restricted anyhow and are allowed to take any number of parents and produce any number of offsprings.
 * <br/><b>Created on:</b> <i>12:24:33 AM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ICrossoverStrategy {

    /**
     * Perform crossover of given parents and produce offsprings.
     * <br/><b>PRE-conditions:</b> non-null parent1, non-null parent2, non-null other
     * <br/><b>POST-conditions:</b> non-null and non-empty result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>12:32:49 AM Sep 3, 2016</i>
     * 
     * @param parent1
     *            first parent for crossover
     * @param parent2
     *            second parent for crossover
     * @param other
     *            other parents for crossover
     * @return offsprings produced by this crossover
     */
    public Stream<IChromosome> crossover(
        final IChromosome parent1,
        final IChromosome parent2,
        final IChromosome... other);

}
