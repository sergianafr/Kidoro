/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import dbUtils.Connect;
import java.sql.PreparedStatement;

/**
 *
 * @author SERGIANA
 */
public class DetailsTransformation {
    private int idTransformation, idUsuelle;
    private int nb;
    private double prixRevient;
    protected Usuelle usuelle;

    public Usuelle getUsuelle() {
        return usuelle;
    }

    public double getPrixRevient() {
        return prixRevient;
    }

    public void setPrixRevient(double prixRevient) {
        this.prixRevient = prixRevient;
    }

    public DetailsTransformation(int idTransformation, int idUsuelle, int nb, Connect c) throws Exception{
       try{
            this.idTransformation = idTransformation;
            this.idUsuelle = idUsuelle;
            this.nb = nb;
            this.setUsuelle(c);
       } catch(Exception e){
           throw e;
       }
        
    }

    public void setUsuelle(Usuelle usuelle) {
        this.usuelle = usuelle;
    }
    
    public int getIdTransformation() {
        return idTransformation;
    }

    public void setIdTransformation(int idTransformation) {
        this.idTransformation = idTransformation;
    }

    public int getIdUsuelle() {
        return idUsuelle;
    }

    public void setIdUsuelle(int idUsuelle) {
        this.idUsuelle = idUsuelle;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }
    
    public void createDetailsTransformation(Connect c)throws Exception{
        try {      
            String Query = "INSERT INTO detailstransformation VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(Query);
            preparedStatement.setInt(1, this.getIdTransformation());
            preparedStatement.setInt(2 , this.getIdUsuelle());
            preparedStatement.setInt(3 , this.getNb());
            preparedStatement.setDouble(4 , this.getPrixRevient());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            c.getConnex().commit();
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
    
    public void setUsuelle(Connect c)throws Exception{
        try{
            Usuelle u = new Usuelle(this.getIdUsuelle());
            u.getById(c);
            this.usuelle = u;
        } catch(Exception e){
            throw e;
        }
    }
    
    
    
    
    
}
