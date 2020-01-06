package com.example.clearcell.navigation


import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.clearcell.R
import com.example.clearcell.databinding.FragmentPlayBinding
import com.example.clearcell.model.Cell
import com.example.clearcell.model.ClearGameAdapter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class PlayFragment : Fragment() {
    private lateinit var gameAdapter: ClearGameAdapter
    private var rowSize : Int = 0
    private var colSize : Int = 8
    private var cellSize : Int = 0
    private val strategy = 1
    private var binding: FragmentPlayBinding? = null
    private var mTimer : Timer = Timer()
    private var cellGridView : GridView? = null
    private var scoreView : TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dm : DisplayMetrics = context!!.resources.displayMetrics

        if (binding == null)
        {
            cellSize = (dm.widthPixels * 0.8 / colSize).toInt()
            rowSize = (dm.heightPixels * 0.8 / cellSize).toInt()
            gameAdapter = ClearGameAdapter(
                context,
                rowSize,
                colSize,
                cellSize,
                strategy
            )
            for (i in 0 until rowSize*colSize)
            {
                gameAdapter.addItem(Cell.EMPTY)
            }
//            setHasOptionsMenu(true)
            binding = DataBindingUtil.inflate<FragmentPlayBinding>(
                inflater, R.layout.fragment_play,
                container, false
            )

        }
        if (cellGridView == null) {
            cellGridView = binding!!.cellGridView
            cellGridView!!.adapter = gameAdapter
            cellGridView!!.columnWidth = (dm.widthPixels * 0.8 / colSize).toInt()
            cellGridView!!.numColumns = colSize
            cellGridView!!.layoutParams.width = (dm.widthPixels * 0.8).toInt()
            cellGridView!!.layoutParams.height = rowSize * cellSize + 1
            scoreView = binding!!.scoreNumTextView
            cellGridView!!.setOnItemClickListener { _, _, position, _ ->
                if (gameAdapter.getCell(position/colSize, position - position/colSize * colSize).getColor() != Color.TRANSPARENT)
                {
                    if (gameAdapter.processCell(position/colSize, position - position/colSize * colSize))
                    {
                        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                    }
                    scoreView!!.text = gameAdapter.getScore().toString()
                }
            }
            mTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    nextAnimation()
                }
            }, 1000, 500)
        }
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mTimer.schedule(object: TimerTask() {
//            override fun run()
//            {
//                nextAnimation()
//            }
//        }, 1000)

        //TODO: cell 클릭시 사라지게 하기
//        view.requestLayout()


    }

    private fun nextAnimation()
    {
        if (binding != null)
        {
            if (gameAdapter.nextAnimationStep())
            {
                this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
//                fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            }
            else
            {
                //Game Over
                mTimer!!.cancel()
                this.view!!.findNavController()
                    .navigate(PlayFragmentDirections.actionPlayFragmentToGameOverFragment())
            }
        }
    }

}
