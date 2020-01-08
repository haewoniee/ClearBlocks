package com.example.clearcell.navigation


import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clearcell.R
import com.example.clearcell.databinding.FragmentPlayBinding
import com.example.clearcell.model.Cell
import com.example.clearcell.model.ClearGameAdapter
import com.example.clearcell.model.RecyclerItemClickListener
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PlayFragment : Fragment() {
    private lateinit var gameAdapter: ClearGameAdapter
    private var rowSize: Int = 0
    private var colSize: Int = 8
    private var cellSize: Int = 0
    private var binding: FragmentPlayBinding? = null
    private var recyclerView: RecyclerView? = null
    private var scoreView: TextView? = null
    private val cellList: ArrayList<Cell> = arrayListOf<Cell>()
    private val handler: Handler = Handler()
    private var handlerTask: Runnable? = null
    private var refreshTask: Runnable? = null
    private var intervalTime: Long = 2000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dm: DisplayMetrics = requireContext().resources.displayMetrics

        if (binding == null) {
            binding = DataBindingUtil.inflate<FragmentPlayBinding>(
                inflater, R.layout.fragment_play,
                container, false
            )
            cellSize = (dm.widthPixels * 0.8 / colSize).toInt()
            rowSize = (dm.heightPixels * 0.8 / cellSize).toInt()
            for (i in 0 until rowSize * colSize) {
                cellList.add(Cell.EMPTY)
            }

            gameAdapter = ClearGameAdapter(
                requireContext(),
                cellList,
                cellSize,
                colSize,
                rowSize
            )

        }
        if (recyclerView == null) {
            recyclerView = binding!!.cellGridRV as RecyclerView
            recyclerView!!.layoutManager = GridLayoutManager(requireContext(), colSize)
            recyclerView!!.adapter = gameAdapter
            recyclerView!!.layoutParams.width = (dm.widthPixels * 0.8).toInt()
            recyclerView!!.layoutParams.height = rowSize * cellSize + 1
            if (scoreView == null) {
                scoreView = binding!!.scoreNumTextView as TextView
            }
            recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(
                context!!,
                object : RecyclerItemClickListener.OnItemTouchListener {
                    override fun onItemTouch(view: View, position: Int) {
                        if (position >= 0) {
                            val rowPos = position / colSize
                            val colPos = position - rowPos * colSize
                            gameAdapter.processCell(
                                rowPos,
                                colPos
                            )
                            scoreView!!.text = gameAdapter.getScore().toString()
                            recyclerView!!.playSoundEffect(SoundEffectConstants.CLICK)
                        }
                    }
                }
            ))
            handlerTask = object : Runnable {
                override fun run() {
                    nextAnimation()
//                    gameAdapter.notifyDataSetChanged()
                    handler.postDelayed(this, intervalTime)
                }
            }
            refreshTask = object : Runnable {
                override fun run() {
                    gameAdapter.notifyDataSetChanged()
                    handler.postDelayed(this, 300)
                }
            }
            handler.post(handlerTask!!)
            handler.post(refreshTask!!)
        }
        return binding!!.root
    }


    private fun nextAnimation() {
        if (binding != null) {
            if (!gameAdapter.nextAnimationStep())
            {
                //Game Over
                handler.removeCallbacks(handlerTask!!)
                handler.removeCallbacks(refreshTask!!)
                this.view?.findNavController()
                    ?.navigate(PlayFragmentDirections.actionPlayFragmentToGameOverFragment())
            }
        }
    }


}
