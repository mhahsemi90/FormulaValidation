package graph;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import java.util.List;

public class Query implements GraphQLRootResolver {
    private final CalculationTransactionRepository calculationTransactionRepository;
    private final PersonRepository personRepository;

    public Query(CalculationTransactionRepository calculationTransactionRepository, PersonRepository personRepository) {
        this.calculationTransactionRepository = calculationTransactionRepository;
        this.personRepository = personRepository;
    }

    public List<CalculationTransaction> allCalculation(String id) {
        return calculationTransactionRepository.getCalculationTransactions();
    }
}
