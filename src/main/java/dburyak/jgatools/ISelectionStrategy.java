package dburyak.jgatools;


import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Natural selection strategy.
 * <br/><b>Created on:</b> <i>4:03:54 AM Sep 5, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete chromosome implementation type
 */
public interface ISelectionStrategy<C extends IChromosome> {

    /**
     * Perfrom natural selection operation on given "buffer" of chromosomes. Usually, this buffer is a collection of
     * mutants, offsprings, previous population and new generated "out of nowhere" chromosomes.
     * <br/><b>PRE-conditions:</b> non-null buffer
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>9:36:22 AM Sep 10, 2016</i>
     * 
     * @param buffer
     *            chromosomes to apply natural selection operation to
     * @param size
     *            number of chromosomes that should be left alive after selection
     * @return result of natural selection
     */
    public Observable<C> select(final Observable<C> buffer, final int size);

}
