package ru.myitschool.work.ui.main

import android.content.Context.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentMainBinding
import java.time.LocalDateTime

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        binding.logout.setOnClickListener { view ->
            sharedPreferences.edit().clear().apply()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)

        }
        binding.refresh.setOnClickListener { view ->
            refresh()
        }
        binding.scan.setOnClickListener{ view ->
            findNavController().navigate(R.id.action_mainFragment_to_qrScanFragment)
        }

    }
    private fun refresh(){
//обновить данные
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }
    private fun updateUser(fullname: String, position: String, lastEntry: LocalDateTime){
        binding.fullname.setText(fullname)
        binding.position.setText(position)
        binding.lastEntry.setText(lastEntry.toString())
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
