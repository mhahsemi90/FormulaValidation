package org.hcm.pcn.formula_validator.service.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.IntValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Objects;

import static graphql.Assert.assertShouldNeverHappen;
import static graphql.scalar.CoercingUtil.isNumberIsh;
import static graphql.scalar.CoercingUtil.typeName;

public class TimeStampScalar implements Coercing<Timestamp, Timestamp> {
    private Timestamp convertImpl(Object input) {
        if (input instanceof Timestamp) {
            return (Timestamp) input;
        } else if (input instanceof String) {
            if (StringUtils.isNotBlank((String) input) && StringUtils.isNumeric((String) input))
                return new Timestamp(Long.parseLong((String) input));
            return null;
        } else if (isNumberIsh(input)) {
            BigDecimal value;
            try {
                value = new BigDecimal(input.toString());
            } catch (NumberFormatException e) {
                // this should never happen because String is handled above
                return assertShouldNeverHappen();
            }
            return new Timestamp(value.longValue());
        }
        return null;
    }

    @Override
    public Timestamp serialize(Object input, GraphQLContext graphQLContext, Locale locale) {
        Timestamp result = convertImpl(input);
        if (result == null) {
            throw new CoercingSerializeException(
                    "Expected type 'Timestamp' but was '" + typeName(input) + "'."
            );
        }
        return result;
    }

    @Override
    public Timestamp parseValue(Object input, GraphQLContext graphQLContext, Locale locale) {
        Timestamp result = convertImpl(input);
        if (result == null) {
            throw new CoercingParseValueException(
                    "Expected type 'Timestamp' but was '" + typeName(input) + "'."
            );
        }
        return result;
    }

    @Override
    public Timestamp parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) {
        if (input instanceof Timestamp) {
            return (Timestamp) input;
        }
        throw new CoercingParseLiteralException(
                "Expected AST type 'IntValue', 'StringValue' or 'FloatValue' but was '" + typeName(input) + "'."
        );
    }

    @Override
    public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
        Timestamp result = Objects.requireNonNull(convertImpl(input));
        return IntValue.newIntValue(BigInteger.valueOf(result.getTime())).build();
    }
}
