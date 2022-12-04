package com.example.crous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val CROUS = "crous"

class CrousFragment: Fragment() {
    private lateinit var crousAdapter: CrousAdapter
    private lateinit var rcvCrous: RecyclerView
    //lateinit var searchView:SearchView
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
        val rootView = inflater.inflate(R.layout.fragment_crous, container, false)

        crousAdapter = CrousAdapter(crous)

        rcvCrous = rootView.findViewById(R.id.rcv_list_fragment)
        rcvCrous.adapter = crousAdapter

        val linearLayoutManager = LinearLayoutManager(context)
        rcvCrous.layoutManager = linearLayoutManager

        /*
        searchView = rootView.findViewById(R.id.searchView_list_fragment)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (crous.) {

                } else {
                    Toast.makeText( "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                crousAdapter.
                return false
            }
        })
        */
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