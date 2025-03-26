package org.hcm.pcn.formula_validator.controller;

import graphql.GraphQLError;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Locale;

@ControllerAdvice
public class GlobalGraphQLExceptionHandler {
    private final MessageSource messageSource;

    public GlobalGraphQLExceptionHandler(@Qualifier("MessageBundleSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GraphQlExceptionHandler(HandledError.class)
    public GraphQLError handleHandledError(HandledError e) {
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(
                        messageSource.getMessage(
                                e.getMessage(),
                                e.getArgs(),
                                e.getLang() != null ? new Locale(e.getLang()) : LocaleContextHolder.getLocale()
                        )
                )
                .build();
    }

    @GraphQlExceptionHandler(Exception.class)
    public GraphQLError handleGenericException(Exception e) {
        return GraphQLError.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(
                        messageSource.getMessage(
                                "An unexpected error occurred",
                                null,
                                LocaleContextHolder.getLocale()
                        )
                        )
                .build();
    }

}
