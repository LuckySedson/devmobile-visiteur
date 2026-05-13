package com.visiteur.app.model

data class Visiteur(
    val numvisiteur: Long = 0,
    val nom: String = "",
    val nombreJours: Int = 0,
    val tarifJournalier: Double = 0.0
) {
    val tarif: Double
        get() = nombreJours * tarifJournalier
}