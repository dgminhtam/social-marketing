package com.social.marketing.search.model;

import com.social.marketing.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QueryOperator {

    EQ("eq"),
    NE("ne"),
    GT("gt"),
    GE("ge"),
    LT("lt"),
    LE("le"),
    CONTAINS("contains"),
    CONTAINS_IGNORE_CASE("containsIgnoreCase"),
    STARTS_WITH("startsWith"),
    STARTS_WITH_IGNORE_CASE("startsWithIgnoreCase"),
    ENDS_WITH("endsWith"),
    ENDS_WITH_IGNORE_CASE("endsWithIgnoreCase"),
    NULL("null"),
    NOT_NULL("notNull"),
    EMPTY("empty"),
    NOT_EMPTY("notEmpty"),
    IN("in"),
    NIN("nin");

    private final String name;

    /**
     * Get Operation enum
     *
     * @param op    String
     * @param value String (When parse failed: operation in request map to value field)
     * @return QueryOperator
     */
    public static QueryOperator getOperation(final String op, final String value) {
        for (QueryOperator operation : QueryOperator.values()) {
            if (operation.getName().equals(op.trim())) {
                return operation;
            }
        }
        throw new BadRequestException("Query is invalid.");
    }

}
