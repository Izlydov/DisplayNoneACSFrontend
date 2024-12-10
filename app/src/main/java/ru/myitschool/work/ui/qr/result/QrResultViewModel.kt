package ru.myitschool.work.ui.qr.result

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.myitschool.work.core.components.employee.EmployeeAuthManager
import javax.inject.Inject
import kotlin.text.toLong

@HiltViewModel
class QrResultViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _state = MutableStateFlow(true)
    val state = _state.asStateFlow()

    fun openDoor(login: String, code: String): Boolean {
        return EmployeeAuthManager.openDoor(login, code.toLong())
    }
}