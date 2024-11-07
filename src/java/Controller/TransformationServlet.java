/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Models.Bloc;
import Models.DetailsTransformation;
import Models.Transformation;
import Models.Usuelle;
import dbUtils.Connect;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author SERGIANA
 */
public class TransformationServlet extends HttpServlet {

    private Exception exception = null;
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
            out.println("<title>Servlet TransformationServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TransformationServlet at " + request.getContextPath() + "</h1>");
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
            // Récupérer les données avec getAll() de Usuelle et Bloc
            List<Usuelle> listUsuelle = Usuelle.getAll(c);
            List<Bloc> listBloc = Bloc.getAll(c);
            
            // Ajouter les listes en tant qu'attributs de requête
            request.setAttribute("listUsuelle", listUsuelle);
            request.setAttribute("listBloc", listBloc);
            
            // Rediriger vers la page JSP qui affichera les données
            request.getRequestDispatcher("/transformation.jsp").forward(request, response);
            
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
        try{
            c.connectToPostgres("kidoro", "Etu002610");
            int idBloc = Integer.parseInt(request.getParameter("blocId"));
            Date dateTr = Date.valueOf(request.getParameter("dateProduction"));
            double longueur = Double.parseDouble(request.getParameter("long"));
            double largeur = Double.parseDouble(request.getParameter("larg"));
            double epaisseur = Double.parseDouble(request.getParameter("epais"));
            List<Usuelle> listUsuelle = Usuelle.getAll(c);
            
            // set la liste d'usuelle fabriquée
            List<DetailsTransformation> dt = new ArrayList<DetailsTransformation>();
            for(Usuelle us : listUsuelle){
                int nb = Integer.parseInt(request.getParameter("usuelle_"+String.valueOf(us.getId())));
                DetailsTransformation odt = new DetailsTransformation(0, us.getId(), nb, c);
                dt.add(odt);
            }
            // set le reste 
            Transformation transformation = new Transformation(0, idBloc, dateTr, dt, c);
            transformation.setReste(longueur, largeur, epaisseur);
            
            transformation.insertTransformationWDetails(c);
            
            request.setAttribute("success", "Transformation reussi");
            doGet(request, response);
            
        } catch (Exception e){
            request.setAttribute("exception", e.getMessage());
            doGet(request, response);
            //e.printStackTrace();
        } finally{
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
