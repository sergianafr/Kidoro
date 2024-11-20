CREATE TABLE Machine (
    id serial PRIMARY KEY,
    nomMachine varchar(50) unique NOT NULL,
);

CREATE TABLE bloc (
    id SERIAL PRIMARY KEY,
    longueur double precision NOT NULL,
    largeur double precision NOT NULL,
    epaisseur double precision NOT NULL,
    volume double precision NOT NULL,
    prixRevientTheorique double precision NOT NULL,
    prixRevientPratique double precision NOT NULL,
    dateProduction DATE NOT NULL,
    idMachine int REFERENCES Machine(id) ON DELETE CASCADE,
    source int default null,
    sourceMere int
);

CREATE TABLE Unite(
    id serial PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);

CREATE TABLE Produit(
    id serial PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    idUnite int REFERENCES Unite(id) ON DELETE CASCADE
);

CREATE TABLE Achat(
    id serial PRIMARY KEY,
    idProduit int REFERENCES Produit(id) ON DELETE CASCADE,
    quantite double precision NOT NULL,
    prixUnitaire double precision NOT NULL,
    dateAchat date NOT NULL
);

CREATE TABLE MvtStock (
    id serial PRIMARY KEY,
    idAchat int REFERENCES Achat(id) ON DELETE CASCADE,
    qteEntree double precision NOT NULL,
    qteSortie double precision NOT NULL,
    dateSaisie date NOT NULL,
);

CREATE TABLE Formule (
    id serial PRIMARY KEY,
    descri varchar(50) NOT NULL
);
CREATE TABLE DetailsFormule(
    idFormule int REFERENCES Formule(id),
    idProduit int REFERENCES Produit(id),
    qte double precision NOT NULL
);

CREATE MATERIALIZED VIEW V_EtatStock AS 
SELECT 
Achat.id, Achat.idProduit, Achat.dateAchat,Achat.prixUnitaire, 
SUM(qteEntree) - SUM(qteSortie) AS reste
FROM MvtStock 
JOIN Achat ON MvtStock.idAchat = Achat.id
GROUP BY idAchat
WITH DATA;

SELECT * FROM V_EtatStock WHERE reste > 0 ORDER BY dateAchat ASC; 

CREATE TABLE DetailsBloc(
    idBloc int REFERENCES bloc(id) PRIMARY KEY,
    idMvtStock int REFERENCES MvtStock(id) PRIMARY KEY,
);

CREATE TABLE usuelle (
    id SERIAL PRIMARY KEY,
    nom varchar(50),
    longueur Double Precision NOT NULL,
    largeur Double Precision NOT NULL,
    epaisseur Double Precision NOT NULL,
    volume Double Precision,
    prixVente DECIMAL(10,2) NOT NULL
);

CREATE TABLE transformation (
    id SERIAL PRIMARY KEY,
    idbloc INT REFERENCES bloc(id) ON DELETE CASCADE,
    dateTransformation DATE NOT NULL
);

CREATE TABLE detailsTransformation (
    idTransformation INT REFERENCES transformation(id) ON DELETE CASCADE,
    idUsuelle INT REFERENCES usuelle(id) ON DELETE CASCADE,
    nb INT NOT NULL,
    PrixRevient double precision not null
);

-- Get most rentable
SELECT * FROM usuelle WHERE
prixVente/volume=(SELECT max(prixVente/volume) FROM usuelle);

-- Get perte minimum
SELECT * FROM USUELLE WHERE
VOLUME = (SELECT min(volume) from usuelle);

-- Get bloc dispo
select * from bloc where id not in (select source from bloc);
SELECT (SUM(PRIXREVIENT)/SUM(NB)) as PRGlobal, IDUSUELLE FROM DETAILSTRANSFORMATION 
GROUP BY IDUSUELLE;






