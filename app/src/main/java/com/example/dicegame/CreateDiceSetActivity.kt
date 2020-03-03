package com.example.dicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_create_dice_set.*

class CreateDiceSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dice_set)

        val diceList = arrayListOf<Dice>()
        for (i in 0..9) {
            diceList.add(Dice(1, 6, ContextCompat.getColor(this, R.color.colorAccent)))
        }
        val adapter = DiceAdapter(this, diceList)
        dice_list_view.adapter = adapter
    }
}
