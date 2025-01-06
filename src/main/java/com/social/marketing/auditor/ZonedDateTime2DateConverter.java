package com.social.marketing.auditor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

@Converter(autoApply = true)
public class ZonedDateTime2DateConverter implements AttributeConverter<ZonedDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(final ZonedDateTime attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return Date.from(attribute.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(final Date dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        return ZonedDateTime.ofInstant(dbData.toInstant(), ZoneId.of(ZoneOffset.UTC.getId()));
    }
}
