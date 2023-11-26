package graph;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class CalculationTransactionRepository {
    private final MongoCollection<Document> calculationTransactions;

    public CalculationTransactionRepository(MongoCollection<Document> calculationTransactions) {
        this.calculationTransactions = calculationTransactions;
    }

    public CalculationTransaction findById(String id) {
        Document doc = calculationTransactions.find(eq("_id", new ObjectId(id))).first();
        return calculationTransaction(doc);
    }

    public String saveCalculationTransaction(CalculationTransaction calculationTransaction) {
        Document doc = new Document();
        doc.append("year", calculationTransaction.getYear());
        doc.append("month", calculationTransaction.getMonth());
        calculationTransactions.insertOne(doc);
        return doc.get("_id").toString();
    }

    private CalculationTransaction calculationTransaction(Document doc) {
        return new CalculationTransaction(
                doc.get("_id").toString(),
                doc.getString("year"),
                doc.getString("month"));
    }

    public List<CalculationTransaction> getCalculationTransactions() {
        List<CalculationTransaction> calculationTransactionList = new ArrayList<>();
        for (Document document : calculationTransactions.find()) {
            calculationTransactionList.add(calculationTransaction(document));
        }
        return calculationTransactionList;
    }
}
