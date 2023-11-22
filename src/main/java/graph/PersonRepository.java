package graph;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class PersonRepository {
    private final MongoCollection<Document> persons;

    public PersonRepository(MongoCollection<Document> persons) {
        this.persons = persons;
    }

    public Person findById(String id) {
        Document doc = persons.find(eq("_id", new ObjectId(id))).first();
        return person(doc);
    }

    public void savePerson(Person person) {
        Document doc = new Document();
        doc.append("code", person.getCode());
        doc.append("firstName", person.getFirstName());
        doc.append("lastName", person.getLastName());
        persons.insertOne(doc);
    }

    private Person person(Document doc) {
        return new Person(
                doc.get("_id").toString(),
                doc.getString("code"),
                doc.getString("firstName"),
                doc.getString("lastName"));
    }
}
