package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import kotlinx.android.synthetic.main.activity_create_dice_set.*

class CreateDiceSetActivity : AppCompatActivity() {
    lateinit var adapter: DiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dice_set)

        initDiceList()
        displayDicesCount()
    }

    private fun initDiceList() {
        val diceList = arrayListOf<Dice>()
        diceList.add(Dice())
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

}
