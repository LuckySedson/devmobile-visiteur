package com.visiteur.app.model

import com.google.gson.annotations.SerializedName

data class Visiteur(
    @SerializedName("numvisiteur")
    val numvisiteur: Long = 0,

    @SerializedName("nom")
    val nom: String = "",

    @SerializedName("nombreJours")
    val nombreJours: Int = 0,

    @SerializedName("tarifJournalier")
    val tarifJournalier: Double = 0.0
) {
    val tarif: Double
        get() = nombreJours * tarifJournalier
}