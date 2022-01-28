package hzt.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static hzt.stream.StreamUtils.by;
import static hzt.stream.StreamUtils.function;
import static hzt.function.predicates.StringPredicates.containsNoneOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyFileUtilsTest {

    private static Predicate<Path> containsNonOf(String... strings) {
        return by(function(Path::toFile).andThen(File::getPath), containsNoneOf(strings));
    }

    @Test
    void testFindFilesWithDuplicateContent() {
        final Map<String, List<Path>> filesWithDuplicateContent = MyFileUtils.findFilesWithDuplicateContent(
                Paths.get(""),
                containsNonOf(".git", "target", "sonarlint", ".iml"));
        assertTrue(filesWithDuplicateContent.isEmpty(), () -> errorMessage(filesWithDuplicateContent.values()));
    }

    private String errorMessage(Collection<List<Path>> values) {
        return values.stream().mapToInt(List::size).sum() + " duplicate files found: " + values;
    }
}
