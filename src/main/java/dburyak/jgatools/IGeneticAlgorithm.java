package dburyak.jgatools;


import javax.annotation.concurrent.Immutable;

import dburyak.jtools.IConfigurable;
import dburyak.jtools.IConfigured;
import dburyak.jtools.INameable;
import dburyak.jtools.INamed;
import dburyak.jtools.InstanceBuilder;


/**
 * Project : jgatools.<br/>
 * Core entity of genetic algorithm operation.
 * <br/><b>Created on:</b> <i>12:14:01 AM Sep 1, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
public interface IGeneticAlgorithm extends INamed, IConfigured {

    public void start();

    public void stop();


    public interface IGeneticAlgorithmBuilder<GA extends IGeneticAlgorithm>
        extends
            InstanceBuilder<IGeneticAlgorithm>,
            INameable,
            IConfigurable {

        public IGeneticAlgorithmBuilder<GA> termCondition(final ITerminationCondition termCondition);

    }

}
