package com.social.marketing.search.strategies;

import com.social.marketing.search.model.QueryOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class StringStrategy extends ParsingStrategy<String> {

    private static final Character ESCAPE_CHARACTER = '\\';
    private static final Character PERCENT_SIGN = '%';
    private static final String REGEX_EXPRESSION = "<[^>]*>";
    private static StringStrategy instance = null;

    private StringStrategy() {

    }

    static StringStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new StringStrategy();
        }
        return instance;
    }

    @Override
    protected Predicate build(final CriteriaBuilder builder, final Path<?> path, final String fieldName,
            final QueryOperator operator, final String value, final List<String> values) {
        String searchValue = prepareSearchValue(value);
        String lowerCasedValue = searchValue.toLowerCase();

        Expression<String> expression = escapeHtml(builder, path, fieldName);
        Expression<String> lowerCasedExpression = builder.lower(expression);

        return switch (operator) {
            case CONTAINS -> builder
                    .like(expression, PERCENT_SIGN + searchValue + PERCENT_SIGN, ESCAPE_CHARACTER);
            case CONTAINS_IGNORE_CASE -> builder
                    .like(lowerCasedExpression, PERCENT_SIGN + lowerCasedValue + PERCENT_SIGN, ESCAPE_CHARACTER);
            case STARTS_WITH -> builder
                    .like(expression, searchValue + PERCENT_SIGN, ESCAPE_CHARACTER);
            case STARTS_WITH_IGNORE_CASE -> builder
                    .like(lowerCasedExpression, lowerCasedValue + PERCENT_SIGN, ESCAPE_CHARACTER);
            case ENDS_WITH -> builder
                    .like(expression, PERCENT_SIGN + searchValue, ESCAPE_CHARACTER);
            case ENDS_WITH_IGNORE_CASE -> builder
                    .like(lowerCasedExpression, PERCENT_SIGN + lowerCasedValue, ESCAPE_CHARACTER);
            default -> super.build(builder, path, fieldName, operator, value, values);
        };
    }

    private String prepareSearchValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }

        return value
                .replace("_", "\\_")
                .replace("%", "\\%");
    }

    @SneakyThrows
    private Expression<String> escapeHtml(final CriteriaBuilder builder, final Path<?> path, final String fieldName) {
        Field field = FieldUtils.getField(path.getJavaType(), fieldName, true);
        JdbcTypeCode jdbcTypeCode = field.getAnnotation(JdbcTypeCode.class);

        Expression<String> expression = path.get(fieldName);

        if (Objects.nonNull(jdbcTypeCode) && SqlTypes.LONGVARCHAR == jdbcTypeCode.value()) {
            return builder.function("regexp_replace", String.class, expression,
                    builder.literal(REGEX_EXPRESSION), builder.literal(StringUtils.EMPTY), builder.literal("g"));
        }
        return expression;
    }

}
