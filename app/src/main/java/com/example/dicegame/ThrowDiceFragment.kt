package com.example.dicegame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ThrowDiceFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.v("Fragment1", "create")
        return inflater.inflate(R.layout.fragment_throw_dice, container, false).apply {
//            findViewById<TextView>(R.id.textViewNumbers).text="0"
        }
    }
}