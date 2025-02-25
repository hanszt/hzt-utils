package org.hzt.utils.function.predicates;

import org.junit.jupiter.api.Test;

import static org.hzt.utils.function.predicates.CollectionPredicates.*;
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
