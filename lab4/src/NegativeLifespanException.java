public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(Person person) {
        super(person.getName() + " born in  " + person.getBirthDate() + " and died in "+ person.getDeathDate() +
                ". Possible time-space loophole detected.");
    }

}