package Util;

import java.sql.Statement;

import dbUtils.Connect;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class CSVUtil {
    public static void copyCsvToPostgres(String csvFilePath, String url, String tableName, Connect c) {
        Statement statement = null;

        try {
            c.connectToPostgres("kidoro", "Etu002610");
            statement = c.getConnex().createStatement();

            String copyQuery = String.format(
                "COPY %s FROM '%s' WITH (FORMAT csv, HEADER true, DELIMITER ',');",
                tableName, new File(csvFilePath).getAbsolutePath()
            );

            // Exécuter la commande COPY
            statement.execute(copyQuery);
            System.out.println("Données insérées avec succès via COPY !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (c.getConnex() != null) c.getConnex().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
