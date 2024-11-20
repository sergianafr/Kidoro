CREATE OR REPLACE FUNCTION get_pr_theorique(   
    formule_id INT,       
    volume_m3 DOUBLE PRECISION,
    date_production DATE
) RETURNS DOUBLE PRECISION AS $$
DECLARE
    total_prix_revient DOUBLE PRECISION := 0; 
    produit RECORD;                        
    reste_a_consumer DOUBLE PRECISION;     
    stock RECORD;                   
    prix_unitaire DOUBLE PRECISION;     
BEGIN
    FOR produit IN 
        SELECT DF.idProduit, DF.qte * volume_m3 AS qte_necessaire
        FROM DetailsFormule DF
        WHERE DF.idFormule = formule_id
    LOOP
        reste_a_consumer := produit.qte_necessaire;

        FOR stock IN
            SELECT * 
            FROM V_EtatStock 
            WHERE idProduit = produit.idProduit AND reste > 0 AND dateAchat <= date_production
            ORDER BY dateAchat ASC
        LOOP
            IF reste_a_consumer <= 0 THEN
                EXIT;
            END IF;

            IF stock.reste >= reste_a_consumer THEN
                prix_unitaire := stock.prixUnitaire; 
                total_prix_revient := total_prix_revient + (reste_a_consumer * prix_unitaire);

                INSERT INTO MvtStock (idAchat, qteEntree, qteSortie, dateSaisie)
                VALUES (stock.id, 0, reste_a_consumer, CURRENT_DATE);

                reste_a_consumer := 0; 
            ELSE
                prix_unitaire := stock.prixUnitaire; 
                total_prix_revient := total_prix_revient + (stock.reste * prix_unitaire);

                INSERT INTO MvtStock (idAchat, qteEntree, qteSortie, dateSaisie)
                VALUES (stock.id, 0, stock.reste, CURRENT_DATE);

                reste_a_consumer := reste_a_consumer - stock.reste; 
            END IF;
        END LOOP;

        IF reste_a_consumer > 0 THEN
            RAISE EXCEPTION 'Stock insuffisant pour le produit %', produit.idProduit;
        END IF;
    END LOOP;
    refresh materialized view v_etatstock with data;

    RETURN total_prix_revient;
END;
$$ LANGUAGE plpgsql;
