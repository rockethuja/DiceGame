package com.example.dicegame

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import java.util.*


class DiceAdapter(mContext: Context, private val dices: ArrayList<Dice>) :
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
            view = inflater.inflate(R.layout.list_item, parent, false)
            holder = ViewHolder()
            holder.colourButton = view.findViewById(R.id.colour_button) as Button
            holder.maxTextView = view.findViewById(R.id.textView_max)
            holder.minTextView = view.findViewById(R.id.textView_min)
            view.tag = holder
        }
        else {
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

}