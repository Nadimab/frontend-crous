package com.example.crous

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.scaleMatrix
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class CrousAdapter(private var crousList : List<ReducedCrous>) : RecyclerView.Adapter<CrousViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrousViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.crous_row, parent,
            false
        )
        return CrousViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CrousViewHolder, position: Int) {
        val crous = crousList[position]
        holder.title.text = crous.title
        holder.type.text = crous.type
        holder.address.text = crous.address
        holder.description.text = crous.shortDesc
        val img = Picasso.get().load(crous.photoURL).error(R.drawable.logo_default).resize(300,300).into(holder.imageURL)
    }

    override fun getItemCount(): Int {
        return crousList.size
    }
}