package com.social.marketing.search.visitor;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.search.antlr4.QueryBaseVisitor;
import com.social.marketing.search.antlr4.QueryParser;
import com.social.marketing.search.model.QueryCriteria;
import com.social.marketing.search.model.QueryOperator;
import com.social.marketing.search.model.QuerySpecification;
import org.antlr.v4.runtime.RuleContext;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class QuerySpecificationVisitor<T> extends QueryBaseVisitor<Specification<T>> {

    @Override
    public Specification<T> visitOpQuery(QueryParser.OpQueryContext ctx) {
        Specification<T> left = visit(ctx.left);
        Specification<T> right = visit(ctx.right);

        return ctx.logicalOp.getText().equals("and") ? left.and(right) : left.or(right);
    }

    @Override
    public Specification<T> visitPriorityQuery(QueryParser.PriorityQueryContext ctx) {
        return visit(ctx.query());
    }

    @Override
    public Specification<T> visitAtomQuery(QueryParser.AtomQueryContext ctx) {
        return visit(ctx.criteria());
    }

    @Override
    public Specification<T> visitCriteria(final QueryParser.CriteriaContext ctx) {
        if (Objects.nonNull(ctx.exception)) {
            throw new BadRequestException("Query is invalid.");
        }
        String key = ctx.key().getText();
        String op;
        List<String> values = Collections.emptyList();
        if (Objects.nonNull(ctx.NO_VALUE_FUNC())) {
            op = ctx.NO_VALUE_FUNC().getText();
        } else {
            values = Optional.ofNullable(ctx.value()).orElse(Collections.emptyList()).stream()
                    .map(RuleContext::getText)
                    .map(this::validValue)
                    .toList();
            if (Objects.nonNull(ctx.ONE_VALUE_FUNC())) {
                op = ctx.ONE_VALUE_FUNC().getText();
                if (values.size() != 1) {
                    throw new BadRequestException("Query is invalid.");
                }
            } else if (Objects.nonNull(ctx.MULTIPLE_VALUE_OP())) {
                op = ctx.MULTIPLE_VALUE_OP().getText();
                if (values.isEmpty()) {
                    throw new BadRequestException("Query is invalid.");
                }
            } else {
                op = ctx.OP().getText();
            }
        }

        QueryCriteria criteria = new QueryCriteria(key, QueryOperator.getOperation(op,
                String.join(",", values)), values);

        return new QuerySpecification<>(criteria);
    }

    private String validValue(final String value) {
        String validValue = value.replace("\\\"", "\"")
                .replace("\\'", "'");
        if (validValue.startsWith("'")) {
            validValue = validValue.substring(1);
        }
        if (validValue.endsWith("'")) {
            validValue = validValue.substring(0, validValue.length() - 1);
        }
        return validValue;
    }
}
