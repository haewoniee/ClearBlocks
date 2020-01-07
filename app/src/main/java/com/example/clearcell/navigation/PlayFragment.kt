package com.example.clearcell.navigation


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
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
            recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(
                context!!,
                recyclerView!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        gameAdapter.processCell(
                            position / colSize,
                            position - position / colSize * colSize
                        )
                    }
                }
            ))
        }
        if (scoreView == null) {
            scoreView = binding!!.scoreNumTextView
            handlerTask = object : Runnable {
                override fun run() {
                    nextAnimation()
                    handler.postDelayed(this, 2000)
                }
            }
            refreshTask = object : Runnable {
                override fun run() {
                    gameAdapter.notifyDataSetChanged()
                    handler.postDelayed(this, 100)
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
                this.view!!.findNavController()
                    .navigate(PlayFragmentDirections.actionPlayFragmentToGameOverFragment())
            }
        }
    }

    class RecyclerItemClickListener(
        context: Context,
        recyclerView: RecyclerView,
        private val listener: OnItemClickListener?
    ) : RecyclerView.OnItemTouchListener {
        private val mGestureDetector: GestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

        interface OnItemClickListener {
            fun onItemClick(view: View, position: Int)
        }

        override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
            val childView = view.findChildViewUnder(e.x, e.y)
            if (childView != null && listener != null && mGestureDetector.onTouchEvent(e)) {
                try {
                    listener.onItemClick(childView, view.getChildAdapterPosition(childView))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return true
            }
            return false
        }

        override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

}
