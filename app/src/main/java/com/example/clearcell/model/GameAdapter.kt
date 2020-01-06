package com.example.clearcell.model


import android.util.Log
import android.widget.BaseAdapter
import java.util.*

abstract class GameAdapter(
    private val rowSize : Int,
    private val colSize : Int,
    private val strategy : Int) : BaseAdapter()
{
    var cellList : ArrayList<Cell> = arrayListOf<Cell>()

    init
    {
        //if row/col is negative
        if (rowSize < 0 || colSize < 0)
        {
            Log.wtf("HW","error! numRow/numCol is negative")
            throw Exception("error! numRow/numCol is negative")
        }
    }
//    constructor(context: Context, boardList: ArrayList<Cell>) : super() {
//        this.context = context
//        this.boardList = boardList
//    }

//    override fun toString(): String
//    {
//        var result = ""
//        for (i in 0 until rowSize)
//        {
//            for (j in 0 until colSize)
//            {
//                result += cellList[i*rowSize + colSize]
//            }
//            result += "\n"
//        }
//        return result
//    }

    override fun getCount(): Int
    {
        return cellList.size
    }

    override fun getItem(position: Int): Any
    {
        return cellList[position]
    }

    override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

    protected fun setUpCell(row : Int, col : Int, cell : Cell)
    {
        try {
            cellList[row * colSize + col] = cell
        }
        catch (exception: Exception)
        {
            val a = 0
        }
    }

    fun getCell(row : Int, col : Int) : Cell
    {
        return cellList[row * colSize + col]
    }

    protected fun getRowSize() : Int
    {
        return rowSize
    }

    protected fun getColSize() : Int
    {
        return colSize
    }

    fun addItem(cell: Cell)
    {
        cellList.add(cell)
    }

    abstract fun isGameOver() : Boolean

    abstract fun getScore() : Int

    abstract fun nextAnimationStep() : Boolean

    abstract fun processCell(row : Int, col: Int) : Boolean

}