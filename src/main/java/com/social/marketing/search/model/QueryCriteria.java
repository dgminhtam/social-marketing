package com.social.marketing.search.model;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class QueryCriteria {
    private final String field;
    private final QueryOperator operator;
    private final List<String> values;
    private final String value;

    public QueryCriteria(final String field, final QueryOperator operator, final List<String> values) {
        this.field = field;
        this.operator = operator;
        this.values = values;
        if (Objects.nonNull(values) && !values.isEmpty()) {
            value = values.get(0);
        } else {
            value = null;
        }
    }
}

