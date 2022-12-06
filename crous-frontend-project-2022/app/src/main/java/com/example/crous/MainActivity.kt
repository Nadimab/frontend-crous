package com.example.crous

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var crousService: CrousService
class MainActivity : AppCompatActivity() {

    lateinit var next_btn: Button
    lateinit var back_btn: Button
    var menu_item_search: MenuItem? = null
    var menu_item_reload: MenuItem? = null
    lateinit var first: TextView
    lateinit var current: TextView
    lateinit var last: TextView

    var counter: Int = 0;
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

        //readDataFromServer()
        tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // creating cases for fragment
                when (tab.position) {
                    0 -> readDataFromServer(counter)
                    1 -> readMapFromServer()
                    2 -> readFavFromServer()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        next_btn = findViewById<Button>(R.id.button_next)
        back_btn = findViewById<Button>(R.id.button_back)
        first = findViewById<TextView>(R.id.first_page)
        current = findViewById<TextView>(R.id.current_page)
        last = findViewById<TextView>(R.id.last_page)

        readDataFromServer(counter)
        back_btn.isEnabled = false
        next_btn.setOnClickListener{
            counter++
            back_btn.isEnabled = true
            readDataFromServer(counter)
        }
        back_btn.setOnClickListener{
            counter--
            back_btn.isEnabled = counter != 0
            readDataFromServer(counter)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu_item_search = menu.getItem(0)
            menu_item_reload = menu.getItem(1)
        };

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

    private fun displayCrousListFragment() {
        val crous = CrousFragment.newInstance(crousCatalogue.getAllCrous())
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, crous)
            .commit()
        back_btn.visibility = View.VISIBLE
        next_btn.visibility = View.VISIBLE
        first.visibility = View.VISIBLE
        current.visibility = View.VISIBLE
        last.visibility = View.VISIBLE
    }

    private fun readDataFromServer(counter: Int){
        menu_item_search?.let{it.isVisible = true}
        menu_item_reload?.let{it.isVisible = true}
        var allData: ReducedResponse?
        crousCatalogue.clean()
        crousService.findAll(counter,10,0,"title",0,0,0).enqueue(object : Callback<ReducedResponse> {
            override fun onResponse(
                call: Call<ReducedResponse>,
                response: Response<ReducedResponse>
            ) {
                allData = response.body()
                allData?.returnData?.forEach{crousCatalogue.addCrous(it)}
                displayCrousListFragment()
                allData?.let { first.setText(it.first.toString() + " .. ") }
                allData?.let { current.setText(" .. " + it.current.toString() +" .. ") }
                allData?.let { last.setText(" .. " +it.last.toString())}
            }
            override fun onFailure(call: Call<ReducedResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun readFavFromServer(){
        menu_item_search?.let{it.isVisible = false}
        menu_item_reload?.let{it.isVisible = false}

        var allData: ReducedResponse?
        crousCatalogue.clean()
        crousService.findAll(0,10000,0,"title",1,0,0).enqueue(object : Callback<ReducedResponse> {
            override fun onResponse(
                call: Call<ReducedResponse>,
                response: Response<ReducedResponse>
            ) {
                allData = response.body()
                allData?.returnData?.let { displayFavFragment(it) }
            }
            override fun onFailure(call: Call<ReducedResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun readMapFromServer(){
        menu_item_search?.let{it.isVisible = false}
        menu_item_reload?.let{it.isVisible = false}
        var allMapsData: List<MapsData>? = listOf()
        crousService.findMapsData(0,10000,0,"title",0,1).enqueue(object : Callback<List<MapsData>> {
            override fun onResponse(
                call: Call<List<MapsData>>,
                response: Response<List<MapsData>>
            ) {
                allMapsData = response.body()
                allMapsData?.let { displayMapFragment(it) }
            }
            override fun onFailure(call: Call<List<MapsData>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayMapFragment(allMapsData:List<MapsData>) {
        val mapFragment = MapsFragment.newInstance(ArrayList(allMapsData))
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, mapFragment)
            .commit()
        back_btn.visibility = View.GONE
        next_btn.visibility = View.GONE
        first.visibility = View.GONE
        current.visibility = View.GONE
        last.visibility = View.GONE
    }

    private fun displayFavFragment(data: ArrayList<ReducedCrous>) {
        val favFragment = FavoriteFragment.newInstance(data)
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout,favFragment)
            .commit()

        back_btn.visibility = View.GONE
        next_btn.visibility = View.GONE
        first.visibility = View.GONE
        current.visibility = View.GONE
        last.visibility = View.GONE

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                var allData: ReducedResponse?
                crousCatalogue.clean()
                crousService.findAll(counter,10,0,"title",0,0,1).enqueue(object : Callback<ReducedResponse> {
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