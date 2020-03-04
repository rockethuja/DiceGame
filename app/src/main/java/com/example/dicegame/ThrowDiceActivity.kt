package com.example.dicegame

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_throw_dice.*
import java.util.*

import kotlin.random.Random

private const val TAG = "ThrowDiceActivity"

class ThrowDiceActivity : AppCompatActivity() {

    private var gridSize = 0
    private var buttonDiceMapping = mutableMapOf<Int, Dice>()
    private var gridButtons: ArrayList<View> = arrayListOf()
    private var diceSet: ArrayList<Dice?>? = arrayListOf()

    private var cyan: Int = 0
    private var green: Int = 0
    private var blue: Int = 0
    private var red: Int = 0
    private var yellow: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_throw_dice)
        getBundleFromIntent()
        initColours()
        buildGrid(diceSet?.size ?: 0)
    }

    private fun initColours() {
        cyan = ContextCompat.getColor(this, R.color.cyan)
        green = ContextCompat.getColor(this, R.color.green)
        blue = ContextCompat.getColor(this, R.color.blue)
        red = ContextCompat.getColor(this, R.color.red)
        yellow = ContextCompat.getColor(this, R.color.yellow)
    }

    private fun getBundleFromIntent() {
        val extras = intent.extras
        diceSet = extras!!.getParcelableArrayList("diceSet")
        Log.i(TAG, "ThrowDiceActivity.onCreate, ref $this")
    }

    private fun buildGrid(newSize: Int) {
        gridSize = newSize
        gridButtons = ArrayList(newSize)
        val constraintLayout: ConstraintLayout = findViewById(R.id.rootLayout)

        val set = ConstraintSet()
        set.clone(constraintLayout)

        val marginTop = 0.25f
        val marginL = 0.05f
        val marginR = 0.05f
        val marginBottom = 0.05f
        val dx = (1.0f - marginL - marginR) / 2
        val yDivider = if (gridSize % 2 == 0) gridSize / 2 else (gridSize + 1) / 2
        val dy = (1.0f - marginBottom - marginTop) / yDivider

        val idHG05 = View.generateViewId()
        set.create(idHG05, ConstraintSet.HORIZONTAL_GUIDELINE)
        set.setGuidelinePercent(idHG05, marginTop)
        val idHG10 = View.generateViewId()
        set.create(idHG10, ConstraintSet.HORIZONTAL_GUIDELINE)
        set.setGuidelinePercent(idHG10, 1.0f - marginBottom)

        val hIds = ArrayList<Int>(gridSize / 2)
        val vIds = ArrayList<Int>(3)

        // add 3 vertial guidelines
        for (i in 0..2) {
            val idV = View.generateViewId()
            set.create(idV, ConstraintSet.VERTICAL_GUIDELINE)
            set.setGuidelinePercent(idV, marginL + i * dx)
            vIds.add(idV)
        }

        for (i in 0..(gridSize + 1) / 2) {
            val idH = View.generateViewId()
            set.create(idH, ConstraintSet.HORIZONTAL_GUIDELINE)
            set.setGuidelinePercent(idH, marginTop + i * dy)
            hIds.add(idH)
        }

        val border = (gridSize + 1) / 2
        var count = 0

        for (i in 0 until border) {
            for (j in 0 until 2) {
                if (gridSize % 2 != 0 && j == 1 && i == (border - 1)) {
                    continue;
                }
                val idB = View.generateViewId()
                buttonDiceMapping[idB] = diceSet!![count]!!
                val button = Button(this).apply {
                    id = idB
                    text = throwDice(buttonDiceMapping[idB]!!.max)
                    textSize = 40F
                    tag = "$i$j"
                    layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    setOnClickListener { v: View -> onClickButton(v) }
                }
                button.setBackgroundColor(buttonDiceMapping[idB]!!.colour)
                constraintLayout.addView(button)
                gridButtons.add(button)
                set.connect(idB, ConstraintSet.LEFT, vIds[j], ConstraintSet.RIGHT, 3)
                set.connect(idB, ConstraintSet.RIGHT, vIds[j + 1], ConstraintSet.LEFT, 3)
                set.connect(idB, ConstraintSet.TOP, hIds[i], ConstraintSet.BOTTOM, 3)
                set.connect(idB, ConstraintSet.BOTTOM, hIds[i + 1], ConstraintSet.TOP, 3)
                count++
            }
        }
        set.applyTo(constraintLayout)
    }

    private fun onClickButton(v: View) {
        val button = v as Button

//        val accent = ContextCompat.getColor(this, R.color.colorAccent)
//        val cyan = ContextCompat.getColor(this, R.color.cyan)
//        val green = ContextCompat.getColor(this, R.color.green)
//        val blue = ContextCompat.getColor(this, R.color.blue)
//        val red = ContextCompat.getColor(this, R.color.red)
//        val yellow = ContextCompat.getColor(this, R.color.yellow)
        val buttonColor = button.background as ColorDrawable
        val colorId = buttonColor.color

        button.background = ColorDrawable(
            when (colorId) {
                cyan, green, blue, red, yellow -> 0
                else -> buttonDiceMapping[v.id]!!.colour
            }
        )

//        if (colorId == accent)
//            button.background = ColorDrawable(0)
//        else
//            button.background = ColorDrawable(accent)
//        val index = v.tag as Int
//        Toast.makeText(this, "Button $index pressed!", Toast.LENGTH_SHORT).show()
    }

    fun throwAllCheck(view: View?) {
        gridButtons.forEach {
            val button = it as Button
            button.setBackgroundColor(
                if (throwAllCheckBox.isChecked)
                    buttonDiceMapping[button.id]!!.colour
//                    ContextCompat.getColor(this, R.color.colorAccent)
                else
                    0
            )
        }
    }

    fun throwDices(view: View?) {
//        val accent = ContextCompat.getColor(this, R.color.colorAccent)
        gridButtons.forEach {
            val button = it as Button
            val buttonColor = button.background as ColorDrawable
            val colorId = buttonColor.color
            if (arrayOf(cyan, blue, green, red, yellow).contains(colorId))
                button.text = throwDice(buttonDiceMapping[button.id]!!.max)
//                button.text = throwDice(diceSet!![count]!!.max)
        }
    }

    private fun throwDice(max: Int): String {
        return Random.nextInt(1, max + 1).toString()
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
