CREATE DATABASE visiteurdb;

\c visiteurdb

CREATE TABLE visiteur (
    numvisiteur SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    nombre_jours INTEGER NOT NULL,
    tarif_journalier DECIMAL(10, 2) NOT NULL
);

-- Données de test
INSERT INTO visiteur (nom, nombre_jours, tarif_journalier) VALUES
('Alice Martin', 3, 50.00),
('Bob Dupont', 5, 75.00),
('Claire Leroy', 2, 60.00),
('David Moreau', 7, 45.00);