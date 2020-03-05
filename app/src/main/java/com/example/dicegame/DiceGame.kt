package com.example.dicegame

import android.graphics.Color.WHITE
import android.os.Parcel
import android.os.Parcelable


//data class DiceGame(val dices : List<Dice>)

data class Dice( var max : Int = 6 , var colour : Int = WHITE): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeInt(max)
        parcel.writeInt(colour)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dice> {
        override fun createFromParcel(parcel: Parcel): Dice {
            return Dice(parcel)
        }

        override fun newArray(size: Int): Array<Dice?> {
            return arrayOfNulls(size)
        }
    }
}
