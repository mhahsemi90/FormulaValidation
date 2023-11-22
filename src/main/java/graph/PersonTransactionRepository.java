package graph;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class PersonTransactionRepository {
    private final MongoCollection<Document> personTransactions;

    public PersonTransactionRepository(MongoCollection<Document> personTransactions) {
        this.personTransactions = personTransactions;
    }

    public PersonTransaction findById(String id) {
        Document doc = personTransactions.find(eq("_id", new ObjectId(id))).first();
        return personTransaction(doc);
    }

    public List<PersonTransaction> findByCalculationTransactionId(String calculationTransactionId) {
        List<PersonTransaction> personTransactionList = new ArrayList<>();
        personTransactions.find(eq("calculationTransactionId", new ObjectId(calculationTransactionId)))
                .forEach((Consumer<? super Document>) doc -> personTransactionList.add(personTransaction(doc)));
        return personTransactionList;
    }

    public void savePersonParamValue(PersonTransaction personTransaction) {
        Document doc = new Document();
        doc.append("type", personTransaction.getType());
        doc.append("personId", personTransaction.getPersonId());
        doc.append("calculationTransactionId", personTransaction.getCalculationTransactionId());
        personTransactions.insertOne(doc);
    }

    private PersonTransaction personTransaction(Document doc) {
        return new PersonTransaction(
                doc.get("_id").toString(),
                doc.getString("type"),
                doc.getString("personId"),
                doc.getString("calculationTransactionId"));
    }
}
