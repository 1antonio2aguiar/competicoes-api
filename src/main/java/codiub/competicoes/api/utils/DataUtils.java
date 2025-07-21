package codiub.competicoes.api.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Date;

public class DataUtils {

    public static Date formatarData(String dataString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(dataString, formatter);
        // Converta LocalDate para Date
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
