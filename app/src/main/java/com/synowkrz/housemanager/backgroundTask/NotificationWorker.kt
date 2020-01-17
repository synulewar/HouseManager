package com.synowkrz.housemanager.backgroundTask

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

import com.synowkrz.housemanager.TAG

import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.homeTaskList.HomeTaskListActivity
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.runBlocking


class NotificationWorker(val appContext : Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    val NOTIFICATION_ID = "houseManager_id"

    override fun doWork(): Result {
        Log.d(TAG, "Do work")
        var repository = HouseRepository(appContext)
        runBlocking {
            repository.refreshTaskList()
        }
        var taskList = repository.getAllHomeTaskAsync()
        var taskToBeDone = 0
        for (task in taskList) {
            if (task.daysExceeded >= 0) {
                taskToBeDone += 1
            }
        }

        if (taskToBeDone > 0 ) {
            sendNotification(taskToBeDone)
        }
        return Result.success()
    }


    fun sendNotification(taksToBeDone : Int) {
        Log.d(TAG, "Lets try to send notification")
        val intent = Intent(applicationContext, HomeTaskListActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val id = "HouseManagerChannel_01"
        val name = "HouseManagerChannel"
        val description = "Chanel to send home task notification"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(channel)


        val title = appContext.getString(R.string.app_name)
        val subtitle = String.format(appContext.getString(R.string.pending_tasks), taksToBeDone)
        val pendingIntent = getActivity(applicationContext, 0, intent, 0)

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setSmallIcon(R.drawable.tasklist)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setDefaults(DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(0, notification.build())
    }
}