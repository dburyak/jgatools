package dburyak.jgatools;


import javax.annotation.concurrent.Immutable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.Validators;


/**
 * Project : jgatools.<br/>
 * Normalized and directional fitness value. Usage of this object ensures uniform fitness handling.<br/>
 * Fitness encapsulates a double "value" such that:
 * <ul>
 * <li> 0.0 &lt= value &lt= 1.0
 * <li> chromosome with fitness f1 is more fittest than chromosome with fitness f2 if {@code f1.value()} &gt
 * {@code f2.value()}
 * <li> chromosome with fitness f1 is less fittest than chromosome with fitness f2 if {@code f1.value()} &lt
 * {@code f2.value()}
 * <li> chromosomes f1 and f2 are equally fittest if {@code f1.value()} == {@code f2.value()}
 * </ul>
 * <br/><b>Created on:</b> <i>12:35:46 AM Sep 1, 2016</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
public final class Fitness implements Comparable<Fitness> {

    /**
     * Default system logger.
     * <br/><b>Created on:</b> <i>7:00:00 PM Sep 3, 2016</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(Fitness.class);

    /**
     * Lower fitness value bound.
     * <br/><b>Created on:</b> <i>5:02:03 PM Sep 2, 2016</i>
     */
    private static final double MIN_VALUE = 0.0;

    /**
     * Upper fitness value bound.
     * <br/><b>Created on:</b> <i>5:02:29 PM Sep 2, 2016</i>
     */
    private static final double MAX_VALUE = 1.0;

    /**
     * Minimal possible fitness.
     * <br/><b>Created on:</b> <i>11:18:23 PM Sep 2, 2016</i>
     */
    private static final Fitness MIN = new Fitness(MIN_VALUE);

    /**
     * Maximum possible fitness.
     * <br/><b>Created on:</b> <i>11:18:34 PM Sep 2, 2016</i>
     */
    private static final Fitness MAX = new Fitness(MAX_VALUE);

    /**
     * Internal boolean fitness value that is managed by this class.
     * <br/><b>Created on:</b> <i>11:18:43 PM Sep 2, 2016</i>
     */
    private final double value;


    /**
     * Validator method for "value" argument.
     * <br/><b>PRE-conditions:</b> not a NaN {@code value} that is {@code MIN_VALUE} &lt= {@code value} &lt=
     * {@code MAX_VALUE}
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:37:30 PM Sep 2, 2016</i>
     * 
     * @param value
     *            {@code value} argument to be validated
     * @return true if {@code value} is valid
     * @throws IllegalArgumentException
     *             if @{code value} is invalid
     */
    @SuppressWarnings("nls")
    private static final boolean validateValue(final double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("value is NaN");
        }
        final int cmpMin = Double.compare(value, MIN_VALUE);
        if (cmpMin < 0) { // value < MIN_VALUE
            throw new IllegalArgumentException("value < MIN_VALUE");
        }
        final int cmpMax = Double.compare(value, MAX_VALUE);
        if (cmpMax > 0) { // value > MAX_VALUE
            throw new IllegalArgumentException("value > MAX_VALUE");
        }
        return true;
    }

    /**
     * Get min fitness double value to be used in equations.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result == 0.0
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:39:45 PM Sep 2, 2016</i>
     * 
     * @return min fitness double value
     */
    public static final double minValue() {
        return MIN_VALUE;
    }

    /**
     * Get max fitness double value to be used in equations.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result == 1.0
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:41:04 PM Sep 2, 2016</i>
     * 
     * @return max fitness double value
     */
    public static final double maxValue() {
        return MAX_VALUE;
    }

    /**
     * Get minimal possible fitness.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result with {@link #value()} == {@link #maxValue()}
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:41:51 PM Sep 2, 2016</i>
     * 
     * @return min possible fitness
     */
    public static final Fitness min() {
        return MIN;
    }

    /**
     * Get max possible fitness.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result with {@link #value()} == {@link #maxValue()}
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:48:49 PM Sep 2, 2016</i>
     * 
     * @return max possible fitness
     */
    public static final Fitness max() {
        return MAX;
    }

    /**
     * Constructor for class : [jgatools] dburyak.jgatools.Fitness.<br/>
     * <br/><b>PRE-conditions:</b> 0.0 &lt= value &lt= 1.0
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:24:40 PM Sep 2, 2016</i>
     * 
     * @param value
     *            fitness value to be used for this fitness
     */
    public Fitness(final double value) {
        validateValue(value);
        this.value = value;
    }

    /**
     * Comparator method. Only internal fitness double values are taken in account for comparison.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:50:21 PM Sep 2, 2016</i>
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param other
     *            other fitness to be compared with
     * @return standard comparison result: result &lt 0 - this fitness is less than {@code other}; result == 0 - this
     *         fitness is equal to {@code other}; result &gt 0 - this fitness is greater than {@code other}
     */
    @Override
    public final int compareTo(final Fitness other) {
        Validators.nonNull(other);
        assert (validateValue(this.value));
        assert (validateValue(other.value));
        return Double.compare(this.value, other.value);
    }

    /**
     * Get fitness double value to be used in equations.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> {@link #minValue()} &lt= result &lt= {@link #maxValue()}
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:55:50 PM Sep 2, 2016</i>
     * 
     * @return fitness double value of this fitness
     */
    public final double value() {
        assert (validateValue(value));
        return value;
    }

    /**
     * Equality check takes in account only internal fitness value. Two different instances with same fitness value are
     * considered equal.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>6:57:54 PM Sep 3, 2016</i>
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param other
     *            other object to check for equality with
     * @return true if other has same fitness value
     */
    @SuppressWarnings({ "boxing", "nls" })
    @Override
    public final boolean equals(final Object other) {
        if (other == null) {
            LOG.warn("other is null");
            return false;
        }
        if (!(other instanceof Fitness)) {
            LOG.warn("other is not instance of Fitness : class = [%s]", other.getClass().getCanonicalName());
            return false;
        }
        return Double.valueOf(value).equals(((Fitness) other).value);
    }

    /**
     * Calculates hash code of internal fitness value. Two different instances with same fitness value will return same
     * hash codes.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>6:57:59 PM Sep 3, 2016</i>
     * 
     * @see java.lang.Object#hashCode()
     * @return hash code of internal fitness value
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    /**
     * Get string representation of this fitness. Internal fitness double value is converted to string.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:12:24 AM Sep 4, 2016</i>
     * 
     * @see java.lang.Object#toString()
     * @return string representation of double internal fitness value
     */
    @Override
    public final String toString() {
        return Double.toString(value);
    }

}
