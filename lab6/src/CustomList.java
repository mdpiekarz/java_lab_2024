import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

class CustomList<T> extends AbstractList<T> {
    private class Node {
        public T value;
        public Node next;

        Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;

    CustomList() {
        this.head = null;
        this.tail = null;
    }

    @Override
    public int size() {
        int counter = 0;
        Node it = this.head;
        while (it != this.tail) {
            it = it.next;
            counter++;
        }
        return counter;
    }

    @Override
    public T get(int index) {
        int counter = 0;
        Node it = this.head;
        while (it != this.tail) {
            if(counter == index) {
                return it.value;
            }
            it = it.next;
            counter++;
        }
        throw new NoSuchElementException();
    }

    public boolean add(T element) {
        addLast(element);
        return true;
    }

    public boolean isEmpty() {
        return this.head == null && this.tail == null;
    }

    void addLast(T item) {
        Node node = new Node(item, null);

        if (isEmpty()) {
            this.head = node;
        } else {
            this.tail.next = node;
        }
        this.tail = node;
    }

    T getLast() {
        if (isEmpty()) {
            return null;
        }
        return this.tail.value;
    }

    void addFirst(T item) {
        Node node = new Node(item, this.head);
        if (isEmpty()) {
            this.tail = node;
        }
        this.head = node;
    }

    T getFirst() {
        if (isEmpty()) {
            return null;
        }
        return this.head.value;
    }

    T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node removedNode = this.head;
        if (this.head == this.tail) {
            this.head = null;
            this.tail = null;

            return removedNode.value;
        }

        this.head = removedNode.next;
        return removedNode.value;
    }

    T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node removedNode = this.tail;
        if (this.head == this.tail) {
            this.head = null;
            this.tail = null;

            return removedNode.value;
        }

        Node it = this.head;
        while (it.next != this.tail) {
            it = it.next;
        }

        it.next = null;
        this.tail = it;

        return removedNode.value;
    }



    @Override
    public String toString() {
        String str = "[";
        Node it = this.head;
        while (it != null) {
            str += it.value + "; ";
            it = it.next;
        }
        str += "]";

        return str;
    }

    public Stream<T> stream(){
        Stream.Builder<T> stream_builder = Stream.builder();

        /*Iterator<T> iterator = iterator();
        while(iterator.hasNext()){
            stream_builder.accept(iterator.next());
        }*/

        for(var element : this){
            stream_builder.accept(element);
        }

        return stream_builder.build();
    }

    public Iterator<T> iterator() {
        Node first = this.head;
        Iterator<T> iter = new Iterator<T>() {
            Node current = first;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }

                T val = current.value;
                current = current.next;
                return val;
            }
        };

        return iter;
    }

    //Zad 4

    public static <T> CustomList<T> filterByClass(CustomList<? extends T> list, Class<?> clazz) {
        CustomList<T> filteredList = new CustomList<>();
        for (T item : list) {
            if (clazz.isInstance(item)) /*clazz.isAssignableFrom(item.getClass()) - uwzględnienie obiektów które dziedziczą po wskazanej klasie*/
            {
                filteredList.addLast(item);
            }
        }
        return filteredList;
    }

    //Zad 5

    public static <T> Predicate<T> isInOpenRange(T lowerBound, T upperBound) {
        return value -> (lowerBound == null || ((Comparable<T>) value).compareTo(lowerBound) > 0) &&
                (upperBound == null || ((Comparable<T>) value).compareTo(upperBound) < 0);
    }

    public static <T> long countElementsInRange(CustomList<T> list, T lowerBound, T upperBound, Predicate<T> predicate) {
        long count = 0;
        for (T item : list) {
            if (predicate.test(item)) {
                count++;
            }
        }
        return count;
    }
    //Zad 6

    public static <T> Comparator<CustomList<T>> compareBySize() {
        return Comparator.comparingInt(customList -> customList.size());
    }

    // Użycie komparatora do porównania CustomList liczb pod względem sumy ich elementów
    public static Comparator<CustomList<? extends Number>> compareBySum() {
        return Comparator.comparingDouble(customList ->
                customList.stream()
                        .mapToDouble(Number::doubleValue)
                        .sum());
    }




}
