package graph;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

public class Mutation implements GraphQLRootResolver {
    private final PersonRepository personRepository;
    private final ParamRepository paramRepository;
    private final PersonParamValueRepository personParamValueRepository;
    private final PersonTransactionRepository personTransactionRepository;
    private final CalculationTransactionRepository calculationTransactionRepository;

    public Mutation(PersonRepository personRepository
            , ParamRepository paramRepository
            , PersonParamValueRepository personParamValueRepository
            , PersonTransactionRepository personTransactionRepository
            , CalculationTransactionRepository calculationTransactionRepository) {
        this.personRepository = personRepository;
        this.paramRepository = paramRepository;
        this.personParamValueRepository = personParamValueRepository;
        this.personTransactionRepository = personTransactionRepository;
        this.calculationTransactionRepository = calculationTransactionRepository;
    }

    public Person createPerson(String code, String firstName, String lastName) {
        Person person = new Person(code, firstName, lastName);
        String id = personRepository.savePerson(person);
        return personRepository.findById(id);
    }

    public Param createParam(String code, String title) {
        Param param = new Param(code, title);
        String id = paramRepository.saveParam(param);
        return paramRepository.findById(id);
    }

    public PersonParamValue createPersonParamValue(Boolean isValid, Float value, String paramId, String personTransactionId) {
        PersonParamValue personParamValue = new PersonParamValue(isValid, Double.valueOf(value), paramId, personTransactionId);
        String id = personParamValueRepository.savePersonParamValue(personParamValue);
        return personParamValueRepository.findById(id);
    }

    public PersonTransaction createPersonTransaction(String type, String personId, String calculationTransactionId) {
        PersonTransaction personTransaction = new PersonTransaction(type, personId, calculationTransactionId);
        String id = personTransactionRepository.savePersonTransaction(personTransaction);
        return personTransactionRepository.findById(id);
    }

    public CalculationTransaction createCalculationTransaction(String year, String month) {
        CalculationTransaction calculationTransaction = new CalculationTransaction(year, month);
        String id = calculationTransactionRepository.saveCalculationTransaction(calculationTransaction);
        return calculationTransactionRepository.findById(id);
    }
}
