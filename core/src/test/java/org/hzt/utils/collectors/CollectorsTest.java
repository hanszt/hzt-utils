package org.hzt.utils.collectors;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hzt.demo.function.IntegerFunctions.mod;
import static org.hzt.utils.collectors.Collectors.groupBy;

class CollectorsTest {

    @Test
    void testCollectGroupBy() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        final Map<Integer, List<Integer>> grouped = Sequence.of(list).collect(groupBy(mod(3)));

        assertThat(grouped).containsEntry(0, Arrays.asList(3, 6, 9));
    }

}