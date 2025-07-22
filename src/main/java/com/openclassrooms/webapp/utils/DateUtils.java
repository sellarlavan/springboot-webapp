package com.openclassrooms.webapp.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate, FORMATTER);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
