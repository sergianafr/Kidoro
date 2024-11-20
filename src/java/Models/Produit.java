package Models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbUtils.Connect;

public class Produit {
    private int id;
    private String nom;
    private int idUnite;

    public int getId() {
        return id;
    }public int getIdUnite() {
        return idUnite;
    }public String getNom() {
        return nom;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setIdUnite(int idUnite) {
        this.idUnite = idUnite;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public Produit() {
    }
    public Produit(int id, String nom, int idUnite) {
        this.id = id;
        this.nom = nom;
        this.idUnite = idUnite;
    }

    public static List<Produit> getAll(Connect c) throws Exception { 
        try {
            String sql = "select * from Produit";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Produit> results = new ArrayList<Produit>();
            while(rs.next()){
                int id = rs.getInt(1);
                String nom = rs.getString(2);
                int idUnite = rs.getInt(3);
                
                Produit prod = new Produit(id, nom, idUnite);
                results.add(prod);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            throw e;
        }
    }
}
