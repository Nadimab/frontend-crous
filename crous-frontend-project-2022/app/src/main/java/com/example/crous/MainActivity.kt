package com.example.crous

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val crousCatalogue = CrousCatalogue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val crous = ReducedCrous(
            "1",
            "Sushi",
            "Sushi and co",
            "Sushi ktir taybe",
            "Gardanne",
            "01686955",
            "sushi@gmail.com",
            1,
            "asdasd"
        )
        crousCatalogue.addCrous(crous)
        displayCrousListFragment()
    }

    private fun displayCrousListFragment() {
        val bookListFragment = CrousFragment.newInstance(crousCatalogue.getAllCrous())
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, bookListFragment)
            .commit()
    }
}