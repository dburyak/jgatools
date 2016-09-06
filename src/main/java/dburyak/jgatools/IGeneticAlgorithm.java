package dburyak.jgatools;


import java.util.function.Supplier;

import javax.annotation.concurrent.NotThreadSafe;

import dburyak.jgatools.IPopulation.IPopulationBuilder;
import dburyak.jtools.IConfigurable;
import dburyak.jtools.IConfigured;
import dburyak.jtools.INameable;
import dburyak.jtools.INamed;
import dburyak.jtools.InstanceBuilder;
import rx.Observable;


/**
 * Project : jgatools.<br/>
 * Core entity of genetic algorithm operation.<br/>
 * Is NOT thread safe since it can be in different states.
 * <br/><b>Created on:</b> <i>12:14:01 AM Sep 1, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 * @param <C>
 *            concrete chromosome implementation type that this GA works with
 * @param <P>
 *            population implementation type
 */
@NotThreadSafe
public interface IGeneticAlgorithm<C extends IChromosome, P extends IPopulation<C>> extends INamed, IConfigured {

    public void start();

    public void stop();


    public static interface IGeneticAlgorithmBuilder<C extends IChromosome, P extends IPopulation<C>>
        extends
            InstanceBuilder<IGeneticAlgorithm<C, P>>,
            INameable,
            IConfigurable {

        public IGeneticAlgorithmBuilder<C, P> termCondition(final ITerminationCondition<C, P> termCondition);

        public IGeneticAlgorithmBuilder<C, P> populationBuilder(
            final Supplier<IPopulationBuilder<C, P>> populationBuilder);

        public IGeneticAlgorithmBuilder<C, P> mutationSelector(final ISelectionPredicate<C> mutationSelector);

        public IGeneticAlgorithmBuilder<C, P> mutationFunc(final IMutationStrategy<C> mutationFunc);

        public IGeneticAlgorithmBuilder<C, P> crossoverFunc(final ICrossoverStrategy<C> crossoverFunc);

        public IGeneticAlgorithmBuilder<C, P> selectionFunc(final ISelectionStrategy<C> selectionFunc);

        public IGeneticAlgorithmBuilder<C, P> appearSource(final Observable<C> appearSource);

        public IGeneticAlgorithmBuilder<C, P> parent1Selector(final ISelectionPredicate<C> parent1Selector);

        public IGeneticAlgorithmBuilder<C, P> matesSelector(final IMatesSelector<C> matesSelector);

        public IGeneticAlgorithmBuilder<C, P> bufferSize(final int bufferSize);

    }

}
