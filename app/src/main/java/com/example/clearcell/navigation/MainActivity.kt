package com.example.clearcell.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.clearcell.R
import com.example.clearcell.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private lateinit var drawerLayout : DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupWithNavController(binding.navView, navController)

        drawerLayout = binding.drawerLayout
        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

//        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)

    }
//        val a : ClearGame = ClearGame(3,4, 1)
//        a.nextAnimationStep()
//        a.nextAnimationStep()
//        a.nextAnimationStep()
//        a.nextAnimationStep()
//        Log.d("HW:BEFORE", a.toString())
//        a.processCell(1,1)
//        Log.d("HW:AFTER", a.toString())

//        class CellAdapter : BaseAdapter {
//            private var context : Context? = null
//            private var game : ClearGame? = null
//            private var rowSize : Int = 0
//            private var colSize : Int = 0
//
//            constructor(context :Context, game : ClearGame, rowSize : Int, colSize : Int) : super()
//            {
//                this.context = context
//                this.game = game
//                this.rowSize = rowSize
//                this.colSize = colSize
//            }
//
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//                val cell = game!!.getCell(position/rowSize-1, position-(position/rowSize)*rowSize-1)
//                var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                var cellView = inflater.inflate(R.layout.cell, null)
//                cellView.setBackgroundColor(cell.getColor())
//                return cellView
//            }
//
//
//
//            override fun getItem(position: Int): Any {
//                return game!!.getCell(position/rowSize-1, position-(position/rowSize)*rowSize-1)
//            }
//
//            override fun getItemId(position: Int): Long {
//                return position.toLong()
//            }
//
//            override fun getCount(): Int {
//                return rowSize*colSize
//            }
//
//        }
}