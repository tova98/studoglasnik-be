package hr.tvz.ntovernic.studoglasnik.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FilenameGenerator {

    public static String generateFilename(final LocalDateTime dateTime, final String originalFilename) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return dateTime.format(formatter) + "-" + originalFilename.replaceAll(" ", "_");
    }
}
