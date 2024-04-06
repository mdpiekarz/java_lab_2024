import javax.xml.transform.Result;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person  {
    public final String name;
    public final LocalDate birth, death;
    private List<Person> parents = new ArrayList<>();
    public Person(String name, LocalDate birth, LocalDate death) {
        this.name = name;
        this.birth = birth;
        this.death = death;
    }
    public static Person fromCsvLine(String line) {
        String[] fields = line.split(",");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String birthString = fields[1];
        String deathString = fields[2];
        LocalDate birth = null, death = null;
        if(!birthString.isEmpty())
            birth = LocalDate.parse(birthString, formatter);
        if(!deathString.isEmpty())
            death = LocalDate.parse(deathString, formatter);

        return new Person(fields[0], birth, death);
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<Person> result = new ArrayList<>();
        List<PersonWithParentsNames> resultWithParents =  new ArrayList<>();
        String line;
        br.readLine();
        try {
            while ((line = br.readLine()) != null) {
                PersonWithParentsNames personWithNames = PersonWithParentsNames.fromCsvLine(line);
                personWithNames.person.validateLifespan();
                personWithNames.person.validateAmbiguity(result);
                resultWithParents.add(personWithNames);
                result.add(personWithNames.person);
            }
            PersonWithParentsNames.linkRelatives(resultWithParents);
            try {
                for(Person person: result)
                    person.validateParentingAge();
            }
            catch(ParentingAgeException exception) {
                Scanner scanner = new Scanner(System.in);
                System.out.println(exception.getMessage());
                System.out.println("Please confirm [Y/N]:");
                String response = scanner.nextLine();
                if(!response.equals("Y") && !response.equals("y"))
                    result.remove(exception.person);
            }
        } catch (NegativeLifespanException | AmbiguousPersonException | UndefinedParentException exception) {
            System.err.println(exception.getMessage());
        }
        return result;
    }
    private void validateLifespan() throws NegativeLifespanException {
        if(this.death != null && this.birth.isAfter(this.death))
            throw new NegativeLifespanException(this);
    }

    private void validateAmbiguity(List<Person> peopleSoFar) throws AmbiguousPersonException {
        for(Person person: peopleSoFar)
            if(person.name.equals(this.name))
                throw new AmbiguousPersonException(name);
    }

    private void validateParentingAge() throws ParentingAgeException {
        for(Person parent: parents)
            if (birth.isBefore(parent.birth.minusYears(15)) || ( parent.death != null && birth.isAfter(parent.death)))
                throw new ParentingAgeException(this, parent);
    }

    public void addParent(Person person) {
        parents.add(person);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birth=" + birth +
                ", death=" + death +
                ", parents=" + parents +
                '}';
    }

    public static void toBinaryFile(List<Person> people, String filename) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(people);
        }
    }

    public static List<Person> fromBinaryFile(String filename) throws IOException, ClassNotFoundException {
        try (
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (List<Person>) ois.readObject();
        }
    }

    public String toPlantUMLTree() {
        String result = "@startuml\n%s\n%s\n@enduml";
        //String objectName = this.name.replaceAll("\\s+", "");
        Function<String, String> objectName = (str) -> {return str.replaceAll("\\s+", "");};
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s\n",str, objectName.apply(str));

        StringBuilder objects = new StringBuilder();
        StringBuilder relations = new StringBuilder();

        for(Person parent: parents) {
            objects.append(objectLine.apply(parent.name));
            relations.append(objectName.apply(parent.name)).append("<--").append(objectName.apply(name)).append("\n");

        }


        objects.append(objectLine.apply(name));

        return String.format(result, objects, relations);
    }

    public static String toPlantUMLTreeV1(List<Person> people) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll("\\s+", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s",str, objectName.apply(str));

        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLine.apply(person.name));
            for (Person parent : person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

//        for(Person person: people)
//            addPerson.accept(person);
        people.forEach(addPerson);

//        StringBuilder objectString = new StringBuilder();
//        for(String object: objects)
//            objectString.append(object).append("\n");
        String objectString = String.join("\n", objects);

//        StringBuilder relationString = new StringBuilder();
//        for(String relation: relations)
//            objectString.append(relation).append("\n");
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

    public static List<Person> filterByName(List<Person> people, String nameSubstring) {
        return people.stream()
                .filter(person -> person.name.contains(nameSubstring))
                .collect(Collectors.toList());
    }

    public static List<Person> sortByBirth(List<Person> people) {
        people.sort(Comparator.comparing(person -> person.birth));
        return people;
    }
    public static List<Person> sortByLifespanDesc(List<Person> people) {
        return people.stream()
                .filter(person -> person.death != null)
                .sorted((person1, person2) -> {
                    long lifespan1 = person1.death.toEpochDay() - person1.birth.toEpochDay();
                    long lifespan2 = person2.death.toEpochDay() - person2.birth.toEpochDay();
                    return Long.compare(lifespan2, lifespan1);
                })
                .collect(Collectors.toList());
    }

    public static String toPlantUMLTreeV2(List<Person> people, Function<String, String> postProcess) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll("\\s+", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s",str, objectName.apply(str));
        Function<String, String> objectLineAndPostprocess = objectLine.andThen(postProcess);

        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLineAndPostprocess.apply(person.name));
            for (Person parent : person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

        people.forEach(addPerson);
        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

    private LocalDate getBirth() {
        return birth;
    }

    public static Person findOldestPersonAlive(List<Person> people) {
        return people.stream()
                .filter(person -> person.death == null)
                .min(Comparator.comparing(Person::getBirth))
                .orElse(null);
    }

    public static String toPlantUMLTreeV3(List<Person> people, Predicate<Person> condition, Function<String, String> postProcess) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll("\\s+", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s",str, objectName.apply(str));
        Function<String, String> objectLineAndPostprocess = objectLine.andThen(postProcess);

        Map<Boolean, List<Person>> groupedPeople = people.stream().collect(Collectors.partitioningBy(condition));

        Set<String> objects = groupedPeople.get(true).stream()
                .map(person -> person.name)
                .map(objectLineAndPostprocess)
                .collect(Collectors.toSet());
        objects.addAll(groupedPeople.get(false).stream()
                .map(person -> person.name)
                .map(objectLine)
                .collect(Collectors.toSet())
        );

        Set<String> relations = people.stream()
                .flatMap(person -> person.parents.stream()
                        .map(parent -> objectName.apply(parent.name) + "<--" + objectName.apply(person.name)))
                .collect(Collectors.toSet());

        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

}
