package com.example.crous

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CrousViewHolder (rootView : View): ViewHolder(rootView){
    var title = rootView.findViewById<TextView>(R.id.row_crous_txttitle)
    var type = rootView.findViewById<TextView>(R.id.row_crous_txttype)
    var address = rootView.findViewById<TextView>(R.id.row_crous_txtaddress)
    var description = rootView.findViewById<TextView>(R.id.row_crous_txtdescription)
    //var imageURL
}