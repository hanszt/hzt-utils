package hzt.function.predicates;

import org.junit.jupiter.api.Test;

import static hzt.function.predicates.CollectionPredicates.containsAll;
import static hzt.function.predicates.CollectionPredicates.containsAny;
import static hzt.function.predicates.CollectionPredicates.containsNone;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CollectionPredicatesTest {

    @Test
    void testContainsAll() {
        assertNotNull(containsAll());
    }

    @Test
    void testContainsAny() {
        assertNotNull(containsAny());
    }

    @Test
    void testContainsNone() {
        assertNotNull(containsNone());
    }
}
