package com.example.crous

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DetailActivity : AppCompatActivity() {
    lateinit var mMessageReceiver: BroadcastReceiver
    lateinit var data : ExpandedCrous
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        //val intent = IntentFilter()
        //LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intent);
        data = getIntent().getSerializableExtra(EXTRA_KEY) as ExpandedCrous
    }

}