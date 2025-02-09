/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import dbUtils.Connect;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SERGIANA
 */
public class Bloc {
    private int id;
    private double longueur, largeur, epaisseur, volume;
    private double prixRevient, prixAchat;
    private Date dateProduction;
    private int source;
    private int sourceMere;

    public int getSourceMere() {
        return sourceMere;
    }

    public void setSourceMere(int sourceMere) {
        this.sourceMere = sourceMere;
    }
    
    

    public Bloc(int id, double longueur, double largeur, double epaisseur, double prixRevient, double prixAchat, Date dateProduction, int source) {
        this.id = id;
        this.longueur = longueur;
        this.largeur = largeur;
        this.epaisseur = epaisseur;
        this.prixRevient = prixRevient;
        this.prixAchat = prixAchat;
        this.dateProduction = dateProduction;
        this.source = source;
    }
    
    public Bloc(int id, double longueur, double largeur, double epaisseur, double volume, double prixRevient, double prixAchat, Date dateProduction, int source) {
        this.id = id;
        this.longueur = longueur;
        this.largeur = largeur;
        this.epaisseur = epaisseur;
        this.volume = volume;
        this.prixRevient = prixRevient;
        this.prixAchat = prixAchat;
        this.dateProduction = dateProduction;
        this.source = source;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
    public Bloc() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public double getEpaisseur() {
        return epaisseur;
    }

    public void setEpaisseur(double epaisseur) {
        this.epaisseur = epaisseur;
    }

    public double getPrixRevient() {
        return prixRevient;
    }

    public void setPrixRevient(double prixRevient) {
        this.prixRevient = prixRevient;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Date getDateProduction() {
        return dateProduction;
    }

    public void setDateProduction(Date dateProduction) {
        this.dateProduction = dateProduction;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Bloc(double longueur, double largeur, double epaisseur) {
        this.longueur = longueur;
        this.largeur = largeur;
        this.epaisseur = epaisseur;
    }
    
    
    public double getPRUnite(){
        return this.getPrixRevient()/this.getVolume();
    }
    public double getPAUnite(){
        return this.getPrixAchat()/this.getVolume();
    }
    
    public double calculateVolume(){
        return this.longueur*this.largeur*this.epaisseur;
    }
    public void createBloc(Connect c)throws Exception{
        try {      
            String Query = "INSERT INTO Bloc(id, longueur,  largeur, epaisseur, prixRevient, prixAchat, dateProduction, source) VALUES (default , ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(Query);
            preparedStatement.setDouble(1 , this.getLongueur());
            preparedStatement.setDouble(2 , this.getLargeur());
            preparedStatement.setDouble(3 , this.getEpaisseur());
            preparedStatement.setDouble(4 , this.getPrixRevient());
            preparedStatement.setDouble(5 , this.getPrixAchat());
            preparedStatement.setDate(6 , this.getDateProduction());
            preparedStatement.setInt(7, this.getSource());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            c.getConnex().commit();
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
    
   public void getByIdSource(Connect c) throws Exception {
    try {
        String sql = "SELECT * FROM Bloc WHERE source = ?";
        PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
        preparedStatement.setInt(1, this.source);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            this.id = rs.getInt("id");
            this.longueur = rs.getDouble("longueur");
            this.largeur = rs.getDouble("largeur");
            this.epaisseur = rs.getDouble("epaisseur");
            this.volume = rs.getDouble("volume");
            this.prixRevient = rs.getDouble("prixRevient");
            this.prixAchat = rs.getDouble("prixAchat");
            this.dateProduction = rs.getDate("dateProduction");
            this.source = rs.getInt("source");
            this.sourceMere = rs.getInt("sourceMere");
        }

        preparedStatement.close();
        rs.close();
    } catch (Exception e) {
        throw e;
    }
}

    public static List<Bloc> getAll(Connect c) throws Exception { 
        try {
            String sql = "select * from Bloc";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Bloc> results = new ArrayList<Bloc>();
            while(rs.next()){
                int id = rs.getInt(1);
                double longueur = rs.getDouble(2);
                double largeur = rs.getDouble(3);
                double epaisseur = rs.getDouble(4);
                double volume = rs.getDouble(5);
                double prixRevient = rs.getDouble(6);
                double prixAchat = rs.getDouble(7);
                Date dateProduction = rs.getDate(8);
                int source = rs.getInt(9);
                
                Bloc b = new Bloc(id, longueur, largeur, epaisseur, volume, prixRevient, prixAchat,dateProduction, source);
                results.add(b);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }

    
    public static void update(int id, double pr, Connect c)throws Exception{
        try {      
            String Query = "UPDATE Bloc SET prixRevient=? WHERE id=?";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(Query);
            preparedStatement.setDouble(1 , pr);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            c.getConnex().commit();
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
    public static List<Bloc> getAllDispo(Connect c) throws Exception { 
        try {
            String sql = "select * from Bloc where id not in (select source from bloc)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Bloc> results = new ArrayList<Bloc>();
            while(rs.next()){
                int id = rs.getInt(1);
                double longueur = rs.getDouble(2);
                double largeur = rs.getDouble(3);
                double epaisseur = rs.getDouble(4);
                double volume = rs.getDouble(5);
                double prixRevient = rs.getDouble(6);
                double prixAchat = rs.getDouble(7);
                Date dateProduction = rs.getDate(8);
                int source = rs.getInt(9);
                
                Bloc b = new Bloc(id, longueur, largeur, epaisseur, volume, prixRevient, prixAchat,dateProduction, source);
                results.add(b);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
    public int getQteTheorique(Usuelle u){
        
        return (int)(this.calculateVolume()/u.calculateVolume());
    }
    
    
    
    
}
