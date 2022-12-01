package com.example.crous_frontend

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CrousViewHolder (rootView : View): ViewHolder(rootView){
    var searchText = rootView.findViewById<TextView>(R.id.text_home)
}