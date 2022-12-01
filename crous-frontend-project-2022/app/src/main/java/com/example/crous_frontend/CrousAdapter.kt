package com.example.crous_frontend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class CrousAdapter(private var crousList: CrousCatalogue) : RecyclerView.Adapter<CrousViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrousViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_main, parent,
            false
        )
        return CrousViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CrousViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return crousList.getTotalNumberOfCrous()
    }
}