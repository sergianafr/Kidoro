package Util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionUtil {
   public static Timestamp convertToTimestamp(String dateTimeString) {
        // Définir le format du datetime local
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        
        // Convertir la chaîne en LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        
        // Convertir LocalDateTime en Timestamp
        return Timestamp.valueOf(localDateTime);
    } 
}
