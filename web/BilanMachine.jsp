<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>
<%@ page import="Models.V_BilanMachine" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bilan Machine</title>
</head>
<body>

<h1>Bilan Machine</h1>
<form action="BilanMachine" method="get">
    <select name="annee" id="annee">
        <option value="0">Tous</option>
        <option value="2022">2022</option>
        <option value="2023">2023</option>
        <option value="2024">2024</option>
    </select>
    <input type="submit" value="filtrer">
</form>

<table border="1">
    <thead>
        <tr>
            <th>ID Machine</th>
            <th>volume</th>
            <th>Prix Revient Pratique</th>
            <th>Prix Revient Theorique</th>
            <th>Difference</th>
        </tr>
    </thead>
    <tbody>
        <% 
            // Retrieve the listBilan attribute set by the servlet
            List<V_BilanMachine> listBilan = (List<V_BilanMachine>) request.getAttribute("listBilan");

            // Iterate through the list and display each item in a table row
            if (listBilan != null) {
                for (V_BilanMachine bilan : listBilan) {
        %>
            <tr>
                <td><%= bilan.getIdMachine() %></td>
                <td><%= bilan.getVolume() %></td>
                <td><%= bilan.getPrixRevientPratique() %></td>
                <td><%= bilan.getPrixRevientTheorique() %></td>
                <td><%= bilan.getDifference() %></td>
            </tr>
        <% 
                }
            }
        %>
    </tbody>
</table>

</body>
</html>
