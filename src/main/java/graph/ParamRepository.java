package graph;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ParamRepository {
    private final MongoCollection<Document> params;

    public ParamRepository(MongoCollection<Document> params) {
        this.params = params;
    }

    public Param findById(String id) {
        Document doc = params.find(eq("_id", new ObjectId(id))).first();
        return param(doc);
    }

    public String saveParam(Param param) {
        Document doc = new Document();
        doc.append("code", param.getCode());
        doc.append("title", param.getTitle());
        params.insertOne(doc);
        return doc.get("_id").toString();
    }

    private Param param(Document doc) {
        return new Param(
                doc.get("_id").toString(),
                doc.getString("code"),
                doc.getString("title"));
    }
}
