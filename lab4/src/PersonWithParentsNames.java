import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonWithParentsNames {
    public final Person person;
    public final String[] parentNames;
    public PersonWithParentsNames(Person person, String[] parents) {
        this.person = person;
        this.parentNames = parents;
    }
    public static PersonWithParentsNames fromCsvLine(String line) throws NegativeLifespanException {
        Person person = Person.fromCsvLine(line);
        String[] fields = line.split(",", -1); //-1 powoduje, ze puste pola na końcu linii nie będą ignorowane, będzie utworzona pusta składowa tablicy
        String[] parents = new String[2];
        for (int i = 0; i < 2; ++i) {
            if (!fields[i + 3].isEmpty())
                parents[i] = fields[i + 3];
        }
        return new PersonWithParentsNames(person, parents);
    }
    public static void linkRelatives(List<PersonWithParentsNames> people) throws UndefinedParentException {
        Map<String, PersonWithParentsNames> peopleMap = new HashMap<>();
        for(PersonWithParentsNames personWithNames : people)
            peopleMap.put(personWithNames.person.getName(), personWithNames);

        for(PersonWithParentsNames personWithNames : people) {
            Person person = personWithNames.person;
            for (int i = 0; i < 2; ++i) {
                String parentName = personWithNames.parentNames[i];
                if(parentName != null)
                    if(peopleMap.containsKey(parentName))
                        person.addParent(peopleMap.get(parentName).person);
                    else
                        throw new UndefinedParentException(person, parentName);
            }
        }
    }
}
