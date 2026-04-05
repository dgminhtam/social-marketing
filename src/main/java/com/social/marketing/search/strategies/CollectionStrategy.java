package com.social.marketing.search.strategies;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.search.model.QueryOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

class CollectionStrategy extends ParsingStrategy<Collection<?>> {

    private static CollectionStrategy instance = null;

    private CollectionStrategy() {

    }

    static CollectionStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CollectionStrategy();
        }
        return instance;
    }

    @Override
    protected Predicate build(final CriteriaBuilder builder, final Path<?> path, final String fieldName,
            final QueryOperator operator, final Collection<?> value, final List<Collection<?>> values) {
        Path<Collection<?>> expression = path.get(fieldName);
        return switch (operator) {
            case EMPTY -> builder.isEmpty(expression);
            case NOT_EMPTY -> builder.isNotEmpty(expression);
            default -> throw new BadRequestException("Query is invalid.");
        };
    }

}
