package com.visiteur.visiteur_api.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visiteur.visiteur_api.model.Visiteur;

@Repository
public interface VisiteurRepository extends JpaRepository<Visiteur, Long> {

    @Query("SELECT MIN(v.nombreJours * v.tarifJournalier) FROM Visiteur v")
    BigDecimal findTarifMin();

    @Query("SELECT MAX(v.nombreJours * v.tarifJournalier) FROM Visiteur v")
    BigDecimal findTarifMax();

    @Query("SELECT SUM(v.nombreJours * v.tarifJournalier) FROM Visiteur v")
    BigDecimal findTarifTotal();
}