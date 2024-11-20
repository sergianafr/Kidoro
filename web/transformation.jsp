<%@ page import="java.util.List" %>
<%@ page import="Models.Usuelle" %>
<%@ page import="Models.Bloc" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire de Saisie</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        h2 {
            color: #333;
            text-align: center;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            width: 100%;
        }

        label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
        }

        input[type="date"],
        input[type="number"],
        select,
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
            width: 100%;
        }

        button:hover {
            background-color: #218838;
        }

        .usuelle-section, .reste-section {
            margin-top: 20px;
            padding-top: 10px;
            border-top: 1px solid #ddd;
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

        .usuelle-section label {
            font-size: 0.9em;
            font-weight: normal;
        }
        .error-message {
            color: red;
            font-weight: bold;
            margin-top: 15px;
        }
        .success-message {
            color: green;
            font-weight: bold;
            margin-top: 15px;
        }
    </style>
</head>
<body>

    <div class="nav-links">
        <a href="index.jsp">Insertion bloc</a>
        <a href="BilanServlet">Bilan</a>
    </div>
<h2>Transformation de bloc</h2>

<form action="TransformationServlet" method="post">
    <label for="dateProduction">Date de transformation :</label>
    <input type="date" id="dateProduction" name="dateProduction" required>

    <label for="blocSelect">Selectionnez un Bloc :</label>
    <select id="blocSelect" name="blocId">
        <% List<Bloc> listBloc = (List<Bloc>) request.getAttribute("listBloc");
           if (listBloc != null) {
               for (Bloc bloc : listBloc) {
                   String displayText = bloc.getId() + " - " + bloc.getLongueur() + "*" + bloc.getLargeur() + "*" + bloc.getEpaisseur();
        %>
            <option value="<%= bloc.getId() %>"><%= displayText %></option>
        <%     }
           }
        %>
    </select>

    <div class="usuelle-section">
        <p>Entrez la quantite obtenue pour chaque forme usuelle</p>
        <% List<Usuelle> listUsuelle = (List<Usuelle>) request.getAttribute("listUsuelle");
           if (listUsuelle != null) {
               for (Usuelle usuelle : listUsuelle) {
                   String labelText = "Usuelle ID: " + usuelle.getId() + " - " + usuelle.getLongueur() + "x" + usuelle.getLargeur() + "x" + usuelle.getEpaisseur();
        %>
            <label for="usuelle_<%= usuelle.getId() %>"><%= labelText %> :</label>
            <input type="number" id="usuelle_<%= usuelle.getId() %>" name="usuelle_<%= usuelle.getId() %>" step="0.01">
        <%     }
           }
        %>
    </div>

    <div class="reste-section">
        <p>Entrez les dimensions du reste</p>
        <label for="long">Longueur :</label>
        <input type="number" id="long" name="long" step="0.0001">

        <label for="larg">Largeur :</label>
        <input type="number" id="larg" name="larg" step="0.0001">

        <label for="epais">Epaisseur :</label>
        <input type="number" id="epais" name="epais" step="0.0001">
    </div>
    
    <button type="submit">Creer</button>
    <% String error = (String) request.getAttribute("exception");
       if (error != null) { %>
       <p class="error-message"><%= error %></p>
    <% } %>
    
    <% String successMessage = (String) request.getAttribute("success");
       if (successMessage != null) { %>
       <p class="success-message"><%= successMessage %></p>
    <% } %>
</form>
    <h2>Modifier Prix Revient</h2>
    <form action="Modifier" method="post">
        <label for="bloc">Selectionnez un Bloc :</label>
        <select id="bloc" name="bloc">
            <% List<Bloc> listB = (List<Bloc>) request.getAttribute("listBloc");
            if (listB != null) {
                for (Bloc bloc : listB) {
                    String displayText = bloc.getId() + " - " + bloc.getLongueur() + "*" + bloc.getLargeur() + "*" + bloc.getEpaisseur();
            %>
                <option value="<%= bloc.getId() %>"><%= displayText %></option>
            <%     }
            }
            %>
        </select>
        <label for="pr">Nouveau prix de revient:</label>
        <input type="number" id="pr" name="pr" step="0.0001">
    </form>
</body>
</html>
