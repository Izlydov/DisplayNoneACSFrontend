package ru.myitschool.work.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.ui.login.LoginDestination
import ru.myitschool.work.ui.login.LoginFragment
import ru.myitschool.work.ui.main.MainDestination
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import ru.myitschool.work.ui.qr.scan.QrScanFragment

// НЕ ИЗМЕНЯЙТЕ НАЗВАНИЕ КЛАССА!
@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        sharedPreferences = this.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val savedLogin = sharedPreferences.getString("LOGIN", "")
        val destination = if (!savedLogin.isNullOrEmpty()) MainDestination else LoginDestination

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
//        if (navHostFragment != null) {
//            val navController = navHostFragment.navController
//            val navInflater = navController.navInflater
//            val navGraph = navInflater.inflate(R.navigation.nav_graph)
//            val startDestination = if (!savedLogin.isNullOrEmpty()) MainDestination else MainDestination
//            Log.d("Navigation", "Setting start destination: $startDestination")
//
//            navGraph.setStartDestination(startDestination)
//            navController.graph = navGraph
//        }


        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onSupportNavigateUp()
                }
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val popBackResult = if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            false
        }
        return popBackResult || super.onSupportNavigateUp()
    }
}