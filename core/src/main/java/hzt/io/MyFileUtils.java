package hzt.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static hzt.stream.StreamUtils.by;
import static hzt.function.predicates.ComparingPredicates.collectionGreaterThan;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.*;

public final class MyFileUtils {

    private MyFileUtils() {
    }

    /**
     * @param start the start location in the file tree to walk along al files down
     * @param pathFilters optional extra filters for the paths found during file walking
     * @return first map which has as key the duplicate message and as value first list with the paths to the duplicate messages
     */
    @SafeVarargs
    public static Map<String, List<Path>> findFilesWithDuplicateContent(Path start, Predicate<Path>... pathFilters) {
        Predicate<Path> combinedPathFilter = Stream.of(pathFilters).reduce(e -> true, Predicate::and);
        try (final Stream<Path> walk = Files.walk(start)) {
            return walk.filter(by(Path::toFile, File::isFile))
                    .filter(combinedPathFilter)
                    .collect(groupingBy(MyFileUtils::loadFileAsTrimmedString))
                    .entrySet()
                    .stream()
                    .filter(by(Entry::getValue, collectionGreaterThan(1)))
                    .collect(toMap(Entry::getKey, Entry::getValue));
        } catch (IOException e) {
            throw new IllegalStateException(start + " not found...", e);
        }
    }

    public static String loadFileAsTrimmedString(Path path) {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
            return lines.map(String::trim)
                    .collect(joining());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
}
