package com.tushar.wallvista.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tushar.wallvista.databases.ImageDatabase
import com.tushar.wallvista.repositiory.Repository
import java.io.File

class LockWallWorker(appContext: Context, workParams: WorkerParameters) :
    Worker(appContext, workParams) {
    private val dao = ImageDatabase.createDatabase(appContext).imageDAO()
    private val repository: Repository = Repository(dao)
    private val lockPrefs =
        appContext.getSharedPreferences("lock_wallpaper_prefs", Context.MODE_PRIVATE)
    private val homePrefs = appContext.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)

    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    override fun doWork(): Result {
        println("edrr lock running tasks")
        val imageList = repository.getLockImages()
        if (imageList.isNotEmpty()) {
            val lastIndex = lockPrefs.getInt("last_index", -1)
            val nextIndex = (lastIndex + 1) % imageList.size
            val selectedImage = imageList[nextIndex]

            try {
                val file = File(selectedImage.img)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    try {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    } catch (_: Exception) {
                        Log.e("WallWorker", "Failed to set lock wallpaper, trying fallback")
                        wallpaperManager.setBitmap(bitmap) // fallback to home screen
                    }
                    // Save next index
                    lockPrefs.edit { putInt("last_index", nextIndex) }
                    showNotification("LockScreen Wallpaper Changed")

                    Log.d("WallWorker", "Wallpaper set: ${file.absolutePath}")
                    return Result.success()
                } else {
                    Log.e("WallWorker", "Image file not found: ${file.absolutePath}")
                }
            } catch (e: Exception) {
                Log.e("WallWorker", "Error setting wallpaper", e)
            }
        } else {
            Log.d("WallWorker", "no images found")
        }
///////////////////////////////////Home screen///////////////////////////////////////////////////

        println("edrr home running tasks")
        val homeImageList = repository.getImages()
        if (homeImageList.isNotEmpty()) {
            val lastIndex = homePrefs.getInt("last_index", -1)
            val nextIndex = (lastIndex + 1) % homeImageList.size
            val selectedImage = homeImageList[nextIndex]
            try {
                val file = File(selectedImage.img)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)

                    // Save next index
                    homePrefs.edit { putInt("last_index", nextIndex) }
                    showNotification("HomeScreen Wallpaper Changed")
                    Log.d("WallWorker", "Wallpaper set: ${file.absolutePath}")
                    return Result.success()
                } else {
                    Log.e("WallWorker", "Image file not found: ${file.absolutePath}")
                }
            } catch (e: Exception) {
                Log.e("WallWorker", "Error setting wallpaper", e)
            }
        } else {
            Log.d("WallWorker", "no images found")
        }


        return Result.success()
    }

    private fun showNotification(message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "lock_wallpaper_channel"

        // Create notification channel (required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lock Wallpaper Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Lock Wallpaper Update")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_gallery) // You can use your own icon here
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

}