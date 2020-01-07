package com.example.clearcell.model

import android.graphics.Color
import kotlin.random.Random

enum class Cell(private var colorIn: Int, private var colorName: String) {
    RED(Color.RED, "R"), GREEN(Color.GREEN, "G"),
    BLUE(Color.BLUE, "B"), YELLOW(Color.YELLOW, "Y"),
    EMPTY(Color.TRANSPARENT, "E");

    override fun toString(): String {
        return colorName
    }

    fun getColor() : Int {
        return colorIn
    }

    fun getName() : String {
        return colorName
    }

}

/*
 *  Returns random color Cell object
 */
fun getNonEmptyRandomCell() : Cell {
    val colorInt : Int = Random.nextInt(numColors() - 1)
    for (cell in Cell.values())
    {
        if (cell.ordinal == colorInt)
        {
            return cell
        }
    }
    throw Exception("Invalid random number generated")
}

private fun numColors() : Int {
    return Cell.values().size
}