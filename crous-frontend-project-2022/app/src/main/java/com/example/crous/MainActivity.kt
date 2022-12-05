package com.example.crous

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
lateinit var crousService: CrousService
class MainActivity : AppCompatActivity() {

    private val crousCatalogue = CrousCatalogue()
    var tabLayout: TabLayout? = null
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getString(R.string.SERVER_BASE_URL))
            .build()

        crousService = retrofit.create(CrousService::class.java)

        readDataFromServer()
        tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // creating cases for fragment
                when (tab.position) {
                    0 -> displayCrousListFragment()
                    1 -> displayMapFragment()
                    2 -> displayFavFragment()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun displayCrousListFragment() {
        val crous = CrousFragment.newInstance(crousCatalogue.getAllCrous())
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, crous)
            .commit()
    }
    private fun readDataFromServer(){
        var allData: ReducedResponse?
        crousCatalogue.clean()
        crousService.findAll(0,10,0,"title",0,0,0).enqueue(object : Callback<ReducedResponse> {
            override fun onResponse(
                call: Call<ReducedResponse>,
                response: Response<ReducedResponse>
            ) {
                allData = response.body()
                allData?.returnData?.forEach{crousCatalogue.addCrous(it)}
                displayCrousListFragment()
            }
            override fun onFailure(call: Call<ReducedResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun displayMapFragment() {
        var allMapsData: List<MapsData>? = listOf()

        crousService.findMapsData(0,10000,0,"title",0,1).enqueue(object : Callback<List<MapsData>> {
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
    private fun displayFavFragment() {
        val favFragment = FavoriteFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout,favFragment)
            .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        //searchItem?.expandActionView()

        if (searchItem!=null) {
            val searchView = searchItem.actionView as SearchView

            searchView.queryHint = "Search"

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    var allData: ArrayList<ReducedCrous>?
                    crousCatalogue.clean()
                    crousService.searchByTitle(query).enqueue(object : Callback<ArrayList<ReducedCrous>> {
                        override fun onResponse(
                            call: Call<ArrayList<ReducedCrous>>,
                            response: Response<ArrayList<ReducedCrous>>
                        ) {
                            allData = ArrayList(response.body())
                            allData?.forEach{crousCatalogue.addCrous(it)}
                            displayCrousListFragment()
                        }
                        override fun onFailure(call: Call<ArrayList<ReducedCrous>>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    var allData: ArrayList<ReducedCrous>?
                    crousCatalogue.clean()
                    crousService.searchByTitle(newText).enqueue(object : Callback<ArrayList<ReducedCrous>> {
                        override fun onResponse(
                            call: Call<ArrayList<ReducedCrous>>,
                            response: Response<ArrayList<ReducedCrous>>
                        ) {
                            allData = ArrayList(response.body())
                            allData?.forEach{crousCatalogue.addCrous(it)}
                            displayCrousListFragment()
                        }
                        override fun onFailure(call: Call<ArrayList<ReducedCrous>>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                var allData: ReducedResponse?
                crousCatalogue.clean()
                crousService.findAll(0,10,0,"title",0,0,1).enqueue(object : Callback<ReducedResponse> {
                    override fun onResponse(
                        call: Call<ReducedResponse>,
                        response: Response<ReducedResponse>
                    ) {
                        allData = response.body()
                        allData?.returnData?.forEach{crousCatalogue.addCrous(it)}
                        displayCrousListFragment()
                    }
                    override fun onFailure(call: Call<ReducedResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }
                })

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}