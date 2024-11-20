/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.Bloc;
import dbUtils.Connect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
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
        double longueur = Double.parseDouble(request.getParameter("longueur"));
        double largeur = Double.parseDouble(request.getParameter("largeur"));
        double epaisseur = Double.parseDouble(request.getParameter("epaisseur"));
        double volume = longueur * largeur * epaisseur;
        double prixRevientPratique = Double.parseDouble(request.getParameter("prixRevient"));
        Date dateProduction = Date.valueOf(request.getParameter("dateProduction"));
        int source = request.getParameter("source") != null ? Integer.parseInt(request.getParameter("source")) : 0;
        int idMachine = request.getParameter("idMachine") != null ? Integer.parseInt(request.getParameter("idMachine")) : 0;

        // Préparation de la connexion et de la requête SQL
        Connect c = new Connect(); // Initialisez votre objet Connect
        try{
            c.connectToPostgres("kidoro", "Etu002610");
            Bloc b = new Bloc(0, longueur,largeur, epaisseur, prixRevientPratique, dateProduction, idMachine, source);
            b.createBloc(c);
            request.setAttribute("success", "Bloc insere avec succes");
           
            
            // Rediriger vers la page JSP qui affichera les données
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            c.closeBD();
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
