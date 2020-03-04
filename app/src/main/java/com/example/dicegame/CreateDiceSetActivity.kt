package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_create_dice_set.*
import kotlinx.android.synthetic.main.dice_item.view.*


class CreateDiceSetActivity : AppCompatActivity() {
    val diceList = arrayListOf<Dice>()
    lateinit var adapter: DiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dice_set)

        for (i in 0..9) {
            diceList.add(Dice())
        }
        adapter = DiceAdapter(this, diceList)
        dice_list_view.adapter = adapter
    }

    fun addDice(view: View) {
        diceList.add(Dice())
        adapter.notifyDataSetChanged()
    }


    fun deleteDice(){


    }

    fun startThrowDiceActivity(view: View) {
        startActivity(
            Intent(this, ThrowDiceActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelableArrayList("diceSet",diceList)
                })
            }
        )
    }


}
