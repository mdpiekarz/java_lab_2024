import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        PlantUMLRunner.setPath("/tmp/download/plantuml-1.2024.3.jar");
        String data = "@startuml\n" +
                "object firstObject\n" +
                "object \"My Second Object\" as o2\n" +
                "@enduml";

        //PlantUMLRunner.generate(data, "tmp/download", "Anna Kowalska");

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try {
            List<Person> people = Person.fromCsv("family.csv");

            //people = Person.filterByName(people, "Nowak");
            //people = Person.sortByBirth(people);
            //people = Person.sortByLifespanDesc(people);

            for(Person person: people) {
                //PlantUMLRunner.generate(person.toPlantUMLTree(), "uml_out", person.name);
                System.out.println(person);
            }

//            PlantUMLRunner.generate(Person.toPlantUMLTreeV2(people,
//                    str -> str + " #E3664A"
//                    ), "uml_out", "family");

//            PlantUMLRunner.generate(Person.toPlantUMLTreeV3(people,
//                    person -> person.death == null,
//                    str -> str + " #6BBF59"
//            ), "uml_out", "family");

            PlantUMLRunner.generate(Person.toPlantUMLTreeV1(people), "out_uml", "family");

//            PlantUMLRunner.generate(Person.toPlantUMLTreeV3(people,
//                    person -> person == Person.findOldestPersonAlive(people),
//                    str -> str + " #6BBF59"
//            ), "out_uml", "family");

            //Person person = people.getLast();


//            Person.toBinaryFile(people, "family.bin");
//            List<Person> loadPeople = Person.fromBinaryFile("family.bin");
//            for(Person person: loadPeople)
//                System.out.println(person);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}