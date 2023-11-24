package graph;

public class PersonParamValue {
    private final String id;
    private final Boolean isValid;
    private final Double value;
    private final String paramId;
    private final String personTransactionId;

    public PersonParamValue(String id, Boolean isValid, Double value, String paramId, String personTransactionId) {
        this.id = id;
        this.isValid = isValid;
        this.value = value;
        this.paramId = paramId;
        this.personTransactionId = personTransactionId;
    }

    public PersonParamValue(Boolean isValid, Double value, String paramId, String personTransactionId) {
        this(null, isValid, value, paramId, personTransactionId);
    }

    public String getId() {
        return id;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public Double getValue() {
        return value;
    }

    public String getParamId() {
        return paramId;
    }

    public String getPersonTransactionId() {
        return personTransactionId;
    }
}
