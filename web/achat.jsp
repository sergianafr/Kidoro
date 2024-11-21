<%@ page import="java.util.List" %>
<%@ page import="Models.Usuelle" %>
<%@ page import="Models.Produit" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvel Achat</title>
</head>
<body>
    <h1>Nouvel achat</h1>
    <form action="AchatServlet" method="post">
        <label for="idProduit">Produit</label>
        <select id="idProduit" name="idProduit">
        <% List<Produit> listProduit = (List<Produit>) request.getAttribute("listProduit");
           if (listProduit != null) {
               for (Produit pr : listProduit) {
        %>
            <option value="<%= pr.getId() %>"><%= pr.getNom() %></option>
        <%     }
           }
        %>
    </select>
        <label for="qte">Quantite</label>
        <input type="number" name="qte" id="qte" step="0.00001">
        <label for="pu">Prix unitaire</label>
        <input type="number" name="pu" id="pu" step="0.00001">
        <label for="dateAchat">Date achat</label>
        <input type="datetime-local" name="dateAchat" id="dateAchat">
        <input type="submit" value="valider">
    </form>
</body>
</html>