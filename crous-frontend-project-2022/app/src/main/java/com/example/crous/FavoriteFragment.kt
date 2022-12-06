package com.example.crous

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton



class FavoriteFragment : Fragment() {
    private lateinit var crousAdapter: CrousAdapter
    private lateinit var rcvCrous: RecyclerView
    private var crous: ArrayList<ReducedCrous> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            crous = it.getSerializable(CROUS) as ArrayList<ReducedCrous>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        // btnAddFav = view?.findViewById(R.id.row_crous_imgbtn)
        //title = view?.findViewById(R.id.row_crous_txttitle)
        crousAdapter = CrousAdapter(crous)

        rcvCrous = rootView.findViewById(R.id.rcv_favorite)
        rcvCrous.adapter = crousAdapter

        val linearLayoutManager = LinearLayoutManager(context)
        rcvCrous.layoutManager = linearLayoutManager

        return rootView
    }
    companion object {
        @JvmStatic
        fun newInstance(data: ArrayList<ReducedCrous>) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CROUS, data)
                }
            }
    }
}