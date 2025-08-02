package com.tushar.wallvista.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tushar.wallvista.R
import com.tushar.wallvista.databinding.ActivityMainBinding
import com.tushar.wallvista.workers.LockWallWorker
import com.tushar.wallvista.workers.WallWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        if (isFirstLaunch(this)) {
            showInfoDialog()
            setFirstLaunchFalse(this)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        val request= PeriodicWorkRequestBuilder<LockWallWorker>(
            200, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "wallpaper_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
        val request2= PeriodicWorkRequestBuilder<WallWorker>(
            200,TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("wallpaper_lock_work",
                ExistingPeriodicWorkPolicy.REPLACE, request2)


        val navHost: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        binding.btnNav.setOnItemSelectedListener {
            if (it.itemId == R.id.home) {
                if (navController.currentDestination?.id != R.id.homeFragment) {
                    navController.navigate(R.id.action_lockFragment_to_homeFragment)
                }
            }
            if (it.itemId == R.id.lock) {
                if (navController.currentDestination?.id != R.id.lockFragment) {
                    navController.navigate(R.id.action_homeFragment_to_lockFragment)
                }
            }
            true
        }


    }
    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("Make unlimited list of Wallpapers and let them change automatically oh their own. \n" +
                    "Use Upload \uD83D\uDCE4 button to add images\n" +
                    "Long Press on Image to Delete ❌ it")

            .setPositiveButton("OK", null)
            .setCancelable(false)
            .show()
    }

    fun isFirstLaunch(context: Context): Boolean {
        val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
        return prefs.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunchFalse(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit { putBoolean("is_first_launch", false) }
    }
}
