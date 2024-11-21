/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.Bloc;
import Models.Machine;
import Models.Produit;
import Util.ConversionUtil;
import dbUtils.Connect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author SERGIANA
 */
public class InsertBloc extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Récupérer les données du formulaire
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connect c = new Connect();
        try {
            c.connectToPostgres("kidoro", "Etu002610");
            List<Machine> listProduit = Machine.getAll(c);
            request.setAttribute("listMachine", listProduit);
            request.getRequestDispatcher("/bloc.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des données.");
        }
        finally{
            c.closeBD();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        double longueur = Double.parseDouble(request.getParameter("longueur"));
        double largeur = Double.parseDouble(request.getParameter("largeur"));
        double epaisseur = Double.parseDouble(request.getParameter("epaisseur"));
        double volume = longueur * largeur * epaisseur;
        double prixRevientPratique = Double.parseDouble(request.getParameter("prixRevient"));
        Timestamp dateProduction = ConversionUtil.convertToTimestamp(request.getParameter("dateProduction"));
        int source = request.getParameter("source") != null ? Integer.parseInt(request.getParameter("source")) : 0;
        int idMachine = request.getParameter("idMachine") != null ? Integer.parseInt(request.getParameter("idMachine")) : 0;

        PrintWriter out = response.getWriter();
        out.println(dateProduction);
        // Préparation de la connexion et de la requête SQL
        // Connect c = new Connect(); // Initialisez votre objet Connect
        // try{
        //     c.connectToPostgres("kidoro", "Etu002610");
        //     Bloc b = new Bloc(0, longueur,largeur, epaisseur, prixRevientPratique, dateProduction, idMachine, source);
        //     b.createBlocMere(c);
        //     request.setAttribute("success", "Bloc insere avec succes");
            
            
           
        //     doGet(request, response);
            
        // }catch(Exception e){
        //     request.setAttribute("exception", e.getMessage());
        //     c.rollback();
        //     e.printStackTrace();
        //     doGet(request, response);
        // }
        // finally{
        //     c.closeBD();
        // }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
