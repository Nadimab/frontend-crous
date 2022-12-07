package com.example.crous

import CircleTransform
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.common.server.converter.StringToIntConverter
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    lateinit var data : ExpandedCrous
    lateinit var img: ImageView
    lateinit var title: TextView
    lateinit var address: TextView
    lateinit var number: TextView
    lateinit var type: TextView
    lateinit var zone: TextView
    lateinit var email: TextView
    lateinit var closing: TextView
    lateinit var Info: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        data = getIntent().getSerializableExtra(EXTRA_KEY) as ExpandedCrous

        img = findViewById<ImageView>(R.id.detail_imgView)
        title = findViewById<TextView>(R.id.detail_title)
        address = findViewById<TextView>(R.id.detail_address)
        number = findViewById<TextView>(R.id.detail_number)
        type = findViewById<TextView>(R.id.detail_type)
        zone = findViewById<TextView>(R.id.detail_zone)
        email = findViewById<TextView>(R.id.detail_email)
        closing = findViewById<TextView>(R.id.detail_closing)
        Info = findViewById<TextView>(R.id.detail_info)

        Picasso.get().load(data.photoURL).error(R.drawable.logo_default).transform(CircleTransform()).resize(300,300).into(img)
        title.setText(data.title).toString()
        address.setText(data.address).toString()
        number.setText(data.phoneNumber).toString()
        type.setText(data.type).toString()
        zone.setText(data.zone).toString()
        email.setText(data.email).toString()
        if(data.closing.toInt() == 1) {
            closing.setText("Open 24/7").toString()
        }
        else {
            closing.setText("Open till 7:00 pm").toString()
        }
        Info.setText(data.info).toString()

        if(data.title.equals("")){
            title.setText("Title not available".toString())
        }
        if(data.address.equals("")){
            address.setText("Address not available".toString())
        }
        if(data.type.equals("")){
            type.setText("Type not available".toString())
        }
        if(data.email.equals("")){
            email.setText("Email not available".toString())
        }
        if(data.zone.equals("")){
            zone.setText("Zone not available".toString())
        }
        if(data.phoneNumber.equals("")){
            number.setText("Number not available".toString())
        }

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
