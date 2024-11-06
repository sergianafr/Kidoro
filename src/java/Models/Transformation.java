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
import java.util.Vector;

/**
 *
 * @author SERGIANA
 */
public class Transformation {
    private int id, idBloc;
    private Date dateTransformation;
    protected List<DetailsTransformation> detailsTransformation;
    protected Bloc bloc;
    protected Bloc reste;
    private final static double THETA = 0.02;

    public Transformation(int id, int idBloc, Date dateTransformation, List<DetailsTransformation> detailsTransformation) {
        this.id = id;
        this.idBloc = idBloc;
        this.dateTransformation = dateTransformation;
        this.detailsTransformation = detailsTransformation;
    }
    
    public Transformation(int id, int idBloc, Date dateTransformation, List<DetailsTransformation> detailsTransformation, Connect c) throws Exception{
        try{
            this.id = id;
            this.idBloc = idBloc;
            this.dateTransformation = dateTransformation;
            this.detailsTransformation = detailsTransformation;
            this.setBloc(c);
        }
        catch(Exception e){
            throw e;
        }
    }
    
    public Transformation(int id, int idBloc, Date dateTransformation, Connect c) throws Exception{
        try{
            this.id = id;
            this.idBloc = idBloc;
            this.dateTransformation = dateTransformation;
            this.setDetailsTransformation(c);
            this.setBloc(c);
            this.setReste(c);
        }
        catch(Exception e){
            throw e;
        }
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBloc() {
        return idBloc;
    }

    public void setIdBloc(int idBloc) {
        this.idBloc = idBloc;
    }

    public Date getDateTransformation() {
        return dateTransformation;
    }

    public void setDateTransformation(Date dateTransformation) {
        this.dateTransformation = dateTransformation;
    }
    
    public void setReste(double longueur, double largeur, double epaisseur){
        this.reste = new Bloc(longueur, largeur, epaisseur);
        reste.setDateProduction(this.getDateTransformation());
        reste.setPrixRevient(bloc.getPRUnite()*reste.calculateVolume());
        reste.setPrixAchat(bloc.getPAUnite()*reste.calculateVolume());
        reste.setSource(this.getBloc().getId());
    }
    
    public void setReste(Connect c) throws Exception{
        try{
            Bloc reste = new Bloc();
            reste.setSource(this.bloc.getId());
        }catch(Exception e){
            throw e;
        }
    }
    
    
    public void insertReste(Connect c) throws Exception{
        try{
            this.reste.createBloc(c);
        }catch(Exception e){
            throw e;
        }
    }
    public void createTransformation(Connect c)throws Exception{
        try {      
            String Query = "INSERT INTO transformation VALUES (default , ?, ?)";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(Query);
            preparedStatement.setInt(1, this.getIdBloc());
            preparedStatement.setDate(2 , this.getDateTransformation());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
            c.getConnex().commit();
        } catch (Exception e) {
            throw e;
        }
    }
    public int generateTransformation(Connect c) throws Exception {
    int generatedId = -1;
    try {
        String query = "INSERT INTO transformation VALUES (default, ?, ?) RETURNING id";
        PreparedStatement preparedStatement = c.getConnex().prepareStatement(query);
        preparedStatement.setInt(1, this.getIdBloc());
        preparedStatement.setDate(2, this.getDateTransformation());

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            generatedId = rs.getInt(1);
        }

        rs.close();
        preparedStatement.close();
        c.getConnex().commit();
    } catch (Exception e) {
        throw e;
    }
    return generatedId;
}

    
    public void insertTransformationWDetails(Connect c)throws Exception{
        try{
            controler(THETA);
            this.setId(this.generateTransformation(c));
            for(DetailsTransformation dt : detailsTransformation){
                dt.setIdTransformation(this.id);
                dt.createDetailsTransformation(c);
            }
            this.insertReste(c);  
        } catch(Exception e){
            throw e;
        }
    }
    
    
    public void setDetailsTransformation(Connect c) throws Exception { 
        try {
            String sql = "select * from DetailsTransformation WHERE idTransformation=?";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            preparedStatement.setInt(1, this.getId());
            ResultSet rs = preparedStatement.executeQuery();
            
            List<DetailsTransformation> results = new ArrayList<DetailsTransformation>();
            while(rs.next()){
                int id = rs.getInt(1);
                int idUsuelle = rs.getInt(2);
                int nb = rs.getInt(3);
                
                DetailsTransformation cueilleur = new DetailsTransformation(id, idUsuelle, nb, c);
                results.add(cueilleur);
            }
            preparedStatement.close();
            rs.close();
            
            this.detailsTransformation = results;
            
        } catch (Exception e) {
            throw e;
        }
    } 
    
    public void setDetailsTransformation(List<DetailsTransformation> list){
        this.detailsTransformation = list;
    }
    
    public double getPRFabrique(){
        double pr = 0;
        for(DetailsTransformation dt : detailsTransformation){
            pr += dt.getUsuelle().calculateVolume()*bloc.getPRUnite();
        }
        return pr;
    }
    public void setBloc(Connect c) throws Exception { 
        try {
            String sql = "select * from Bloc WHERE id=?";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            preparedStatement.setInt(1, this.getIdBloc());
            ResultSet rs = preparedStatement.executeQuery();
            
            Bloc bloc = null;
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
                
                int nb = rs.getInt(3);
                
                bloc = new Bloc(id, longueur, largeur, epaisseur, volume, prixRevient, prixAchat, dateProduction, source);
            }
            preparedStatement.close();
            rs.close();
            
            this.bloc = bloc;
            
        } catch (Exception e) {
            throw e;
        }
    } 

    public Bloc getBloc() {
        return bloc;
    }

    public List<DetailsTransformation> getDetailsTransformation() {
        return detailsTransformation;
    }
    
   public double getVolumeFabrique(){
        double pr = 0;
        for(DetailsTransformation dt : detailsTransformation){
            pr += dt.getUsuelle().calculateVolume();
        }
        return pr;
    }
    public void checkPR()throws Exception{
        if(this.getPRFabrique()+this.reste.getPrixRevient() != this.bloc.getPrixRevient()){
            throw new Exception("Le prix de revient insere n'est pas egal aux prix de revient attendu");
        }
    }
    
    public void checkPerte(double theta)throws Exception{
        if(this.bloc.calculateVolume()-(getVolumeFabrique()+this.reste.calculateVolume())>theta){
            throw new Exception("La perte a depassee le seuil autorise");
        }
    }
    public void controler(double theta)throws Exception{
        try{
            checkPR();
            checkPerte(theta);
        } catch(Exception e){
            throw e;
        }
    }
    
    public void getById(Connect c) throws Exception {
        try {
            String sql = "SELECT * FROM Transformation WHERE id = ?";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            preparedStatement.setInt(1, this.getId());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.idBloc = rs.getInt("idBloc");
                this.dateTransformation = rs.getDate("dateTransformation");
            }

            preparedStatement.close();
            rs.close();
        } catch (Exception e) {
            throw e;
        }
    }
    
    

    public double getPVTsotra(){
        double pv = 0;
        for(DetailsTransformation dt : detailsTransformation){
            pv += dt.getUsuelle().getPrixVente();
        }
        return pv;
    }
    
    public double getPVRentable(Connect c)throws Exception{
        double res = 0;
        try{
            Usuelle rent = Usuelle.getMostRentable(c);
            res = getPVTsotra()+(this.getBloc().getQteTheorique(rent)*rent.getPrixVente());
        }catch(Exception e){
            throw e;
        }
        return res;
    }
    
    public double getPVMoinsPerte(Connect c)throws Exception{
        double res = 0;
        try{
            Usuelle rent = Usuelle.getMoinsPerte(c);
            res = getPVTsotra()+(this.getBloc().getQteTheorique(rent)*rent.getPrixVente());
        }catch(Exception e){
            throw e;
        }
        return res;
    }
   
}
