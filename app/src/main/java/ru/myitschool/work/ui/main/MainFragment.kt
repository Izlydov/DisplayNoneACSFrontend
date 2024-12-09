package ru.myitschool.work.ui.main

import android.content.Context.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.core.components.employee.EmployeeAuthManager
import ru.myitschool.work.core.components.employee.models.Employee
import ru.myitschool.work.databinding.FragmentMainBinding
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var login: String = ""

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        login = sharedPreferences.getString("LOGIN", "no login").toString()
        refresh()
        initButtons()
        if (QrScanDestination.REQUEST_KEY != "keq_qr"){
            Log.w("NOONE1", QrScanDestination.REQUEST_KEY)
        }
    }
    private fun initButtons(){
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
        Thread {
            val employee = EmployeeAuthManager.getEmployeeInfo(login)
            requireActivity().runOnUiThread {
                updateUser(employee.get())
            }
        }.start();
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUser(employee: Employee){
        binding.fullname.setText(employee.name)
        binding.position.setText(employee.position)
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        binding.lastEntry.setText((SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(employee.lastVisit)))
//        binding.lastEntry.setText(employee.lastVisit.toString())
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
