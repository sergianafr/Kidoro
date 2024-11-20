package Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbUtils.Connect;

public class Machine {
    private int id;
    private String nomMachine;

    public int getId() {
        return id;
    }

    public String getNomMachine() {
        return nomMachine;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNomMachine(String nomMachine) {
        this.nomMachine = nomMachine;
    }

    public Machine(int id, String nomMachine) {
        this.id = id;
        this.nomMachine = nomMachine;
    }


    public static List<Machine> getAll(Connect c) throws Exception { 
        try {
            String sql = "select * from Machine";
            PreparedStatement preparedStatement = c.getConnex().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            List<Machine> results = new ArrayList<Machine>();
            while(rs.next()){
                int id = rs.getInt(1);
                String nomMachine = rs.getString(2);
                
                Machine unit = new Machine(id, nomMachine);
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
