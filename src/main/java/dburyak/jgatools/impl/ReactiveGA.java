package dburyak.jgatools.impl;


import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jgatools.IChromosome;
import dburyak.jgatools.ICrossoverStrategy;
import dburyak.jgatools.IGeneticAlgorithm;
import dburyak.jgatools.IMatesSelector;
import dburyak.jgatools.IMutationStrategy;
import dburyak.jgatools.IPopulation;
import dburyak.jgatools.IPopulation.IPopulationBuilder;
import dburyak.jgatools.ISelectionPredicate;
import dburyak.jgatools.ISelectionStrategy;
import dburyak.jgatools.ITerminationCondition;
import dburyak.jtools.Validators;
import dburyak.jtools.tuples.Tuples;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;


/**
 * Project : jgatools.<br/>
 * Highly concurrent GA implementation that uses rxjava.
 * <br/><b>Created on:</b> <i>2:32:31 AM Sep 4, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 * @param <C>
 *            concrete chromosome implementation type this GA works with
 * @param <P>
 *            concrete population implementation type
 */
@ThreadSafe
final class ReactiveGA<C extends IChromosome, P extends IPopulation<C>> implements IGeneticAlgorithm<C, P> {

    /**
     * System logger.
     * <br/><b>Created on:</b> <i>2:33:40 AM Sep 4, 2016</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(ReactiveGA.class);

    /**
     * Name of this instance.
     * <br/><b>Created on:</b> <i>3:55:56 AM Sep 6, 2016</i>
     */
    private final String name;

    /**
     * Properties for this instance. Used for some fine-tuning that can't be configured by builder.
     * <br/><b>Created on:</b> <i>3:56:10 AM Sep 6, 2016</i>
     */
    private final Properties props;

    /**
     * Termination condition evaluation function used by this GA.
     * <br/><b>Created on:</b> <i>3:57:26 AM Sep 6, 2016</i>
     */
    private final ITerminationCondition<C, P> termCondition;

    /**
     * Observable that produces initial chromosomes (chromosomes out of nowhere).
     * <br/><b>Created on:</b> <i>3:57:52 AM Sep 6, 2016</i>
     */
    private final Observable<C> appearSource;

    /**
     * Selector function to be used for choosing chromomsomes for mutation.
     * <br/><b>Created on:</b> <i>3:58:59 AM Sep 6, 2016</i>
     */
    private final ISelectionPredicate<C> mutationSelector;

    /**
     * Mutation strategy to be applied to chromosomes under mutation.
     * <br/><b>Created on:</b> <i>3:59:24 AM Sep 6, 2016</i>
     */
    private final IMutationStrategy<C> mutationFunc;

    /**
     * Selector for choosing first parent for crossover operation.
     * <br/><b>Created on:</b> <i>4:02:49 AM Sep 6, 2016</i>
     */
    private final ISelectionPredicate<C> parent1Selector;

    /**
     * Selector for choosing other mates for crossover operation.
     * <br/><b>Created on:</b> <i>4:03:14 AM Sep 6, 2016</i>
     */
    private final IMatesSelector<C> matesSelector;

    /**
     * Crossover strategy to be applied to chromosomes for crossover.
     * <br/><b>Created on:</b> <i>4:03:38 AM Sep 6, 2016</i>
     */
    private final ICrossoverStrategy<C> crossoverFunc;

    /**
     * Timeout for parameters incompatibility detection : population size and crossover mates number. If crossover mates
     * number is less than population size, then algorithm loops infinitely when searching for crossover mates in
     * population.
     * <br/><b>Created on:</b> <i>4:05:22 AM Sep 6, 2016</i>
     */
    private final Duration matesSelectTimeout;

    /**
     * Strategy for performing evolution selection operation.
     * <br/><b>Created on:</b> <i>4:08:09 AM Sep 6, 2016</i>
     */
    private final ISelectionStrategy<C> selectFunc;

    /**
     * Size of chromosomes "buffer" zone. Mutatants and offsprings go to buffer, then original population chromosomes go
     * to buffer, and if brand new are generated if there's space left. Natural selection operation is performed upon
     * buffer.
     * <br/><b>Created on:</b> <i>4:08:37 AM Sep 6, 2016</i>
     */
    private final int bufferSize;

    /**
     * Supplier function that creates new builders for {@link IPopulation} instances. It is used on each evolution
     * iteration step to create a new population.
     * <br/><b>Created on:</b> <i>4:13:11 AM Sep 6, 2016</i>
     */
    private final Supplier<IPopulationBuilder<C, P>> populationBuilderProducer;

    /**
     * Subscription of evolution loop. Is created on each start of GA. If is unsubscribed, then GA terminates.
     * <br/><b>Created on:</b> <i>4:14:13 AM Sep 6, 2016</i>
     */
    @GuardedBy("this")
    private Subscription evolutionPipelineSubscr = null;

    /**
     * Auxiliary object to create recursive loop in RX pipeline, where population received at the end of pipeline is
     * examined and pushed into the beginning of pipeline.
     * <br/><b>Created on:</b> <i>4:15:17 AM Sep 6, 2016</i>
     */
    @GuardedBy("this")
    private PublishSubject<P> populations = null;


    /**
     * Constructor for class : [jgatools] dburyak.jgatools.impl.ReactiveGA.<br/>
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>4:18:06 AM Sep 6, 2016</i>
     * 
     * @param name
     *            name of this GA
     * @param props
     *            configuration for this GA
     * @param termCondition
     *            termination condition
     * @param appearSource
     *            source of new generated chromosomes
     * @param mutationSelector
     *            selector for mutating chromosomes
     * @param mutationFunc
     *            mutation strategy
     * @param parent1Selector
     *            selector for choosing first parent for crossover
     * @param matesSelector
     *            selector for choosing other mates for crossover
     * @param crossoverFunc
     *            crossover strategy
     * @param matesSelectTimeout
     *            timeout for detecting incompatible population size and number of mates parameters
     * @param selectFunc
     *            natural selection strategy
     * @param bufferSize
     *            size of "buffer" intermediate zone
     * @param populationBuilderProducer
     *            supplier of population builder instances
     */
    private ReactiveGA(
        final String name,
        final Properties props,
        final ITerminationCondition<C, P> termCondition,
        final Observable<C> appearSource,
        final ISelectionPredicate<C> mutationSelector,
        final IMutationStrategy<C> mutationFunc,
        final ISelectionPredicate<C> parent1Selector,
        final IMatesSelector<C> matesSelector,
        final ICrossoverStrategy<C> crossoverFunc,
        final Duration matesSelectTimeout,
        final ISelectionStrategy<C> selectFunc,
        final int bufferSize,
        final Supplier<IPopulationBuilder<C, P>> populationBuilderProducer) {

        this.name = name;
        this.props = props;
        this.termCondition = termCondition;
        this.appearSource = appearSource;

        this.mutationSelector = mutationSelector;
        this.mutationFunc = mutationFunc;

        this.parent1Selector = parent1Selector;
        this.matesSelector = matesSelector;
        this.crossoverFunc = crossoverFunc;
        this.matesSelectTimeout = matesSelectTimeout;

        this.bufferSize = bufferSize;

        this.populationBuilderProducer = populationBuilderProducer;
        this.selectFunc = selectFunc;

        evolutionPipelineSubscr = null;
        populations = null;
    }

    /**
     * Get name of GA.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:23:12 AM Sep 6, 2016</i>
     * 
     * @see dburyak.jtools.INamed#name()
     * @return name of this GA
     */
    @Override
    public final String name() {
        return name;
    }

    /**
     * Get specific property of this GA.
     * <br/><b>PRE-conditions:</b> non-empty key
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:24:01 AM Sep 6, 2016</i>
     * 
     * @see dburyak.jtools.IConfigured#property(java.lang.String)
     * @param key
     *            property key
     * @return property value or null if there's no value defined for given key
     */
    @Override
    public final String property(final String key) {
        return props.getProperty(key);
    }

    /**
     * Get {@code Observable} that produces initial population "out of nowhere". Population chromosomes are generated
     * purely from {@link #appearSource}.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:25:09 AM Sep 6, 2016</i>
     * 
     * @return observable that emits "out of nowhere" initial population
     */
    private final Observable<P> initialPopulation() {
        final IPopulationBuilder<C, P> initPopulationBuilder = populationBuilderProducer.get()
            .chromosomes(appearSource); // set chromosomes generator
        return Observable.fromCallable(initPopulationBuilder::build);
    }

    /**
     * Core algorithm function. Performs mutation, crossover and selection on current population and produces next
     * generation population.
     * <br/><b>PRE-conditions:</b> non-null population
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> appear source is subscribed, mutation and crossover functions are called
     * <br/><b>Created on:</b> <i>4:29:35 AM Sep 6, 2016</i>
     * 
     * @param population
     *            current population
     * @return observable that emits next generation population
     */
    private final Observable<P> buildIterationPipeline(final IPopulation<C> population) {
        final Observable<C> origin = population.chromosomes();
        final int eliteCount = population.eliteCount();

        final Observable<C> modified = Observable.merge(
            // MUTATION
            origin
                .filter(mutationSelector::select) // choose mutants
                .map(mutationFunc::mutate), // mutate

            // CROSSOVER
            origin.filter(parent1Selector::select) // choose parent1
                .map(p1 -> {
                    return matesSelector.select(p1, origin.repeat()) // choose mates
                        .startWith(p1); // don't forget about parent1
                })
                // detect infinite mates selection, @see IMatesSelector#select(C, Observable<C>)
                .timeout(matesSelectTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .flatMap(crossoverFunc::crossover) // apply crossover on received Observable with parents
        );

        final Observable<C> buffer =
            origin.take(eliteCount) // original elite
                .concatWith(modified)
                .concatWith(origin.skip(eliteCount)) // original non-elite
                .concatWith(appearSource)
                .take(bufferSize);

        final Observable<C> nextGeneration = selectFunc.select(buffer); // selection

        final IPopulationBuilder<C, P> nextPopulationBuilder = populationBuilderProducer.get() // new population builder
            .chromosomes(nextGeneration); // set chromosomes for next population
        return Observable.fromCallable(nextPopulationBuilder::build);
    }

    /**
     * Build evolution pipeline and start evolution process.
     * <br/><b>PRE-conditions:</b> population != null
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:49:56 AM Sep 6, 2016</i>
     * 
     * @return subscription to evolution pipeline
     */
    @SuppressWarnings({ "nls", "boxing" })
    private final Subscription startEvolution() {
        final Observable<Integer> iterations = Observable.from(() -> IntStream.iterate(0, i -> i + 1).iterator());
        final Instant startTime = Instant.now();
        final Observable<Duration> durations = Observable.from(() -> {
            return Stream.generate(() -> 0).map(tick -> Duration.between(startTime, Instant.now())).iterator();
        });
        return Observable.zip(
            initialPopulation().concatWith(
                populations.doOnCompleted(() -> LOG.debug("populations completed"))),
            iterations,
            durations,
            (p, i, d) -> {
                return Tuples.create(p, i, d);
            })
            .subscribe(
                t3 -> { // tuple (population, iteration, duration)
                    LOG.debug("evolution iteration : iteration = [%d] ; duration = [%s] ; population = [%s]",
                        t3._2, t3._3, t3._1);
                    if (termCondition.shouldTerminate(t3._1, t3._2, t3._3)) {
                        populations.onCompleted();
                    } else {
                        buildIterationPipeline(t3._1).subscribe(p -> populations.onNext(p));
                    }
                },
                error -> {
                    LOG.error("error in evolution pipeline : error = [%s]", error);
                },
                () -> {
                    LOG.info("evolution pipeline completed");
                });
    }

    /**
     * Start genetic algorithm.
     * <br/><b>PRE-conditions:</b> this GA is stopped (or haven't been run)
     * <br/><b>POST-conditions:</b> this GA becomes running
     * <br/><b>Side-effects:</b> internal state is changed
     * <br/><b>Created on:</b> <i>4:51:13 AM Sep 6, 2016</i>
     * 
     * @see dburyak.jgatools.IGeneticAlgorithm#start()
     */
    @SuppressWarnings("nls")
    @Override
    public final synchronized void start() {
        if (evolutionPipelineSubscr != null) {
            LOG.error("already started");
            return;
        }
        populations = PublishSubject.create();
        evolutionPipelineSubscr = startEvolution();
    }

    /**
     * Stop this genetic algorithm.
     * <br/><b>PRE-conditions:</b> this GA is running
     * <br/><b>POST-conditions:</b> this GA becomes stopped
     * <br/><b>Side-effects:</b> internal state is changed
     * <br/><b>Created on:</b> <i>4:52:29 AM Sep 6, 2016</i>
     * 
     * @see dburyak.jgatools.IGeneticAlgorithm#stop()
     */
    @SuppressWarnings("nls")
    @Override
    public final synchronized void stop() {
        if (evolutionPipelineSubscr == null) {
            LOG.error("already stopped");
            return;
        }
        evolutionPipelineSubscr.unsubscribe();
        evolutionPipelineSubscr = null;
        populations.onCompleted();
        populations = null;
    }


    /**
     * Project : jgatools.<br/>
     * Builder for {@link ReactiveGA} instance.
     * <br/><b>Created on:</b> <i>4:53:37 AM Sep 6, 2016</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     * @param <C>
     *            concrete chromosome implementation type for target GA
     * @param <P>
     *            concrete population implementation type for target GA
     */
    static final class ReactiveGABuilder<C extends IChromosome, P extends IPopulation<C>>
        implements
            IGeneticAlgorithmBuilder<C, P> {

        /**
         * Name for target GA.
         * <br/><b>Created on:</b> <i>4:55:04 AM Sep 6, 2016</i>
         */
        private String name = null;

        /**
         * Properties for target GA.
         * <br/><b>Created on:</b> <i>4:55:12 AM Sep 6, 2016</i>
         */
        private Properties props = null;

        /**
         * Termination condition for target GA.
         * <br/><b>Created on:</b> <i>4:55:22 AM Sep 6, 2016</i>
         */
        private ITerminationCondition<C, P> termCondition = null;

        /**
         * Appearance observable for target GA.
         * <br/><b>Created on:</b> <i>4:55:35 AM Sep 6, 2016</i>
         */
        private Observable<C> appearSource = null;

        /**
         * Population builder for target GA.
         * <br/><b>Created on:</b> <i>4:55:58 AM Sep 6, 2016</i>
         */
        private Supplier<IPopulationBuilder<C, P>> populationBuilder = null;

        /**
         * Mutation selector for target GA.
         * <br/><b>Created on:</b> <i>4:56:13 AM Sep 6, 2016</i>
         */
        private ISelectionPredicate<C> mutationSelector = null;

        /**
         * Mutation strategy for target GA.
         * <br/><b>Created on:</b> <i>4:56:27 AM Sep 6, 2016</i>
         */
        private IMutationStrategy<C> mutationFunc = null;

        /**
         * Crossover strategy for target GA.
         * <br/><b>Created on:</b> <i>4:56:50 AM Sep 6, 2016</i>
         */
        private ICrossoverStrategy<C> crossoverFunc = null;

        /**
         * Natural selection strategy for target GA.
         * <br/><b>Created on:</b> <i>4:57:07 AM Sep 6, 2016</i>
         */
        private ISelectionStrategy<C> selectionFunc = null;

        /**
         * Selector for first parent for crossover for target GA.
         * <br/><b>Created on:</b> <i>5:12:39 AM Sep 6, 2016</i>
         */
        private ISelectionPredicate<C> parent1Selector = null;

        /**
         * Mates selector for crossover for target GA.
         * <br/><b>Created on:</b> <i>5:15:12 AM Sep 6, 2016</i>
         */
        private IMatesSelector<C> matesSelector = null;

        /**
         * Mates selection timeout for detection conflicting configuration.
         * TODO : make this configurable
         * <br/><b>Created on:</b> <i>5:21:16 AM Sep 6, 2016</i>
         */
        private Duration matesSelectTimeout = Duration.ofSeconds(2);

        /**
         * Size of the "buffer" intermediate chromosomes container.
         * <br/><b>Created on:</b> <i>5:34:54 AM Sep 6, 2016</i>
         */
        private int bufferSize = -1;


        /**
         * Constructor for class : [jgatools] dburyak.jgatools.impl.ReactiveGABuilder.<br/>
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>4:57:27 AM Sep 6, 2016</i>
         */
        ReactiveGABuilder() {
            props = new Properties();
        }

        /**
         * Build target genetic algorithm.
         * <br/><b>PRE-conditions:</b> all necessary parameters were specified
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>5:08:18 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#build()
         * @return new instance of {@link ReactiveGA} that is configured from current builder state
         * @throws IllegalStateException
         *             if wrong configuration was detected
         */
        @Override
        public final IGeneticAlgorithm<C, P> build() throws IllegalStateException {
            if (!isValid()) {
                throw new IllegalStateException();
            }
            return new ReactiveGA<>(name,
                props,
                termCondition,
                appearSource,
                mutationSelector,
                mutationFunc,
                parent1Selector,
                matesSelector,
                crossoverFunc,
                matesSelectTimeout,
                selectionFunc,
                bufferSize,
                populationBuilder);
        }

        /**
         * Check if current builder state is valid and target GA can be built.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>5:28:23 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.InstanceBuilder#isValid()
         * @return true if builder is valid and GA can be built, false otherwise
         */
        @Override
        public final boolean isValid() {
            if (name == null || props == null) {
                return false;
            }
            if (termCondition == null) {
                return false;
            }
            if (appearSource == null) {
                return false;
            }
            if (populationBuilder == null) {
                return false;
            }
            if (mutationSelector == null) {
                return false;
            }
            if (mutationFunc == null) {
                return false;
            }
            if (crossoverFunc == null) {
                return false;
            }
            if (selectionFunc == null) {
                return false;
            }
            if (parent1Selector == null) {
                return false;
            }
            if (matesSelector == null) {
                return false;
            }
            if (matesSelectTimeout == null) {
                return false;
            }
            if (bufferSize <= 0) {
                return false;
            }
            return true;
        }

        /**
         * Set name for target GA.
         * <br/><b>PRE-conditions:</b> non-empty name
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:29:27 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.INameable#name(java.lang.String)
         * @param name
         *            name for target GA
         * @return previous name
         */
        @SuppressWarnings("hiding")
        @Override
        public final String name(final String name) {
            Validators.nonEmpty(name);
            final String prevName = name;
            this.name = name;
            return prevName;
        }

        /**
         * Get name for target GA.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>5:30:33 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.INamed#name()
         * @return current name for target GA
         */
        @Override
        public final String name() {
            return name;
        }

        /**
         * Set property for target GA.
         * <br/><b>PRE-conditions:</b> non-empty key, non-null value
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:31:03 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.IConfigurable#property(java.lang.String, java.lang.String)
         * @param key
         *            key for property
         * @param value
         *            property value
         * @return previous property value
         */
        @Override
        public final String property(final String key, final String value) {
            Validators.nonEmpty(key);
            Validators.nonNull(value);
            // seems like an API flaw, cast looks ugly but should work in any case
            return (String) props.setProperty(key, value);
        }

        /**
         * Remove property from target GA.
         * <br/><b>PRE-conditions:</b> non-empty key
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:32:16 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.IConfigurable#removeProperty(java.lang.String)
         * @param key
         *            property key
         * @return previous value of property, or null if association wasn't established
         */
        @Override
        public final String removeProperty(final String key) {
            Validators.nonEmpty(key);
            // seems like an API flaw, cast looks ugly but should work in any case
            return (String) props.remove(key);
        }

        /**
         * Get property for given key.
         * <br/><b>PRE-conditions:</b> non-empty key
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>5:33:28 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jtools.IConfigured#property(java.lang.String)
         * @param key
         *            property key
         * @return property value, or null if ther's no such property
         */
        @Override
        public final String property(final String key) {
            Validators.nonEmpty(key);
            return props.getProperty(key);
        }

        /**
         * Set termination condition function for target GA.
         * <br/><b>PRE-conditions:</b> non-null termCondition
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:34:14 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#termCondition(dburyak.jgatools.ITerminationCondition)
         * @param termCondition
         *            GA termination condition
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> termCondition(final ITerminationCondition<C, P> termCondition) {
            Validators.nonNull(termCondition);
            this.termCondition = termCondition;
            return this;
        }

        /**
         * Set {@code Observable} that produces initial chromosomes "out of nowhere" for target GA.
         * <br/><b>PRE-conditions:</b> non-null appearSource
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:35:44 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#appearSource(rx.Observable)
         * @param appearSource
         *            source of initial chromosomes
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> appearSource(final Observable<C> appearSource) {
            Validators.nonNull(appearSource);
            this.appearSource = appearSource;
            return this;
        }

        /**
         * Set population builder producer to be used in target GA for producing new population on each evolution
         * iteration.
         * <br/><b>PRE-conditions:</b> non-null populationBuilder
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:37:50 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#populationBuilder(java.util.function.Supplier)
         * @param populationBuilder
         *            supplier that produces population builder
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> populationBuilder(
            final Supplier<IPopulationBuilder<C, P>> populationBuilder) {

            Validators.nonNull(populationBuilder);
            this.populationBuilder = populationBuilder;
            return this;
        }

        /**
         * Set mutation selector function for target GA.
         * <br/><b>PRE-conditions:</b> non-null mutationSelector
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:40:38 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#mutationSelector(dburyak.jgatools.ISelectionPredicate)
         * @param mutationSelector
         *            mutation selector function to be used in target GA
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> mutationSelector(final ISelectionPredicate<C> mutationSelector) {
            Validators.nonNull(mutationSelector);
            this.mutationSelector = mutationSelector;
            return this;
        }

        /**
         * Set mutation strategy for target GA.
         * <br/><b>PRE-conditions:</b> non-null mutationFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:41:47 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#mutationFunc(dburyak.jgatools.IMutationStrategy)
         * @param mutationFunc
         *            mutation function for target GA
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> mutationFunc(final IMutationStrategy<C> mutationFunc) {
            Validators.nonNull(mutationFunc);
            this.mutationFunc = mutationFunc;
            return this;
        }

        /**
         * Set crossover strategy for target GA.
         * <br/><b>PRE-conditions:</b> non-null crossoverFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:44:53 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#crossoverFunc(dburyak.jgatools.ICrossoverStrategy)
         * @param crossoverFunc
         *            crossover strategy for target GA
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> crossoverFunc(final ICrossoverStrategy<C> crossoverFunc) {
            Validators.nonNull(crossoverFunc);
            this.crossoverFunc = crossoverFunc;
            return this;
        }

        /**
         * Set natural selection strategy for target GA.
         * <br/><b>PRE-conditions:</b> non-null selectionFunc
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:45:55 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#selectionFunc(dburyak.jgatools.ISelectionStrategy)
         * @param selectionFunc
         *            natural selection strategy for target GA
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> selectionFunc(final ISelectionStrategy<C> selectionFunc) {
            Validators.nonNull(selectionFunc);
            this.selectionFunc = selectionFunc;
            return this;
        }

        /**
         * Set selector for the first parent for crossover operation.
         * <br/><b>PRE-conditions:</b> non-null parent1Selector
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:47:02 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#parent1Selector(dburyak.jgatools.ISelectionPredicate)
         * @param parent1Selector
         *            selector for choosing first parent for crossover
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> parent1Selector(final ISelectionPredicate<C> parent1Selector) {
            Validators.nonNull(parent1Selector);
            this.parent1Selector = parent1Selector;
            return this;
        }

        /**
         * Set mates selector for crossover operation for target GA.
         * <br/><b>PRE-conditions:</b> non-null matesSelector
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:48:09 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#matesSelector(dburyak.jgatools.IMatesSelector)
         * @param matesSelector
         *            selector for choosing other mates for crossover
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> matesSelector(final IMatesSelector<C> matesSelector) {
            Validators.nonNull(matesSelector);
            this.matesSelector = matesSelector;
            return this;
        }

        /**
         * Set size of "buffer" intermediate chromosomes container.
         * <br/><b>PRE-conditions:</b> bufferSize &gt 0
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> state is changed
         * <br/><b>Created on:</b> <i>5:49:14 AM Sep 6, 2016</i>
         * 
         * @see dburyak.jgatools.IGeneticAlgorithm.IGeneticAlgorithmBuilder#bufferSize(int)
         * @param bufferSize
         *            size of "buffer" container
         * @return this builder (for call chaining)
         */
        @SuppressWarnings("hiding")
        @Override
        public final IGeneticAlgorithmBuilder<C, P> bufferSize(final int bufferSize) {
            Validators.positive(bufferSize);
            this.bufferSize = bufferSize;
            return null;
        }

    }

}
