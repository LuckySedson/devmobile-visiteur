package com.visiteur.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.visiteur.app.adapter.VisiteurAdapter
import com.visiteur.app.api.RetrofitClient
import com.visiteur.app.model.Visiteur
import com.visiteur.app.ui.FormulaireActivity
import com.visiteur.app.ui.StatsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: VisiteurAdapter
    private val visiteurs = mutableListOf<Visiteur>()

    private lateinit var tvTotal: TextView
    private lateinit var tvMin: TextView
    private lateinit var tvMax: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VisiteurAdapter(visiteurs,
            onEdit   = { v -> ouvrirFormulaire(v) },
            onDelete = { v -> confirmerSuppression(v) }
        )
        recyclerView.adapter = adapter

        // Footer stats
        tvTotal = findViewById(R.id.tvTotal)
        tvMin   = findViewById(R.id.tvMin)
        tvMax   = findViewById(R.id.tvMax)

        // Boutons
        findViewById<Button>(R.id.btnAjouter).setOnClickListener {
            ouvrirFormulaire(null)
        }
        findViewById<Button>(R.id.btnStats).setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        chargerVisiteurs()
    }

    override fun onResume() {
        super.onResume()
        chargerVisiteurs()
    }

    private fun chargerVisiteurs() {
        lifecycleScope.launch {
            try {
                val reponse = RetrofitClient.instance.getVisiteurs()
                if (reponse.isSuccessful) {
                    val liste = reponse.body() ?: emptyList()
                    adapter.updateData(liste)
                    chargerStats()
                }
            } catch (e: java.net.ConnectException) {
                Toast.makeText(this@MainActivity,
                    "⚠️ Serveur inaccessible, vérifiez votre connexion",
                    Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity,
                    "Erreur : ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun chargerStats() {
        lifecycleScope.launch {
            try {
                val reponse = RetrofitClient.instance.getStats()
                if (reponse.isSuccessful) {
                    val stats = reponse.body() ?: return@launch
                    tvTotal.text = "Total : ${stats["total"]?.toInt()} Ar"
                    tvMin.text   = "Min   : ${stats["min"]?.toInt()} Ar"
                    tvMax.text   = "Max   : ${stats["max"]?.toInt()} Ar"
                }
            } catch (e: Exception) { /* silencieux */ }
        }
    }

    private fun ouvrirFormulaire(visiteur: Visiteur?) {
        val intent = Intent(this, FormulaireActivity::class.java)
        visiteur?.let {
            intent.putExtra("numvisiteur",     it.numvisiteur)
            intent.putExtra("nom",             it.nom)
            intent.putExtra("nombreJours",     it.nombreJours)
            intent.putExtra("tarifJournalier", it.tarifJournalier)
        }
        startActivity(intent)
    }

    private fun confirmerSuppression(visiteur: Visiteur) {
        AlertDialog.Builder(this)
            .setTitle("Supprimer")
            .setMessage("Supprimer ${visiteur.nom} ?")
            .setPositiveButton("Oui") { _, _ ->
                lifecycleScope.launch {
                    try {
                        RetrofitClient.instance.deleteVisiteur(visiteur.numvisiteur)
                        chargerVisiteurs()
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity,
                            "Erreur suppression", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}