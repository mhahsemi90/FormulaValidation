package graph;

public class Person {
    private final String id;
    private final String code;
    private final String firstName;
    private final String lastName;

    public Person(String id, String code, String firstName, String lastName) {
        this.id = id;
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String code, String firstName, String lastName) {
        this(null,code,firstName,lastName);
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
