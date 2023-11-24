package graph;

import com.coxautodev.graphql.tools.GraphQLResolver;

import java.util.ArrayList;
import java.util.List;

public class CalculationTransactionResolver implements GraphQLResolver<CalculationTransaction> {
    private final PersonTransactionRepository personTransactionRepository;

    public CalculationTransactionResolver(PersonTransactionRepository personTransactionRepository) {
        this.personTransactionRepository = personTransactionRepository;
    }

    public List<PersonTransaction> personTransactionList(CalculationTransaction calculationTransaction) {
        if (calculationTransaction.getId() == null)
            return new ArrayList<>();
        return personTransactionRepository.findByCalculationTransactionId(calculationTransaction.getId());
    }
}
