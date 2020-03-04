package com.example.dicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_create_dice_set.*
import kotlinx.android.synthetic.main.dice_item.*

class CreateDiceSetActivity : AppCompatActivity() {
    val diceList = arrayListOf<Dice>()
    lateinit var adapter : DiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dice_set)


        for (i in 0..9) {
            diceList.add(Dice())
        }
        val adapter = DiceAdapter(this, diceList)
        dice_list_view.adapter = adapter

    }

  fun addDice(view: View){
        diceList.add(Dice())
        adapter.notifyDataSetChanged()
    }


    fun increaseInteger(view: View) {
        var amount = amount_of_page.text.toString().toInt()
        amount++
        amount_of_page.text = amount.toString()
    }

    fun setAmountOfPages(amount: Int) {
        amount_of_page.text = amount.toString()
    }

    fun reduceInteger(view: View) {
        var amount = amount_of_page.text.toString().toInt()
        amount--
        amount_of_page.text = amount.toString()

    }
}
