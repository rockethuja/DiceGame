package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import kotlinx.android.synthetic.main.activity_create_dice_set.*



class CreateDiceSetActivity : AppCompatActivity() {
    val diceList = arrayListOf<Dice>()
    lateinit var adapter: DiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dice_set)

        if(savedInstanceState !=null){

        }else {
            diceList.add(Dice())
        }
        adapter = DiceAdapter(this, diceList)
        dice_list_view.adapter = adapter
    }

    fun addDice(view: View) {
        adapter.addDice()
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
