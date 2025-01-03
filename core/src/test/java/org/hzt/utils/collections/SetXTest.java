package org.hzt.utils.collections;

import org.hzt.test.model.Painting;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SetXTest {

    @Test
    void testToSetYieldsUnModifiableSet() {
        final var auction = Generator.createVanGoghAuction();
        final var yearToAdd = Year.of(2000);

        final var years = auction.toSetOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void testSetXCanNotBeCastToMutableSetX() {
        final var setX = SetX.of(1, 2, 3, 4, 5, 10);
        //noinspection RedundantClassCall
        assertAll(
                () -> assertThrows(ClassCastException.class, () -> MutableSetX.class.cast(setX)),
                () -> assertEquals(6, setX.size())
        );
    }

    @Nested
    class EqualsTests {

        @Test
        void testSetXEquals() {
            final var set1 = SetX.of("This", "is", "a", "test");
            final var set2 = SetX.of("is", "This", "a", "test");

            assertAll(
                    () -> assertEquals(set2, set1),
                    () -> assertEquals(set1, set2)
            );
        }

        @Test
        void testSetXAndSetDoNotEqual() {
            final var setX = SetX.of("This", "is", "a", "test");
            final var set = Set.of("This", "is", "a", "test");

            assertAll(
                    () -> assertNotEquals(set, setX),
                    () -> assertNotEquals(setX, set)
            );
        }
    }

    @Nested
    class BuildSetTests {

        @Test
        void testBuildSet() {
            final var strings = SetX.build(this::getStringSet);

            final var iterator = strings.iterator();
            iterator.next();

            assertAll(
                    () -> assertEquals(100, strings.size()),
                    () -> assertThat(strings).contains("42"),
                    () -> assertThrows(UnsupportedOperationException.class, () -> ((MutableSetX<String>) strings).add("add")),
                    () -> assertThrows(UnsupportedOperationException.class, () -> iterator.remove())
            );
        }

        @Test
        void testBuildSizedSet() {
            final var reference = new AtomicReference<MutableSetX<String>>();
            final var strings = SetX.<String>build(100, set -> {
                getStringSet(set);
                reference.set(set);
            });

            assertAll(
                    () -> assertEquals(100, strings.size()),
                    () -> assertThat(strings).contains("42"),
                    () -> assertThrows(UnsupportedOperationException.class, () -> ((MutableSetX<String>) strings).add("add")),
                    () -> assertThrows(UnsupportedOperationException.class, () -> reference.get().add("add"))
            );
        }

        private void getStringSet(final MutableSetX<String> set) {
            for (var i = 0; i < 100; i++) {
                set.add(String.valueOf(i));
            }
        }
    }
}
