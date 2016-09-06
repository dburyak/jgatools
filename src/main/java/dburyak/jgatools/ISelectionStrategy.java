package dburyak.jgatools;


import rx.Observable;


/**
 * Project : jgatools.<br/>
 * <br/><b>Created on:</b> <i>4:03:54 AM Sep 5, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ISelectionStrategy<C extends IChromosome> {

    public Observable<C> select(final Observable<C> buffer);

}
