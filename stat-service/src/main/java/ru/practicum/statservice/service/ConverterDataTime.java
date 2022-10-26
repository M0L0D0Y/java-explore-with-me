package ru.practicum.statservice.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ConverterDataTime {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
   // private static final String PATTERN = "yyyy-MM-ddTHH:mm:ss";
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat(PATTERN);

    public LocalDateTime toLocalDataTime(String dateTime) {
        try {
            Date date = sdf.parse(dateTime);
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(LocalDateTime localDateTime) {
        Date dateToConvert = java.sql.Timestamp.valueOf(localDateTime);
        try {
            Date date = sdf.parse(sdf.format(dateToConvert));
            return sdf.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
