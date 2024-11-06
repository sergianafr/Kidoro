/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import dbUtils.Connect;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author SERGIANA
 */
public class Bilan {
    private Transformation transformation;
    private double pvTsotra;
    private double pvRentable;
    private double pvMoinsPerte;

    
    public Bilan(Transformation t, Connect c)throws Exception{
        try{
            this.setTransformation(t);
            this.setPvTsotra(t.getPVTsotra());
            this.setPvRentable(t.getPVRentable(c));
            this.setPvMoinsPerte(t.getPVMoinsPerte(c)); 
        } catch(Exception e){
            throw e;
        }
        
    }
    public Transformation getTransformation() {
        return transformation;
    }
    
    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public double getPvTsotra() {
        return pvTsotra;
    }

    public void setPvTsotra(double pvTsotra) {
        this.pvTsotra = pvTsotra;
    }

    public double getPvRentable() {
        return pvRentable;
    }

    public void setPvRentable(double pvRentable) {
        this.pvRentable = pvRentable;
    }

    public double getPvMoinsPerte() {
        return pvMoinsPerte;
    }

    public void setPvMoinsPerte(double pvMoinsPerte) {
        this.pvMoinsPerte = pvMoinsPerte;
    }
    
    
    public Vector<Bilan> getAllBilan(Connect c)throws Exception{
        try{
            List<Transformation> list = Transformation.getAll(c);
            Vector<Bilan> listBilan = new Vector<Bilan>();
            for(Transformation tr : list){
                Bilan b = new Bilan(tr, c);
                listBilan.add(b);
            }
            return listBilan;
        }catch(Exception e){
            throw e;
        }
    }
    
    
}
