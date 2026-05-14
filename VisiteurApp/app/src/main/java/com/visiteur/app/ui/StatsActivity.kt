package com.visiteur.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
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
                    val barDataSet = BarDataSet(barEntries, "Tarifs (Ar)").apply {
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

                    // PieChart — Total, Min, Max
                    val pieChart = findViewById<PieChart>(R.id.pieChart)
                    val pieEntries = listOf(
                        PieEntry(stats["total"]?.toFloat() ?: 0f, "Total"),
                        PieEntry(stats["min"]?.toFloat()   ?: 0f, "Min"),
                        PieEntry(stats["max"]?.toFloat()   ?: 0f, "Max")
                    )

                    val pieColors = listOf(
                        ColorTemplate.MATERIAL_COLORS[0],
                        ColorTemplate.MATERIAL_COLORS[1],
                        ColorTemplate.MATERIAL_COLORS[2]
                    )

                    val pieDataSet = PieDataSet(pieEntries, "").apply {
                        colors = pieColors
                        valueTextSize = 13f
                        valueTextColor = android.graphics.Color.WHITE
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "${value.toInt()} Ar"
                            }
                        }
                    }

                    pieChart.data = PieData(pieDataSet)
                    pieChart.description.isEnabled = false
                    pieChart.legend.isEnabled = true
                    pieChart.setEntryLabelColor(android.graphics.Color.WHITE)
                    pieChart.setEntryLabelTextSize(12f)
                    pieChart.animateY(1000)
                    pieChart.invalidate()
                }
            } catch (e: java.net.ConnectException) {
                Toast.makeText(this@StatsActivity,
                    "⚠️ Serveur inaccessible, vérifiez votre connexion",
                    Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@StatsActivity,
                    "Erreur stats : ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}