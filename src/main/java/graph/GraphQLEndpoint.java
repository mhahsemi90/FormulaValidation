package graph;


import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
    private static final PersonRepository personRepository;
    private static final ParamRepository paramRepository;
    private static final PersonParamValueRepository personParamValueRepository;
    private static final PersonTransactionRepository personTransactionRepository;
    private static final CalculationTransactionRepository calculationTransactionRepository;

    static {
        //Change to `new MongoClient("<host>:<port>")`
        //if you don't have Mongo running locally on port 27017
        MongoDatabase mongo = new MongoClient().getDatabase("hackernews");
        personRepository = new PersonRepository(mongo.getCollection("persons"));
        paramRepository = new ParamRepository(mongo.getCollection("params"));
        personParamValueRepository = new PersonParamValueRepository(mongo.getCollection("personParamValues"));
        personTransactionRepository = new PersonTransactionRepository(mongo.getCollection("personTransactions"));
        calculationTransactionRepository = new CalculationTransactionRepository(mongo.getCollection("calculationTransactions"));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(
                        new Query(calculationTransactionRepository, personRepository)
                        , new Mutation(personRepository, paramRepository, personParamValueRepository, personTransactionRepository, calculationTransactionRepository)
                        , new PersonParamValueResolver(paramRepository)
                        , new PersonTransactionResolver(personRepository, personParamValueRepository)
                        , new CalculationTransactionResolver(personTransactionRepository)
                )
                .build()
                .makeExecutableSchema();
    }
}
