package ru.myitschool.work.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.camera.core.ImageProcessor.Response
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.myitschool.work.core.components.employee.EmployeeAuthManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _state = MutableStateFlow(true)
    val state = _state.asStateFlow()

    fun checkUserAuth(login: String): Boolean {
        return EmployeeAuthManager.checkUserAuth(login)
    }

    fun saveUserLogin(login: String, sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString("LOGIN", login)
        editor.apply()
    }
}