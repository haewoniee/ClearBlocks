package com.example.clearcell.model

import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(
    context: Context,
    private val listener: OnItemTouchListener?
) : RecyclerView.OnItemTouchListener {
    //    private val mGestureDetector: GestureDetector =
//        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
////            override fun onSingleTapDown(e: MotionEvent): Boolean {
////                return true
////            }
//            override fun onDown(e: MotionEvent?): Boolean {
//                return true
//            }
//        })
//
    interface OnItemTouchListener {
        fun onItemTouch(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_DOWN) {
            val childView = view.findChildViewUnder(e.x, e.y)
            if (childView != null && listener != null) {
                try {
                    listener.onItemTouch(childView, view.getChildAdapterPosition(childView))
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return true
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
