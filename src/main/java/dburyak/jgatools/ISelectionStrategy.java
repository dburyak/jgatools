package dburyak.jgatools;


import java.util.stream.Stream;


/**
 * Project : jgatools.<br/>
 * Function that performs selection operation - filters out chromosomes that are not satisfactory by some criteria. It
 * is a stateful filter operation. It can be used in several ways:
 * <ul>
 * <li> select chromosomes that should be kept alive by evolution
 * <li> select chromosome(s) that should be used for crossover
 * <li> select chromosome that should be used for mutation
 * </ul>
 * <br/><b>Created on:</b> <i>11:20:50 PM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ISelectionStrategy {

    /**
     * Select chromosomes that should be left alive by evolution.
     * <br/><b>PRE-conditions:</b> non-null chromosomes
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:26:08 PM Sep 3, 2016</i>
     * 
     * @param chromosomes
     *            stream of chromosomes that are under selection operation
     * @return chromosomes that have successfully passed selection operation
     */
    public Stream<IChromosome> select(final Stream<IChromosome> chromosomes);

}
