package com.example.dicegame


import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startCreateDiceActivity(view : View){
        val intent = Intent(this, CreateDiceSetActivity::class.java)
        startActivity(intent)
    }



}







