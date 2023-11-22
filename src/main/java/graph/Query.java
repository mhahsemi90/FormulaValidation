package graph;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import java.util.List;

public class Query implements GraphQLRootResolver {
    private final CalculationTransactionRepository calculationTransactionRepository;

    public Query(CalculationTransactionRepository calculationTransactionRepository) {
        this.calculationTransactionRepository = calculationTransactionRepository;
    }

    public List<CalculationTransaction> allCalculation() {
        return calculationTransactionRepository.getCalculationTransactions();
    }
}
