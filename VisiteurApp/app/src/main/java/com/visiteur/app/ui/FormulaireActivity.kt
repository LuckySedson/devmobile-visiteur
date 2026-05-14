package com.visiteur.app.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.visiteur.app.R
import com.visiteur.app.api.RetrofitClient
import com.visiteur.app.model.Visiteur
import kotlinx.coroutines.launch

class FormulaireActivity : AppCompatActivity() {

    private var numvisiteur: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        val etNom    = findViewById<EditText>(R.id.etNom)
        val etJours  = findViewById<EditText>(R.id.etNombreJours)
        val etTarifJ = findViewById<EditText>(R.id.etTarifJournalier)
        val tvTarif  = findViewById<TextView>(R.id.tvTarifCalcule)
        val btnSave  = findViewById<Button>(R.id.btnSauvegarder)

        // Pré-remplir si modification
        numvisiteur = intent.getLongExtra("numvisiteur", 0)
        if (numvisiteur != 0L) {
            etNom.setText(intent.getStringExtra("nom"))
            etJours.setText(intent.getIntExtra("nombreJours", 0).toString())
            etTarifJ.setText(intent.getDoubleExtra("tarifJournalier", 0.0).toString())
            title = "Modifier visiteur"
        } else {
            title = "Ajouter visiteur"
        }

        btnSave.setOnClickListener {
            val nom    = etNom.text.toString().trim()
            val jours  = etJours.text.toString().toIntOrNull()
            val tarifJ = etTarifJ.text.toString().toDoubleOrNull()

            if (nom.isEmpty() || jours == null || tarifJ == null) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tarif = jours * tarifJ
            tvTarif.text = "Tarif total : ${tarif.toInt()} Ar"

            val visiteur = Visiteur(numvisiteur, nom, jours, tarifJ)

            lifecycleScope.launch {
                try {
                    android.util.Log.d("FORMULAIRE", "Envoi: nom=$nom, jours=$jours, tarifJ=$tarifJ")

                    val response = if (numvisiteur == 0L) {
                        RetrofitClient.instance.createVisiteur(visiteur)
                    } else {
                        RetrofitClient.instance.updateVisiteur(numvisiteur, visiteur)
                    }

                    // Log réponse
                    android.util.Log.d("FORMULAIRE", "Code: ${response.code()}")
                    android.util.Log.d("FORMULAIRE", "Erreur body: ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        Toast.makeText(this@FormulaireActivity,
                            if (numvisiteur == 0L) "Visiteur ajouté !" else "Visiteur modifié !",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormulaireActivity,
                            "Erreur serveur : ${response.code()}",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    android.util.Log.e("FORMULAIRE", "Exception: ${e.message}")
                    Toast.makeText(this@FormulaireActivity,
                        "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}