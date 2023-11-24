package graph;

import com.coxautodev.graphql.tools.GraphQLResolver;

public class PersonParamValueResolver implements GraphQLResolver<PersonParamValue> {
    private final ParamRepository paramRepository;

    public PersonParamValueResolver(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    public Param param(PersonParamValue personParamValue) {
        if (personParamValue.getParamId() == null)
            return null;
        return paramRepository.findById(personParamValue.getParamId());
    }
}
