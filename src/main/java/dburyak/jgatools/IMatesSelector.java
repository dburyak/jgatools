package dburyak.jgatools;


import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Strategy for selecting parents for crossover. It is triggered when first parent is chosen and algorithm starts to
 * select subsequent parents.
 * <br/><b>Created on:</b> <i>2:09:17 AM Sep 5, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete chromosome implementation type this selector works with
 */
public interface IMatesSelector<C extends IChromosome> {

    /**
     * Select mates for particular parent from candidates.<br/>
     * <p>
     * Note that implementation can trigger exception if there're too few candidates. This may happen when candidates
     * observable is finished before all mates are chosen . Thus, it's advisable that either caller or implementation or
     * even both use {@link Observable#repeat()} operator on candidates observable to loop it in a closed circle.<br/>
     * But even in this case it is still possible to have small enough candidates source that this selector would stuck
     * forever infinitely looping through candidates and discarding each of them. For instance, this can happen when we
     * need 4 parents for crossover, but candidates size is 2.
     * <br/><b>PRE-conditions:</b> non-null parent1, non-null and non-empty candidates
     * <br/><b>POST-conditions:</b> non-null and non-empty result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>2:24:10 AM Sep 5, 2016</i>
     * 
     * @param parent1
     *            first parent for crossover
     * @param candidates
     *            candidate mates for crossover
     * @return mates that were chosen for crossover from candidates
     */
    public Observable<C> select(final C parent1, final Observable<C> candidates);

}
