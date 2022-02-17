package hzt.iterables;

import hzt.collections.ListView;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SkipableTest {

    @Test
    void testSkip() {
        final ListView<Museum> museumList = ListView.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = museumList.stream().skip(3).collect(Collectors.toList());

        final ListView<Museum> actual = museumList.skip(3);

        It.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

}
