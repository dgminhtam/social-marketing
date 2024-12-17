package com.social.marketing.search.strategies;


import com.social.marketing.exception.BadRequestException;

import java.util.Objects;

class BooleanStrategy extends ParsingStrategy<Boolean> {

    private static BooleanStrategy instance = null;

    private BooleanStrategy() {

    }

    static BooleanStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new BooleanStrategy();
        }
        return instance;
    }

    @Override
    protected Boolean parse(final String value, final Class<?> fieldClass) {
        return switch (value) {
            case "true" -> Boolean.TRUE;
            case "false" -> Boolean.FALSE;
            default -> throw new BadRequestException("Query is invalid.");
        };
    }

}
