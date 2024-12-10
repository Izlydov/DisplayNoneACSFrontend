package ru.myitschool.work.ui.qr.result


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentQrResultBinding

@AndroidEntryPoint
class QrResultFragment : Fragment(R.layout.fragment_qr_result) {
    private var _binding: FragmentQrResultBinding? = null
    private val binding: FragmentQrResultBinding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var login: String = ""

    private val viewModel: QrResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQrResultBinding.bind(view)
        login = sharedPreferences.getString("LOGIN", "no login").toString()
        binding.close.setOnClickListener(this::closeQrScanFragment)

        this.openDoor()
    }

    private fun openDoor() {
        Thread {
            val result = viewModel.openDoor(login, getQrCode())

            requireActivity().runOnUiThread {
                setResult(if (result) getString(R.string.success) else getString(R.string.wrong))
            }
        }.start()
    }

    private fun getQrCode(): String {
        return arguments?.getString("qrCode") ?: "No QR Code Provided"
    }

    private fun closeQrScanFragment(view: View) {
        findNavController().navigate(R.id.action_qrResultFragment_to_mainFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }

    private fun setResult(result: String) {
        binding.result.text = result
    }
}