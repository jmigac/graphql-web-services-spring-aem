package io.ecx.spring.graphql.webservice.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalDatePretvarac {

    public static LocalDate pretvoriDatum(final Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate pretvoriDatum(final String datum) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(datum, formatter);
    }

    public static long pocetakDana() {
        final LocalDateTime lokalniDatum = datumULokalniDatum(new Date());
        final LocalDateTime pocetakDana = lokalniDatum.with(LocalDateTime.MIN);
        return pocetakDana.toEpochSecond(ZoneOffset.UTC);
    }

    public static long krajData() {
        final LocalDateTime lokalniDatum = datumULokalniDatum(new Date());
        final LocalDateTime krajDana = lokalniDatum.with(LocalDateTime.MAX);
        return krajDana.toEpochSecond(ZoneOffset.UTC);
    }

    private static LocalDateTime datumULokalniDatum(final Date datum) {
        return LocalDateTime.ofInstant(datum.toInstant(), ZoneId.systemDefault());
    }

    private static Date lokalniDatumUdatum(final LocalDateTime lokalniDatum) {
        return Date.from(lokalniDatum.atZone(ZoneId.systemDefault()).toInstant());
    }

}
