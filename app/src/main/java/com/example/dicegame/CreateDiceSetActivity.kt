package com.example.dicegame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_create_dice_set.*
import java.nio.charset.Charset


class CreateDiceSetActivity : AppCompatActivity() {
    lateinit var adapter: DiceAdapter
    var diceList = arrayListOf<Dice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dice_set)

//        var diceList = arrayListOf<Dice>()

        if (savedInstanceState != null) {
            val key = resources.getString(R.string.save_create_dice_set_key)
            val json = savedInstanceState.getString(key)
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Dice>>() {}.type
            diceList = gson.fromJson(json, type)
//            val gson = Gson()
//            val json: String = gson.toJson(adapter.dices)
        } else {
            diceList.add(Dice())
        }

//        diceList = arrayListOf<Dice>()

        initDiceList(diceList)
        displayDicesCount()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) { // Save something
        val key = resources.getString(R.string.save_create_dice_set_key)
        val gson = Gson()
        val json: String = gson.toJson(diceList)
        savedInstanceState.putString(key, json)
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)
    }

    // is called after onStart(), whereas onCreate() is called before onStart()
    // called only when recreating activity after it was killed
    // same bundle as in onCreate
    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState)
        // restore something
        val key = resources.getString(R.string.save_create_dice_set_key)
        val json = savedInstanceState.getString(key)
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Dice>>() {}.type
        diceList = gson.fromJson(json, type)

        initDiceList(diceList)
        displayDicesCount()
    }

    private fun initDiceList(diceList: ArrayList<Dice>) {
//        val diceList = arrayListOf<Dice>()
//        diceList.add(Dice())
        adapter = DiceAdapter(this, diceList)
        dice_list_view.adapter = adapter
    }

    fun displayDicesCount() {
        val size = adapter.dices.size
        if (size == 0)
            startGame.isEnabled = false
        val dice = if (size == 1) "dice" else "dices"
        dicesCount.text = " $size $dice"
    }

    fun addDice(view: View) {
        adapter.addDice()
        startGame.isEnabled = true
        displayDicesCount()

    }

    fun startThrowDiceActivity(view: View) {
        startActivity(
            Intent(this, ThrowDiceActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelableArrayList("diceSet", adapter.dices)
                })
            }
        )
    }

    fun saveDices(view: View) {
        try {
            openFileOutput(Constants.DICE_FILE, Context.MODE_PRIVATE).use {
                val gson = Gson()
                val json: String = gson.toJson(adapter.dices)
                it.write(json.toByteArray(Charset.forName("UTF-8")))
                Toast.makeText(this, "Diceset saved", Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
