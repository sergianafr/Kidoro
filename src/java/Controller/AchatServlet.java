/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.Achat;
import Models.Bloc;
import Models.Produit;
import Models.Usuelle;
import dbUtils.Connect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author SERGIANA
 */
public class AchatServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AchatServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AchatServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        Connect c = new Connect();
        try {
            c.connectToPostgres("kidoro", "Etu002610");
            List<Produit> listProduit = Produit.getAll(c);
            request.setAttribute("listProduit", listProduit);
            request.getRequestDispatcher("/achat.jsp").forward(request, response);
            
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
        Connect c = new Connect();
        String idProduit = request.getParameter("idProduit");
        String qteStr = request.getParameter("qte");
        String puStr = request.getParameter("pu");
        String dateAchatStr = request.getParameter("dateAchat");

        try {
            c.connectToPostgres("kidoro", "Etu002610");
            double qte = Double.parseDouble(qteStr);
            double pu = Double.parseDouble(puStr);
            Date dateAchat = Date.valueOf(dateAchatStr);
            Achat achat = new Achat(Integer.parseInt(idProduit), qte, pu, dateAchat);
            achat.createAchat(c);
            
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            c.closeBD();
        }
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
