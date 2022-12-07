package com.example.crous

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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

    var tabLayout: TabLayout? = null

    var sortBy: String = "type"
    private var dropDownLabels = arrayOf( "title", "address", "type")
    var counter: Int = 0;
    private val crousCatalogue = CrousCatalogue()
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getString(R.string.SERVER_BASE_URL))
            .build()

        crousService = retrofit.create(CrousService::class.java)

//        initSpinner()
        initTabs()
        initButtons()
        initPaginationView()
        readDataFromServer(counter, sortBy,0)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var m: Menu? = initMenu(menu)
        initSearch(m)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                readDataFromServer(counter,sortBy,1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun initSpinner(){
//        val spinner: Spinner = findViewById(R.id.ddl_sortBy)
//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
//            this,
//            android.R.layout.simple_spinner_item, dropDownLabels
//        )
//        spinner.adapter = adapter
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//                when (pos) {
//                    1 -> {
//                        sortBy = "title"
//                        readDataFromServer(counter, sortBy,0)
//                    }
//                    2 -> {
//                        sortBy = "address"
//                        readDataFromServer(counter, sortBy,0)
//                    }
//                    3 -> {
//                        sortBy = "type"
//                        readDataFromServer(counter, sortBy,0)
//                    }
//                }
//            }
//        }
//    }

    private fun initTabs(){
        tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // creating cases for fragment
                when (tab.position) {
                    0 -> readDataFromServer(counter,sortBy,0)
                    1 -> readMapDataFromServer()
                    2 -> readFavFromServer()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun initButtons(){
        next_btn = findViewById<Button>(R.id.button_next)
        back_btn = findViewById<Button>(R.id.button_back)
        back_btn.isEnabled = false
        next_btn.setOnClickListener{
            counter++
            back_btn.isEnabled = true
            readDataFromServer(counter,sortBy,0)
        }
        back_btn.setOnClickListener{
            counter--
            back_btn.isEnabled = counter != 0
            readDataFromServer(counter,sortBy, 0)
        }
    }

    private fun initPaginationView(){
        first = findViewById<TextView>(R.id.first_page)
        current = findViewById<TextView>(R.id.current_page)
        last = findViewById<TextView>(R.id.last_page)
    }

    private fun initMenu(menu: Menu?): Menu?{
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu_item_search = menu.getItem(0)
            menu_item_reload = menu.getItem(1)
        };

        return menu
    }

    private fun initSearch(menu: Menu?){
        val searchItem = menu?.findItem(R.id.menu_search)
        //searchItem?.expandActionView()
        val searchView = searchItem?.actionView as SearchView
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

    private fun readDataFromServer(counter: Int, sortby: String, refresh: Int){
        menu_item_search?.let{it.isVisible = true}
        menu_item_reload?.let{it.isVisible = true}
        var allData: ReducedResponse?
        crousCatalogue.clean()
        crousService.findAll(counter,10,0,sortby,0,0,refresh).enqueue(object : Callback<ReducedResponse> {
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

    private fun readMapDataFromServer(){
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

    private fun displayCrousListFragment() {
        val crous = CrousFragment.newInstance(crousCatalogue.getAllCrous())
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, crous)
            .commit()

        setPaginationViewState(View.VISIBLE)
    }

    private fun displayMapFragment(allMapsData:List<MapsData>) {
        val mapFragment = MapsFragment.newInstance(ArrayList(allMapsData))
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, mapFragment)
            .commit()

        setPaginationViewState(View.GONE)
    }

    private fun displayFavFragment(data: ArrayList<ReducedCrous>) {
        val favFragment = FavoriteFragment.newInstance(data)
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout,favFragment)
            .commit()

        setPaginationViewState(View.GONE)
    }

    fun setPaginationViewState(state: Int){
        back_btn.visibility = state
        next_btn.visibility = state
        first.visibility = state
        current.visibility = state
        last.visibility = state
    }
}