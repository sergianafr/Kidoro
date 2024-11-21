/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dbUtils.Connect;

/**
 *
 * @author SERGIANA
 */
public class V_BilanMachine {
    private int idMachine;
    private double prixRevientPratique;
    private double prixRevientTheorique;
    private double difference;

    public int getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(int idMachine) {
        this.idMachine = idMachine;
    }

    public double getPrixRevientPratique() {
        return prixRevientPratique;
    }

    public void setPrixRevientPratique(double prixRevientPratique) {
        this.prixRevientPratique = prixRevientPratique;
    }

    public double getPrixRevientTheorique() {
        return prixRevientTheorique;
    }

    public void setPrixRevientTheorique(double prixRevientTheorique) {
        this.prixRevientTheorique = prixRevientTheorique;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }
    
    
    public V_BilanMachine(int idMachine, double prixRevientPratique, double prixRevientTheorique, double difference) {
        this.idMachine = idMachine;
        this.prixRevientPratique = prixRevientPratique;
        this.prixRevientTheorique = prixRevientTheorique;
        this.difference = difference;
    }

    public static List<V_BilanMachine> getAll(Connect c) throws Exception { 
        try {
            String sql = "SELECT idMACHINE, SUM(prixrevientpratique) as prixRevientPratique, SUM(prixrevienttheorique) as prixRevientTheorique, SUM(prixrevientpratique)-sum(prixrevienttheorique) as difference from bloc group by idMachine order by difference asc";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<V_BilanMachine> results = new ArrayList<V_BilanMachine>();
            while(rs.next()){
                int id = rs.getInt(1);
               double prPratique = rs.getDouble(2);
               double prTheorique = rs.getDouble(3);
               double difference = rs.getDouble(4);
                
                V_BilanMachine vbm = new V_BilanMachine(id, prPratique, prTheorique, difference);
                results.add(vbm);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }

    public static List<V_BilanMachine> getAllByYear(int annee, Connect c) throws Exception { 
        try {
            String sql = "SELECT idMACHINE, SUM(prixrevientpratique) as prixRevientPratique, SUM(prixrevienttheorique) as prixRevientTheorique, SUM(prixrevientpratique)-sum(prixrevienttheorique) as difference from bloc where EXTRACT(year from dateProduction)= ? group by idMachine order by difference asc";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            preparedStatement.setInt(1, annee);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<V_BilanMachine> results = new ArrayList<V_BilanMachine>();
            while(rs.next()){
                int id = rs.getInt(1);
               double prPratique = rs.getDouble(2);
               double prTheorique = rs.getDouble(3);
               double difference = rs.getDouble(4);
                
                V_BilanMachine vbm = new V_BilanMachine(id, prPratique, prTheorique, difference);
                results.add(vbm);
            }
            preparedStatement.close();
            rs.close();
            return results;
            
        } catch (Exception e) {
            c.closeBD();
            throw e;
        }
    }
}
