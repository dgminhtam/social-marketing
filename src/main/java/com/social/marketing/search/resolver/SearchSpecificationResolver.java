package com.social.marketing.search.resolver;

import com.social.marketing.search.anotation.Search;
import com.social.marketing.search.antlr4.QueryLexer;
import com.social.marketing.search.antlr4.QueryParser;
import com.social.marketing.search.visitor.QuerySpecificationVisitor;
import jakarta.annotation.Nonnull;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class SearchSpecificationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        parameter.getParameterAnnotation(Search.class);
        return Specification.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter,
            @Nonnull ModelAndViewContainer mavContainer,
            @Nonnull NativeWebRequest webRequest,
            @Nonnull WebDataBinderFactory binderFactory) {
        String query = webRequest.getParameter("$filter");
        if (StringUtils.isBlank(query)) {
            return null;
        }

        QueryLexer lexer = new QueryLexer(CharStreams.fromString(query));
        QueryParser parser = new QueryParser(new CommonTokenStream(lexer));
        QueryParser.QueryContext queryContext = parser.query();

        QuerySpecificationVisitor<?> visitor = new QuerySpecificationVisitor<>();
        return visitor.visit(queryContext);
    }
}
