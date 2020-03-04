package com.example.dicegame

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
    private var gridButtons: ArrayList<View> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_throw_dice)

        buildGrid(9)
    }

    private fun buildGrid(newSize: Int) {
        gridSize = newSize
        gridButtons = ArrayList(newSize)
        val constraintLayout: ConstraintLayout = findViewById(R.id.rootLayout)

        // Constraints hier sammeln
        val set = ConstraintSet()
        set.clone(constraintLayout)

        val marginTop = 0.25f
        val marginL = 0.05f
        val marginR = 0.05f
        val marginBottom = 0.05f
        val dx = (1.0f - marginL - marginR) / 2
        val dy = (1.0f - marginBottom - marginTop) / (gridSize.toFloat() / 2)

        // obere und untere "Grenze" zwischen 10% und 40%
        val idHG05 = View.generateViewId()
        set.create(idHG05, ConstraintSet.HORIZONTAL_GUIDELINE)
        set.setGuidelinePercent(idHG05, marginTop)
        val idHG10 = View.generateViewId()
        set.create(idHG10, ConstraintSet.HORIZONTAL_GUIDELINE)
        set.setGuidelinePercent(idHG10, 1.0f - marginBottom)

//        val divideBy =
//            if (gridSize % 3 == 0) gridSize / 3 else if (gridSize % 2 == 0) gridSize / 2 else 1
//        val dicesPerRow = if (gridSize % 3 == 0) 3 else if (gridSize % 2 == 0) 2 else 1

        val hIds = ArrayList<Int>(gridSize / 2)
        val vIds = ArrayList<Int>(3)

        // add 3 vertial guidelines
        for (i in 0..2) {
            val idV = View.generateViewId()
//            val idH = View.generateViewId()

            set.create(idV, ConstraintSet.VERTICAL_GUIDELINE)
            set.setGuidelinePercent(idV, marginL + i * dx)
            vIds.add(idV)

//            set.create(idH, ConstraintSet.HORIZONTAL_GUIDELINE)
//            set.setGuidelinePercent(idH, marginTop + divideBy * i * dy)
//            hIds.add(idH)
        }

        for (i in 0..gridSize / 2) {
            val idH = View.generateViewId()
            set.create(idH, ConstraintSet.HORIZONTAL_GUIDELINE)
            set.setGuidelinePercent(idH, marginTop + i * dy)
            hIds.add(idH)
        }

        for (i in 0 until gridSize / 2) {
            for (j in 0 until 2) {
                val idB = View.generateViewId()
                val button = Button(this).apply {
                    id = idB
                    text = "Button $i"
                    tag = i
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setOnClickListener { v: View -> onClickButton(v) }
                }
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                constraintLayout.addView(button)
                gridButtons.add(button)
                set.connect(idB, ConstraintSet.LEFT, vIds[j], ConstraintSet.RIGHT, 1)
                set.connect(idB, ConstraintSet.RIGHT, vIds[j + 1], ConstraintSet.LEFT, 1)
                set.connect(idB, ConstraintSet.TOP, hIds[i], ConstraintSet.BOTTOM, 1)
                set.connect(idB, ConstraintSet.BOTTOM, hIds[i + 1], ConstraintSet.TOP, 1)
            }
        }

//        // gridSize Buttons zwischen den Guidelines i und i+1
//        for (i in 0 until gridSize / divideBy) {
//            for (j in 0 until gridSize / divideBy) {
//                val idB = View.generateViewId()
//                val button = Button(this).apply {
//                    id = idB
//                    text = "Button $i$j"
//                    tag = i + j
//                    width = ViewGroup.LayoutParams.WRAP_CONTENT
//                    height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    setOnClickListener { v: View -> onClickButton(v) }
//                }
//                button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
//                constraintLayout.addView(button)
//                gridButtons.add(button)
//                set.connect(idB, ConstraintSet.LEFT, vIds[i], ConstraintSet.RIGHT, 1)
//                set.connect(idB, ConstraintSet.RIGHT, vIds[i + 1], ConstraintSet.LEFT, 1)
//                set.connect(idB, ConstraintSet.TOP, hIds[j], ConstraintSet.BOTTOM, 1)
//                set.connect(idB, ConstraintSet.BOTTOM, hIds[j + 1], ConstraintSet.TOP, 1)
//            }
//        }
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

        val index = v.tag as Int
        Toast.makeText(this, "Button $index pressed!", Toast.LENGTH_SHORT).show()
    }

    fun throwAllCheck(view: View?) {
        gridButtons.forEach {
            val button = it as Button
            button.setBackgroundColor(
                if (throwAllCheckBox.isChecked)
                    ContextCompat.getColor(this, R.color.colorAccent)
                else
                    0
            )
        }
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
