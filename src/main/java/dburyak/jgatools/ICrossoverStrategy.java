package dburyak.jgatools;


import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Binary or N-ary operator to be applied to chromosomes when performing crossover.<br/>
 * General and most simple implementation takes two parents and produces one offspring. But implementations are not
 * restricted anyhow and are allowed to take any number of parents and produce any number of offsprings.
 * <br/><b>Created on:</b> <i>12:24:33 AM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 * @param <C>
 *            concrete implementation type of chromosome that this strategy works with
 */
public interface ICrossoverStrategy<C extends IChromosome> {

    /**
     * Perform crossover of given parents and produce offsprings.<br/>
     * Can trigger exceptions if given unexpected number of parents - too few or too much.
     * <br/><b>PRE-conditions:</b> non-null parents and non-empty parents
     * <br/><b>POST-conditions:</b> non-null and non-empty result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>12:32:49 AM Sep 3, 2016</i>
     * 
     * @param parents
     *            parents that participate in crossover
     * @return offsprings produced by this crossover
     */
    public Observable<C> crossover(final Observable<C> parents);

}
