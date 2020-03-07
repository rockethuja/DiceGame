package com.example.dicegame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
    }

    fun startCreateDiceActivity(view: View) {
        val intent = Intent(this, CreateDiceSetActivity::class.java)
        startActivity(intent)
    }

    fun loadDiceSet(view: View) {
        try {
            var json = ""
            openFileInput(Constants.DICE_FILE).use {
                json = Scanner(it, "UTF-8").useDelimiter("\\A").next()
            }
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Dice>>() {}.type
            val dices: ArrayList<Dice> = gson.fromJson(json, type)
            startActivity(
                Intent(this, ThrowDiceActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putParcelableArrayList("diceSet", dices)
                    })
                }
            )
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}







