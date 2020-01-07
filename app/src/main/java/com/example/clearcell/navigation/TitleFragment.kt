package com.example.clearcell.navigation


import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.clearcell.R
import com.example.clearcell.databinding.FragmentTitleBinding


/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

//        binding.playbutton.setOnClickListener { view: View ->
//            view.findNavController()
//                .navigate(TitleFragmentDirections.actionTitleFragmentToPlayFragment())
//        }
        var rect: Rect? = null
        binding.playbutton.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    rect = Rect(view.left, view.top, view.right, view.bottom)
                    Log.d("플레이버튼 액션", "DOWN")
                    binding.playbutton.setImageResource(R.drawable.play_button_clicked)
                }
                MotionEvent.ACTION_UP -> {
                    if (rect!!.contains(
                            view.left + motionEvent.x.toInt(),
                            view.top + motionEvent.y.toInt()
                        )
                    ) {
                        Log.d("플레이버튼 액션", "UP: 버튼 이벤트")
                        view.findNavController()
                            .navigate(TitleFragmentDirections.actionTitleFragmentToPlayFragment())
                    } else {
                        Log.d("플레이버튼 액션", "UP: 버튼 취소")
                    }
                    binding.playbutton.setImageResource(R.drawable.play_button_normal)
                }
            }
            true
        }

        return binding.root
    }

//    recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(
//    context!!,
//    recyclerView!!,
//    object : RecyclerItemClickListener.OnItemClickListener {
//        override fun onItemClick(view: View, position: Int) {
//            if (position >= 0)
//            {
//                val rowPos = position / colSize
//                val colPos = position - rowPos * colSize
//                gameAdapter.processCell(
//                    rowPos,
//                    colPos
//                )
//                scoreView!!.text = gameAdapter.getScore().toString()
//            }
//        }
//    }
//    ))

}
