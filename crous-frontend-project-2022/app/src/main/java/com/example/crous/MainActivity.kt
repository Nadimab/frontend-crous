package com.example.crous

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val crousCatalogue = CrousCatalogue()
    var tabLayout: TabLayout? = null
    lateinit var retrofit: Retrofit
    lateinit var crousService: CrousService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val crous1 = ReducedCrous(
            "1",
            "Sushi",
            "Sushi and co",
            "Sushi very delicious",
            "Gardanne",
            "01686955",
            "sushi@gmail.com",
            1,
            "Click"
        )
        val crous2 = ReducedCrous(
            "2",
            "Chicken",
            "MCchicken",
            "Chicken sandwiches and wrap",
            "Gardanne",
            "01756897",
            "chicken@gmail.com",
            1,
            "UnClick"
        )

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getString(R.string.SERVER_BASE_URL))
            .build()

        crousService = retrofit.create(CrousService::class.java)

        crousCatalogue.addCrous(crous1)
        crousCatalogue.addCrous(crous2)
        displayCrousListFragment()
        tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // creating cases for fragment
                when (tab.position) {
                    0 -> displayCrousListFragment()
                    1 -> displayMapFragment()
                    2 -> displayInfoFragment()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun displayCrousListFragment() {
        val crousListFragment = CrousFragment.newInstance(crousCatalogue.getAllCrous())
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, crousListFragment)
            .commit()
    }
    private fun displayMapFragment() {
        var allMapsData: List<MapsData>? = listOf()

        crousService.findAll(0,10000,0,"title",0,1).enqueue(object : Callback<List<MapsData>> {
                override fun onResponse(
                    call: Call<List<MapsData>>,
                    response: Response<List<MapsData>>
                ) {
                    allMapsData = response.body()

                    val mapFragment = MapsFragment.newInstance(ArrayList(allMapsData))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.a_main_frame_layout, mapFragment)
                        .commit()

                }
                override fun onFailure(call: Call<List<MapsData>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }
                })
    }
    private fun displayInfoFragment() {
        val infoFragment = AppInfoFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout,infoFragment)
            .commit()
    }
}