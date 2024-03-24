import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        Person p = null;
        try {
            p = Person.fromCsvLine("Anna Dąbrowska,07.02.1930,22.12.1991,Ewa Kowalska,Marek Kowalski");
            System.out.println(p.toString());
            List<Person> persons = Person.fromCsv("family.csv");

            for (Person person : persons) {
                System.out.println(person);
            }
        } catch (/*NegativeLifespanException*/Exception e) {
            //throw new RuntimeException(e);
            //System.out.println(e.getMessage());
            System.err.println(e.getMessage());
        }
        //System.out.println("Koniec");

    }
}
