CREATE TABLE bloc (
    id SERIAL PRIMARY KEY,
    longueur double precision NOT NULL,
    largeur double precision NOT NULL,
    epaisseur double precision NOT NULL,
    volume double precision NOT NULL,
    prixRevient double precision NOT NULL,
    prixAchat double precision NOT NULL,
    dateProduction TIMESTAMP NOT NULL,
    source int default null,
    sourceMere int
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
    dateTransformation TIMESTAMP NOT NULL
);

CREATE TABLE detailsTransformation (
    idTransformation INT REFERENCES transformation(id) ON DELETE CASCADE,
    idUsuelle INT REFERENCES usuelle(id) ON DELETE CASCADE,
    nb INT NOT NULL,
    PrixRevient double precision not null
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

------ calcul volume
CREATE OR REPLACE FUNCTION calculate_volume()
RETURNS TRIGGER AS $$
BEGIN
    NEW.volume := NEW.longueur * NEW.largeur * NEW.epaisseur;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_insert_calculate_volume
BEFORE INSERT ON bloc
FOR EACH ROW
EXECUTE FUNCTION calculate_volume();

CREATE TRIGGER before_insert_calculate_volume_usuelle
BEFORE INSERT ON usuelle
FOR EACH ROW
EXECUTE FUNCTION calculate_volume();



--- update prix revient
CREATE OR REPLACE FUNCTION update_prix_revient_trigger()
RETURNS TRIGGER AS $$
DECLARE
    old_prixRevient DOUBLE PRECISION;
    new_prixRevient DOUBLE PRECISION;
    proportion DOUBLE PRECISION;
BEGIN
    old_prixRevient := OLD.prixRevient;
    new_prixRevient := NEW.prixRevient;
    proportion := new_prixRevient / old_prixRevient;

    UPDATE bloc
    SET prixRevient = prixRevient * proportion
    WHERE source = OLD.id;

    UPDATE detailsTransformation
    SET PrixRevient = PrixRevient * proportion
    WHERE idTransformation IN (
        SELECT id FROM transformation WHERE idbloc = OLD.id
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_prix_revient
AFTER UPDATE OF prixRevient ON bloc
FOR EACH ROW
WHEN (OLD.prixRevient IS DISTINCT FROM NEW.prixRevient)
EXECUTE FUNCTION update_prix_revient_trigger();



--- set source mere

CREATE OR REPLACE FUNCTION get_source_mere(bloc_id INT) 
RETURNS INT AS $$
DECLARE
    current_source INT;
BEGIN
    SELECT source INTO current_source
    FROM bloc
    WHERE id = bloc_id;

    IF current_source = 0 THEN
        RETURN bloc_id;  
    ELSE
        RETURN get_source_mere(current_source);
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_source_mere_on_insert() 
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.source != 0 THEN
        NEW.sourceMere := get_source_mere(NEW.source);
    ELSE
        NEW.sourceMere := NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_set_source_mere_on_insert ON bloc;
CREATE TRIGGER trg_set_source_mere_on_insert
BEFORE INSERT ON bloc
FOR EACH ROW
EXECUTE FUNCTION set_source_mere_on_insert();

DROP TRIGGER IF EXISTS trg_set_source_mere_on_update ON bloc;
CREATE TRIGGER trg_set_source_mere_on_update
BEFORE UPDATE ON bloc
FOR EACH ROW
EXECUTE FUNCTION set_source_mere_on_insert();

--- donnees usuelles
INSERT INTO usuelle(nom,longueur,largeur,epaisseur, prixVente) VALUES 
('U1', 16, 4, 2, 20000), ('U2', 10, 7, 1, 12000), ('U3', 5, 1, 1, 600);

-- CREATE TABLE resTransformation (
--     idTransformation INT REFERENCES transformation(id) ON DELETE CASCADE,
--     nbU1 INT,
--     nbU2 INT,
--     nbU3 INT,
--     nbU4 INT  -- Note: renamed duplicate nbU3 to nbU4 to avoid duplicate column names
-- );

-- CREATE TABLE sourceBloc (
--     idBloc INT REFERENCES bloc(id) ON DELETE CASCADE,
--     source INT REFERENCES bloc(id) ON DELETE CASCADE
-- );

100, 250, 500 r=500, 2, 3


