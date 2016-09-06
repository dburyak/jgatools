package dburyak.jgatools;


/**
 * Project : jgatools.<br/>
 * <br/><b>Created on:</b> <i>4:27:38 PM Sep 4, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete chromosome implementation that this function works for
 */
public interface IFitnessFunction<C extends IChromosome> {

    /**
     * Calculate fitness of given chromosome.
     * <br/><b>PRE-conditions:</b> non-null chromosome
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>4:28:28 PM Sep 4, 2016</i>
     * 
     * @param chromosome
     *            chromosome to calculate fitness for
     * @return fitness of given chromosome
     */
    public Fitness calcFitness(final C chromosome);

}
