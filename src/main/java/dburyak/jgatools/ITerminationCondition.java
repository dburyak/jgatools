package dburyak.jgatools;


import java.time.Duration;


/**
 * Project : jgatools.<br/>
 * <br/><b>Created on:</b> <i>12:07:58 AM Sep 4, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ITerminationCondition {

    /**
     * Check if genetic algorithm should terminate.
     * <br/><b>PRE-conditions:</b> non-null population, iteration &gt= 0, non-null runtime
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>1:52:18 AM Sep 4, 2016</i>
     * 
     * @param population
     *            population at given iteration
     * @param iteration
     *            current iteration
     * @param runtime
     *            time that GA is running
     * @return true if genetic algorithm should terminate, false if it needs to search further
     */
    public boolean shouldTerminate(final IPopulation population, final int iteration, final Duration runtime);

}
