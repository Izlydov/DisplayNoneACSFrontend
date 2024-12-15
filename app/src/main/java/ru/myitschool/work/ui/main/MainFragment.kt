package ru.myitschool.work.ui.main


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
=======
import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.core.components.employee.EmployeeAuthManager
import ru.myitschool.work.core.components.employee.models.Employee
import ru.myitschool.work.databinding.FragmentMainBinding
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import java.text.SimpleDateFormat
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

        waitForQRScanResult()
    }

    private fun waitForQRScanResult() {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            QrScanDestination.REQUEST_KEY
        )?.observe(viewLifecycleOwner) { bundle ->
            val qrCode = bundle.getString("key_qr")
            if (!qrCode.isNullOrEmpty()) navigateToQrResultFragment(qrCode)
        }
    }

    private fun navigateToQrResultFragment(qrCode: String) {
        val bundle = Bundle().apply {
            putString("qrCode", qrCode)
        }

        findNavController().navigate(R.id.action_mainFragment_to_qrResultFragment, bundle)
    }

    private fun initButtons() {
        binding.logout.setOnClickListener { view ->
            sharedPreferences.edit().clear().apply()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
        binding.refresh.setOnClickListener { view ->
            refresh()
        }
        binding.scan.setOnClickListener { view ->
            findNavController().navigate(R.id.action_mainFragment_to_qrScanFragment)
        }
    }

    private fun refresh() {
        Thread {
            val employee = EmployeeAuthManager.getEmployeeInfo(login)
            requireActivity().runOnUiThread {
                if (!employee.isEmpty) processLoadEmployeeSuccess(employee.get()) else processLoadEmployeeError()
            }
        }.start()
    }

    private fun setGlobalVisibility(visibility: Int) {
        val root = binding.root as ViewGroup
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            child.visibility = visibility
        }
    }

    private fun showAll() {
        setGlobalVisibility(View.VISIBLE)
    }

    private fun hideAll() {
        setGlobalVisibility(View.GONE)
    }

    private fun showError() {
        binding.error.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.error.visibility = View.GONE
    }

    private fun processLoadEmployeeError() {
        hideAll()
        showError()
        binding.refresh.visibility = View.VISIBLE
    }

    private fun processLoadEmployeeSuccess(employee: Employee) {
        showAll()
        hideError()
        updateUser(employee)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }

    private fun updateUser(employee: Employee) {
        binding.fullname.text = employee.name
        binding.position.text = employee.position
        binding.lastEntry.text = (SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(employee.lastVisit))
        this.setUserImage(employee.photo)
    }

    private fun setUserImage(url: String) {
        Thread {
            var image: Bitmap? = null

            try {
                val img = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(img)

                requireActivity().runOnUiThread {
                    binding.photo.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}