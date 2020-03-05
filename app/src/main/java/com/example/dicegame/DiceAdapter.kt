package com.example.dicegame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner

class DiceAdapter(private val mContext: Context, val dices: ArrayList<Dice>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dices.size
    }

    override fun getItem(position: Int): Any {
        return dices[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.dice_item, parent, false)
            holder = ViewHolder()

            holder.dicePosition = view.findViewById(R.id.dicePosition)
            holder.increase = view.findViewById(R.id.increase)
            holder.reduce = view.findViewById(R.id.reduce)
            holder.spinner = view.findViewById(R.id.spinner)
            holder.maxTextView = view.findViewById(R.id.maxTextView)
            holder.frameLayout = view.findViewById(R.id.frameLayout1)
            holder.deleteButton = view.findViewById(R.id.deleteButton)

            view.tag = holder

        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val dicePosition = holder.dicePosition
        val maxTextView = holder.maxTextView
        val spinner = holder.spinner
        val increase = holder.increase
        val reduce = holder.reduce
        val frameLayout = holder.frameLayout
        val deleteButton = holder.deleteButton

        val dice = getItem(position) as Dice
        val uiPosition = position +1

        dicePosition.text = uiPosition.toString()
        maxTextView.text = dice.max.toString()
        setUpSpinner(spinner, frameLayout, position)

        increase.setOnClickListener {
            dice.max++
            maxTextView.text = dice.max.toString()
        }

        reduce.setOnClickListener {
            dice.max--
            maxTextView.text = dice.max.toString()
        }

        deleteButton.setOnClickListener {
            dices.remove(dice)
            this.notifyDataSetChanged()
        }

        return view
    }

    fun addDice() {
        dices.add(Dice())
        this.notifyDataSetChanged()
    }

    fun changeColor(color: Int, frame: FrameLayout, position: Int) {
        frame.setBackgroundColor(color)
        val dice: Dice = dices[position]
        dice.colour = color
    }

    private class ViewHolder {
        lateinit var dicePosition: TextView
        lateinit var maxTextView: TextView
        lateinit var spinner: Spinner
        lateinit var increase: ImageButton
        lateinit var reduce: ImageButton
        lateinit var frameLayout: FrameLayout
        lateinit var deleteButton: ImageButton

    }

    private fun setUpSpinner(spinner: Spinner, frame: FrameLayout, dicePosition: Int) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                changeColor(getColourCode(selectedItem), frame, dicePosition)

//
//
//                when (selectedItem) {
//                    "mauve" -> changeColor(Colours.MAUVE, frame, dicePosition)
//                    "ice" -> changeColor(Colours.ICE, frame, dicePosition)
//                    "mint" -> changeColor(Colours.MINT, frame, dicePosition)
//                    "mustard" -> changeColor(Colours.MUSTARD, frame, dicePosition)
//                    "apricot" -> changeColor(Colours.APRICOT, frame, dicePosition)
//                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        ArrayAdapter.createFromResource(
            mContext, R.array.colours,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(getColourPosition(dices[dicePosition].colour))
        }
    }

    private fun getColourPosition(colour: Int): Int {
        val colours = mContext.resources.getStringArray(R.array.colours)

        for (i in colours.indices) {
            val colourCode = getColourCode(colours[i])
            if (colourCode == colour)
                return i
        }
        return -1
    }

    private fun getColourCode(colour: String): Int {
        return when (colour) {
            "mauve" -> Colours.MAUVE
            "steel" -> Colours.STEEL
            "mint" -> Colours.MINT
            "mustard" -> Colours.MUSTARD
            "apricot" -> Colours.APRICOT
            else -> 0
        }
    }
}
