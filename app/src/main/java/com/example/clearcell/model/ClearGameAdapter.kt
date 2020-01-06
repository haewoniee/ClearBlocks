package com.example.clearcell.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clearcell.R

class ClearGameAdapter(
    private val context: Context?,
    rowSize: Int,
    colSize: Int,
    cellSize: Int,
    strategy: Int
) : GameAdapter(rowSize, colSize, strategy) {

    private var score: Int = 0
    private var dpWidth: Int = 0
    private var cellSize: Int = cellSize
    private var curRow: Int = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

//        var view : View = if (convertView != null) convertView!!.findViewById<View>(R.id.cellImageView)
//            else LayoutInflater.from(context).inflate(R.layout.cell,null)
        val view = LayoutInflater.from(context).inflate(R.layout.cell, null)

        //view를 cell이미지뷰에 연결해주기
        val cellView = view.findViewById<ImageView>(R.id.cellImageView)

        //position에 있는 cell의 컬러를 받아 view에 설정해주기
        val cell = cellList[position]
        when (cell.getColor()) {
            Cell.RED.getColor() -> cellView.setImageResource(R.color.red)
            Cell.BLUE.getColor() -> cellView.setImageResource(R.color.blue)
            Cell.GREEN.getColor() -> cellView.setImageResource(R.color.green)
            Cell.YELLOW.getColor() -> cellView.setImageResource(R.color.yellow)
            else -> cellView.setImageResource(R.color.transparent)
        }
        //cell 사이즈 설정하기
        if (convertView != null) {
            cellView.layoutParams.height = cellSize
            cellView.layoutParams.width = cellSize
        }
        return view
    }

    private fun rowEmpty(rowIn: Int): Boolean {
        for (i in 0 until getColSize()) {
            if (getCell(rowIn, i) != Cell.EMPTY) {
                return false
            }
        }
        return true
    }

    override fun isGameOver(): Boolean {
        //Game Over when last row is not empty
        return !rowEmpty(getRowSize()-1)
    }

    override fun getScore(): Int {
        return score
    }

    override fun nextAnimationStep(): Boolean {

        if (!isGameOver()) {
            // from the second row, each row goes down by one
            for (row in (curRow - 1) downTo 0) {
                for (col in 0 until getColSize()) {
                    setUpCell(row + 1, col, getCell(row, col))
                }
            }

            //set up new row on the top
            for (col in 0 until getColSize()) {
//                addItem(getNonEmptyRandomCell())
                setUpCell(0, col, getNonEmptyRandomCell())
            }
            curRow++
            return true
        }
        return false
    }

    override fun processCell(row: Int, col: Int) : Boolean{
        val color = getCell(row, col).getColor()
        return processCell(row, col, color, 0, false)
    }

    private fun processCell(row: Int, col: Int, color: Int, prevDir : Int, flag: Boolean) : Boolean{
        //prevDir : Previous Direction. 0: own, 1: up
        //clear all connected cells with same color, using DFS
        if (getCell(row, col) == Cell.EMPTY) {
            return flag
        }

        //clear this cell
        setUpCell(row, col, Cell.EMPTY)
        score++

        //TODO: Set up previous cell
//        if ()

        //UP
        if (row < getRowSize() - 1 && getCell(row + 1, col).getColor() == color) {
            processCell(row + 1, col, color, 1, true)
        }

        //DOWN
        if (row > 0 && getCell(row - 1, col).getColor() == color) {
            processCell(row - 1, col, color, -1, true)
        }

        //RIGHT
        if (col < getColSize() - 1 && getCell(row, col + 1).getColor() == color) {
            processCell(row, col + 1, color, 0, true)
        }

        //LEFT
        if (col > 0 && getCell(row, col - 1).getColor() == color) {
            processCell(row, col - 1, color, 0, true)
        }

        return true
    }

}