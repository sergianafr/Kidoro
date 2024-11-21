package Models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import dbUtils.Connect;

public class Achat {
    private int id;
    private int idProduit;
    private double quantite;
    private double prixUnitaire;
    private Timestamp dateAchat;

    public Achat(int idProduit, double quantite, double prixUnitaire, Timestamp dateAchat) {
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.dateAchat = dateAchat;
    }

    public Timestamp getDateAchat() {
        return dateAchat;
    }public int getId() {
        return id;
    }public int getIdProduit() {
        return idProduit;
    }public double getPrixUnitaire() {
        return prixUnitaire;
    }public double getQuantite() {
        return quantite;
    }
    public void setDateAchat(Timestamp dateAchat) {
        this.dateAchat = dateAchat;
    }public void setId(int id) {
        this.id = id;
    }public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public void createAchat(Connect c)throws Exception{
        try {      
            String Query = "INSERT INTO Achat(id, idProduit,  quantite, prixUnitaire, dateAchat) VALUES (default , ?, ?, ?, ?)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(Query);
            preparedStatement.setInt(1, this.getIdProduit());
            preparedStatement.setDouble(2, this.getQuantite());
            preparedStatement.setDouble(3 , this.getPrixUnitaire());
            preparedStatement.setTimestamp(4 , this.getDateAchat());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            c.getConnex().commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        }
    }
}
