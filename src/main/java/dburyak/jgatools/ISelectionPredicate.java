package dburyak.jgatools;


/**
 * Project : jgatools.<br/>
 * Function that performs selection operation - filters out chromosomes that are not satisfactory by some criteria.<br/>
 * It can be used in several ways:
 * <ul>
 * <li> select chromosome(s) that should be used for crossover
 * <li> select chromosome that should be used for mutation
 * </ul>
 * <br/><b>Created on:</b> <i>11:20:50 PM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 * @param <C>
 *            concrete implementation type of chromosome this predicate works with
 */
public interface ISelectionPredicate<C extends IChromosome> {

    /**
     * Test if chromosome should be selected or not.
     * <br/><b>PRE-conditions:</b> non-null chromosome
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>11:26:08 PM Sep 3, 2016</i>
     * 
     * @param chromosome
     *            chromosome that should be tested
     * @return true if chromosome should be selected, false if it fails selection
     */
    public boolean select(final C chromosome);

}
