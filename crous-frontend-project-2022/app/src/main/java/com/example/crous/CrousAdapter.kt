package com.example.crous

import CircleTransform
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.scaleMatrix
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val EXTRA_KEY = "extra-key"

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
        holder.fav.isChecked=crous.favorite
        holder.fav.setOnCheckedChangeListener { buttonView, isChecked ->
            val crousreduced = crousList.find { it.title.lowercase() == holder.title.text.toString().lowercase() }
            if (crousreduced != null) {
                crousService.toggleFavorite(crousreduced.id).enqueue(object : Callback<JSONObject>{
                    override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                    }

                    override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                        Toast.makeText(holder.btn.context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

        val img = Picasso.get().load(crous.photoURL).error(R.drawable.logo_default).transform(CircleTransform()).resize(300,300).into(holder.imageURL)
        holder.btn.setOnClickListener {
            var allData: ExpandedCrous?

            val crousreduced = crousList.find { it.title.lowercase() == holder.title.text.toString().lowercase() }
            if (crousreduced != null) {
                crousService.findOneById(crousreduced.id).enqueue(object : Callback<ExpandedCrous> {
                    override fun onResponse(
                        call: Call<ExpandedCrous>,
                        response: Response<ExpandedCrous>
                    ) {
                        allData = response.body()
                        val returnIntent = Intent(holder.btn.context,DetailActivity::class.java)
                        returnIntent.putExtra(EXTRA_KEY, allData)
                        //LocalBroadcastManager.getInstance(holder.btn.context).sendBroadcast(returnIntent);
                        holder.btn.context.startActivity(returnIntent)
                    }

                    override fun onFailure(call: Call<ExpandedCrous>, t: Throwable) {
                        Toast.makeText(holder.btn.context, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return crousList.size
    }
}