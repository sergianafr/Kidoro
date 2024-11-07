<%-- 
    Document   : Bilan
    Created on : 7 nov. 2024, 05:55:59
    Author     : SERGIANA
--%>

<%@page import="java.util.Locale"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page import="Models.DetailsTransformation" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>
<%@ page import="Models.Bloc" %>
<%@ page import="Models.Usuelle" %>
<%@ page import="Models.Transformation" %>
<%@ page import="Models.Bilan" %>
<% Locale frenchLocale= Locale.FRENCH;
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(frenchLocale);
    
    df.applyPattern("#,##0.0#");

%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Tableau des Blocs et Usuelles</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 20px;
        }

        h2 {
            text-align: center;
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #4CAF50;
            color: white;
            font-weight: bold;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #e1f5fe;
        }
    </style>
</head>
<body>

<h2>Bilan des Transformations</h2>

<table>
    <thead>
        <tr>
            <th>Bloc</th>
            <th>Source Bloc</th>
            <th>Date Transformation</th>
            
            <!-- Colonnes dynamiques pour chaque Usuelle -->
            <%
                List<Usuelle> listUsuelle = (List<Usuelle>) request.getAttribute("listUsuelle");
                if (listUsuelle != null) {
                    for (Usuelle usuelle : listUsuelle) {
            %>
                <th><%= "U" + usuelle.getId() %></th>
            <%
                    }
                }
            %>

            <th>Prix Vente Tsotra</th>
            <th>Prix Vente Plus Rentable</th>
            <th>Prix Vente Moindre Perte</th>
        </tr>
    </thead>
    <tbody>
        <%
            Vector<Bilan> listBilan = (Vector<Bilan>) request.getAttribute("listBilan");
            if (listBilan != null) {
                for (Bilan bilan : listBilan) {
                    List<DetailsTransformation> dt = bilan.getTransformation().getDetailsTransformation();
        %>
        <tr>
            <td><%= bilan.getTransformation().getIdBloc() %></td>
            <td><%= bilan.getTransformation().getBloc().getSource() %></td>
            <td><%= bilan.getTransformation().getDateTransformation() %></td>
            <% for (DetailsTransformation dtr : dt) { %>
                <td><%= dtr.getNb() %></td>
            <% } %>
            <td><%= df.format(bilan.getPvTsotra()) %></td>
            <td><%= df.format(bilan.getPvRentable()) %></td>
            <td><%= df.format(bilan.getPvMoinsPerte()) %></td>
        </tr>
        <%
                }
            }
        %>
    </tbody>
</table>

</body>
</html>
