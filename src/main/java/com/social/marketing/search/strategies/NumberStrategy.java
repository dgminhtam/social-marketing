package com.social.marketing.search.strategies;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.search.model.QueryOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

class NumberStrategy extends ParsingStrategy<Number> {

    private static NumberStrategy instance = null;

    private NumberStrategy() {
    }

    static NumberStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new NumberStrategy();
        }
        return instance;
    }

    @Override
    protected Number parse(final String value, final Class<?> fieldClass) {
        try {
            return (Number) fieldClass.getConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            throw new BadRequestException("Query is invalid.");
        }
    }

    @Override
    protected Predicate build(final CriteriaBuilder builder, final Path<?> path, final String fieldName,
            final QueryOperator operator, final Number value, final List<Number> values) {
        Path<BigDecimal> expression = path.get(fieldName);
        return switch (operator) {
            case GT -> builder.gt(expression, value);
            case GE -> builder.ge(expression, value);
            case LT -> builder.lt(expression, value);
            case LE -> builder.le(expression, value);
            default -> super.build(builder, path, fieldName, operator, value, values);
        };
    }

}
