package dburyak.jgatools;


import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import rx.Observable;
import rx.Subscriber;


/**
 * Project : jgatools.<br>
 * <br><b>Created on:</b> <i>6:13:53 PM Sep 12, 2016</i>
 * 
 * @author <i>Dmytro Buryak &lt;dmytro.buryak@gmail.com&gt;</i>
 * @version 0.1
 */
public final class Gaussian {

    private static final Logger LOG = LogManager.getFormatterLogger(Gaussian.class);

    private static final int NUM = 500000;


    /**
     * <br><b>PRE-conditions:</b>
     * <br><b>POST-conditions:</b>
     * <br><b>Side-effects:</b>
     * <br><b>Created on:</b> <i>6:13:53 PM Sep 12, 2016</i>
     * 
     * @param args
     */
    public static void main(String[] args) {
        final Random rnd = new Random(System.currentTimeMillis());
        final double min = 0.0D;
        final double max = 1.0D;
        final int bins = 100;
        final double mean = 0.5D;
        final double factor = 0.18D;

        final DoubleStream values = DoubleStream.generate(rnd::nextGaussian)
            .map(d -> {
                return (d * factor) + mean;
            })
            .filter(d -> Double.compare(min, d) <= 0 && Double.compare(d, max) <= 0);

        Observable.from(values::iterator)
            .take(NUM)
            .subscribe(new Subscriber<Double>() {

                private final HistogramDataset dataset = new HistogramDataset();
                private final List<Double> values = new ArrayList<>((int) (NUM * 1.1D));


                private final void showChart() {
                    final JFreeChart chart = ChartFactory.createHistogram("Gaussian", "values", "density", dataset,
                        PlotOrientation.VERTICAL, true, true, false);
                    final ChartPanel panel = new ChartPanel(chart);
                    SwingUtilities.invokeLater(() -> {
                        final JFrame frame = new JFrame("gaussian");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.getContentPane().add(panel);
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        // frame.pack();
                        frame.setVisible(true);
                    });
                }

                @Override
                public final void onCompleted() {
                    final DoubleSummaryStatistics stats = values.stream().mapToDouble(Double::doubleValue)
                        .summaryStatistics();
                    final double avg = stats.getAverage();
                    final DoubleSummaryStatistics devStats = values.stream().mapToDouble(Double::doubleValue)
                        .map(d -> Math.abs(avg - d)) // get deviation
                        .summaryStatistics();

                    LOG.info("STATS :\n\tmax = [%f]\n\tmin = [%f]\n\tavg = [%f]\n\tdev = [%f]",
                        stats.getMax(), stats.getMin(), avg, devStats.getAverage());

                    final double[] valuesArr = values.stream().mapToDouble(Double::doubleValue).toArray();
                    dataset.addSeries("mean = " + mean + "factor = " + factor, valuesArr, bins, min, max);

                    showChart();
                }

                @Override
                public final void onError(final Throwable e) {
                    LOG.error("got error when generating values : e = [%s]", e);
                }

                @Override
                public final void onNext(final Double value) {
                    values.add(value);
                }

            });
    }

}
