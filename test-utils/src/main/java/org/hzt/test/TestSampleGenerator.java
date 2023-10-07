package org.hzt.test;

import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Book;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public final class TestSampleGenerator {

    private static final String FICTION = "Fiction";

    private static final class Holder {
        private static final List<Museum> museums = createMuseumList();
    }

    private TestSampleGenerator() {
    }

    public static List<Book> createBookList() {
        return List.of(new Book("Harry Potter", FICTION),
                new Book("Lord of the Rings", FICTION),
                new Book("Pragmatic Programmer", "Programming"),
                new Book("OCP 11 Volume 1", "Programming"),
                new Book("Homo Deus", "Educational"),
                new Book("The da Vinci Code", FICTION),
                new Book("The da Vinci Code", FICTION));
    }

    public static List<Painting> createPaintingList() {
        final var picasso = new Painter("Pablo", "Picasso", LocalDate.of(1881, 10, 25));
        picasso.setDateOfDeath(LocalDate.of(1973, 4, 9));
        final var vermeer = new Painter("Johannes", "Vermeer", LocalDate.of(1632, 10, 31));
        vermeer.setDateOfDeath(LocalDate.of(1675, 12, 1));
        final var vanGogh = new Painter("Vincent", "van Gogh", LocalDate.of(1853, 3,20));
        vanGogh.setDateOfDeath(LocalDate.of(1890, 7, 29));

        final var guernica = new Painting("Guernica", picasso, Year.of(1937), true);
        final var lesDemoiselles = new Painting("Les Demoiselles d'Avignon", picasso, Year.of(1907), true);
        final var leReve = new Painting("Le Rêve", picasso, Year.of(1932), true);
        final var meisjeMetDeParel = new Painting("Meisje met de parel", vermeer, Year.of(1665), true);
        final var hetMelkmeisje = new Painting("Het melkmeisje", vermeer, Year.of(1658), true);
        final var meisjeMetDeRodeHoed = new Painting("Meisje met de rode hoed", vermeer, Year.of(1665), true);
        final var lenteTuin = new Painting("Lentetuin, de pastorietuin te Nuenen in het voorjaar", vanGogh, Year.of(1884), false);
        final var deSterrennacht = new Painting("De sterrennacht", vanGogh, Year.of(1889), true);

        picasso.addPaintings(guernica, lesDemoiselles, leReve);
        vermeer.addPaintings(meisjeMetDeParel, meisjeMetDeRodeHoed, hetMelkmeisje);
        vanGogh.addPaintings(lenteTuin, deSterrennacht);

        return List.of(guernica, lesDemoiselles, leReve, meisjeMetDeParel, hetMelkmeisje,
                meisjeMetDeRodeHoed, lenteTuin, deSterrennacht
        );
    }

    public static List<Museum> getMuseumListContainingNulls() {
        return Holder.museums;
    }

    public static List<Museum> createMuseumList() {

        final var groupedByLastName = createPaintingList().stream()
                .collect(Collectors.groupingBy(painting -> painting.painter().getLastname()));

        final var vanGoghPaintings = groupedByLastName.get("van Gogh");
        final var vermeerPaintings = groupedByLastName.get("Vermeer");
        final var picassoPaintings = groupedByLastName.get("Picasso");

        final var painter = new Painter("Hans", "Zuidervaart", LocalDate.of(1989, 10 ,18));
        return List.of(
                new Museum(null, null, List.of(new Painting("Test", painter, Year.of(1997), false))),
                new Museum("Van Gogh Museum", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings),
                new Museum("Vermeer Museum", LocalDate.of(1940, Month.JANUARY, 23), vermeerPaintings),
                new Museum("Picasso Museum", LocalDate.of(1965, Month.AUGUST, 4), picassoPaintings));
    }

    public static List<BankAccount> createSampleBankAccountList() {
        return List.of(
                new BankAccount("NL43INGB0008541648", new Customer("1", "Zuidervaart"), BigDecimal.valueOf(-4323)),
                new BankAccount("BL54ABNA0004536472", new Customer("2", "Jansen"), BigDecimal.valueOf(234235.34)),
                new BankAccount("NL32BUNQ0004358592", new Customer("3", "Vullings"), BigDecimal.valueOf(2342)),
                new BankAccount("NL32INGB0004524542", new Customer("8", "Burgmeijer"), BigDecimal.valueOf(23)),
                new BankAccount("NL65RABO00004342356", new Customer("22", "Claassen"), BigDecimal.valueOf(234))
        );
    }

    public static List<BankAccount> createSampleBankAccountListContainingNulls() {
        final List<BankAccount> bankAccounts = new ArrayList<>(createSampleBankAccountList());
        bankAccounts.add(null);
        bankAccounts.add(new BankAccount("", null, null));
        bankAccounts.add(new BankAccount("Test", new Customer("", "", Collections.emptyList()), null));
        return bankAccounts;
    }

    public static DoubleStream gaussianDoubles(final int amount,
                                               final double targetMean,
                                               final double targetStdDev,
                                               final Random random) {
        return IntStream.range(0, amount)
                .mapToDouble(i -> targetMean + targetStdDev * random.nextGaussian());
    }

    public static Map<String, Museum> createMuseumMap() {
        return createMuseumList().stream()
                .filter(e -> e.getName() != null)
                .collect(Collectors.toUnmodifiableMap(Museum::getName, Function.identity()));
    }

    public static List<String> getEnglishNameList() {
        final var name = "/english_names.txt";
        final var path = Optional.ofNullable(TestSampleGenerator.class.getResource(name))
                .map(URL::getFile)
                .map(File::new)
                .map(File::toPath)
                .orElseThrow(() -> new NoSuchElementException("Could not find resource " + name));

        try (final var s = Files.lines(path)) {
            return s.toList();
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
