package com.visiteur.visiteur_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "visiteur")
@Data
public class Visiteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numvisiteur;

    @Column(nullable = false)
    private String nom;

    @Column(name = "nombre_jours", nullable = false)
    private Integer nombreJours;

    @Column(name = "tarif_journalier", nullable = false)
    private BigDecimal tarifJournalier;

    @Transient
    public BigDecimal getTarif() {
        if (nombreJours != null && tarifJournalier != null) {
            return tarifJournalier.multiply(BigDecimal.valueOf(nombreJours));
        }
        return BigDecimal.ZERO;
    }
}