package com.example.clearcell.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clearcell.R
import kotlinx.android.synthetic.main.cell.view.*

class ClearGameAdapter(
    private val context: Context,
    private var cellList: ArrayList<Cell>,
    private val cellSize: Int,
    private val colSize: Int,
    private val rowSize: Int
) : GameAdapter(context, cellList, /*rowSize,*/ colSize) {

    private var score: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cellLayout: View = LayoutInflater.from(context).inflate(
            R.layout.cell,
            parent,
            false
        )
        if (cellLayout.layoutParams.height == 0 || cellLayout.layoutParams.width == 0) {
            cellLayout.layoutParams.height = cellSize
            cellLayout.layoutParams.width = cellSize
        }
        return ViewHolder(cellLayout)
    }


    private fun rowEmpty(rowIn: Int): Boolean {
        for (i in 0 until colSize) {
            if (getCell(rowIn, i) != Cell.EMPTY) {
                return false
            }
        }
        return true
    }

    override fun isGameOver(): Boolean {
        //Game Over when last row is not empty
        return !rowEmpty(rowSize - 1)
    }

    override fun getScore(): Int {
        return score
    }

    override fun nextAnimationStep(): Boolean {

        if (!isGameOver()) {
            // from the second row, each row goes down by one
            for (row in (rowSize - 2) downTo 0) {
                for (col in 0 until colSize) {
                    setCell(row + 1, col, getCell(row, col))
                }

            }

            //set up new row on the top
            for (col in 0 until colSize) {
                setCell(0, col, getNonEmptyRandomCell())
            }
            return true
        }
        return false
    }


    override fun processCell(row: Int, col: Int): Boolean {
        val color = getCell(row, col).getColor()
        val success = processCell(row, col, color, false)
        if (success) {
            cleanUp()
        }
        return success
    }

    private fun processCell(row: Int, col: Int, color: Int, flag: Boolean): Boolean {
        //prevDir : Previous Direction. 0: own, 1: up
        //clear all connected cells with same color, using DFS
        if (getCell(row, col) == Cell.EMPTY) {
            return flag
        }

        //clear this cell
        setCell(row, col, Cell.EMPTY)
        score++
        notifyItemChanged(row * colSize + col, "click")

        //TODO: Set up previous cell

        //UP
        if (row < rowSize - 1 && getCell(row + 1, col).getColor() == color) {
            processCell(row + 1, col, color, true)
        }

        //DOWN
        if (row > 0 && getCell(row - 1, col).getColor() == color) {
            processCell(row - 1, col, color, true)
        }

        //RIGHT
        if (col < colSize - 1 && getCell(row, col + 1).getColor() == color) {
            processCell(row, col + 1, color, true)
        }

        //LEFT
        if (col > 0 && getCell(row, col - 1).getColor() == color) {
            processCell(row, col - 1, color, true)
        }

        return true
    }

    private fun cleanUp() {

        for (c in 0 until colSize) {
            for (r1 in 0 until rowSize - 1) {
                if (getCell(r1, c) == Cell.EMPTY)
                //빈 셀을 찾으면 다음에 셀이 더이상 없거나 비지 않은 셀을 찾을때까지 계속함
                {
                    var nextPos = r1
                    for (r2 in r1 until rowSize - 1) {
                        if (getCell(r2, c) != Cell.EMPTY) {
                            nextPos = r2
                            break
                        }
                    }
                    if (nextPos > r1) {
                        for (r3 in 0..(nextPos - r1)) {
                            setCell(r3 + r1, c, getCell(nextPos + r3, c))
                            setCell(nextPos + r3, c, Cell.EMPTY)
                            notifyItemChanged(r3 + r1, c)
                            notifyItemChanged(nextPos + r3, c)
                        }
                    }
//                    setCell(r, c, getCell(r+1, c))
//                    setCell(r+1, c, Cell.EMPTY)
                }
            }
//
        }
//        notifyDataSetChanged()
    }

/*
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        var holder: RecyclerView.ViewHolder
//
//        val view = LayoutInflater.from(context).inflate(R.layout.cell, null)
//
//        //view를 cell이미지뷰에 연결해주기
//        val cellView = view.findViewById<ImageView>(R.id.cellImageView)
//
//        //position에 있는 cell의 컬러를 받아 view에 설정해주기
//        val cell = cellList[position]
//        when (cell.getColor()) {
//            Cell.RED.getColor() -> cellView.setImageResource(R.color.red)
//            Cell.BLUE.getColor() -> cellView.setImageResource(R.color.blue)
//            Cell.GREEN.getColor() -> cellView.setImageResource(R.color.green)
//            Cell.YELLOW.getColor() -> cellView.setImageResource(R.color.yellow)
//            else -> cellView.setImageResource(R.color.transparent)
//        }
//        //cell 사이즈 설정하기
//        if (convertView != null) {
//            holder = RecyclerView.ViewHolder()
//            cellView.layoutParams.height = cellSize
//            cellView.layoutParams.width = cellSize
//        }
//        return view
//    }
*/


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cellView: AppCompatTextView = view.cellView as AppCompatTextView
        //    var cellView : View = view.cellView
        //    val width = view.width
        //    val height = view.height
        //    var color = (view.background as ColorDrawable).color
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cellView.setBackgroundColor(cellList[position].getColor())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (payload is String && payload == "click") {
                    holder.cellView.animate()
                        .setDuration(500)
                        .setInterpolator(OvershootInterpolator())
                        .alpha(0f)
                }
            }
        }
    }
}
