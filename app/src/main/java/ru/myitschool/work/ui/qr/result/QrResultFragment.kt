package ru.myitschool.work.ui.qr.result



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
import ru.myitschool.work.core.components.employee.EmployeeAuthManager
import ru.myitschool.work.databinding.FragmentQrResultBinding
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import java.time.LocalDateTime

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
        val result = getResult()
        val message = if (result == null || result == false) {
            getString(R.string.cancel)
        } else {
            if (result) {
                getString(R.string.success)
            } else {
                getString(R.string.wrong)
            }
        }
        setResult(message)
    }
    private fun getQrCode(): String? {
        return findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.get<Bundle>(QrScanDestination.REQUEST_KEY)
            ?.let { bundle ->
                QrScanDestination.getDataIfExist(bundle)
            }
    }


    private fun getResult(): Boolean {
//как-то брать code из QrScanDestination в QrScanFragment
        Log.w("test", "code")

        val code = getQrCode()
        Log.w("test", code!!)
        val result = EmployeeAuthManager.openDoor(login, code!!.toLong()) // what is code
        return result
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }

    private fun setResult(result: String){
        binding.result.setText(result)
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
