package com.example.clearcell.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.clearcell.R
import com.example.clearcell.databinding.FragmentGameOverBinding

/**
 * A simple [Fragment] subclass.
 */
class GameOverFragment : Fragment() {

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        navController = this.findNavController()
        val binding: FragmentGameOverBinding = DataBindingUtil.inflate<FragmentGameOverBinding>(
            inflater,
            R.layout.fragment_game_over,
            container,
            false
        )
        return binding.root
    }


}
