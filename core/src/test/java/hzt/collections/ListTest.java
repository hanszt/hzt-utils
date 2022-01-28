package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void testEqualTestForClassTypeISDifferentForInstanceOfAndGetClassEqualsSign() {
        Object list = new ArrayList<Integer>();
        Object other = new LinkedList<Integer>();
        assertTrue(list instanceof List<?>);
        assertTrue(other instanceof List<?>);
        assertNotSame(list.getClass(), other.getClass());
    }
}
