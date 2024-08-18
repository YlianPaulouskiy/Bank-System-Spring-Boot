package by.aston.bank.model.converter;

import jakarta.persistence.*;
import java.sql.Date;

@Converter
public class DateConverter implements AttributeConverter<String, Date> {

    @Override
    public Date convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return Date.valueOf(attribute);
    }

    @Override
    public String convertToEntityAttribute(Date dbData) {
        if (dbData == null) return null;
        return dbData.toString();
    }

}
