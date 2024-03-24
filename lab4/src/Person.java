import java.io.*;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Person {
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
//    private String fatherName;
//    private String motherName;
    private List<Person> parents = new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String csvLine){
        String[] fields = csvLine.split(",");
        String name = fields[0].trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String birthString = fields[1];
        String deathString = fields[2];
        LocalDate birth = null, death = null;
        if(!birthString.isEmpty())
            birth = LocalDate.parse(birthString, formatter);
        if(!deathString.isEmpty())
            death = LocalDate.parse(deathString, formatter);

        return new Person(name, birth, death);
    }

    private void validateLifespan() throws NegativeLifespanException {
        if(this.deathDate != null && this.birthDate.isAfter(this.deathDate))
            throw new NegativeLifespanException(this);
    }

    private void validateAmbiguity(List<Person> peopleSoFar) throws AmbiguousPersonException {
        for(Person person: peopleSoFar)
            if(person.name.equals(this.name))
                throw new AmbiguousPersonException(name);
    }

    // Getters and setters (not shown for brevity)

    @Override
    public String toString() {

        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", parents = " + parents +
//                ", fatherName='" + parents.get(1).getName() + '\'' +
//                ", motherName='" + parents.get(2).getName() + '\'' +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException, NegativeLifespanException, UndefinedParentException {
        List<Person> people = new ArrayList<>();
        List<PersonWithParentsNames> peopleWithParents =  new ArrayList<>();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(path));
        // Pomijamy nagłówek
        br.readLine();

            while ((line = br.readLine())!=null) {
                PersonWithParentsNames personWithNames = PersonWithParentsNames.fromCsvLine(line);
                personWithNames.person.validateLifespan();
                personWithNames.person.validateAmbiguity(people);
                peopleWithParents.add(personWithNames);
                people.add(personWithNames.person);
            }

            PersonWithParentsNames.linkRelatives(peopleWithParents);
        try {
            for(Person person: people)
                person.validateParentingAge();
        }
        catch(ParentingAgeException exception) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(exception.getMessage());
            System.out.println("Please confirm [Y/N]:");
            String response = scanner.nextLine();
            if(!response.equals("Y") && !response.equals("y"))
                people.remove(exception.person);
        }
        return people;
    }

    private void validateParentingAge() throws ParentingAgeException {
        for(Person parent: parents)
            if (birthDate.isBefore(parent.birthDate.plusYears(15)) || ( parent.deathDate != null && birthDate.isAfter(parent.deathDate)))
                throw new ParentingAgeException(this, parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }
    public void addParent(Person person) {
        parents.add(person);
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
}
