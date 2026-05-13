package com.visiteur.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.visiteur.app.R
import com.visiteur.app.api.RetrofitClient
import kotlinx.coroutines.launch

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        title = "Statistiques"
        chargerStats()
    }

    private fun chargerStats() {
        lifecycleScope.launch {
            try {
                val statsRes    = RetrofitClient.instance.getStats()
                val visiteursRes = RetrofitClient.instance.getVisiteurs()

                if (statsRes.isSuccessful && visiteursRes.isSuccessful) {
                    val stats     = statsRes.body()    ?: return@launch
                    val visiteurs = visiteursRes.body() ?: return@launch

                    // BarChart
                    val barChart = findViewById<BarChart>(R.id.barChart)
                    val barEntries = listOf(
                        BarEntry(0f, stats["total"]?.toFloat() ?: 0f),
                        BarEntry(1f, stats["min"]?.toFloat()   ?: 0f),
                        BarEntry(2f, stats["max"]?.toFloat()   ?: 0f)
                    )
                    val barDataSet = BarDataSet(barEntries, "Tarifs (€)").apply {
                        colors = ColorTemplate.MATERIAL_COLORS.toList()
                        valueTextSize = 12f
                    }
                    barChart.data = BarData(barDataSet)
                    barChart.xAxis.valueFormatter =
                        com.github.mikephil.charting.formatter.IndexAxisValueFormatter(
                            listOf("Total", "Min", "Max")
                        )
                    barChart.description.isEnabled = false
                    barChart.animateY(1000)
                    barChart.invalidate()

                    // PieChart
                    val pieChart = findViewById<PieChart>(R.id.pieChart)
                    val pieEntries = visiteurs.map { v ->
                        PieEntry(v.tarif.toFloat(), v.nom)
                    }
                    val pieDataSet = PieDataSet(pieEntries, "Répartition").apply {
                        colors = ColorTemplate.COLORFUL_COLORS.toList()
                        valueTextSize = 11f
                    }
                    pieChart.data = PieData(pieDataSet)
                    pieChart.description.isEnabled = false
                    pieChart.animateY(1000)
                    pieChart.invalidate()
                }
            } catch (e: Exception) {
                Toast.makeText(this@StatsActivity,
                    "Erreur stats : ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}