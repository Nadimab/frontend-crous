package com.example.crous

import android.media.Image
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CrousViewHolder (rootView : View): ViewHolder(rootView){
    var title = rootView.findViewById<TextView>(R.id.row_crous_txttitle)
    var type = rootView.findViewById<TextView>(R.id.row_crous_txttype)
    var address = rootView.findViewById<TextView>(R.id.row_crous_txtaddress)
    var description = rootView.findViewById<TextView>(R.id.row_crous_txtdescription)
    var imageURL = rootView.findViewById<ImageView>(R.id.row_crous_imgview)
    var btn = rootView.findViewById<ImageButton>(R.id.row_crous_imgbtn)
    var fav = rootView.findViewById<CheckBox>(R.id.row_crous_checkbox)
}