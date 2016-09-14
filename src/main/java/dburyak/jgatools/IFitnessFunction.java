package dburyak.jgatools;


/**
 * Project : jgatools.<br/>
 * <br/><b>Created on:</b> <i>4:27:38 PM Sep 4, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <D>
 *            type of underlying genetic data used by chromosome. However, this type may differ from real internal data
 *            type for sake of convenience.
 */
public interface IFitnessFunction<D extends Cloneable> {

    /**
     * Calculate fitness of given chromosome genetic data. It should be used in chromosome builders.
     * <br><b>PRE-conditions:</b> non-null data
     * <br><b>POST-conditions:</b> non-null result
     * <br><b>Side-effects:</b> UNKNOWN
     * <br><b>Created on:</b> <i>4:51:24 PM Sep 12, 2016</i>
     * 
     * @param data
     *            genetic data of chromosome
     * @return fitness of given genetic data
     */
    public Fitness calcFitness(final D data);

}
