package com.social.marketing.search.model;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.search.strategies.ParsingStrategy;
import jakarta.persistence.criteria.*;
import org.hibernate.metamodel.model.domain.internal.AbstractPluralAttribute;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.sqm.tree.domain.AbstractSqmSimplePath;
import org.hibernate.query.sqm.tree.domain.SqmEntityValuedSimplePath;
import org.hibernate.query.sqm.tree.domain.SqmPluralValuedSimplePath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuerySpecification<T> implements Specification<T> {

    private final transient QueryCriteria criteria;
    private final transient ParsingStrategy<Object> strategy = new ParsingStrategy<>();

    public QuerySpecification(final QueryCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    @NonNull
    public Predicate toPredicate(@NonNull final Root<T> root, @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder builder) {
        List<String> nestedKey = new ArrayList<>(Arrays.asList(criteria.getField().split("\\.")));
        Path<?> nestedRoot = getNestedRoot(root, nestedKey);
        String criteriaKey = nestedKey.get(nestedKey.size() - 1);
        Class<?> fieldClass = getFieldClass(nestedRoot, criteriaKey);
        query.distinct(true);
        return strategy.buildPredicate(builder, nestedRoot, fieldClass, criteriaKey, criteria);
    }

    private Path<?> getNestedRoot(final Root<T> root, final List<String> nestedKey) {
        List<String> prefix = new ArrayList<>(nestedKey);
        prefix.remove(nestedKey.size() - 1);
        Path<?> temp = root;
        for (String s : prefix) {
            Path<?> path = temp.get(s);
            if (path instanceof SqmPluralValuedSimplePath<?> || path instanceof SqmEntityValuedSimplePath<?>) {
                temp = ((From<?, ?>) temp).join(s);
            } else {
                temp = temp.get(s);
            }
        }
        return temp;
    }

    private Class<?> getFieldClass(final Path<?> nestedRoot, final String criteriaKey) {
        try {
            Path<Object> objectPath = nestedRoot.get(criteriaKey);
            SqmPathSource<?> sqmPathSource = ((AbstractSqmSimplePath<?>) objectPath).getNodeType();
            if (sqmPathSource instanceof AbstractPluralAttribute<?, ?, ?> abstractPluralAttribute) {
                return abstractPluralAttribute.getJavaType();
            }
            return objectPath.getJavaType();
        } catch (Exception e) {
            throw new BadRequestException("Query is invalid.");
        }
    }

}
