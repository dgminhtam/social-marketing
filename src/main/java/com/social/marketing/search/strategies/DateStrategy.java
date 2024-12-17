package com.social.marketing.search.strategies;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.BaseException;
import com.social.marketing.search.model.QueryOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public abstract class DateStrategy<T extends Comparable<T>> extends ParsingStrategy<T> {

    protected DateStrategy() {
    }

    @Override
    protected T parse(final String value, final Class<?> fieldClass) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return parse(value);
        } catch (Exception e) {
            throw new BadRequestException("Query is invalid.");
        }
    }

    @Override
    protected Predicate build(final CriteriaBuilder builder, final Path<?> path, final String fieldName,
                              final QueryOperator operator, final T value, final List<T> values) {
        Path<T> expression = path.get(fieldName);
        return switch (operator) {
            case GT -> builder.greaterThan(expression, value);
            case GE -> builder.greaterThanOrEqualTo(expression, value);
            case LT -> builder.lessThan(expression, value);
            case LE -> builder.lessThanOrEqualTo(expression, value);
            default -> super.build(builder, path, fieldName, operator, value, values);
        };
    }

    abstract T parse(String value);

}
