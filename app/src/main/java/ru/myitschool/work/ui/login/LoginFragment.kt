package ru.myitschool.work.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import okio.IOException
import ru.myitschool.work.R
import ru.myitschool.work.databinding.FragmentLoginBinding
import ru.myitschool.work.utils.collectWhenStarted

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private var savedLogin: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        savedLogin = sharedPreferences.getString("LOGIN", "") ?: ""

        setupLoginButton()
        subscribe()
    }

    private fun isAuthorized(): Boolean {
        return savedLogin.isNotEmpty()
    }

    private fun setupLoginButton() {
        binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.error.visibility = View.GONE
                val username = s.toString()
                binding.login.isEnabled = isUsernameValid(username)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isUsernameValid(username: String): Boolean {
        val alf = "^[a-zA-Z0-9]+$".toRegex()
        return username.isNotEmpty() &&
                username.length >= 3 &&
                !username[0].isDigit() &&
                alf.matches(username)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    }

    private fun subscribe() {
        viewModel.state.collectWhenStarted(this) { state ->
            binding.login.setOnClickListener(this::onLoginButtonClicked)
        }
    }

    private fun onLoginButtonClicked(view: View) {
        val login = binding.username.text.toString()
        if (login.isEmpty()) return

        Thread {
            try {
                val authResult = viewModel.checkUserAuth(login)

                requireActivity().runOnUiThread {
                    if (authResult) processAuthSuccess(login) else processAuthError()
                }
            } catch (_: IOException) {
                processAuthError()
            }
        }.start()
    }

    private fun processAuthSuccess(login: String) {
        viewModel.saveUserLogin(login, sharedPreferences)
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun processAuthError() {
        binding.error.visibility = View.VISIBLE
    }
}