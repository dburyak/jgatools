package dburyak.jgatools;


import java.util.Random;

import rx.Observable;


/**
 * Project : jgatools.<br>
 * <br><b>Created on:</b> <i>11:17:35 PM Sep 12, 2016</i>
 * 
 * @author <i>Dmytro Buryak &lt;dmytro.buryak@gmail.com&gt;</i>
 * @version 0.1
 */
public final class Sorted {

    /**
     * <br><b>PRE-conditions:</b>
     * <br><b>POST-conditions:</b>
     * <br><b>Side-effects:</b>
     * <br><b>Created on:</b> <i>11:17:35 PM Sep 12, 2016</i>
     * 
     * @param args
     */
    public static void main(String[] args) {
        final Random rnd = new Random(System.currentTimeMillis());
        Observable.from(rnd.ints()::iterator)
            .take(50)
            .sorted((i1, i2) -> -Integer.compare(i1, i2))
            .subscribe(System.out::println);
    }

}
