package com.example.dicegame

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.core.content.ContextCompat


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

        dicePosition.text = position.toString()
        maxTextView.text = dice.max.toString()
        setUpSpinner(spinner, frameLayout, position)

        increase.setOnClickListener {
            //            var amount = dice.max//maxTextView.text.toString().toInt()
//            amount++
            dice.max++
            maxTextView.text = dice.max.toString()
        }

        reduce.setOnClickListener {
            //            var amount = maxTextView.text.toString().toInt()
//            amount--
            dice.max--
            maxTextView.text = dice.max.toString()
        }

        deleteButton.setOnClickListener {
            dices.remove(dice)
//            dices.remove(dices.get(position))
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
//        frame.setBackgroundColor(R.color.cyan)
        val dice: Dice = dices[position] as Dice
        dice.colour = color
    }


    private class ViewHolder {
        lateinit var dicePosition: TextView
        lateinit var maxTextView: TextView
        lateinit var spinner: Spinner
        lateinit var increase: Button
        lateinit var reduce: Button
        lateinit var frameLayout: FrameLayout
        lateinit var deleteButton: Button

    }

    fun setUpSpinner(spinner: Spinner, frame: FrameLayout, dicePosition: Int) {
        //die position der farbe die der wüfel im spinner ausgewählt hat

//        val spinner: Spinner = view.findViewById(R.id.spinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val cyan = ContextCompat.getColor(mContext, R.color.cyan)
                val green = ContextCompat.getColor(mContext, R.color.green)
                val blue = ContextCompat.getColor(mContext, R.color.blue)
                val red = ContextCompat.getColor(mContext, R.color.red)
                val yellow = ContextCompat.getColor(mContext, R.color.yellow)


                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "cyan" -> changeColor(cyan, frame, dicePosition)
                    "red" -> changeColor(red, frame, dicePosition)
                    "green" -> changeColor(green, frame, dicePosition)
                    "yellow" -> changeColor(yellow, frame, dicePosition)
                    "blue" -> changeColor(blue, frame, dicePosition)
                }
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

    fun getColourPosition(colour: Int) : Int {
        val colours =  mContext.resources.getStringArray(R.array.colours);

        for (i in colours.indices) {
            if (Color.parseColor(colours[i]) == colour)
                return i
        }
        return -1
    }

}