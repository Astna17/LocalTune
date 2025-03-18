package com.app.localtune

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "music_channel"

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.layout.music_player_activity)
        mediaPlayer?.isLooping = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "PAUSE" -> {
                    mediaPlayer?.takeIf { it.isPlaying }?.pause()
                }
                "STOP" -> {
                    stopSelf()
                }
                "PLAY" -> {
                    mediaPlayer?.takeIf { !it.isPlaying }?.start()
                }

                else -> {}
            }
        } ?: run {
            mediaPlayer?.start()
            createNotification()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.apply {
            stop()
            release()
        }
    }

    private fun createNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(CHANNEL_ID, "Music Player", NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)

        val pauseIntent = PendingIntent.getService(this, 0, Intent(this, MusicService::class.java).setAction("PAUSE"), PendingIntent.FLAG_IMMUTABLE)
        val stopIntent = PendingIntent.getService(this, 0, Intent(this, MusicService::class.java).setAction("STOP"), PendingIntent.FLAG_IMMUTABLE)
        val playIntent = PendingIntent.getService(this, 0, Intent(this, MusicService::class.java).setAction("PLAY"), PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Lecture en cours")
            .setContentText("Appuyez pour contrôler la musique")
            .setSmallIcon(R.drawable.logo) // Remplace par ton icône
            .setOngoing(true)
            .addAction(R.drawable.baseline_pause_24, "Pause", pauseIntent)
            .addAction(R.drawable.baseline_play_arrow_24, "Play", playIntent)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

}
