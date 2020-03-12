package com.example.dicegame

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_throw_dice.*
import java.util.*
import kotlin.random.Random


private const val TAG = "ThrowDiceActivity"

class ThrowDiceActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private var constraintSet = ConstraintSet()
    private lateinit var dragListener: MyDragEventListener
    private lateinit var longListener: MyOnLongClickListener

    private lateinit var mSensorManager: SensorManager
    private var mAccel // acceleration apart from gravity
            = 0f
    private var mAccelCurrent // current acceleration including gravity
            = 0f
    private var mAccelLast // last acceleration including gravity
            = 0f

    private var gridSize = 0
    private var buttonDiceMapping = mutableMapOf<Int, Dice>()
    private var gridButtons: ArrayList<View> = arrayListOf()
    private var checkBoxes = mutableMapOf<Int, View>()
    private var diceSet: ArrayList<Dice?>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_throw_dice)
        constraintLayout = findViewById(R.id.rootLayout)
        getBundleFromIntent()
        buildGrid(diceSet?.size ?: 0)
        buildCheckboxes()
        initLongClickListener()
        initDragListener()

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
            mSensorListener,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(mSensorListener)
        super.onPause()
    }

    private fun initLongClickListener() {
        longListener = MyOnLongClickListener()
        gridButtons.forEach {
            findViewById<View>(it.id).setOnLongClickListener(longListener)
        }
    }

    private fun initDragListener() {
        dragListener = MyDragEventListener()
        gridButtons.forEach {
            findViewById<View>(it.id).setOnDragListener(dragListener)
        }
    }

    private fun buildCheckboxes() {
        val colours = getDistinctColours()
        if (colours.size == 1) {
            throwAllCheckBox.buttonTintList = ColorStateList.valueOf(colours[0])
            return
        }

        checkBoxes = HashMap(colours.size)
//        val set = ConstraintSet()
        constraintSet.clone(constraintLayout)
        val idHG05 = View.generateViewId()
        constraintSet.create(idHG05, ConstraintSet.HORIZONTAL_GUIDELINE)
        constraintSet.setGuidelinePercent(idHG05, 0.95f)
        val idHG10 = View.generateViewId()
        constraintSet.create(idHG10, ConstraintSet.HORIZONTAL_GUIDELINE)
        constraintSet.setGuidelinePercent(idHG10, 0.99f)
        val marginL = 0.05f
        val marginR = 0.05f
        val dx = (1.0f - marginL - marginR) / colours.size.toFloat()
        val ids = ArrayList<Int>(colours.size + 1)
        for (i in 0..colours.size) {
            val idG = View.generateViewId()
            constraintSet.create(idG, ConstraintSet.VERTICAL_GUIDELINE)
            constraintSet.setGuidelinePercent(idG, marginL + i * dx)
            ids.add(idG)
        }
        for (i in colours.indices) {
            val idB = View.generateViewId()
            val checkBox = CheckBox(this).apply {
                id = idB
                isChecked = true
                buttonTintList = ColorStateList.valueOf(colours[i])
                tag = colours[i]
                setOnClickListener { v: View -> onClickCheckBox(v) }
            }
            constraintLayout.addView(checkBox)
            checkBoxes[checkBox.id] = checkBox
            constraintSet.connect(idB, ConstraintSet.LEFT, ids[i], ConstraintSet.RIGHT, 0)
            constraintSet.connect(idB, ConstraintSet.RIGHT, ids[i + 1], ConstraintSet.LEFT, 0)
            constraintSet.connect(idB, ConstraintSet.TOP, idHG05, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(idB, ConstraintSet.BOTTOM, idHG10, ConstraintSet.TOP, 0)
        }
        constraintSet.applyTo(constraintLayout)
    }

    private fun getDistinctColours(): ArrayList<Int> {
        val distinctColours = arrayListOf<Int>()

        for (i in 0 until diceSet!!.size) {
            val dice = diceSet!![i]!!
            if (distinctColours.contains(dice.colour))
                continue
            else
                distinctColours.add(dice.colour)
        }

        return distinctColours
    }

    private fun getBundleFromIntent() {
        val extras = intent.extras
        diceSet = extras!!.getParcelableArrayList("diceSet")
        Log.i(TAG, "ThrowDiceActivity.onCreate, ref $this")
    }

    private fun buildGrid(newSize: Int) {
        gridSize = newSize
        gridButtons = ArrayList(newSize)
//        val constraintLayout: ConstraintLayout = findViewById(R.id.rootLayout)

        constraintSet.clone(constraintLayout)

        val marginTop = 0.05f
        val marginL = 0.05f
        val marginR = 0.05f
        val marginBottom = 0.25f

        val dx = (1.0f - marginL - marginR) / 2
        val yDivider = if (gridSize % 2 == 0) gridSize / 2 else (gridSize + 1) / 2
        val dy = (1.0f - marginBottom - marginTop) / yDivider

        val idHG05 = View.generateViewId()
        constraintSet.create(idHG05, ConstraintSet.HORIZONTAL_GUIDELINE)
        constraintSet.setGuidelinePercent(idHG05, marginTop)
        val idHG10 = View.generateViewId()
        constraintSet.create(idHG10, ConstraintSet.HORIZONTAL_GUIDELINE)
        constraintSet.setGuidelinePercent(idHG10, 1.0f - marginBottom)

        val hIds = ArrayList<Int>(gridSize / 2)
        val vIds = ArrayList<Int>(3)

        // add 3 vertial guidelines
        for (i in 0..2) {
            val idV = View.generateViewId()
            constraintSet.create(idV, ConstraintSet.VERTICAL_GUIDELINE)
            constraintSet.setGuidelinePercent(idV, marginL + i * dx)
            vIds.add(idV)
        }

        for (i in 0..(gridSize + 1) / 2) {
            val idH = View.generateViewId()
            constraintSet.create(idH, ConstraintSet.HORIZONTAL_GUIDELINE)
            constraintSet.setGuidelinePercent(idH, marginTop + i * dy)
            hIds.add(idH)
        }

        if (gridSize == 1) {
            val idB = View.generateViewId()
            buttonDiceMapping[idB] = diceSet!![0]!!
            val button = Button(this).apply {
                id = idB
                text = throwDice(buttonDiceMapping[idB]!!.max)
                textSize = 40F
                tag = idB
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener { v: View -> onClickButton(v) }
            }
            button.setBackgroundColor(buttonDiceMapping[idB]!!.colour)
            constraintLayout.addView(button)
            gridButtons.add(button)
            constraintSet.connect(idB, ConstraintSet.LEFT, vIds[0], ConstraintSet.RIGHT, 3)
            constraintSet.connect(idB, ConstraintSet.RIGHT, vIds[2], ConstraintSet.LEFT, 3)
            constraintSet.connect(idB, ConstraintSet.TOP, hIds[0], ConstraintSet.BOTTOM, 3)
            constraintSet.connect(idB, ConstraintSet.BOTTOM, hIds[1], ConstraintSet.TOP, 3)
            constraintSet.applyTo(constraintLayout)
            return
        }

        val border = (gridSize + 1) / 2
        var count = 0

        for (i in 0 until border) {
            for (j in 0 until 2) {
                if (gridSize % 2 != 0 && j == 1 && i == (border - 1)) {
                    continue
                }
                val idB = View.generateViewId()
                buttonDiceMapping[idB] = diceSet!![count]!!
                val button = Button(this).apply {
                    id = idB
                    text = throwDice(buttonDiceMapping[idB]!!.max)
                    textSize = 40F
                    tag = idB
                    layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    setOnClickListener { v: View -> onClickButton(v) }
                }
                button.setBackgroundColor(buttonDiceMapping[idB]!!.colour)
                constraintLayout.addView(button)
                gridButtons.add(button)
                constraintSet.connect(idB, ConstraintSet.LEFT, vIds[j], ConstraintSet.RIGHT, 3)
                constraintSet.connect(idB, ConstraintSet.RIGHT, vIds[j + 1], ConstraintSet.LEFT, 3)
                constraintSet.connect(idB, ConstraintSet.TOP, hIds[i], ConstraintSet.BOTTOM, 3)
                constraintSet.connect(idB, ConstraintSet.BOTTOM, hIds[i + 1], ConstraintSet.TOP, 3)
                count++
            }
        }
        constraintSet.applyTo(constraintLayout)
    }

    private fun onClickButton(v: View) {
        val button = v as Button
        val buttonColor = button.background as ColorDrawable
        val colorId = buttonColor.color

        button.background = ColorDrawable(
            if (Constants.ALL_COLOURS.contains(colorId)) 0
            else buttonDiceMapping[v.id]!!.colour
        )
    }

    fun throwAllCheck(view: View?) {
        gridButtons.forEach {
            val button = it as Button
            button.setBackgroundColor(
                if (throwAllCheckBox.isChecked)
                    buttonDiceMapping[button.id]!!.colour
                else
                    0
            )
        }
    }

    private fun onClickCheckBox(v: View) {
        gridButtons.forEach {
            val button = it as Button
            val dice = buttonDiceMapping[button.id]!!
            val colour = v.tag as Int

            if (colour == dice.colour) {
                button.setBackgroundColor(
                    if ((checkBoxes[v.id] as CheckBox).isChecked)
                        buttonDiceMapping[button.id]!!.colour
                    else
                        0
                )
            }
        }
    }

    fun throwDices(view: View?) {
        throwDices()
    }

    private fun throwDices() {
        gridButtons.forEach {
            val button = it as Button
            val buttonColor = button.background as ColorDrawable
            val colorId = buttonColor.color
            if (Constants.ALL_COLOURS.contains(colorId)) {
                Thread(Runnable {
                    for (i in 0..11) {
                        runOnUiThread {
                            button.text = throwDice(buttonDiceMapping[button.id]!!.max)
                        }
                        sleep(60)
                    }
                }).start()
                button.text = throwDice(buttonDiceMapping[button.id]!!.max)
            }
        }
    }

    private fun throwDice(max: Int): String {
        return Random.nextInt(1, max + 1).toString()
    }

    private fun sleep(millis: Long) {
        try {
            Thread.sleep(millis, 0)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    inner class MyOnLongClickListener : View.OnLongClickListener {
        override fun onLongClick(view: View): Boolean {
            val tag = view.tag.toString()
            val dragData = ClipData.newPlainText("text", tag)
            view.startDragAndDrop(dragData, View.DragShadowBuilder(view), null, 0)
            return true
        }
    }

    inner class MyDragEventListener : View.OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            var rc = true
            when (event.action) {
//                // determines if this View can accept the dragged data
                DragEvent.ACTION_DRAG_STARTED -> {
                    if (!event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                        rc = false
                }
                DragEvent.ACTION_DROP -> {
                    val dragData = event.clipData.getItemAt(0).text.toString().toInt()
                    val tag = v.tag.toString().toInt()
                    swapButtons(dragData, tag)
                }
                else -> {
                    rc = false
                }
            }
            return rc
        }

        private fun swapButtons(id1: Int, id2: Int) {
            val button1 = gridButtons.find { it.id == id1 }
            val button2 = gridButtons.find { it.id == id2 }

            val params1 = button1?.layoutParams as ConstraintLayout.LayoutParams
            val params2 = button2?.layoutParams as ConstraintLayout.LayoutParams
            val button1Top = params1.topToBottom
            val button1Bottom = params1.bottomToTop
            val button1Left = params1.leftToRight
            val button1Right = params1.rightToLeft
            val button2Top = params2.topToBottom
            val button2Bottom = params2.bottomToTop
            val button2Left = params2.leftToRight
            val button2Right = params2.rightToLeft

            constraintSet.clone(constraintLayout)

            constraintSet.connect(id1, ConstraintSet.LEFT, button2Left, ConstraintSet.RIGHT, 3)
            constraintSet.connect(id1, ConstraintSet.RIGHT, button2Right, ConstraintSet.LEFT, 3)
            constraintSet.connect(id1, ConstraintSet.TOP, button2Top, ConstraintSet.BOTTOM, 3)
            constraintSet.connect(id1, ConstraintSet.BOTTOM, button2Bottom, ConstraintSet.TOP, 3)

            constraintSet.connect(id2, ConstraintSet.LEFT, button1Left, ConstraintSet.RIGHT, 3)
            constraintSet.connect(id2, ConstraintSet.RIGHT, button1Right, ConstraintSet.LEFT, 3)
            constraintSet.connect(id2, ConstraintSet.TOP, button1Top, ConstraintSet.BOTTOM, 3)
            constraintSet.connect(id2, ConstraintSet.BOTTOM, button1Bottom, ConstraintSet.TOP, 3)

            constraintSet.applyTo(constraintLayout)
        }
    }

    private val mSensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(se: SensorEvent) {
            val x = se.values[0]
            val y = se.values[1]
            val z = se.values[2]
            mAccelLast = mAccelCurrent
            mAccelCurrent =
                Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = mAccelCurrent - mAccelLast
            mAccel = mAccel * 0.9f + delta // perform low-cut filter

            if (mAccel > 12) {
                throwDices()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }
}
