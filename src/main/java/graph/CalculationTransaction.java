package graph;

public class CalculationTransaction {
    private final String id;
    private final String year;
    private final String month;

    public CalculationTransaction(String id, String year, String month) {
        this.id = id;
        this.year = year;
        this.month = month;
    }

    public CalculationTransaction(String year, String month) {
        this(null, year, month);
    }

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return "CalculationTransaction{" +
                "id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}
