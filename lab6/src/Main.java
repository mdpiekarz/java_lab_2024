import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        CustomList<Integer> list = new CustomList();
        list.addLast(2);
        list.addLast(4);
        list.addLast(6);
        list.add(-8);
        System.out.println(list.get(2));

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        System.out.println(list);

        Stream<Integer> stream = list.stream();
        stream.map(element -> {
            return element + 5;
        }).forEach(element -> {
            System.out.println(element);
        });

        CustomList<Integer> filteredList = CustomList.filterByClass(list, Integer.class);
        //CustomList<Integer> filteredList = CustomList.filterByClass(list, String.class);

        filteredList
                .stream()
                .map(o -> o + 10)
                .forEach(o-> System.out.println(o));


        /////

        // Przykładowe granice przedziału
        Integer lowerBound = 5;
        Integer upperBound = 10;

        // Wywołanie metody isInOpenRange, aby utworzyć predykat dla określonego przedziału
        Predicate<Integer> inRangePredicate = CustomList.isInOpenRange(lowerBound, upperBound);

        // Wywołanie metody countElementsInRange, aby zliczyć elementy w określonym przedziale
        long count = CustomList.countElementsInRange(list, lowerBound, upperBound, inRangePredicate);
            System.out.println("Liczba elementów w przedziale: " + count);

        CustomList<Integer> list1 = new CustomList<>();
            list1.add(1);
            list1.add(70);
        CustomList<Integer> list2 = new CustomList<>();
            list2.add(1);
            list2.add(2);
            list2.add(3);

        // Porównanie CustomList pod względem liczby elementów
        Comparator<CustomList<Integer>> sizeComparator = CustomList.compareBySize();
            System.out.println("Porównanie CustomList pod względem liczby elementów:");
            System.out.println(sizeComparator.compare(list1, list2));

        // Porównanie CustomList liczb pod względem sumy ich elementów
        Comparator<CustomList<? extends Number>> sumComparator = CustomList.compareBySum();
            System.out.println("\nPorównanie CustomList liczb pod względem sumy:");
            System.out.println(sumComparator.compare(list1, list2));
    }
}
