package dburyak.jgatools;


/**
 * Project : jgatools.<br/>
 * Unary operator to be applied to the chromosome when it is selected for mutation.
 * <br/><b>Created on:</b> <i>11:39:47 PM Sep 2, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete implementation type of chromosome that this mutation strategy works with
 */
public interface IMutationStrategy<C extends IChromosome> {

    /**
     * Apply mutation operation to given chromosome and produce a new one.
     * <br/><b>PRE-conditions:</b> non-null chromosome
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>12:21:51 AM Sep 3, 2016</i>
     * 
     * @param chromosome
     *            chromosome to be mutated
     * @return a brand new mutant chromosome
     */
    public C mutate(final C chromosome);

}
