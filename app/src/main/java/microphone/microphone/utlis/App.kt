package microphone.microphone.utlis
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.core.app.NotificationCompat
import microphone.microphone.MainActivity
import microphone.microphone.R
import java.io.File

class App: Application() {
    companion object{
        var mp: MediaPlayer? = null
        lateinit var mp3File: File
        var count = 1
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        var isRecording = false
        lateinit var news: Notification
        var thread: Thread? = null
        var i = 0
        lateinit var file: File
        lateinit var recorder: MediaRecorder
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(Common.TAG, Context.MODE_PRIVATE)
        isRecording = sharedPreferences.getBoolean(Common.ISRECORD, false)
        editor = sharedPreferences.edit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serChannel = NotificationChannel("channel_id", "name", NotificationManager.IMPORTANCE_MIN)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serChannel)
        }

        val pendIntent = PendingIntent.getActivity(this, 123, Intent(this, MainActivity::class.java), 0)

        news = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("Diseño Web Cantabria Penagos")
            .setSmallIcon(R.drawable.mic_red)
            .setContentIntent(pendIntent)
            .build()
    }
}