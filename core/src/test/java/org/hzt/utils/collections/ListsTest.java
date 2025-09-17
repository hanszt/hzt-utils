package org.hzt.utils.collections;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.hzt.utils.collections.Lists.nthSmallest;

class ListsTest {

    @Test
    void testFindMedianUsingQuickSelect() {
        final var rnd = new Random();
        for (int size = 1; size <= 10; size++) {
            final var list = rnd.ints(size, 0, 100).boxed().toList();
            System.out.println("Median of " + list + " is " + median(list, Comparator.naturalOrder()));
        }
    }

    /**
     * @param nrs a list of Comparable nrs
     * @return the median of the nrs
     */
    private static <T extends Number> double median(List<T> nrs, Comparator<T> comp) {
        final var n = nrs.size() / 2;
        // even number of items; find the middle two and average them
        return nrs.size() % 2 == 0 ? (nthSmallest(n - 1, comp, nrs).doubleValue() + nthSmallest(n, comp, nrs).doubleValue()) / 2.0 :
                // odd number of items; return the one in the middle
                nthSmallest(n, comp, nrs).doubleValue();
    }
}
