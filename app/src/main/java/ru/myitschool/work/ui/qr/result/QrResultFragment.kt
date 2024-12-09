package ru.myitschool.work.ui.qr.result
import ru.myitschool.work.ui.qr.result.QrResultViewModel


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.core.components.employee.EmployeeAuthManager
import ru.myitschool.work.databinding.FragmentQrResultBinding
import kotlin.math.log

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
        Log.i("wtf", login)

        Thread {
            val result = getResult()

            requireActivity().runOnUiThread {
                Log.i("es", result.toString())
//                val message = if (!result) {
//                    getString(R.string.cancel)
//                } else {
//                    if (result) {
//                        getString(R.string.success)
//                    } else {
//                        getString(R.string.wrong)
//                    }
//                }
//                setResult(message)
            }
        }.start()
    }

    private fun getQrCode(): String? {
        return arguments?.getString("qrCode") ?: "No QR Code Provided"
    }

    private fun getResult(): Boolean {
        val code = getQrCode()
        Log.w("test", code!!)
        Log.w("test", login)


        val result = EmployeeAuthManager.openDoor(login, code.toLong())
        return result

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }

    private fun setResult(result: String) {
        binding.result.setText(result)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}