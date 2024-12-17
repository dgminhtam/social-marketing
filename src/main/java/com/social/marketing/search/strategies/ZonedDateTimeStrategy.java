package com.social.marketing.search.strategies;

import com.social.marketing.search.model.QueryOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

class ZonedDateTimeStrategy extends DateStrategy<ChronoZonedDateTime<?>> {

    private static ZonedDateTimeStrategy instance = null;

    private ZonedDateTimeStrategy() {
        super();
    }

    static ZonedDateTimeStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ZonedDateTimeStrategy();
        }
        return instance;
    }

    @Override
    protected Predicate build(final CriteriaBuilder builder, final Path<?> path, final String fieldName, final QueryOperator operator,
                              final ChronoZonedDateTime<?> value, final List<ChronoZonedDateTime<?>> values) {
        Path<ChronoZonedDateTime<?>> expression = path.get(fieldName);
        ChronoZonedDateTime<?> to = value.plus(1, ChronoUnit.DAYS);

        return switch (operator) {
            case EQ -> builder.and(
                    builder.greaterThanOrEqualTo(expression, value),
                    builder.lessThan(expression, to)
            );
            case LE -> builder.lessThanOrEqualTo(expression, to);
            default -> super.build(builder, path, fieldName, operator, value, values);
        };
    }

    @Override
    ZonedDateTime parse(final String value) {
        return ZonedDateTime.parse(value);
    }

}
