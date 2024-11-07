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
public class Usuelle {
    private int id;
    private String nom;
    private double longueur, largeur, epaisseur, volume;
    private double prixVente;

    public Usuelle(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Usuelle() {
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

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public Usuelle(int id, String nom, double longueur, double largeur, double epaisseur, double volume, double prixVente) {
        this.id = id;
        this.nom = nom;
        this.longueur = longueur;
        this.largeur = largeur;
        this.epaisseur = epaisseur;
        this.volume = volume;
        this.prixVente = prixVente;
    }
    
    public void getById(Connect c)throws Exception{
        try{
            String sql = "select * from Usuelle where id = ?";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            preparedStatement.setInt(1 , this.getId());
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                this.nom = rs.getString(2);
               this.longueur = rs.getDouble(3);
               this.largeur = rs.getDouble(4);
               this.epaisseur = rs.getDouble(5);
               this.volume = rs.getDouble(6);
               this.prixVente = rs.getDouble(7);
            }
            preparedStatement.close();
            rs.close();
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    public double calculateVolume(){
        return this.longueur*this.largeur*this.epaisseur;
    }
    
    public static List<Usuelle> getAll(Connect c) throws Exception { 
        try {
            String sql = "select * from Usuelle";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Usuelle> results = new ArrayList<Usuelle>();
            while(rs.next()){
                int id = rs.getInt(1);
                String nom = rs.getString(2);
               double longueur = rs.getDouble(3);
               double largeur = rs.getDouble(4);
               double epaisseur = rs.getDouble(5);
               double volume = rs.getDouble(6);
               double prixVente = rs.getDouble(7);
                
                Usuelle us = new Usuelle(id, nom, longueur, largeur, epaisseur, volume, prixVente);
                results.add(us);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
    public static Usuelle getMostRentable(Connect c)throws Exception{
        Usuelle us = new Usuelle();
        try{
          
            String sql = "select * from Usuelle where prixVente/volume=(SELECT max(prixVente/volume) FROM usuelle)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
               us.id = rs.getInt(1);
               us.nom = rs.getString(2);
               us.longueur = rs.getDouble(3);
               us.largeur = rs.getDouble(4);
               us.epaisseur = rs.getDouble(5);
               us.volume = rs.getDouble(6);
               us.prixVente = rs.getDouble(7);
            }
            preparedStatement.close();
            rs.close();
            
        } catch (Exception e) {
            throw e;
        }
        return us;
    }
    
    public static List<Usuelle> getPrixRevient(Connect c)throws Exception{
        try {
            String sql = "SELECT SUM(PRIXREVIENT)/SUM(NB) as PRGlobal, IDUSUELLE FROM DETAILSTRANSFORMATION GROUP BY IDUSUELLE";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Usuelle> results = new ArrayList<Usuelle>();
            while(rs.next()){
                int id = rs.getInt(2);
               
               double prixVente = rs.getDouble(1);
                
                Usuelle us = new Usuelle();
                us.setId(id);
                us.setPrixVente(prixVente);
                results.add(us);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
     public static Usuelle getMoinsPerte(Connect c)throws Exception{
        Usuelle us = new Usuelle();
        try{
          
            String sql = "select * from Usuelle where VOLUME = (SELECT min(volume) from usuelle)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
               us.id = rs.getInt(1);
               us.nom = rs.getString(2);
               us.longueur = rs.getDouble(3);
               us.largeur = rs.getDouble(4);
               us.epaisseur = rs.getDouble(5);
               us.volume = rs.getDouble(6);
               us.prixVente = rs.getDouble(7);
            }
            preparedStatement.close();
            rs.close();
            
        } catch (Exception e) {
            throw e;
        }
        return us;
    }
    
}
