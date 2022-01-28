package org.hzt.test;

import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Book;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public final class TestSampleGenerator {

    private static final Random RANDOM = new Random();
    private static final String FICTION = "Fiction";

    private static final List<Function<Integer, Number>> TO_NUMBER_TYPE_FUNCTIONS = List.of(
            Integer::byteValue,
            Integer::shortValue,
            Integer::valueOf,
            Float::valueOf,
            Long::valueOf,
            Double::valueOf,
            BigInteger::valueOf,
            BigDecimal::valueOf,
            AtomicInteger::new,
            AtomicLong::new
    );

    private static List<Museum> museums;

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
        final Painter picasso = new Painter("Pablo", "Picasso", LocalDate.of(1881, 10, 25));
        picasso.setDateOfDeath(LocalDate.of(1973, 4, 9));
        final Painter vermeer = new Painter("Johannes", "Vermeer", LocalDate.of(1632, 10, 31));
        vermeer.setDateOfDeath(LocalDate.of(1675, 12, 1));
        final Painter vanGogh = new Painter("Vincent", "van Gogh", LocalDate.of(1853, 3,20));
        vanGogh.setDateOfDeath(LocalDate.of(1890, 7, 29));

        final Painting guernica = new Painting("Guernica", picasso, Year.of(1937), true);
        final Painting lesDemoiselles = new Painting("Les Demoiselles d'Avignon", picasso, Year.of(1907), true);
        final Painting le_reve = new Painting("Le Rêve", picasso, Year.of(1932), true);
        final Painting meisje_met_de_parel = new Painting("Meisje met de parel", vermeer, Year.of(1665), true);
        final Painting het_melkmeisje = new Painting("Het melkmeisje", vermeer, Year.of(1658), true);
        final Painting meisje_met_de_rode_hoed = new Painting("Meisje met de rode hoed", vermeer, Year.of(1665), true);
        final Painting lenteTuin = new Painting("Lentetuin, de pastorietuin te Nuenen in het voorjaar", vanGogh, Year.of(1884), false);
        final Painting de_sterrennacht = new Painting("De sterrennacht", vanGogh, Year.of(1889), true);

        picasso.addPaintings(guernica, lesDemoiselles, le_reve);
        vermeer.addPaintings(meisje_met_de_parel, meisje_met_de_rode_hoed, het_melkmeisje);
        vanGogh.addPaintings(lenteTuin, de_sterrennacht);

        return List.of(guernica, lesDemoiselles, le_reve, meisje_met_de_parel, het_melkmeisje,
                meisje_met_de_rode_hoed, lenteTuin, de_sterrennacht
        );
    }

    public static List<Museum> getMuseumListContainingNulls() {
        if (museums == null) {
            museums = createMuseumList();
        }
        return museums;
    }

    public static List<Museum> createMuseumList() {

        final var groupedByLastName = createPaintingList().stream()
                .collect(Collectors.groupingBy(painting -> painting.painter().getLastname()));

        final List<Painting> vanGoghPaintings = groupedByLastName.get("van Gogh");
        final List<Painting> vermeerPaintings = groupedByLastName.get("Vermeer");
        final List<Painting> picassoPaintings = groupedByLastName.get("Picasso");

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
        bankAccounts.add(new BankAccount("", null, null));
        bankAccounts.add(new BankAccount("Test", new Customer("", "", Collections.emptyList()), null));
        return bankAccounts;
    }

    public static List<Number> createRandomNumberTypeList(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(TestSampleGenerator::toRandomNumberType)
                .toList();
    }

    private static Number toRandomNumberType(int integer) {
        return TO_NUMBER_TYPE_FUNCTIONS.get(RANDOM.nextInt(TO_NUMBER_TYPE_FUNCTIONS.size())).apply(integer);
    }

    public static List<Number> createNumberTypeList(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(TestSampleGenerator::toNumberType)
                .toList();
    }

    public static DoubleStream gaussianDoubles(int amount, double targetMean, double targetStdDev) {
        return IntStream.range(0, amount)
                .mapToDouble(i -> RANDOM.nextGaussian(targetMean, targetStdDev));
    }

    private static Number toNumberType(int integer) {
        return TO_NUMBER_TYPE_FUNCTIONS.get(integer % TO_NUMBER_TYPE_FUNCTIONS.size()).apply(integer);
    }

    public static Map<String, Museum> createMuseumMap() {
        return createMuseumList().stream()
                .filter(e -> e.getName() != null)
                .collect(Collectors.toUnmodifiableMap(Museum::getName, Function.identity()));
    }
}
