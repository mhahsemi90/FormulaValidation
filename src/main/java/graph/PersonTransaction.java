package graph;

public class PersonTransaction {
    private final String id;
    private final String type;
    private final String personId;
    private final String calculationTransactionId;

    public PersonTransaction(String id, String type, String personId, String calculationTransactionId) {
        this.id = id;
        this.type = type;
        this.personId = personId;
        this.calculationTransactionId = calculationTransactionId;
    }

    public PersonTransaction(String type, String personId, String calculationTransactionId) {
        this(null, type, personId, calculationTransactionId);
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPersonId() {
        return personId;
    }

    public String getCalculationTransactionId() {
        return calculationTransactionId;
    }
}
