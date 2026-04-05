package com.social.marketing.search.strategies;

import com.social.marketing.exception.BadRequestException;

import java.util.Objects;

class EnumStrategy extends ParsingStrategy<Enum<?>> {

    private static EnumStrategy instance = null;

    private EnumStrategy() {

    }

    static EnumStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new EnumStrategy();
        }
        return instance;
    }

    @Override
    protected Enum<?> parse(final String value, final Class<?> fieldClass) {
        if (Objects.isNull(value)) {
            return null;
        }
        try {
            return (Enum<?>) Class.forName(fieldClass.getName())
                    .getMethod("valueOf", String.class)
                    .invoke(null, value);
        } catch (Exception e) {
            throw new BadRequestException("Query is invalid.");
        }
    }

}
