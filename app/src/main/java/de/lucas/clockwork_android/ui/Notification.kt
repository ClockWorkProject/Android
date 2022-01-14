package de.lucas.clockwork_android.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.lucas.clockwork_android.MainActivity
import de.lucas.clockwork_android.R

@Composable
internal fun Notification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    // Create NotificationChannel for "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O"
    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }

    notification(
        context,
        channelId,
        notificationId,
        textTitle,
        textContent,
        priority
    )
}

fun notification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    // Create an intent for MainActivity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    /**
     * Builder for the notification (setOngoing = true), so user can't remove by clicking or swiping
     * Notification gets automatically removed after active toggle is finished
     */
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_clock)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)
        .setContentIntent(pendingIntent)
        .setOngoing(true)
    // Show notification
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

/**
 * For higher Android versions (8.0), create NotificationChannel with NotificationChannel Class
 */
fun createNotificationChannel(channelId: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.app_name)
        val desc = "${context.getString(R.string.app_name)}'s Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = desc
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

// Remove the currently shown notification
fun removeNotification(context: Context) = NotificationManagerCompat.from(context).cancel(0)