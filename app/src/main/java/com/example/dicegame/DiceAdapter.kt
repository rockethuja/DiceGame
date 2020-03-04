package com.example.dicegame

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
            view = inflater.inflate(R.layout.list_item, parent, false)
            holder = ViewHolder()
            holder.colourButton = view.findViewById(R.id.colour_button) as Button
            holder.maxTextView = view.findViewById(R.id.textView_max)
            holder.minTextView = view.findViewById(R.id.textView_min)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val maxTextView = holder.maxTextView
        val minTextView = holder.minTextView
        val colourButton = holder.colourButton

        val dice = getItem(position) as Dice

        maxTextView.text = dice.min.toString()
        minTextView.text = dice.max.toString()
        colourButton.background = ColorDrawable(dice.colour)

        return view
    }

    private class ViewHolder {
        lateinit var maxTextView: TextView
        lateinit var minTextView: TextView
        lateinit var colourButton: Button
    }



    fun setUpSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.spinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                val frame = view.findViewById<FrameLayout>(R.id.frameLayout1)
                when (selectedItem) {
                    "black" -> frame.setBackgroundColor(Color.BLACK)
                    "red" -> frame.setBackgroundColor(Color.RED)
                    "green" -> frame.setBackgroundColor(Color.GREEN)
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

    fun increaseInteger(view: View) {
        //@TODO IDs aus ViewHolder holen
        var amount = view.findViewById<TextView>(R.id.amount_of_page).text.toString().toInt()
        amount++
        view.findViewById<TextView>(R.id.amount_of_page).text = amount.toString()
    }

    fun reduceInteger(view: View) {
        var amount = view.findViewById<TextView>(R.id.amount_of_page).text.toString().toInt()
        amount--
        view.findViewById<TextView>(R.id.amount_of_page).text = amount.toString()

    }




}