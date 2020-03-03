package com.example.dicegame

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_throw_dice.*
import java.util.ArrayList
import kotlin.random.Random

class ThrowDiceActivity : AppCompatActivity() {

    private var gridSize = 0
    // private var gridButtons: ArrayList<View>? = null
    private var gridButtons: ArrayList<View> = arrayListOf() // empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_throw_dice)

        buildGrid(9)
    }

    private fun buildGrid(newSize: Int) {
        gridSize = newSize
        gridButtons = ArrayList(newSize)
        // das Ziellayout
        // val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.rootLayout)
        val constraintLayout: ConstraintLayout = findViewById(R.id.rootLayout)
//        constraintLayout.removeAllViews()
        // Constraints hier sammeln
        val set = ConstraintSet()
        set.clone(constraintLayout)
        // obere und untere "Grenze" zwischen 10% und 40%
        val idHG05 = View.generateViewId()
        set.create(idHG05, ConstraintSet.HORIZONTAL_GUIDELINE)
        set.setGuidelinePercent(idHG05, 0.25f)
        val idHG10 = View.generateViewId()
        set.create(idHG10, ConstraintSet.HORIZONTAL_GUIDELINE)
        set.setGuidelinePercent(idHG10, 0.55f)
        // gridSize+1 vertikale Guidelines zwischen 5% und 95%
        val marginL = 0.05f
        val marginR = 0.05f
        val dx = (1.0f - marginL - marginR) / gridSize.toFloat()
        val ids = ArrayList<Int>(gridSize + 1)
        for (i in 0..gridSize) {
            val idG = View.generateViewId()
            set.create(idG, ConstraintSet.VERTICAL_GUIDELINE)
            set.setGuidelinePercent(idG, marginL + i * dx)
            ids.add(idG)
        }
        // gridSize Buttons zwischen den Guidelines i und i+1
        for (i in 0 until gridSize) {
//            val button = Button(this)
//            val idB = View.generateViewId()
//            button.id = idB
//            button.text = "Button $i"
//            button.tag = i
//            button.setOnClickListener { v: View -> onClickButton(v) } // Methodenreferenz
            val idB = View.generateViewId()
            val button = Button(this).apply {
                id = idB
                text = "Button $i"
                tag = i
                setOnClickListener { v: View -> onClickButton(v) }
            }
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
            constraintLayout.addView(button)
            //gridButtons!!.add(button)
            gridButtons.add(button)
            set.connect(idB, ConstraintSet.LEFT, ids[i], ConstraintSet.RIGHT, 1)
            set.connect(idB, ConstraintSet.RIGHT, ids[i + 1], ConstraintSet.LEFT, 1)
            set.connect(idB, ConstraintSet.TOP, idHG05, ConstraintSet.BOTTOM, 1)
            set.connect(idB, ConstraintSet.BOTTOM, idHG10, ConstraintSet.TOP, 1)
        }
        // und alle Constraints aktivieren
        set.applyTo(constraintLayout)
    }

    private fun onClickButton(v: View) {
        val button = v as Button

        val accent = ContextCompat.getColor(this, R.color.colorAccent)
        val buttonColor = button.background as ColorDrawable
        val colorId = buttonColor.color

        if (colorId == accent)
            button.background = ColorDrawable(0)
        else
            button.background = ColorDrawable(accent)

//        button.setBackgroundColor(accent)
        val index = v.tag as Int
        Toast.makeText(this, "Button $index pressed!", Toast.LENGTH_SHORT).show()
    }

    fun throwAllCheck(view: View?) {
        gridButtons.forEach {
            val button = it as Button
//            button.isEnabled = throwAllCheckBox.isChecked
            button.setBackgroundColor(
                if (throwAllCheckBox.isChecked)
                    ContextCompat.getColor(this, R.color.colorAccent)
                else
                    0
            )
        }

//        if (!throwAllCheckBox.isChecked)
//            gridButtons.forEach {
//                val button = it as Button
//                button.isEnabled = false
//            }
//        else {
//            gridButtons.forEach {
//                val button = it as Button
//                button.isEnabled = true
//            }
//        }
    }

    fun throwDices(view: View?) {
        val accent = ContextCompat.getColor(this, R.color.colorAccent)
        gridButtons.forEach {
            val button = it as Button
            val buttonColor = button.background as ColorDrawable
            val colorId = buttonColor.color
            if (colorId == accent)
                button.text = Random.nextInt(0, 100).toString()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // getMenuInflater().inflate(R.menu.menu_main, menu)
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_size3 -> buildGrid(3)
//            R.id.action_size4 -> buildGrid(4)
//            R.id.action_size5 -> buildGrid(5)
//        }
//        return super.onOptionsItemSelected(item)
//    }

}
