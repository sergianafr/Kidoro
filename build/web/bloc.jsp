<%-- 
    Document   : CreationBloc
    Created on : 6 nov. 2024, 23:46:32
    Author     : SERGIANA
--%>
<%@ page import="Models.Machine" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire Bloc</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
        }

        h2 {
            color: #333;
        }

        .nav-links {
            margin-bottom: 20px;
        }

        .nav-links a {
            margin-right: 10px;
            text-decoration: none;
            color: #007BFF;
            font-weight: bold;
        }

        .nav-links a:hover {
            text-decoration: underline;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }

        label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
        }

        input[type="number"],
        input[type="date"],
        button {
            width: 100%;
            padding: 10px;
            margin: 5px 0 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button {
            background-color: #28a745;
            color: #fff;
            border: none;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover {
            background-color: #218838;
        }

        .success-message {
            color: #28a745;
            font-weight: bold;
            margin-top: 15px;
        }
    </style>
</head>
<body>

    <div class="nav-links">
        <a href="TransformationServlet">Transformation</a>
        <a href="BilanServlet">Bilan</a>
        <a href="AchatServlet">Achat</a>
        <a href="BilanMachine">Bilan machine</a>
    </div>

    <h2>Formulaire de saisie pour Bloc</h2>
    <form action="InsertBloc" method="post">
        <label for="idMachine">Machine</label>
        <select id="idMachine" name="idMachine">
        <% List<Machine> listProduit = (List<Machine>) request.getAttribute("listMachine");
           if (listProduit != null) {
               for (Machine pr : listProduit) {
        %>
            <option value="<%= pr.getId() %>"><%= pr.getNomMachine() %></option>
        <%     }
           }
        %>
        </select>       
        <label for="longueur">Longueur :</label>
        <input type="number" step="0.001" id="longueur" name="longueur" required>

        <label for="largeur">Largeur :</label>
        <input type="number" step="0.0001" id="largeur" name="largeur" required>

        <label for="epaisseur">Epaisseur :</label>
        <input type="number" step="0.0001" id="epaisseur" name="epaisseur" required>

        <label for="prixRevient">Prix de revient :</label>
        <input type="number" step="0.0001" id="prixRevient" name="prixRevient" required>

        <label for="dateProduction">Date de production :</label>
        <input type="date" id="dateProduction" name="dateProduction" required>
        
        <button type="submit">Valider</button>
    </form>
     <% String error = (String) request.getAttribute("exception");
       if (error != null) { %>
       <p class="error-message"><%= error %></p>
    <% } %>
    
    <% String successMessage = (String) request.getAttribute("success");
       if (successMessage != null) { %>
       <p class="success-message"><%= successMessage %></p>
    <% } %>

    


</body>
</html>
