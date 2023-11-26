package graph;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class PersonParamValueRepository {
    private final MongoCollection<Document> personParamValues;

    public PersonParamValueRepository(MongoCollection<Document> personParamValues) {
        this.personParamValues = personParamValues;
    }

    public PersonParamValue findById(String id) {
        Document doc = personParamValues.find(eq("_id", new ObjectId(id))).first();
        return personParamValue(doc);
    }

    public List<PersonParamValue> findByPersonTransactionId(String personTransactionId) {
        List<PersonParamValue> personParamValueList = new ArrayList<>();
        personParamValues.find(eq("personTransactionId", personTransactionId))
                .forEach((Consumer<? super Document>) doc -> personParamValueList.add(personParamValue(doc)));
        return personParamValueList;
    }

    public String savePersonParamValue(PersonParamValue personParamValue) {
        Document doc = new Document();
        doc.append("isValid", personParamValue.getIsValid());
        doc.append("value", personParamValue.getValue());
        doc.append("paramId", personParamValue.getParamId());
        doc.append("personTransactionId", personParamValue.getPersonTransactionId());
        personParamValues.insertOne(doc);
        return doc.get("_id").toString();
    }

    private PersonParamValue personParamValue(Document doc) {
        return new PersonParamValue(
                doc.get("_id").toString(),
                doc.getBoolean("isValid"),
                doc.getDouble("value"),
                doc.getString("paramId"),
                doc.getString("personTransactionId"));
    }
}
