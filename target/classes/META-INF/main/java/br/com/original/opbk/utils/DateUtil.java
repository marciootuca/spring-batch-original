package br.com.original.opbk.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private DateUtil(){}
    public static LocalDateTime convertStringToLocalDateTime(String date){
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss");
        return LocalDate.parse(date, parser).atStartOfDay();
    }
}
