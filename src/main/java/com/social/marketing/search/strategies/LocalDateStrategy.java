package com.social.marketing.search.strategies;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;

class LocalDateStrategy extends DateStrategy<ChronoLocalDate> {

    private static LocalDateStrategy instance = null;

    private LocalDateStrategy() {
    }

    static LocalDateStrategy getInstance() {
        if (Objects.isNull(instance)) {
            instance = new LocalDateStrategy();
        }
        return instance;
    }

    @Override
    LocalDate parse(final String value) {
        return LocalDate.parse(value);
    }

}
