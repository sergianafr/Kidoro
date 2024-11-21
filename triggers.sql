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

ALTER TABLE usuelle DISABLE TRIGGER before_insert_calculate_volume_usuelle;
ALTER TABLE bloc DISABLE TRIGGER before_insert_calculate_volume;

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

ALTER TABLE bloc DISABLE TRIGGER trg_update_prix_revient;

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
ALTER TABLE bloc DISABLE TRIGGER trg_set_source_mere_on_insert;

DROP TRIGGER IF EXISTS trg_set_source_mere_on_update ON bloc;
CREATE TRIGGER trg_set_source_mere_on_update
BEFORE UPDATE ON bloc
FOR EACH ROW
EXECUTE FUNCTION set_source_mere_on_insert();

ALTER TABLE bloc DISABLE TRIGGER trg_set_source_mere_on_update;


--- entree mvt Stock 
CREATE OR REPLACE FUNCTION insert_entree_mvtstock()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO MvtStock (idAchat, qteEntree, qteSortie, dateSaisie)
    VALUES (NEW.id, NEW.quantite, 0, NEW.dateAchat);
    refresh materialized view v_etatstock with data;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
DROP TRIGGER IF EXISTS trg_insert_mvt_stock ON Achat;
CREATE TRIGGER trg_insert_mvt_stock
AFTER INSERT ON Achat
FOR EACH ROW
EXECUTE FUNCTION insert_entree_mvtstock();

--- verification entree bloc 
CREATE OR REPLACE FUNCTION check_machine_usage()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM bloc
        WHERE idMachine = NEW.idMachine
          AND dateProduction = NEW.dateProduction
    ) THEN
        RAISE EXCEPTION 'La machine % est deja occupee pour la date et heure %', NEW.idMachine, NEW.dateProduction;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_verif_machine
BEFORE INSERT ON bloc
FOR EACH ROW
EXECUTE FUNCTION check_machine_usage();






 