package com.example.crous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val CROUS = "crous"

class CrousFragment: Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_crous, container, false)

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