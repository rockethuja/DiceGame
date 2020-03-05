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


class DiceAdapter(private val mContext: Context, private val dices: ArrayList<Dice>) :
    BaseAdapter() {

    // private val context : Context = mContext

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

            holder.dicePosition = view.findViewById(R.id.Position)
            holder.increase = view.findViewById(R.id.increase)
            holder.reduce = view.findViewById(R.id.reduce)
            holder.spinner = view.findViewById(R.id.spinner)
            holder.maxTextView = view.findViewById(R.id.textView_max)
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
        setUpSpinner(spinner,frameLayout, position)

        increase.setOnClickListener {
            var amount = maxTextView.text.toString().toInt()
            amount++
            maxTextView.text = amount.toString()
        }

        reduce.setOnClickListener {
            var amount = maxTextView.text.toString().toInt()
            amount--
            maxTextView.text = amount.toString()
        }

        deleteButton.setOnClickListener{
            dices.remove(dices.get(position))
            this.notifyDataSetChanged()
        }


        return view
    }
    fun addDice(){
        dices.add(Dice())
        this.notifyDataSetChanged()
    }

    fun changeColor(color :String, frame :FrameLayout, position: Int){
        frame.setBackgroundColor(Color.parseColor(color))
        val dice : Dice = dices.get(position)
        dice.colour = Color.parseColor(color)
    }


    private class ViewHolder {
        lateinit var  dicePosition: TextView
        lateinit var maxTextView: TextView
        lateinit var spinner: Spinner
        lateinit var increase: Button
        lateinit var reduce: Button
        lateinit var frameLayout: FrameLayout
        lateinit var  deleteButton: Button

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
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "cyan" -> changeColor("#03FFEE", frame, dicePosition)
                    "red" -> changeColor("#E80231", frame, dicePosition)
                    "green" -> changeColor("#83E802" ,frame, dicePosition)
                    "yellow"-> changeColor("#FFA40B", frame, dicePosition)
                    "blue"-> changeColor("#1C03FF", frame, dicePosition)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        ArrayAdapter.createFromResource(
            mContext, R.array.colours,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

}