package com.example.dicegame

import android.graphics.Color.WHITE


//data class DiceGame(val dices : List<Dice>)

data class Dice(val min : Int = 1, val max : Int = 6 , val colour : Int = WHITE)
