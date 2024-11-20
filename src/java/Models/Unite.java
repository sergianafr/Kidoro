package Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbUtils.Connect;

public class Unite {
    private int id;
    private String nom;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Unite(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }


    public static List<Unite> getAll(Connect c) throws Exception { 
        try {
            String sql = "select * from Unite";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Unite> results = new ArrayList<Unite>();
            while(rs.next()){
                int id = rs.getInt(1);
                String nom = rs.getString(2);
                
                Unite unit = new Unite(id, nom);
                results.add(unit);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            throw e;
        }
    }
}
