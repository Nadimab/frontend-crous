package com.example.crous

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

 const val CROUS = "crous"

class CrousFragment: Fragment() {
    private lateinit var crousAdapter: CrousAdapter
    private lateinit var rcvCrous: RecyclerView
    private var crous: ArrayList<ReducedCrous> = arrayListOf()
    //var btnAddFav: Button? = null
    //var title: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            crous = it.getSerializable(CROUS) as ArrayList<ReducedCrous>
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_crous, container, false)

       // btnAddFav = view?.findViewById(R.id.row_crous_imgbtn)
        //title = view?.findViewById(R.id.row_crous_txttitle)
        crousAdapter = CrousAdapter(crous)

        rcvCrous = rootView.findViewById(R.id.rcv_list_fragment)
        rcvCrous.adapter = crousAdapter

        val linearLayoutManager = LinearLayoutManager(context)
        rcvCrous.layoutManager = linearLayoutManager

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(crous: ArrayList<ReducedCrous>) =
            CrousFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CROUS, crous)
                }
            }
    }
}