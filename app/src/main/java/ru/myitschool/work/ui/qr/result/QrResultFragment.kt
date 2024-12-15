package ru.myitschool.work.ui.qr.result


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
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
        binding.close.setOnClickListener { v -> this.closeResultFragment() }

        this.openDoor()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeResultFragment()
                }
            }
        )
    }

    private fun openDoor() {
        val qrCodeValue: String? = getQrCodeOrNull()
        if (qrCodeValue == null || qrCodeValue == "Отмена") {
            setResult(getString(R.string.cancel))
            return
        }

        val qrCodeValueLong: Long

        try {
            qrCodeValueLong = qrCodeValue.toLong()
        } catch (exception: Exception) {
            when (exception) {
                is NumberFormatException, is IllegalArgumentException -> setResult(getString(R.string.wrong))
                else -> throw exception
            }
            return
        }

        Thread {
            val result = viewModel.openDoor(login, qrCodeValueLong)

            requireActivity().runOnUiThread {
                if (result == 200) setResult(getString(R.string.success)) else setResult(getString(R.string.wrong))
            }
        }.start()
    }

    private fun getQrCodeOrNull(): String? {
        return arguments?.getString("qrCode")
    }

    private fun closeResultFragment() {
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