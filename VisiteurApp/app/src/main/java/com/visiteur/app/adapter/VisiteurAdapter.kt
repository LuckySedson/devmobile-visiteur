package com.visiteur.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.visiteur.app.R
import com.visiteur.app.model.Visiteur

class VisiteurAdapter(
    private var visiteurs: MutableList<Visiteur>,
    private val onEdit: (Visiteur) -> Unit,
    private val onDelete: (Visiteur) -> Unit
) : RecyclerView.Adapter<VisiteurAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNom: TextView         = view.findViewById(R.id.tvNom)
        val tvNombreJours: TextView = view.findViewById(R.id.tvNombreJours)
        val tvTarifJ: TextView      = view.findViewById(R.id.tvTarifJournalier)
        val tvTarif: TextView       = view.findViewById(R.id.tvTarif)
        val btnEdit: ImageButton    = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton  = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visiteur, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val v = visiteurs[position]
        holder.tvNom.text         = v.nom
        holder.tvNombreJours.text = "${v.nombreJours} jours"
        holder.tvTarifJ.text = "${v.tarifJournalier.toInt()} Ar/jour"
        holder.tvTarif.text  = "Total : ${v.tarif.toInt()} Ar"

        holder.btnEdit.setOnClickListener   { onEdit(v) }
        holder.btnDelete.setOnClickListener { onDelete(v) }
    }

    override fun getItemCount() = visiteurs.size

    fun updateData(newList: List<Visiteur>) {
        visiteurs.clear()
        visiteurs.addAll(newList)
        notifyDataSetChanged()
    }
}