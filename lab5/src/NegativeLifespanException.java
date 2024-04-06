public class NegativeLifespanException extends Exception {
    public NegativeLifespanException(Person person) {
        super(person.name + " born in  " + person.birth + " and died in "+ person.death +
                ". Possible time-space loophole detected.");
    }

}