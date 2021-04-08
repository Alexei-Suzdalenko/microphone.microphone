package microphone.microphone.utlis
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.widget.Toast
import microphone.microphone.utlis.App.Companion.file
import microphone.microphone.utlis.App.Companion.recorder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecordAudioService: Service() {
    val timestamp = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.US).format(Date())

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        recorder = MediaRecorder()
        val folder = File(getExternalFilesDir(null) ,  "rec_mic")
        if(!folder.exists()){
            folder.mkdirs()
        }
        file = File(folder, "$timestamp.mp3")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(4, App.news)
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            recorder.setAudioEncodingBitRate(16 * 44100)
            recorder.setAudioSamplingRate(44100)
            recorder.setOutputFile(file.path)
            recorder.prepare()
            recorder.start()
        } catch (e: Exception) { Toast.makeText(this, "Error: " +  e.message.toString(), Toast.LENGTH_LONG).show() }
        return START_STICKY
    }
}