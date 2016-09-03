package dburyak.jgatools;


/**
 * Project : jgatools.<br/>
 * Null-ary operator to be used to generate chromosomes from nothing.
 * <br/><b>Created on:</b> <i>1:41:49 AM Sep 3, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface IAppearanceStrategy {

    /**
     * Create new chromosome.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:43:37 AM Sep 3, 2016</i>
     * 
     * @return new chromosome
     */
    public IChromosome appear();

}
