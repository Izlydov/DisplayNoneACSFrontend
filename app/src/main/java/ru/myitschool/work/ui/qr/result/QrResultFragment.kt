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
import ru.myitschool.work.databinding.FragmentQrResultBinding
import java.time.LocalDateTime

@AndroidEntryPoint
class QrResultFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentQrResultBinding? = null
    private val binding: FragmentQrResultBinding get() = _binding!!

    private val viewModel: QrResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQrResultBinding.bind(view)

    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
