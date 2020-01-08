package com.example.clearcell.model


import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class GameAdapter(
    private val context: Context,
    private var cellList: ArrayList<Cell>,
    private val colSize: Int
) : RecyclerView.Adapter<ClearGameAdapter.ViewHolder>() {

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClearGameAdapter.ViewHolder

    override fun getItemCount(): Int {
        return cellList.size
    }


    protected fun setCell(row: Int, col: Int, cell: Cell) {
        cellList[row * colSize + col] = cell

    }

    fun getCell(row : Int, col : Int) : Cell
    {
        return cellList[row * colSize + col]
    }

    abstract fun isGameOver(): Boolean

    abstract fun getScore(): Int

    abstract fun nextAnimationStep(): Boolean

    abstract fun processCell(row: Int, col: Int): Boolean

/*    override fun toString(): String
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
//
//    override fun getCount(): Int
//    {
//        return cellList.size
//    }
//
//    override fun getItem(position: Int): Any
//    {
//        return cellList[position]
//    }
//
//    override fun getItemId(position: Int): Long
//    {
//        return position.toLong()
//    }
//    protected fun getRowSize() : Int
//    {
//        return rowSize
//    }
//
//    protected fun getColSize() : Int
//    {
//        return colSize
//    }
//
//    fun addItem(cell: Cell)
//    {
//        cellList.add(cell)
//    }
//
*/


}

