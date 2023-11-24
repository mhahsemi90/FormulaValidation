package graph;

import com.coxautodev.graphql.tools.GraphQLResolver;

import java.util.ArrayList;
import java.util.List;

public class PersonTransactionResolver implements GraphQLResolver<PersonTransaction> {
    private final PersonRepository personRepository;
    private final PersonParamValueRepository personParamValueRepository;

    public PersonTransactionResolver(PersonRepository personRepository, PersonParamValueRepository personParamValueRepository) {
        this.personRepository = personRepository;
        this.personParamValueRepository = personParamValueRepository;
    }

    public Person person(PersonTransaction personTransaction) {
        if (personTransaction.getPersonId() == null)
            return null;
        return personRepository.findById(personTransaction.getPersonId());
    }

    public List<PersonParamValue> personParamValueList(PersonTransaction personTransaction) {
        if (personTransaction.getId() == null)
            return new ArrayList<>();
        return personParamValueRepository.findByPersonTransactionId(personTransaction.getId());
    }
}
