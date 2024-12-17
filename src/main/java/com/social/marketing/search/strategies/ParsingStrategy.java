package com.social.marketing.search.strategies;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.BaseException;
import com.social.marketing.search.model.QueryCriteria;
import com.social.marketing.search.model.QueryOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ParsingStrategy<T> {

    private static ParsingStrategy<?> getStrategy(final Class<?> fieldClass) {
        if (isNumber(fieldClass)) {
            return NumberStrategy.getInstance();
        }
        if (Arrays.asList(Boolean.class, boolean.class).contains(fieldClass)) {
            return BooleanStrategy.getInstance();
        }
        if (fieldClass == ZonedDateTime.class) {
            return ZonedDateTimeStrategy.getInstance();
        }
        if (fieldClass == LocalDate.class) {
            return LocalDateStrategy.getInstance();
        }
        if (fieldClass.isEnum()) {
            return EnumStrategy.getInstance();
        }
        if (Collection.class.isAssignableFrom(fieldClass)) {
            return CollectionStrategy.getInstance();
        }
        return StringStrategy.getInstance();
    }

    private static boolean isNumber(final Class<?> fieldClass) {
        if (Arrays.asList(int.class, long.class, float.class, double.class).contains(fieldClass)) {
            return true;
        }
        return Number.class.isAssignableFrom(fieldClass);
    }

    /**
     * Build Predicate
     *
     * @param builder    CriteriaBuilder
     * @param path       Path<?>
     * @param fieldClass Class<?>
     * @param fieldName  String
     * @param criteria   QueryCriteria
     * @return Predicate
     */
    @SuppressWarnings("all")
    public final Predicate buildPredicate(final CriteriaBuilder builder, final Path<?> path, final Class<?> fieldClass,
                                          final String fieldName, final QueryCriteria criteria) {
        ParsingStrategy<T> strategy = (ParsingStrategy<T>) getStrategy(fieldClass);
        T value = strategy.parse(criteria.getValue(), fieldClass);
        List<T> values = strategy.parse(criteria.getValues(), fieldClass);
        return strategy.build(builder, path, fieldName, criteria.getOperator(), value, values);
    }

    @SuppressWarnings("all")
    protected T parse(final String value, final Class<?> fieldClass) {
        return (T) value;
    }

    /**
     * Parse list value
     *
     * @param values     List<String>
     * @param fieldClass Class<?>
     * @return List<T>
     */
    protected final List<T> parse(final List<String> values, final Class<?> fieldClass) {
        return values.stream().map(value -> parse(value, fieldClass)).toList();
    }

    /**
     * Build Predicate
     *
     * @param builder   CriteriaBuilderCriteriaBuilder
     * @param path      Path<?>
     * @param fieldName String
     * @param operator  QueryOperator
     * @param value     T
     * @param values    List<T>
     * @return Predicate
     */
    protected Predicate build(final CriteriaBuilder builder, final Path<?> path, final String fieldName,
                              final QueryOperator operator, final T value, final List<T> values) {
        Path<Object> expression = path.get(fieldName);
        return switch (operator) {
            case EQ -> builder.equal(expression, value);
            case NE -> builder.notEqual(expression, value);
            case NULL -> builder.isNull(expression);
            case NOT_NULL -> builder.isNotNull(expression);
            case IN -> builder.in(expression).value(values);
            case NIN -> builder.not(builder.in(expression).value(values));
            default -> throw new BadRequestException("Query is invalid.");
        };
    }

}
