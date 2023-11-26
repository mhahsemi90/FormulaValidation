package graph;

public class Param {
    private final String id;
    private final String code;
    private final String title;

    public Param(String id, String code, String title) {
        this.id = id;
        this.code = code;
        this.title = title;
    }

    public Param(String code, String title) {
        this(null, code, title);
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Param{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
